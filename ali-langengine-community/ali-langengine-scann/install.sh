#!/bin/bash

# ScaNN 模块安装脚本

echo "Installing ali-langengine-scann module..."

# 编译和安装模块
mvn clean compile install -DskipTests

if [ $? -eq 0 ]; then
    echo "ali-langengine-scann module installed successfully."
else
    echo "Failed to install ali-langengine-scann module."
    exit 1
fi
