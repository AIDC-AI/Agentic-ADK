#!/bin/bash

# Feishu LangEngine Module Dependency Script
# This script handles dependency management for the Feishu integration module

echo "Installing dependencies for ali-langengine-feishu..."

# Install Maven dependencies
mvn clean install -DskipTests

echo "Dependencies installed successfully!"
