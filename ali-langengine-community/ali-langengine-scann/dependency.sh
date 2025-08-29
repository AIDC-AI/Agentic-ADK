#!/bin/bash

# ScaNN 模块依赖安装脚本

echo "Installing dependencies for ali-langengine-scann..."

# 安装 Maven 依赖
mvn clean install -DskipTests

echo "Dependencies installation completed for ali-langengine-scann."
