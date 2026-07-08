# 足球俱乐部管理系统

基于 Spring Boot + MyBatis-Plus + Vue 的足球俱乐部综合管理平台。

## 项目介绍

面向足球俱乐部的信息化管理系统，覆盖俱乐部日常运营中的核心业务场景：赛事管理、公告发布、教练与球员管理、训练计划制定、球员数据统计、合同管理等。系统分为前台展示和后台管理两个子系统。

**项目定位**：单体 Spring Boot 应用，前后端半分离架构（后台 Vue SPA，前台静态 HTML + API），适合中小型俱乐部快速部署使用。

## 在线演示

> 部署环境：阿里云 ECS（Ubuntu 22.04 / 2核2G / MySQL + Redis）

- **前台门户**：http://120.26.174.97:8080/zuqiujulebguanli/front/index.html
- **后台管理**：http://120.26.174.97:8080/zuqiujulebguanli/admin/dist/index.html
  - 管理员账号：`manager` / `manager`
  - 教练账号：`coach_chen` / `123456`
  - 球员账号：`zhangwei` / `123456`

## 系统截图

**前台门户** — 公告信息、赛事信息、足球资讯展示

![前台门户](docs/screenshots/frontend-portal.png)

**后台管理仪表盘** — 数据统计、最新公告、最新赛事一览

![后台管理仪表盘](docs/screenshots/admin-dashboard.png)

## 技术栈

| 层次 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 2.2.2.RELEASE |
| ORM | MyBatis-Plus | 2.3 |
| 数据库 | MySQL | 5.7+ |
| 缓存 | Redis | 5.0+（字典数据缓存，不可用时自动降级） |
| 连接池 | HikariCP | (Spring Boot 内置) |
| 权限 | Apache Shiro | 1.3.2 |
| 模板引擎 | Thymeleaf | (Spring Boot 内置) |
| JSON | FastJSON | 1.2.8 |
| 工具库 | Hutool | 5.8.25 |
| 后台前端 | Vue 2 + Element UI + Vue Router | (CDN + Webpack 打包) |
| 前台前端 | 原生 HTML + Vue.js (CDN) + Layui | — |
| 构建工具 | Maven (后端) / Webpack (前端) | — |

## 目录结构

```
zuqiu/
├── pom.xml                                  # Maven 配置
├── db.sql                                   # 数据库初始化脚本
├── README.md
├── docs/                                    # 项目文档
│   ├── sql-optimization.md                  # SQL 优化分析
│   ├── deploy.md                            # 部署指南
│   └── screenshots/                         # 系统截图
├── .vscode/                                 # VS Code 配置
├── src/main/java/com/
│   ├── ZuqiujulebguanliApplication.java     # 应用启动入口
│   ├── annotation/                          # 自定义注解 (@ColumnInfo, @IgnoreAuth)
│   ├── common/                              # 公共类
│   │   ├── BusinessException.java           # 业务异常
│   │   ├── ResultCode.java                  # 统一响应状态码枚举
│   │   └── GlobalExceptionHandler.java      # 全局异常处理器
│   ├── config/
│   │   ├── MybatisPlusConfig.java           # MyBatis-Plus 分页插件配置
│   │   ├── RedisConfig.java                 # Redis 序列化配置
│   │   └── WebMvcConfig.java                # Web MVC 配置(拦截器/静态资源)
│   ├── controller/                          # REST 控制器 (10 个模块)
│   │   ├── ConfigController.java
│   │   ├── DictionaryController.java
│   │   ├── ExternalNewsController.java
│   │   ├── FileController.java
│   │   ├── GonggaoController.java           # 公告
│   │   ├── HetongController.java            # 合同
│   │   ├── JiaolianController.java          # 教练
│   │   ├── SaishiController.java            # 赛事 (核心模块)
│   │   ├── ShujuController.java             # 球员数据
│   │   ├── UsersController.java             # 管理员
│   │   ├── XunlianController.java           # 训练计划
│   │   └── YonghuController.java            # 用户/球员
│   ├── dao/                                 # MyBatis-Plus Mapper 接口
│   ├── entity/                              # 数据库实体 (对应表)
│   │   └── view/                            # 视图对象 (带字典翻译字段)
│   ├── service/                             # 业务接口
│   │   ├── DictionaryCacheService.java      # Redis 字典缓存服务（含降级策略）
│   │   └── impl/
│   │       ├── BaseService.java             # 抽象基类 (模板方法模式)
│   │       ├── GonggaoServiceImpl.java
│   │       ├── SaishiServiceImpl.java       # 赛事服务 (逻辑删除示例)
│   │       └── ...
│   ├── interceptor/
│   │   └── AuthorizationInterceptor.java    # 登录鉴权拦截器
│   ├── listener/
│   │   └── DictionaryServletContextListener.java  # 字典表初始化监听器
│   └── utils/
│       ├── R.java                           # 统一响应封装 (继承 HashMap)
│       ├── PageUtils.java                   # 分页工具
│       ├── Query.java                       # 查询参数封装
│       ├── MPUtil.java                      # MyBatis-Plus 工具
│       ├── CommonUtil.java                  # 通用工具
│       ├── DateUtil.java                    # 日期工具
│       ├── SQLFilter.java                   # SQL 注入过滤
│       └── StringUtil.java                  # 字符串工具
├── src/main/resources/
│   ├── application.yml                      # 主配置文件 (数据源/端口/MyBatis-Plus)
│   ├── mapper/                              # MyBatis XML 映射文件 (9 个)
│   │   └── SaishiDao.xml                    # 赛事动态 SQL 查询
│   ├── front/front/                         # 前台静态页面 (无需构建)
│   │   ├── index.html                       # 首页
│   │   ├── pages/                           # 各模块页面
│   │   └── js/ config.js modules/           # 前端配置
│   ├── admin/
│   │   ├── admin/                           # 后台 Vue 项目源码
│   │   │   ├── package.json
│   │   │   ├── vue.config.js                # Vue CLI 配置 (outputDir/dist 路径)
│   │   │   └── src/
│   │   │       ├── main.js                  # Vue 入口
│   │   │       ├── router/router-static.js  # 路由定义
│   │   │       ├── utils/http.js            # axios 封装
│   │   │       └── views/modules/           # 各模块页面组件
│   │   └── dist/                            # Webpack 构建输出 (git ignored)
│   └── static/upload/                       # 上传文件目录
└── target/                                  # Maven 编译输出
```

