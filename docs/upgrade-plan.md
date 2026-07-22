# 技术栈升级计划

> 目标：全面现代化后端（JDK / Spring Boot / MyBatis-Plus / FastJSON）与前端（Vue2 → Vue3），
> 一次性升到目标最新主流版本（非小步过渡），线上环境（阿里云 ECS）升级期间可接受短暂停机。
>
> 本计划基于对代码库的只读扫描结果制定，所有文件路径已核实存在。

## 当前执行状态（2026-07-22）

后端升级主线（Phase 1-6）已完成并在线上验证通过；前端 Vue3（Phase 7）和可选前台升级（Phase 8）未执行。

| Phase | 内容 | 状态 |
|-------|------|------|
| 0 | 建立基线：人工基线 + 独立分支 | 部分完成：后续在线上完成验证；分支为 `feature/interview-enhancements` |
| 1 | JDK 8 → 17 | 已完成：`pom.xml` 使用 `java.version=17`，本地和 ECS 均可运行 |
| 2 | 删除 Shiro / POI 僵尸依赖 | 已完成：未使用依赖已清理 |
| 3 | `javax.*` → `jakarta.*` | 已完成：代码已迁移到 Jakarta 命名空间 |
| 4 | MyBatis-Plus 2.3 → 3.5.7 | 已完成：使用 `mybatis-plus-spring-boot3-starter`，并保留旧版 Service 方法兼容层 |
| 5 | Spring Boot → 3.3.7 | 已完成：parent、MySQL 驱动、静态资源配置和异常处理已适配 |
| 6 | FastJSON → fastjson2 | 已完成：依赖和代码均已切到 `com.alibaba.fastjson2` |
| 7 | 后台 Vue2 → Vue3 | 未做：`package.json` 仍为 Vue 2.6 + Element UI，暂缓为独立任务 |
| 8 | 前台门户 Vue3 | 未做：仍为本地 Vue2 UMD + Layui，可选优化 |

其他已完成改动：

| 项 | 当前状态 |
|---|---|
| Redis 字典缓存 | 已保留，Redis 不可用时降级读取 |
| AI Function Calling | 已接入 OpenRouter/OpenAI 兼容接口，支持 6 类业务查询 |
| 前台首页路径问题 | 已修正基础 URL 重复斜杠问题 |
| Spring Boot 3 静态资源异常 | 已将 `NoResourceFoundException` 按 404 处理，避免误报 500 |

线上验证结果：

- ECS Java 版本：OpenJDK 17.0.19
- jar 上传后 hash 与本地一致
- 应用启动成功：`Tomcat started on port 8080 (http) with context path '/zuqiujulebguanli'`
- `/saishi/page`、`/gonggao/page` 接口返回 `code=0`
- `/ai/chat` 在配置 API Key 后可访问 OpenRouter；未配置时返回明确提示

当前遗留项：

- AI 工具暂未包含教练表查询。用户问“有几个教练”时，模型无法调用真实 `jiaolian` 表，需要后续新增 `queryCoaches` 工具。
- 后台管理前端仍为 Vue2 + Element UI，暂不建议立即升级到 Vue3，避免影响当前后端升级成果。
- `commons-logging` 冲突、缺少 Bean Validation Provider、Thymeleaf templates 目录 warning 暂不影响启动，可后续清理。

## 0. 现状扫描结论

| 项 | 现状版本 | 代码引用情况 | 结论 |
|---|---|---|---|
| JDK | 8 | 全项目 | 必须先升级为前提条件 |
| Spring Boot | 2.2.2.RELEASE | 全项目（Controller/Config/启动类） | 目标 3.x |
| MyBatis-Plus | 2.3（`mybatisplus-spring-boot-starter` 1.0.5） | **几乎所有 Dao/Entity/Service** | 目标 3.5.x，**全项目工作量最大的部分** |
| Shiro | 1.3.2 | **零引用**（鉴权是自研 `AuthorizationInterceptor` + Token 表） | **直接删除依赖**，无需升级 |
| Apache POI | 3.9（5 个子模块） | **零引用** | **直接删除依赖**，无需升级 |
| FastJSON | 1.2.8 | 10 个文件（Controller 出参、`AiChatService`） | 目标 fastjson2（API 基本兼容） |
| commons-lang3 | 3.0 | `StringUtils` 等多处 | 顺带升到 3.14+（Spring Boot 3 起版本管理会自动对齐，一般不用手动锁） |
| validation-api / javax.activation-api | javax 系 | 少量 | 随 jakarta 迁移一起换掉 |
| 后台管理前端 | Vue 2.6 + Element UI 2.15 + vue-router 3.1 + Vue CLI 4 | `src/main/resources/admin/admin/src/**` 全部组件 | 目标 Vue3 + Element Plus + vue-router4（建议顺带 Vite 替换 Vue CLI） |
| 前台门户 | 本地 `js/vue.js`（Vue2 UMD），纯 `<script>` 无构建 | `front/front/**` | 影响小，可保留或单独评估，不阻塞后端升级 |

