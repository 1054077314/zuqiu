## 部署指南

本文档说明如何在本地或服务器上部署足球俱乐部管理系统。

### 1. 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | 编译和运行 Spring Boot 3 |
| MySQL | 5.7+ | 数据库 |
| Redis | 5.0+ | 字典数据缓存（可选，不可用时自动降级） |
| Maven | 3.x | 编译打包 |
| Node.js | 12+ | 仅后台管理前端构建时需要 |

### 2. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE zuqiujulebguanli DEFAULT CHARACTER SET utf8mb4;

# 导入建表和初始数据
USE zuqiujulebguanli;
SOURCE /path/to/db.sql;
```

如果 MySQL 密码不是 `123456`，需要修改 `src/main/resources/application.yml` 中的 `spring.datasource.password`。

### 3. Redis 启动

Redis 用于字典数据缓存，提升字典翻译性能。

**Windows（本项目开发环境）：**

```bash
# 进入 Redis 安装目录
cd D:\bq\Redis

# 启动 Redis 服务
redis-server.exe redis.windows.conf
```

**Linux（生产环境）：**

```bash
# 方式一：系统服务
sudo systemctl start redis

# 方式二：直接启动
redis-server /etc/redis/redis.conf
```

验证 Redis 是否运行：

```bash
redis-cli ping
# 输出 PONG 表示正常
```

Redis 不可用时系统仍可正常运行，字典数据会自动降级到 ServletContext 内存缓存。

**Redis 降级与恢复说明：**

- **启动时 Redis不可用**：系统正常启动，字典数据仅使用 ServletContext 缓存，功能不受影响。
- **运行中 Redis 宕机**：字典翻译自动降级到 ServletContext，30 秒内不再尝试连接 Redis，避免频繁报错。
- **Redis 恢复后**：不会自动补偿同步。需要以下任一操作触发 Redis 重新加载：
  1. 重启应用（启动时 Listener 会重新写入 Redis）
  2. 在后台管理中执行一次字典数据的增/改/删操作（会触发 `refresh` 方法重新加载缓存）
- **注意**：Redis 宕机期间如果修改过字典数据，Redis 恢复后数据可能是旧的，务必在恢复后执行一次字典变更或重启应用。

### 4. 编译打包

```bash
# 进入项目根目录
cd zuqiu

# Maven 打包（跳过测试，加快打包速度）
mvn clean package -DskipTests

