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
import java.util.Map;


public class CodeUtilsTest {

    private static int testCount = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("开始 CodeUtils 测试...\n");
        
        testExecutePythonCodeSuccess();
        testExecutePythonCodeWithError();
        testExecutePythonCodeWithMath();
        testCodeExecutionTiming();
        testMd5Hash();
        testContentStrWithString();
        testContentStrWithNull();
        testExecuteCommandWithValidCommand();
        testCodeExecutionResultBuilder();
        testCodeExecutionResultStaticMethods();
        
        System.out.println("\n=== 测试完成 ===");
        System.out.println("总计: " + testCount + " 个测试");
        System.out.println("通过: " + passedTests + " 个测试");
        System.out.println("失败: " + (testCount - passedTests) + " 个测试");
        
        if (passedTests == testCount) {
            System.out.println("🎉 所有测试通过！");
        } else {
            System.out.println("❌ 部分测试失败");
        }
    }

    public static void testExecutePythonCodeSuccess() {
        System.out.println("测试: Python代码执行成功");
        testCount++;
        
        try {
            String code = "print('Hello from Python!')";
            Map<String, Object> kwargs = new HashMap<>();
            
            CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, kwargs);
            
            if (result != null && result.getExitcode() == 0 && 
                result.getLogs().contains("Hello from Python!") &&
                result.getStartTime() != null && result.getEndTime() != null) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 执行结果不符合预期");
                System.out.println("  退出码: " + (result != null ? result.getExitcode() : "null"));
                System.out.println("  输出: " + (result != null ? result.getLogs() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testExecutePythonCodeWithError() {
        System.out.println("测试: Python代码执行失败");
        testCount++;
        
        try {
            String code = "print('Start')\nraise ValueError('Test error')\nprint('End')";
            Map<String, Object> kwargs = new HashMap<>();
            
            CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, kwargs);
            
            if (result != null && result.getExitcode() != 0 && 
                (result.getLogs().contains("ValueError") || result.getLogs().contains("Test error"))) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 执行应该失败但没有失败");
                System.out.println("  退出码: " + (result != null ? result.getExitcode() : "null"));
                System.out.println("  输出: " + (result != null ? result.getLogs() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testExecutePythonCodeWithMath() {
        System.out.println("测试: Python数学计算");
        testCount++;
        
        try {
            String code = "import math\nresult = math.sqrt(16)\nprint(f'Square root of 16 is {result}')";
            Map<String, Object> kwargs = new HashMap<>();
            
            CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, kwargs);
            
            if (result != null && result.getExitcode() == 0 && result.getLogs().contains("4.0")) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 数学计算结果不正确");
                System.out.println("  退出码: " + (result != null ? result.getExitcode() : "null"));
                System.out.println("  输出: " + (result != null ? result.getLogs() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testCodeExecutionTiming() {
        System.out.println("测试: 代码执行时间计算");
        testCount++;
        
        try {
            String code = "import time\ntime.sleep(0.1)\nprint('Done')";
            Map<String, Object> kwargs = new HashMap<>();
            
            CodeExecutionResult result = CodeUtils.executeCode(code, "python", null, false, kwargs);
            result.calculateExecutionTime();
            
            if (result != null && result.getExitcode() == 0 && 
                result.getExecutionTime() != null && 
                result.getExecutionTime().toMillis() >= 100) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 执行时间计算不正确");
                System.out.println("  执行时间: " + (result != null && result.getExecutionTime() != null ? 
                    result.getExecutionTime().toMillis() + "ms" : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testMd5Hash() {
        System.out.println("测试: MD5哈希计算");
        testCount++;
        
        try {
            String input = "test string";
            String hash1 = CodeUtils.md5(input);
            String hash2 = CodeUtils.md5(input);
            
            if (hash1 != null && hash1.equals(hash2) && hash1.length() == 32) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: MD5哈希计算不正确");
                System.out.println("  哈希1: " + hash1);
                System.out.println("  哈希2: " + hash2);
                System.out.println("  长度: " + (hash1 != null ? hash1.length() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testContentStrWithString() {
        System.out.println("测试: contentStr处理字符串");
        testCount++;
        
        try {
            String input = "Simple string";
            String result = CodeUtils.contentStr(input);
            
            if (input.equals(result)) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 字符串处理不正确");
                System.out.println("  输入: " + input);
                System.out.println("  输出: " + result);
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testContentStrWithNull() {
        System.out.println("测试: contentStr处理null");
        testCount++;
        
        try {
            String result = CodeUtils.contentStr(null);
            
            if ("".equals(result)) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: null处理不正确");
                System.out.println("  输出: " + result);
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testExecuteCommandWithValidCommand() {
        System.out.println("测试: 执行系统命令");
        testCount++;
        
        try {
            ExecuteCommandResult result;
            // 根据操作系统选择合适的命令
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                result = CodeUtils.executeCommand("cmd", "/c", "echo Hello");
            } else {
                result = CodeUtils.executeCommand("echo", "Hello");
            }
            
            if (result != null && result.getExitCode() == 0 && result.getOutput().contains("Hello")) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 命令执行不正确");
                System.out.println("  退出码: " + (result != null ? result.getExitCode() : "null"));
                System.out.println("  输出: " + (result != null ? result.getOutput() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testCodeExecutionResultBuilder() {
        System.out.println("测试: CodeExecutionResult 构造函数");
        testCount++;
        
        try {
            CodeExecutionResult result = new CodeExecutionResult();
            result.setExitcode(0);
            result.setLogs("Success output");
            
            if (result != null && result.getExitcode() == 0 && 
                "Success output".equals(result.getLogs()) && result.hasOutput()) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 构造函数不正确");
                System.out.println("  退出码: " + (result != null ? result.getExitcode() : "null"));
                System.out.println("  日志: " + (result != null ? result.getLogs() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }

    public static void testCodeExecutionResultStaticMethods() {
        System.out.println("测试: CodeExecutionResult静态方法");
        testCount++;
        
        try {
            // 测试成功结果创建
            CodeExecutionResult successResult = CodeExecutionResult.success("Success!");
            
            // 测试失败结果创建
            CodeExecutionResult failureResult = CodeExecutionResult.failure(1, "Error occurred");
            
            if (successResult != null && successResult.getExitcode() == 0 && 
                "Success!".equals(successResult.getLogs()) &&
                failureResult != null && failureResult.getExitcode() == 1 && 
                "Error occurred".equals(failureResult.getErrorOutput())) {
                System.out.println("✅ 通过");
                passedTests++;
            } else {
                System.out.println("❌ 失败: 静态方法不正确");
                System.out.println("  成功结果退出码: " + (successResult != null ? successResult.getExitcode() : "null"));
                System.out.println("  失败结果退出码: " + (failureResult != null ? failureResult.getExitcode() : "null"));
            }
        } catch (Exception e) {
            System.out.println("❌ 失败: " + e.getMessage());
        }
        System.out.println();
    }
}
