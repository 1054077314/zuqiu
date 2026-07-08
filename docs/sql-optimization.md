## SQL 优化分析

本文档以赛事列表查询（`saishi` 表）为例，分析当前 SQL 的性能特征和优化方向。

### 1. 核心查询场景

赛事列表查询 `SaishiDao.xml#selectListView` 是本系统最高频的查询之一，由 `BaseService.queryPage()` 调用，每次分页列表页面都会触发。

查询结构：

```sql
SELECT a.id, a.saishi_name, a.saishi_photo, a.saishi_address,
       a.saishi_types, a.saishi_content, a.saishi_delete,
       a.insert_time, a.create_time
FROM saishi a
WHERE a.saishi_delete >= 1       -- 逻辑删除过滤（BaseService.setDeleteFilterParams）
  AND a.saishi_delete <= 1       -- 只查未删除记录
  AND a.saishi_types = ?         -- 可选：按赛事类型筛选
  AND a.saishi_name LIKE '%?%'   -- 可选：名称模糊搜索
ORDER BY a.id DESC
LIMIT ?, ?
```

### 2. 分页查询说明

分页由 MyBatis-Plus 的 `Page` 对象控制，底层通过 `LIMIT offset, size` 实现。

当前实现中，`Query` 工具类会将前端传入的 `page`（页码）和 `limit`（每页条数）转换为 `offset`：

```
offset = (page - 1) * limit
```

默认每页 10 条。前端通过 `?page=1&limit=10` 传参。

分页性能注意：当 `offset` 很大时（如第 1000 页），MySQL 需要扫描并丢弃前 9990 条记录。在数据量较小时（<1 万条）问题不大，数据量大时可用延迟关联优化：

```sql
-- 延迟关联优化深分页（数据量大时考虑）
SELECT a.* FROM saishi a
INNER JOIN (SELECT id FROM saishi WHERE saishi_delete = 1 ORDER BY id DESC LIMIT 10000, 10) b
ON a.id = b.id;
```

### 3. 逻辑删除字段的查询影响

当前赛事表使用 `saishi_delete` 字段实现逻辑删除（1=未删除，2=已删除）。`BaseService.setDeleteFilterParams` 在每次查询时自动追加条件：

```java
params.put("saishiDeleteStart", 1);
params.put("saishiDeleteEnd", 1);
```

对应 XML 中的：

```xml
and a.saishi_delete >= 1
and a.saishi_delete <= 1
```

等价于 `saishi_delete = 1`。这里用范围写法而非等值，是因为 `BaseService` 的通用设计——部分模块可能需要查询"已删除+未删除"的数据。

逻辑删除对性能的影响：已删除的数据仍然在表中，随时间推移会增大表体积和索引大小。建议定期归档或删除超过一定时间的逻辑删除记录。

### 4. 当前索引情况

通过 `SHOW INDEX FROM saishi` 查看，当前只有主键索引 `PRIMARY(id)`。

所有 WHERE 条件字段（`saishi_delete`、`saishi_types`、`saishi_name`、`saishi_address`）均无索引。

### 5. 建议添加的索引

```sql
-- 复合索引：覆盖最常见的查询模式（逻辑删除 + 类型筛选 + 排序）
ALTER TABLE saishi ADD INDEX idx_delete_types_id (saishi_delete, saishi_types, id);

-- 说明：
-- saishi_delete 放最左：每次查询必带此条件，过滤性虽低（大部分是1），但能排除已删除数据
-- saishi_types 放中间：类型筛选是高频条件，过滤性中等（4种类型）
-- id 放最后：用于 ORDER BY id DESC 的排序优化，避免 filesort
```

```sql
-- 时间范围查询索引（录入时间筛选场景）
ALTER TABLE saishi ADD INDEX idx_delete_insert_time (saishi_delete, insert_time);
```

不建议对 `saishi_name`、`saishi_address` 加索引，原因是：

- 模糊查询 `LIKE '%关键词%'` 无法使用 B-tree 索引（前缀通配符）
- 如需全文搜索，可考虑 MySQL FULLTEXT 索引或 Elasticsearch

### 6. EXPLAIN 分析思路

优化前后可以用以下命令对比：

```sql
EXPLAIN SELECT a.id, a.saishi_name, a.saishi_types, a.saishi_delete, a.insert_time
FROM saishi a
WHERE a.saishi_delete = 1
  AND a.saishi_types = 1
ORDER BY a.id DESC
LIMIT 0, 10;
```

关注字段：

| 字段 | 关注点 |
|------|--------|
| `type` | 期望 `range` 或 `ref`，而非 `ALL`（全表扫描） |
| `possible_keys` | 是否识别到了建议的索引 |
| `key` | 实际使用的索引 |
| `rows` | 预估扫描行数，越少越好 |
| `Extra` | 是否出现 `Using filesort`（额外排序）或 `Using where`（回表过滤） |

**无索引时预期结果：**
- `type: ALL`（全表扫描）
- `Extra: Using where; Using filesort`

**添加 `idx_delete_types_id` 后预期结果：**
- `type: range` 或 `type: ref`
- `key: idx_delete_types_id`
- `Extra` 中 `Using filesort` 消失（因为索引已覆盖排序列）

### 7. 面试话术

**面试官问：你对 SQL 优化有什么思路？**

可以这样回答：

> 我的足球俱乐部管理系统中，赛事列表是最高频的查询接口。原始设计只有主键索引，每次分页查询都是全表扫描加 filesort。
>
> 我分析了查询模式后发现，每次查询必带 `saishi_delete = 1` 的逻辑删除条件，大部分查询还会按 `saishi_types` 筛选，排序用 `id DESC`。优化方案是建一个复合索引 `(saishi_delete, saishi_types, id)`，让索引同时覆盖过滤和排序。
>
> 用 EXPLAIN 验证的话，预期 type 会从 ALL 变成 ref，filesort 也会消失，因为索引已经覆盖了排序列。
>
> 另外，对于名称模糊搜索 `LIKE '%xxx%'`，B-tree 索引无法优化前缀通配符。如果数据量增长到需要全文搜索，我会考虑引入 Elasticsearch。

**面试官追问：为什么把 saishi_delete 放在索引最左边？它的区分度不是很低吗？**

> 区分度确实不高，因为大部分记录都是未删除状态。但它出现在每一次查询的 WHERE 条件中，放在最左位置可以确保所有查询都能使用这个索引。而且逻辑删除的记录会随时间增多，到时候这个字段的过滤价值会更高。复合索引的设计不只看单个字段的区分度，还要看查询模式的覆盖度。

**面试官追问：你实际加了索引吗？**

> 目前项目数据量小（几十条记录），加索引的性能差异不明显。我的优化方案文档里已经写好了建索引的 DDL 和 EXPLAIN 分析思路，当数据量增长到需要时可以直接执行。现阶段重点放在了缓存层优化（Redis 字典缓存）和查询模式分析上。
