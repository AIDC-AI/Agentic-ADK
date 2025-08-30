#!/bin/bash

echo "=== 飞书工具集成验证脚本 ==="

# 检查Java环境
echo "1. 检查Java环境..."
java -version
javac -version

# 检查Maven环境
echo "2. 检查Maven环境..."
mvn -version

# 检查源代码文件
echo "3. 检查源代码文件..."
echo "主要类文件数量："
find src/main/java -name "*.java" | wc -l

echo "测试文件数量："
find src/test/java -name "*.java" | wc -l

# 检查依赖
echo "4. 检查依赖..."
mvn dependency:tree -Dverbose=false -DoutputFile=dependency-tree.txt
if [ -f dependency-tree.txt ]; then
    echo "依赖树已生成到 dependency-tree.txt"
    head -20 dependency-tree.txt
else
    echo "依赖树生成失败"
fi

# 尝试编译
echo "5. 尝试编译..."
mvn clean compile -Dmaven.compiler.verbose=true -Dmaven.compiler.debug=true > compile-output.txt 2>&1
COMPILE_RESULT=$?

if [ $COMPILE_RESULT -eq 0 ]; then
    echo "✅ 编译成功"
    echo "编译后的类文件："
    find target/classes -name "*.class" | head -10
else
    echo "❌ 编译失败，错误信息："
    tail -20 compile-output.txt
fi

# 检查编译后的类文件结构
echo "6. 检查编译结果..."
if [ -d target/classes/com/alibaba/langengine/feishu ]; then
    echo "✅ 飞书模块类文件已生成"
    find target/classes/com/alibaba/langengine/feishu -name "*.class" | wc -l
    echo "个类文件"
else
    echo "❌ 飞书模块类文件未生成"
fi

# 尝试运行单个测试
echo "7. 尝试运行单个测试..."
mvn test -Dtest=FeishuConfigurationTest > test-output.txt 2>&1
TEST_RESULT=$?

if [ $TEST_RESULT -eq 0 ]; then
    echo "✅ 测试运行成功"
else
    echo "❌ 测试运行失败，错误信息："
    tail -10 test-output.txt
fi

echo "=== 验证完成 ==="
echo "详细日志文件："
echo "- compile-output.txt: 编译输出"
echo "- test-output.txt: 测试输出"
echo "- dependency-tree.txt: 依赖树"