# 打包完成后，jar 文件在 target/ 目录下
ls target/*.jar
# 输出：zuqiujulebguanli-0.0.1-SNAPSHOT.jar
```

如果 IDEA 运行 Maven，请确认 Maven Runner 的 JRE 使用 JDK 17 或更高版本。出现 `无效的目标发行版: 17` 时，说明当前 Maven 使用的是 JDK 8，需要切换 Project SDK / Maven Runner JRE。

本地命令行如果没有配置 `mvn`，可以直接使用 IDEA 自带 Maven，例如：

```powershell
$env:JAVA_HOME='C:\Users\王\.jdks\ms-21.0.11'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'D:\java\idea\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd' -DskipTests clean package
```

### 5. 启动应用

```bash
# 基本启动
java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar

# 指定生产环境配置（可选）
java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://192.168.1.100:3306/zuqiujulebguanli?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false \
  --spring.datasource.username=root \
  --spring.datasource.password=your_password \
  --spring.redis.host=192.168.1.100

# 后台运行（Linux 服务器）
nohup java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

#### 5.1 阿里云 ECS 部署命令

本项目当前线上路径为 `/opt/zuqiu/target/`，jar 名称为 `zuqiujulebguanli-0.0.1-SNAPSHOT.jar`。

本地上传：

```powershell
scp "D:\bq\zuqiu\target\zuqiujulebguanli-0.0.1-SNAPSHOT.jar" root@<服务器IP>:/opt/zuqiu/target/
```

服务器校验：

```bash
cd /opt/zuqiu/target
ls -lh zuqiujulebguanli-0.0.1-SNAPSHOT.jar
sha256sum zuqiujulebguanli-0.0.1-SNAPSHOT.jar
```

启动前确认 JDK：

```bash
java -version
# 需要看到 openjdk version "17.x"
```

后台重启：

```bash
cd /opt/zuqiu/target
pkill -f 'zuqiujulebguanli-0.0.1-SNAPSHOT.jar'
: > /opt/zuqiu/app.log
nohup java -jar zuqiujulebguanli-0.0.1-SNAPSHOT.jar > /opt/zuqiu/app.log 2>&1 &
sleep 8
tail -n 120 /opt/zuqiu/app.log
```

出现以下日志表示启动成功：

```text
Tomcat started on port 8080 (http) with context path '/zuqiujulebguanli'
Started ZuqiujulebguanliApplication
```

#### 5.2 AI 接口环境变量

AI Key 不写入仓库和配置文件，通过环境变量注入。当前代码读取：

| 环境变量 | 说明 |
|----------|------|
| `DEEPSEEK_API_KEY` | OpenRouter 或 DeepSeek API Key |
| `AI_API_URL` | OpenAI 兼容接口地址 |
| `AI_MODEL` | 模型 ID |
| `AI_HTTP_REFERER` | OpenRouter 请求来源，可配置公网访问地址 |

OpenRouter 示例：

```bash
export DEEPSEEK_API_KEY='<your-openrouter-key>'
export AI_API_URL='https://openrouter.ai/api/v1/chat/completions'
export AI_MODEL='poolside/laguna-xs-2.1:free'
export AI_HTTP_REFERER='http://<服务器IP>:8080'

nohup java -jar zuqiujulebguanli-0.0.1-SNAPSHOT.jar > /opt/zuqiu/app.log 2>&1 &
```

如果 `/ai/chat` 返回 `AI 功能还没有配置 API Key`，说明 Java 进程启动时没有拿到 `DEEPSEEK_API_KEY`，需要带上环境变量后重启服务。

启动成功后控制台输出：

```
Tomcat started on port 8080 (http) with context path '/zuqiujulebguanli'
----------字典表 ServletContext 初始化完成----------
----------字典表 Redis 缓存初始化完成----------
```

### 6. 访问地址

| 页面 | URL |
|------|-----|
| 前台门户 | `http://localhost:8080/zuqiujulebguanli/front/index.html` |
| 后台管理 | `http://localhost:8080/zuqiujulebguanli/admin` |

默认账号：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | manager | manager |
| 球员 | zhangwei | 123456 |
| 教练 | coach_chen | 123456 |

接口验证：

```bash
curl -s "http://127.0.0.1:8080/zuqiujulebguanli/saishi/page?page=1&limit=1"
curl -s "http://127.0.0.1:8080/zuqiujulebguanli/gonggao/page?page=1&limit=1"
curl -s -H "Content-Type: application/json" \
  -d '{"message":"查最新赛事"}' \
  "http://127.0.0.1:8080/zuqiujulebguanli/ai/chat"
```

### 7. Nginx 反向代理（可选）

生产环境建议用 Nginx 做反向代理，隐藏端口号和 context-path：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 静态资源直接由 Nginx 处理（可选，需先构建前端）
    location /admin/ {
        alias /opt/zuqiu/src/main/resources/admin/dist/;
        try_files $uri $uri/ /admin/index.html;
    }

    # API 请求转发到 Spring Boot
    location /zuqiujulebguanli/ {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 上传文件访问
    location /upload/ {
        alias /opt/zuqiu/src/main/resources/static/upload/;
    }
}
```

### 8. 验证 Redis 缓存

启动后可通过 `redis-cli` 查看缓存数据：

```bash
redis-cli

# 查看所有字典缓存 key
KEYS zuqiu:dict:*
# 输出示例：
# 1) "zuqiu:dict:saishi_types"
# 2) "zuqiu:dict:gonggao_types"
# 3) "zuqiu:dict:sex_types"
# ...

# 查看赛事类型缓存内容
HGETALL zuqiu:dict:saishi_types
# 输出示例：
# 1) "1"
# 2) "中超联赛"
# 3) "2"
# 4) "足协杯"
# 5) "3"
# 6) "亚冠联赛"
# 7) "4"
# 8) "热身赛"
```

### 9. 常见问题

#### 9.1 `ClassNotFoundException: com.mysql.cj.protocol.ExportControlled`

如果启动日志同时出现 MySQL 驱动类、Tomcat 类缺失，优先检查 jar 是否上传完整：

```bash
ls -lh /opt/zuqiu/target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar
sha256sum /opt/zuqiu/target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar
```

本地与服务器 hash 不一致时，重新上传 jar。

#### 9.2 首页提示“服务器内部错误”

Spring Boot 3 对不存在的静态资源会抛出 `NoResourceFoundException`。项目已在 `GlobalExceptionHandler` 中将该异常按 404 处理，前端请求路径也统一去掉重复斜杠。如果仍出现错误，查看最新日志：

```bash
tail -n 200 /opt/zuqiu/app.log
```

重点检查是否存在 `No static resource ...`、接口 500 或数据库异常。

#### 9.3 AI 能回答但没有查系统数据

AI 系统数据查询依赖 Function Calling 工具链。当前已接入的工具包括球员档案、公告、赛事、训练计划、合同、球员数据。未接入的业务表不会被模型主动查询，例如教练表需要新增 `queryCoaches` 工具后才能通过自然语言查询。
