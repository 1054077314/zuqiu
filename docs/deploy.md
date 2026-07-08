## 部署指南

本文档说明如何在本地或服务器上部署足球俱乐部管理系统。

### 1. 环境要求

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | 编译和运行 Spring Boot |
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

启动成功后控制台输出：

```
Tomcat started on port(s): 8080 (http)
----------字典表 ServletContext 初始化完成----------
----------字典表 Redis 缓存初始化完成----------
```

### 6. 访问地址

| 页面 | URL |
|------|-----|
| 前台门户 | `http://localhost:8080/zuqiujulebguanli/front/index.html` |
| 后台管理 | `http://localhost:8080/zuqiujulebguanli/admin/dist/index.html` |

默认账号：

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | manager | manager |
| 球员 | zhangwei | 123456 |
| 教练 | coach_chen | 123456 |

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
