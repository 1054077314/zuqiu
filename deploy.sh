#!/bin/bash
# ============================================
# 足球俱乐部管理系统 - 一键部署脚本
# 适用于 Ubuntu 22.04 全新服务器
# ============================================

set -e

DB_PASSWORD="${DB_PASSWORD:-zuqiu_dev_pass}"
DEEPSEEK_API_KEY="${DEEPSEEK_API_KEY:-}"

echo "========== [1/6] 安装 JDK 17 =========="
apt-get update -qq
apt-get install -y openjdk-17-jdk
java -version

echo "========== [2/6] 安装 MySQL =========="
apt-get install -y mysql-server
systemctl start mysql
systemctl enable mysql

mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '${DB_PASSWORD}'; FLUSH PRIVILEGES;" 2>/dev/null || true

echo "========== [3/6] 安装 Redis =========="
apt-get install -y redis-server
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

mysql -u root -p"${DB_PASSWORD}" -e "CREATE DATABASE IF NOT EXISTS zuqiujulebguanli DEFAULT CHARACTER SET utf8mb4;" 2>/dev/null
mysql -u root -p"${DB_PASSWORD}" zuqiujulebguanli < db.sql 2>/dev/null || echo "数据库已导入或已存在"

mvn clean package -DskipTests -q

echo "========== [6/6] 启动应用 =========="
pkill -f 'zuqiujulebguanli-0.0.1-SNAPSHOT.jar' 2>/dev/null || true
sleep 2

export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:mysql://127.0.0.1:3306/zuqiujulebguanli?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false"
export DB_USERNAME=root
export DB_PASSWORD="${DB_PASSWORD}"
export REDIS_HOST=127.0.0.1
export REDIS_PORT=6379

nohup java -jar target/zuqiujulebguanli-0.0.1-SNAPSHOT.jar > /opt/app.log 2>&1 &

echo "等待应用启动..."
sleep 10

if curl -s http://localhost:8080/zuqiujulebguanli/front/index.html | grep -q "html"; then
    echo ""
    echo "=========================================="
    echo "  部署成功！"
    echo "=========================================="
    echo ""
    echo "  前台门户: http://<服务器IP>:8080/zuqiujulebguanli/front/index.html"
    echo "  后台管理: http://<服务器IP>:8080/zuqiujulebguanli/admin"
    echo "  AI 问答:  http://<服务器IP>:8080/zuqiujulebguanli/front/pages/chat/chat.html"
    echo ""
    echo "  请通过环境变量配置 DB_PASSWORD / DEEPSEEK_API_KEY"
    echo "  日志文件: tail -f /opt/app.log"
    echo "=========================================="
else
    echo "应用可能还在启动中，请稍后检查："
    echo "  curl http://localhost:8080/zuqiujulebguanli/front/index.html"
    echo "  查看日志: tail -100 /opt/app.log"
fi
