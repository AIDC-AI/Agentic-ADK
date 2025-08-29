#!/bin/bash

# ScaNN 模块部署脚本

echo "Deploying ali-langengine-scann module..."

# 编译、测试和部署
mvn clean compile test package deploy

if [ $? -eq 0 ]; then
    echo "ali-langengine-scann module deployed successfully."
else
    echo "Failed to deploy ali-langengine-scann module."
    exit 1
fi