**强制依赖顺序**（Spring Boot 3.x 编译期即要求 jakarta 命名空间 + 兼容的 MyBatis-Plus 版本，无法跳过）：

```
Phase 1: JDK 17/21
Phase 2: 清理僵尸依赖（Shiro / POI）—— 随时可做，零风险，建议提前处理掉噪音
Phase 3: javax.* → jakarta.* 命名空间迁移
Phase 4: MyBatis-Plus 2.3 → 3.5.x（核心工作量）
Phase 5: Spring Boot 2.2.2 → 3.x（依赖 1+3+4 完成）
Phase 6: FastJSON 1.2.8 → fastjson2（可与 3~5 并行）
Phase 7: 前端 Vue3 迁移（完全独立于后端，可并行开线）
```

---

## Phase 0：建立基线（不改代码）

在动手前，先固定一份"升级前正常状态"的证据，用于后续每个阶段升级后回归对比（项目目前 `src/test` 下只有 1 个测试文件，缺乏自动化验证手段，必须靠人工基线兜底）：

- [ ] 确认 `mvn clean package -DskipTests` 能成功、`java -jar target/*.jar` 能正常启动
- [ ] 手工走一遍核心链路并记录结果（截图/接口返回 JSON）：
  - 管理员 / 教练 / 球员三种角色登录（`manager/manager`、`coach_chen/123456`、`zhangwei/123456`）
  - 公告、赛事的分页查询 + 详情 + 新增/编辑/删除（含逻辑删除的赛事模块）
  - 字典缓存降级逻辑（Redis 可用/不可用两种场景）
  - 后台管理 `npm run build` 产物能正常访问
- [ ] `git` 在分支 `feature/interview-enhancements` 上推进，每个 Phase 独立 commit，方便出问题时 `git bisect` 定位

---

## Phase 1：JDK 8 → 17（LTS）

**改动文件**：`pom.xml`

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

- 本机/CI 需要安装 JDK 17（当前环境未检测到 `java`/`mvn` 在 PATH 中，需要先确认 IDE 或 CI 使用的 JDK 路径）
- IDEA 中同步切换 Project SDK 为 17
- **验证**：`mvn -v` 显示 17，`mvn clean compile` 通过（此时代码还是 javax，Spring Boot 2.2 在 JDK17 上可能有个别兼容 warning 但一般能跑）

> 备选：JDK 21（同为 LTS，Spring Boot 3.2+ 支持更好）。如果没有历史包袱，建议直接用 21。

---

## Phase 2：清理僵尸依赖

**改动文件**：`pom.xml`

删除以下从未被引用的依赖：

```xml
<!-- 删除：shiro-spring 1.3.2，代码全项目零引用，鉴权由 AuthorizationInterceptor 自研实现 -->
<!-- 删除：poi-examples / poi-excelant / poi-ooxml / poi-ooxml-schemas / poi-scratchpad（3.9），代码零引用 -->
```

**验证**：`mvn clean package` 通过，功能无变化（这两个包本来就没被用到）。

---

## Phase 3：`javax.*` → `jakarta.*`

**受影响文件**（已扫描确认，共 27 处 `javax.servlet.*`，分布在）：

- `src/main/java/com/filter/RequestLoggingFilter.java`
- `src/main/java/com/interceptor/AuthorizationInterceptor.java`
- `src/main/java/com/listener/DictionaryServletContextListener.java`
- `src/main/java/com/controller/*.java`（Dictionary/Gonggao/Saishi/Xunlian/Shuju/Jiaolian/Hetong/Yonghu/Users 共 9 个）
- `src/main/java/com/service/*.java` 及 `service/impl/*.java`（Yonghu/Shuju/Xunlian/Hetong/Jiaolian/Saishi/Gonggao/Dictionary/BaseService，共 10 个）