## 后端启动步骤

### 环境要求
- JDK 8
- Maven 3.6+
- MySQL 5.7+（已安装并运行）
- Redis 5.0+（可选，字典缓存；不可用时自动降级到内存缓存）

### 1. 创建数据库并导入数据

在 MySQL 中执行：

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS zuqiujulebguanli DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 导入数据
mysql -u root -p zuqiujulebguanli < db.sql
```

> **注意**：`db.sql` 文件开头有 BOM 头可能导致导入报错。如遇到 `1064` 语法错误，先用 VS Code / Notepad++ 将文件编码转为 UTF-8 without BOM。

### 2. 修改数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/zuqiujulebguanli?...参数省略...
    username: root        # 改为你的 MySQL 用户名
    password: 123456      # 改为你的 MySQL 密码
```

### 3. 编译运行

```bash
# 方式一：Maven 命令行
mvn clean package -DskipTests
java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar

# 方式二：IDE 运行
# 在 IDEA 中直接运行 ZuqiujulebguanliApplication.main()
```

### 4. 访问验证

- 后端启动端口：`8080`
- 上下文路径：`/zuqiujulebguanli`
- 前台首页：`http://localhost:8080/zuqiujulebguanli/front/index.html`
- 后台管理：`http://localhost:8080/zuqiujulebguanli/admin/dist/index.html`
- 默认管理员：`manager` / `manager`

## 前端启动/打包步骤

### 后台管理系统 (Vue 2 + Element UI)

项目位于 `src/main/resources/admin/admin/`。

```bash
cd src/main/resources/admin/admin

# 安装依赖
npm install

# 开发模式 (热更新，端口 8081，代理到后端 8080)
npm run serve

# 生产构建 (输出到 ../dist/)
npm run build
```

`vue.config.js` 关键配置：
- `outputDir`: `../dist`（构建到 `src/main/resources/admin/dist/`）
- `devServer.port`: `8081`
- `devServer.proxy`: `/zuqiujulebguanli` → `http://localhost:8080`

### 前台展示页面

位于 `src/main/resources/front/front/`，纯静态 HTML，**无需构建**。Spring Boot 直接作为静态资源提供服务。

## API 接口说明

### 统一响应格式

所有接口返回 JSON，使用 `com.utils.R`（继承 `HashMap`）封装：

```json
// 成功
{ "code": 0, "data": {...}, "msg": "ok" }

// 成功(分页)
{ "code": 0, "data": { "total": 100, "pageSize": 10, "currPage": 1, "totalPage": 10, "list": [...] } }

// 错误
{ "code": 500, "msg": "错误信息" }

// 未登录
{ "code": 511, "msg": "未找到数据" }
```

### 通用 CRUD 接口模式

