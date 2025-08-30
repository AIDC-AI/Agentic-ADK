#!/bin/bash

# Feishu LangEngine Module Package Script
# This script packages the Feishu integration module

echo "Packaging ali-langengine-feishu module..."

# Clean and package
mvn clean package

echo "ali-langengine-feishu module packaged successfully!"