替换规则：`javax.servlet.*` → `jakarta.servlet.*`（纯 import 替换，逐字对应，可用 IDE 全局替换 + 编译报错兜底查漏）。

另外两个依赖需要换成 jakarta 版：

```xml
<!-- javax.validation:validation-api:2.0.1.Final → jakarta.validation:jakarta.validation-api -->
<!-- javax.activation:javax.activation-api:1.2.0 → jakarta.activation:jakarta.activation-api（Spring Boot 3 一般会自动管理版本，可去掉手动声明先试试） -->
```

**验证**：编译报错清单会非常明确（找不到 `javax.servlet` 包），改到编译通过为止；此阶段功能不应有变化。

---

## Phase 4：MyBatis-Plus 2.3 → 3.5.x（核心阶段）

**依赖替换**：

```xml
<!-- 删除 -->
<!-- com.baomidou:mybatis-plus:2.3 -->
<!-- com.baomidou:mybatisplus-spring-boot-starter:1.0.5 -->

<!-- 替换为 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.7</version>
</dependency>
```

**API 映射表**（按扫描到的实际用法整理）：

| 2.x 用法 | 3.x 用法 |
|---|---|
| `com.baomidou.mybatisplus.mapper.EntityWrapper` | `com.baomidou.mybatisplus.core.conditions.query.QueryWrapper`（或 `LambdaQueryWrapper`） |
| `com.baomidou.mybatisplus.mapper.Wrapper` | `com.baomidou.mybatisplus.core.conditions.Wrapper` |
| `com.baomidou.mybatisplus.mapper.BaseMapper` | `com.baomidou.mybatisplus.core.mapper.BaseMapper` |
| `com.baomidou.mybatisplus.plugins.Page` | `com.baomidou.mybatisplus.extension.plugins.pagination.Page`（实现 `IPage`） |
| `com.baomidou.mybatisplus.plugins.pagination.Pagination` | 同上，统一用 `Page`/`IPage` |
| `com.baomidou.mybatisplus.plugins.PaginationInterceptor` | `com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor` + 内部添加 `PaginationInnerInterceptor` |
| `com.baomidou.mybatisplus.service.IService` | `com.baomidou.mybatisplus.extension.service.IService` |
| `com.baomidou.mybatisplus.service.impl.ServiceImpl` | `com.baomidou.mybatisplus.extension.service.impl.ServiceImpl` |
| `com.baomidou.mybatisplus.annotations.TableName/TableId/TableField` | `com.baomidou.mybatisplus.annotation.TableName/TableId/TableField`（**annotations → annotation，去掉 s**） |
| `com.baomidou.mybatisplus.enums.IdType/FieldFill` | `com.baomidou.mybatisplus.annotation.IdType/FieldFill`（枚举挪进了 annotation 包） |

**受影响文件清单**（已扫描，共 60+ 处引用，分布在）：

- 配置：`src/main/java/com/config/MybatisPlusConfig.java`（分页插件重写）
- 模板基类：`src/main/java/com/service/impl/BaseService.java`（`BaseMapper`/`Page`/`ServiceImpl` 三处）
- 全部 Dao 接口（9 个）：`src/main/java/com/dao/*.java`
- 全部 Entity（9 个）+ View（7 个）：`src/main/java/com/entity/**/*.java`
- 全部 Service 接口（9 个）：`src/main/java/com/service/*Service.java`
- 全部 Service 实现（9 个）：`src/main/java/com/service/impl/*ServiceImpl.java`
- 工具类：`src/main/java/com/utils/MPUtil.java`、`Query.java`、`PageUtils.java`
- 其他引用点：`AiChatService.java`、`DictionaryController.java`、`FileController.java`、`UsersController.java`、`DictionaryServletContextListener.java`

**XML 层**：`src/main/resources/mapper/*.xml`（9 个文件）里的动态 SQL（`<where>`、`<![CDATA[]]>` 区间查询、`order by ${params.sort}`）在 MyBatis-Plus 3.x 下语法基本兼容，**不需要重写 SQL 本身**，但要重新核对分页参数是否还能正确从 `IPage` 里取到（`SaishiDao.xml` 的 `selectListView` 重点检查）。

**验证**：
- 逐模块编译通过后，针对每个模块跑一遍 Phase 0 里记录的基线操作（分页查询、详情、增删改）
- 重点回归赛事模块的逻辑删除（`saishi_delete` 1↔2）和字典翻译（`DictionaryService.dictionaryConvert()`）

