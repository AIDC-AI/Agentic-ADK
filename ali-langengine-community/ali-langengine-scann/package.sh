#!/bin/bash

# ScaNN 模块打包脚本

echo "Packaging ali-langengine-scann module..."

# 清理并打包
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "ali-langengine-scann module packaged successfully."
    echo "JAR files are available in target/ directory."
else
    echo "Failed to package ali-langengine-scann module."
    exit 1
fi
