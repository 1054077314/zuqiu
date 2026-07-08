#!/bin/bash
# ============================================
# 足球俱乐部管理系统 - 一键部署脚本
# 适用于 Ubuntu 22.04 全新服务器
# ============================================

set -e
echo "========== [1/6] 安装 JDK 8 =========="
apt-get update -qq
apt-get install -y openjdk-8-jdk
java -version

echo "========== [2/6] 安装 MySQL 5.7 (使用 MySQL 8.0 替代) =========="
apt-get install -y mysql-server
systemctl start mysql
systemctl enable mysql

# 设置 root 密码（默认 123456，后续可改）
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456'; FLUSH PRIVILEGES;" 2>/dev/null || true

echo "========== [3/6] 安装 Redis =========="
apt-get install -y redis-server
sed -i 's/^bind 127.0.0.1/bind 127.0.0.1/' /etc/redis/redis.conf
systemctl start redis-server
systemctl enable redis-server
redis-cli ping

echo "========== [4/6] 安装 Maven =========="
apt-get install -y maven
mvn -version

echo "========== [5/6] 克隆项目并编译 =========="
cd /opt
if [ -d "zuqiu" ]; then
    echo "项目目录已存在，拉取最新代码..."
    cd zuqiu
    git pull
else
    git clone https://github.com/1054077314/zuqiu.git
    cd zuqiu
fi

# 创建数据库并导入
mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS zuqiujulebguanli DEFAULT CHARACTER SET utf8mb4;" 2>/dev/null
mysql -u root -p123456 zuqiujulebguanli < db.sql 2>/dev/null || echo "数据库已导入或已存在"

# 编译打包
mvn clean package -DskipTests -q

echo "========== [6/6] 启动应用 =========="
# 先杀掉旧进程
pkill -f 'zuqiujulebguanli-0.0.1-SNAPSHOT.jar' 2>/dev/null || true
sleep 2

# 后台启动
nohup java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url="jdbc:mysql://127.0.0.1:3306/zuqiujulebguanli?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false" \
  --spring.datasource.username=root \
  --spring.datasource.password=123456 \
  --spring.redis.host=127.0.0.1 \
  --spring.redis.port=6379 \
  > /opt/app.log 2>&1 &

echo "等待应用启动..."
sleep 10

# 检查是否启动成功
if curl -s http://localhost:8080/zuqiujulebguanli/front/index.html | grep -q "html"; then
    echo ""
    echo "=========================================="
    echo "  部署成功！"
    echo "=========================================="
    echo ""
    echo "  前台门户: http://120.26.174.97:8080/zuqiujulebguanli/front/index.html"
    echo "  后台管理: http://120.26.174.97:8080/zuqiujulebguanli/admin/dist/index.html"
    echo ""
    echo "  管理员账号: manager / manager"
    echo "  日志文件: tail -f /opt/app.log"
    echo "=========================================="
else
    echo "应用可能还在启动中，请稍后检查："
    echo "  curl http://localhost:8080/zuqiujulebguanli/front/index.html"
    echo "  查看日志: tail -100 /opt/app.log"
fi