每个业务模块提供统一的 5 个接口：

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/模块/page` | 分页查询（支持多条件筛选 + 时间范围） |
| GET | `/模块/info/{id}` | 查详情（含字典翻译） |
| POST | `/模块/save` | 新增（含唯一性校验 + 默认值） |
| POST | `/模块/update` | 修改（含字段清理） |
| POST | `/模块/delete` | 删除（物理删除 / 逻辑删除） |

### 分页查询参数

```
?page=1&limit=10&sort=id&order=desc
```

- `page`: 页码（默认 1）
- `limit`: 每页条数（默认 10）
- `sort`: 排序字段（默认 id）
- `order`: 排序方向（asc / desc，默认 desc）
- 其他字段：作为过滤条件，空值和 "null" 字符串自动忽略

## 面试可讲模块

### 1. BaseService 模板方法设计模式

`com.service.impl.BaseService` 是 Service 层的抽象基类，定义了 9 个模板方法：

- **必须实现**：`selectListView`、`toView`、`checkUniqueness`
- **可选覆盖**：`sanitizeFields`、`initEntityForInsert`、`applyRoleFilter`、`setDeleteFilterParams`、`softDeleteBatch`

**亮点**：
- 将公共分页查询、字典转换、角色过滤逻辑提升到父类
- 子类只需实现 3 个方法即可完成 CRUD
- 符合「对扩展开放，对修改关闭」原则

### 2. 逻辑删除（软删除）

以赛事模块 (`SaishiServiceImpl`) 为例：

- 删除时（`softDeleteBatch`）：将 `saishi_delete` 从 1 改为 2
- 查询时（`setDeleteFilterParams`）：自动注入 `saishiDeleteStart=1, saishiDeleteEnd=1`
- 配合 MyBatis XML 中的 `<![CDATA[ and a.saishi_delete >= #{...} ]]>` 过滤已删除数据

### 3. MyBatis-Plus 自定义分页 + 动态 SQL

`SaishiDao.xml` 中的 `selectListView` 展示了：
- 动态 `<where>` 标签：前端传了参数才加条件
- `<![CDATA[]]>` 区间查询：防止 `>=` 被 XML 解析
- `order by ${params.sort} ${params.order}` 动态排序
- 防 SQL 注入：`SQLFilter.sqlInject()` 校验排序参数

### 4. View 视图模式 + 字典翻译

- Entity（`SaishiEntity`）：对应数据库表字段
- View（`SaishiView extends SaishiEntity`）：扩展了 `saishiValue` 字典翻译字段
- `DictionaryService.dictionaryConvert()`：查询时将 `saishiTypes=1` 翻译为 `saishiValue="中超联赛"`
- 避免多次连表查询，字典表数据通过启动监听器加载到内存

### 5. 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)  // 业务异常 → 511
    @ExceptionHandler(Exception.class)          // 未知异常 → 500
}
```

### 6. Redis 字典缓存 + 降级策略

`DictionaryCacheService` 将字典数据缓存到 Redis Hash 结构中（key: `zuqiu:dict:{dic_code}`）：

- **启动时**：`DictionaryServletContextListener` 同时将字典数据写入 ServletContext（原方案）和 Redis
- **查询时**：`dictionaryConvert()` 优先读 Redis，返回 null 时自动降级到 ServletContext
- **变更时**：字典增删改操作同步刷新 Redis 和 ServletContext
- **降级策略**：每个 Redis 操作用 try-catch 包裹，失败后 30 秒内不重试，避免频繁报错
- **限制**：Redis 宕机期间的字典变更不会自动补偿，恢复后需重启应用或触发一次字典变更

详见 `docs/deploy.md` 中的 Redis 降级与恢复说明。

## 部署方式

### 生产部署架构

```
Nginx (80/443)
├── /                    → 前端静态资源 (admin/dist + front/front)
├── /zuqiujulebguanli/   → 反向代理 → localhost:8080
└── /upload/             → 上传文件目录

Spring Boot (8080)
├── 内嵌 Tomcat
├── MySQL 连接
└── 文件上传处理
```

### 部署步骤

```bash
# 1. 后端打包
mvn clean package -DskipTests

# 2. 前端打包
cd src/main/resources/admin/admin && npm run build

# 3. 将 jar 和静态资源部署到服务器
# - target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar → /opt/app/
# - admin/dist/ 和 front/front/ → Nginx 静态目录

# 4. Nginx 配置 (示例)
# location /zuqiujulebguanli/ {
#     proxy_pass http://127.0.0.1:8080/zuqiujulebguanli/;
# }
# location /upload/ {
#     alias /opt/app/upload/;
# }

# 5. 启动后端
nohup java -jar zuqiujulebguanli-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

## 项目规范

### 命名规范
- Controller: `XxxController` → `@RequestMapping("/xxx")`
- Service 接口: `XxxService extends IService<XxxEntity>`
- Service 实现: `XxxServiceImpl extends BaseService<XxxDao, XxxEntity, XxxView>`
- Dao: `XxxDao extends BaseMapper<XxxEntity>`
- Entity: `XxxEntity` → `@TableName("xxx")`
- View: `XxxView extends XxxEntity`

### 逻辑删除字段规范
- 未删除: `1`
- 已删除: `2`
- 查询时通过 `setDeleteFilterParams` 注入删除过滤条件

## 常见问题

### Q: 启动报 "Unknown database"
A: MySQL 中不存在 `zuqiujulebguanli` 数据库，先执行 `CREATE DATABASE` 并导入 `db.sql`。

### Q: 导入 db.sql 报 1064 语法错误
A: SQL 文件开头有 BOM 头（UTF-8 with BOM），用 VS Code 转存为 UTF-8 without BOM。

### Q: 前台页面 404
A: 正确路径是 `/front/index.html`，不是 `/front/front/index.html`。`application.yml` 中 `spring.resources.static-locations` 已映射 `classpath:front/`。

### Q: 后台管理页面空白
A: 需要先 `npm run build` 构建 admin 项目到 `admin/dist/` 目录。

### Q: multi-catch 编译报错
A: `pom.xml` 中未显式配置 `maven-compiler-plugin`，已修复 source/target 为 1.8。
