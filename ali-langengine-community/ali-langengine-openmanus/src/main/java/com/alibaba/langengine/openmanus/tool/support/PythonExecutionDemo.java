/**
 * Copyright (C) 2024 AIDC-AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.openmanus.tool.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PythonExecutionDemo {

    public static void main(String[] args) {
        System.out.println("=== Python执行功能演示 ===\n");
        
        // 演示1: 代码提取功能
        demonstrateCodeExtraction();
        
        // 演示2: 基础Python执行
        demonstrateBasicExecution();
        
        // 演示3: 数学计算
        demonstrateMathCalculation();
        
        // 演示4: 错误处理
        demonstrateErrorHandling();
        
        // 演示5: 执行时间统计
        demonstrateTimingFeatures();
        
        // 演示6: 复杂数据处理
        demonstrateDataProcessing();
        
        System.out.println("\n=== 演示完成 ===");
    }
    
    private static void demonstrateCodeExtraction() {
        System.out.println("1. 代码提取功能演示:");
        
        String text = "这里有一些Python代码:\n```python\nprint('Hello, World!')\nx = 1 + 2\nprint(f'1 + 2 = {x}')\n```\n还有JavaScript代码:\n```javascript\nconsole.log('Hello from JS');\n```";
        
        List<SimplePair<String, String>> extracted = CodeUtils.extractCode(text, false);
        
        System.out.println("提取到 " + extracted.size() + " 个代码块:");
        for (int i = 0; i < extracted.size(); i++) {
            SimplePair<String, String> codeBlock = extracted.get(i);
            System.out.println("  代码块 " + (i + 1) + " (语言: " + codeBlock.getLeft() + "):");
            System.out.println("    " + codeBlock.getRight().replace("\n", "\n    "));
        }
        System.out.println();
    }
    
    private static void demonstrateBasicExecution() {
        System.out.println("2. 基础Python执行演示:");
        
        String code = "print('Hello from Python!')\nprint('Python版本检查')\nimport sys\nprint(f'Python版本: {sys.version_info.major}.{sys.version_info.minor}')";
        
        CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, new HashMap<>());
        result.calculateExecutionTime();
        
        System.out.println("执行结果:");
        System.out.println("  退出码: " + result.getExitcode());
        System.out.println("  执行时间: " + (result.getExecutionTime() != null ? result.getExecutionTime().toMillis() + "ms" : "未知"));
        System.out.println("  输出内容:");
        System.out.println("    " + result.getLogs().replace("\n", "\n    "));
        System.out.println();
    }
    
    private static void demonstrateMathCalculation() {
        System.out.println("3. 数学计算演示:");
        
        String code = "import math\n" +
                     "# 各种数学计算\n" +
                     "print(f'π的值: {math.pi:.6f}')\n" +
                     "print(f'e的值: {math.e:.6f}')\n" +
                     "print(f'√16 = {math.sqrt(16)}')\n" +
                     "print(f'2的10次方 = {2**10}')\n" +
                     "print(f'sin(π/2) = {math.sin(math.pi/2):.6f}')";
        
        CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, new HashMap<>());
        result.calculateExecutionTime();
        
        System.out.println("数学计算结果:");
        System.out.println("  退出码: " + result.getExitcode());
        System.out.println("  输出内容:");
        System.out.println("    " + result.getLogs().replace("\n", "\n    "));
        System.out.println();
    }
    
    private static void demonstrateErrorHandling() {
        System.out.println("4. 错误处理演示:");
        
        String code = "print('开始执行')\n" +
                     "try:\n" +
                     "    x = 1 / 0  # 这会引发除零错误\n" +
                     "except ZeroDivisionError as e:\n" +
                     "    print(f'捕获到错误: {e}')\n" +
                     "print('错误处理完成')";
        
        CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, new HashMap<>());
        result.calculateExecutionTime();
        
        System.out.println("错误处理结果:");
        System.out.println("  退出码: " + result.getExitcode());
        System.out.println("  输出内容:");
        System.out.println("    " + result.getLogs().replace("\n", "\n    "));
        
        // 演示未处理的错误
        String errorCode = "print('这会导致错误')\nraise ValueError('测试错误')\nprint('这行不会执行')";
        CodeExecutionResult errorResult = CodeUtils.executeCode(errorCode, "python", null, false, new HashMap<>());
        
        System.out.println("  未处理错误结果:");
        System.out.println("    退出码: " + errorResult.getExitcode());
        System.out.println("    错误输出: " + errorResult.getLogs().replace("\n", " | "));
        System.out.println();
    }
    
    private static void demonstrateTimingFeatures() {
        System.out.println("5. 执行时间统计演示:");
        
        String code = "import time\n" +
                     "print('开始延时任务')\n" +
                     "time.sleep(0.2)  # 睡眠200毫秒\n" +
                     "print('延时任务完成')";
        
        CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, new HashMap<>());
        result.calculateExecutionTime();
        
        System.out.println("时间统计结果:");
        System.out.println("  开始时间: " + result.getStartTime());
        System.out.println("  结束时间: " + result.getEndTime());
        System.out.println("  执行时间: " + (result.getExecutionTime() != null ? result.getExecutionTime().toMillis() + "ms" : "未知"));
        System.out.println("  输出内容:");
        System.out.println("    " + result.getLogs().replace("\n", "\n    "));
        System.out.println();
    }
    
    private static void demonstrateDataProcessing() {
        System.out.println("6. 复杂数据处理演示:");
        
        String code = "# 数据处理示例\n" +
                     "data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]\n" +
                     "print(f'原始数据: {data}')\n" +
                     "\n" +
                     "# 过滤偶数\n" +
                     "even_numbers = [x for x in data if x % 2 == 0]\n" +
                     "print(f'偶数: {even_numbers}')\n" +
                     "\n" +
                     "# 计算平方\n" +
                     "squares = [x**2 for x in data]\n" +
                     "print(f'平方: {squares}')\n" +
                     "\n" +
                     "# 统计信息\n" +
                     "print(f'总和: {sum(data)}')\n" +
                     "print(f'平均值: {sum(data)/len(data):.2f}')\n" +
                     "print(f'最大值: {max(data)}')\n" +
                     "print(f'最小值: {min(data)}')";
        
        CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, new HashMap<>());
        result.calculateExecutionTime();
        
        System.out.println("数据处理结果:");
        System.out.println("  退出码: " + result.getExitcode());
        System.out.println("  执行时间: " + (result.getExecutionTime() != null ? result.getExecutionTime().toMillis() + "ms" : "未知"));
        System.out.println("  输出内容:");
        System.out.println("    " + result.getLogs().replace("\n", "\n    "));
        System.out.println();
    }
}