---

## Phase 5：Spring Boot 2.2.2 → 3.x

**改动文件**：`pom.xml`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.x</version> <!-- 或按发布时最新的 3.x 稳定版 -->
</parent>
```

需要一起检查/调整的点：

- `mysql:mysql-connector-java` → `com.mysql:mysql-connector-j`（groupId/artifactId 都变了）
- `spring-boot-starter-data-redis`：3.x 默认走 Lettuce，`RedisConfig.java` 的序列化配置需要按新版 API 核对
- `spring-boot-starter-thymeleaf`：Thymeleaf 版本随 Spring Boot 3 自动升级，模板语法基本兼容
- `application.yml` 里个别 3.x 改名/废弃的配置项需要跑起来看启动日志报错逐一核对（`spring.resources.static-locations` 等旧式配置在 Spring Boot 3 里可能需要调整为 `spring.web.resources.static-locations`）
- `maven-compiler-plugin`：确认 `source`/`target` 跟随 `java.version=17` 生效

**验证**：应用能启动、Phase 0 基线全部走通，重点看启动日志有没有 deprecation/error。

---

## Phase 6：FastJSON 1.2.8 → fastjson2

**改动文件**：`pom.xml` + 10 个使用 `JSONObject`/`JSONArray` 的文件

```xml
<!-- 替换 -->
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
    <version>2.0.x</version>
</dependency>
```

fastjson2 兼容包 `com.alibaba.fastjson2:fastjson2-extension` 提供了 `com.alibaba.fastjson.JSONObject` 兼容 API，可以先用兼容包让 import 不用全改，验证通过后再逐步替换成 `com.alibaba.fastjson2.JSONObject` 原生 API。

受影响文件：`AiChatService.java`、`AuthorizationInterceptor.java`、`DictionaryController.java`、`GonggaoController.java`、`SaishiController.java`、`XunlianController.java`、`ShujuController.java`、`JiaolianController.java`、`HetongController.java`、`YonghuController.java`。

**验证**：所有接口返回 JSON 格式跟基线截图/记录逐一对比，重点看日期字段序列化格式是否变化（fastjson1→2 默认日期格式可能不同，需要显式配置）。

---

## Phase 7：前端后台管理 Vue2 → Vue3（独立线，不阻塞后端）

范围：`src/main/resources/admin/admin/`

- `vue: ^2.6.10` → `vue: ^3.x`
- `element-ui: ^2.15.10` → `element-plus`
- `vue-router: ^3.1.5` → `vue-router: ^4.x`
- `vue-template-compiler` → 移除（Vue3 不需要）
- 构建工具建议：Vue CLI 4（已停止维护）→ Vite（`@vue/cli-service` 相关全部替换）
- 逐组件检查：Options API 在 Vue3 基本兼容，但 `this.$refs`、全局过滤器（`filters`）、事件总线（`$on`/`$off`/`$emit` 用于兄弟组件通信）等 Vue2 特性在 Vue3 中被移除，需要按组件逐个核实
- `router/router-static.js` 路由定义需要按 vue-router4 的 `createRouter` API 重写

**建议独立拆分任务**，先在新分支上跑通登录 + 一个模块（比如公告管理）作为验证 PoC，确认整体迁移路径可行后再铺开到其余模块。

---

## Phase 8（可选/低优先级）：前台门户

`src/main/resources/front/front/` 是纯 `<script>` 引入的静态页面，没有构建流程，不参与 npm 生态，因此不受 Vue CLI/webpack 淘汰的直接影响。可以：
- 保持现状（本地 `js/vue.js` 版本的 Vue2 UMD 包仍能正常工作，只是不再有安全更新）
- 或单独找时间替换为新版本 Vue3 UMD CDN/本地包，语法改动量小（页面逻辑简单）

不阻塞本次后端/后台管理升级，可放在最后单独排期。

---

## 风险与回滚

- 每个 Phase 单独 commit，出问题可精确回退到上一个 Phase
- Phase 4（MyBatis-Plus）是唯一"大范围改代码"的阶段，建议先挑 1-2 个模块（如 Gonggao 公告，逻辑最简单）试跑通过，再批量套用到其余 8 个模块，降低一次性改错的排查成本
- 线上 ECS 部署：本地/新分支全部验证通过后，一次性替换 jar + 前端产物，允许的停机窗口内完成切换，保留旧 jar 可随时回退
