#!/bin/bash

# Feishu LangEngine Module Deploy Script
# This script deploys the Feishu integration module

echo "Deploying ali-langengine-feishu module..."

# Package and deploy
mvn clean package deploy

echo "ali-langengine-feishu module deployed successfully!"
