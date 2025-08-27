/**
 * Copyright (C) 2024 AIDC-AI
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.openmanus.tool;

import com.alibaba.langengine.core.callback.ExecutionContext;
import com.alibaba.langengine.core.tool.BaseTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;


public class PythonExecute extends BaseTool {

    // === 配置属性 ===
    
    /**
     * 是否使用ARM64架构（仅在macOS上有效）
     */
    private Boolean arm64 = false;
    
    /**
     * 工作目录（可选）
     */
    private String workingDirectory;
    
    /**
     * 执行超时时间（秒）
     */
    private Integer timeoutSeconds = 30;
    
    /**
     * 是否启用详细日志
     */
    private Boolean verboseLogging = false;

    /**
     * 工具名称
     */
    private String name = "python_execute";
    
    /**
     * 工具描述
     */
    private String description = "执行Python代码字符串。注意：只有print输出是可见的，函数返回值不会被捕获。使用print语句来查看结果。支持复杂的数据处理、计算和可视化任务。";

    // === 常量定义 ===
    
    /**
     * 工具参数的JSON Schema定义
     */
    private static final String PARAMETERS = "{\n" +
            "\t\"type\": \"object\",\n" +
            "\t\"properties\": {\n" +
            "\t\t\"code\": {\n" +
            "\t\t\t\"type\": \"string\",\n" +
            "\t\t\t\"description\": \"要执行的Python代码。注意：只有print输出是可见的，函数返回值不会被捕获。使用print语句来查看结果。\"\n" +
            "\t\t},\n" +
            "\t\t\"filename\": {\n" +
            "\t\t\t\"type\": \"string\",\n" +
            "\t\t\t\"description\": \"可选的文件名，如果不提供将自动生成\"\n" +
            "\t\t},\n" +
            "\t\t\"work_dir\": {\n" +
            "\t\t\t\"type\": \"string\",\n" +
            "\t\t\t\"description\": \"可选的工作目录\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"required\": [\"code\"]\n" +
            "}";

    /**
     * 默认的数学计算Python代码模板
     */
    private static final String LLMMATH_PYTHON_CODE = 
        "import math\n" +
        "import numpy as np\n" +
        "try:\n" +
        "    result = %s\n" +
        "    print(f\"计算结果: {result}\")\n" +
        "except Exception as e:\n" +
        "    print(f\"计算错误: {e}\")\n";

    // === 构造方法 ===
    
    public PythonExecute() {
        setName(name);
        setDescription(description);
        setParameters(PARAMETERS);
        initializeTool();
    }
    
    /**
     * 使用自定义配置初始化
     * @param arm64 是否使用ARM64架构
     * @param workingDirectory 工作目录
     * @param timeoutSeconds 超时时间（秒）
     */
    public PythonExecute(Boolean arm64, String workingDirectory, Integer timeoutSeconds) {
        this.arm64 = arm64;
        this.workingDirectory = workingDirectory;
        this.timeoutSeconds = timeoutSeconds;
        setName(name);
        setDescription(description);
        setParameters(PARAMETERS);
        initializeTool();
    }
    
    /**
     * 初始化工具配置
     */
    private void initializeTool() {
        if (verboseLogging) {
            System.out.println("PythonExecute tool initialized with arm64=" + arm64 + 
                             ", workingDirectory=" + workingDirectory + 
                             ", timeoutSeconds=" + timeoutSeconds);
        }
    }

    // === 核心执行方法 ===
    
    /**
     * 执行Python代码
     * 
     * @param toolInput JSON格式的输入参数或直接的Python代码
     * @return 执行结果
     */
    public PythonExecutionResult execute(String toolInput) {
        System.out.println("PythonExecute executing with input: " + toolInput);
        
        try {
            // 解析输入参数
            Map<String, Object> params = parseInput(toolInput);
            
            String code = (String) params.get("code");
            String filename = (String) params.get("filename");
            String workDir = (String) params.get("work_dir");
            
            // 验证必需参数
            if (code == null || code.trim().isEmpty()) {
                String errorMsg = "Python代码不能为空";
                System.err.println(errorMsg);
                return new PythonExecutionResult(errorMsg, false);
            }
            
            // 准备执行环境
            String effectiveWorkDir = determineWorkingDirectory(workDir);
            String effectiveFilename = generateFilename(filename);
            
            if (verboseLogging) {
                System.out.println("Executing Python code with filename: " + effectiveFilename);
                System.out.println("Python code to execute:\n" + code);
            }
            
            // 执行代码
            CodeExecutionResult executionResult = executeCode(code, effectiveFilename, effectiveWorkDir);
            
            // 处理执行结果
            String result = formatExecutionResult(executionResult);
            boolean success = executionResult.getExitCode() == 0;
            
            if (success) {
                System.out.println("Python code executed successfully");
            } else {
                System.err.println("Python code execution failed with exit code: " + executionResult.getExitCode());
            }
            
            return new PythonExecutionResult(result, success, executionResult);
            
        } catch (Exception e) {
            String errorMsg = "Python代码执行失败: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return new PythonExecutionResult(errorMsg, false);
        }
    }
    
    /**
     * 解析输入参数
     * 支持JSON格式和直接代码字符串两种形式
     */
    private Map<String, Object> parseInput(String input) {
        Map<String, Object> params = new HashMap<>();
        
        // 简单判断是否为JSON格式
        if (input.trim().startsWith("{") && input.trim().endsWith("}")) {
            // 简单的JSON解析（仅支持基本情况）
            try {
                params = parseSimpleJson(input);
            } catch (Exception e) {
                // 如果JSON解析失败，将整个输入作为代码
                params.put("code", input);
            }
        } else {
            // 直接作为Python代码处理
            params.put("code", input);
        }
        
        return params;
    }
    
    /**
     * 简单的JSON解析（仅支持基本的键值对）
     * 注意：这是一个简化实现，生产环境建议使用Jackson或Gson
     */
    private Map<String, Object> parseSimpleJson(String json) {
        Map<String, Object> result = new HashMap<>();
        
        // 输入验证
        if (json == null || json.trim().length() < 2) {
            throw new IllegalArgumentException("Invalid JSON input");
        }
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("JSON must start with { and end with }");
        }
        
        // 移除首尾的大括号
        json = json.substring(1, json.length() - 1).trim();
        
        if (json.isEmpty()) {
            return result;
        }
        
        // 简单的分割和解析
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("[\"']", "");
                String value = keyValue[1].trim().replaceAll("[\"']", "");
                if (!key.isEmpty()) {
                    result.put(key, value);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 确定工作目录
     */
    private String determineWorkingDirectory(String inputWorkDir) {
        if (inputWorkDir != null && !inputWorkDir.trim().isEmpty()) {
            return inputWorkDir;
        }
        if (workingDirectory != null && !workingDirectory.trim().isEmpty()) {
            return workingDirectory;
        }
        return System.getProperty("user.dir");
    }
    
    /**
     * 生成文件名
     */
    private String generateFilename(String inputFilename) {
        if (inputFilename != null && !inputFilename.trim().isEmpty()) {
            return inputFilename.endsWith(".py") ? inputFilename : inputFilename + ".py";
        }
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "python_exec_" + uuid + ".py";
    }
    
    /**
     * 执行Python代码
     */
    private CodeExecutionResult executeCode(String code, String filename, String workDir) throws Exception {
        Instant startTime = Instant.now();
        
        // 创建工作目录
        Path workDirPath = Paths.get(workDir);
        if (!Files.exists(workDirPath)) {
            Files.createDirectories(workDirPath);
        }
        
        // 创建Python文件
        Path filePath = workDirPath.resolve(filename);
        Files.write(filePath, code.getBytes("UTF-8"));
        
        try {
            // 准备执行命令
            String[] command = buildCommand(filePath);
            
            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(workDirPath.toFile());
            processBuilder.redirectErrorStream(false);
            
            Process process = processBuilder.start();
            
            // 处理输出
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<String> outputFuture = executor.submit(() -> readStream(process.getInputStream()));
            Future<String> errorFuture = executor.submit(() -> readStream(process.getErrorStream()));
            
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            
            if (!finished) {
                process.destroyForcibly();
                throw new TimeoutException("Python代码执行超时 (" + timeoutSeconds + "秒)");
            }
            
            String output = outputFuture.get();
            String errorOutput = errorFuture.get();
            int exitCode = process.exitValue();
            
            executor.shutdown();
            
            Instant endTime = Instant.now();
            Duration executionTime = Duration.between(startTime, endTime);
            
            return new CodeExecutionResult(output, errorOutput, exitCode, executionTime, filePath.toString());
            
        } finally {
            // 清理临时文件
            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                System.err.println("Warning: Failed to clean up temp file: " + filePath + ", error: " + e.getMessage());
            }
        }
    }
    
    /**
     * 构建执行命令
     */
    private String[] buildCommand(Path filePath) {
        if (System.getProperty("os.name").toLowerCase().contains("mac") && arm64) {
            return new String[]{"arch", "-arm64", "python3", filePath.toString()};
        } else if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return new String[]{"python", filePath.toString()};
        } else {
            return new String[]{"python3", filePath.toString()};
        }
    }
    
    /**
     * 读取流内容
     */
    private String readStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            // 忽略读取错误
        }
        return output.toString();
    }
    
    /**
     * 格式化执行结果
     */
    private String formatExecutionResult(CodeExecutionResult executionResult) {
        if (executionResult.getExitCode() == 0) {
            // 成功时返回输出内容
            String output = executionResult.getOutput();
            if (output == null || output.trim().isEmpty()) {
                return "代码执行成功，但没有输出内容";
            }
            return output.trim();
        } else {
            // 失败时返回错误信息
            StringBuilder errorBuilder = new StringBuilder();
            errorBuilder.append("Python代码执行失败 (退出码: ").append(executionResult.getExitCode()).append(")");
            
            if (executionResult.hasOutput()) {
                errorBuilder.append("\n输出: ").append(executionResult.getOutput().trim());
            }
            
            if (executionResult.hasErrorOutput()) {
                errorBuilder.append("\n错误: ").append(executionResult.getErrorOutput().trim());
            }
            
            if (executionResult.getExecutionTime() != null) {
                errorBuilder.append("\n执行时间: ").append(executionResult.getExecutionTime().toMillis()).append("ms");
            }
            
            return errorBuilder.toString();
        }
    }

    // === Getter 和 Setter 方法 ===
    
    public Boolean getArm64() {
        return arm64;
    }

    public void setArm64(Boolean arm64) {
        this.arm64 = arm64;
    }
    
    public String getWorkingDirectory() {
        return workingDirectory;
    }
    
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
    
    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }
    
    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
    
    public Boolean getVerboseLogging() {
        return verboseLogging;
    }
    
    public void setVerboseLogging(Boolean verboseLogging) {
        this.verboseLogging = verboseLogging;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameters() {
        return PARAMETERS;
    }

    // === 内部类定义 ===
    
    /**
     * 代码执行结果类
     */
    public static class CodeExecutionResult {
        private final String output;
        private final String errorOutput;
        private final int exitCode;
        private final Duration executionTime;
        private final String filePath;

        public CodeExecutionResult(String output, String errorOutput, int exitCode, Duration executionTime, String filePath) {
            this.output = output;
            this.errorOutput = errorOutput;
            this.exitCode = exitCode;
            this.executionTime = executionTime;
            this.filePath = filePath;
        }

        public String getOutput() {
            return output;
        }

        public String getErrorOutput() {
            return errorOutput;
        }

        public int getExitCode() {
            return exitCode;
        }

        public Duration getExecutionTime() {
            return executionTime;
        }

        public String getFilePath() {
            return filePath;
        }

        public boolean hasOutput() {
            return output != null && !output.trim().isEmpty();
        }

        public boolean hasErrorOutput() {
            return errorOutput != null && !errorOutput.trim().isEmpty();
        }

        @Override
        public String toString() {
            return String.format("CodeExecutionResult{exitCode=%d, executionTime=%dms, hasOutput=%s, hasErrorOutput=%s}",
                exitCode, executionTime.toMillis(), hasOutput(), hasErrorOutput());
        }
    }

    /**
     * Python执行结果类
     */
    public static class PythonExecutionResult {
        private final String result;
        private final boolean success;
        private final CodeExecutionResult codeExecutionResult;

        public PythonExecutionResult(String result, boolean success) {
            this.result = result;
            this.success = success;
            this.codeExecutionResult = null;
        }

        public PythonExecutionResult(String result, boolean success, CodeExecutionResult codeExecutionResult) {
            this.result = result;
            this.success = success;
            this.codeExecutionResult = codeExecutionResult;
        }

        public String getResult() {
            return result;
        }

        public boolean isSuccess() {
            return success;
        }

        public CodeExecutionResult getCodeExecutionResult() {
            return codeExecutionResult;
        }

        @Override
        public String toString() {
            return String.format("PythonExecutionResult{success=%s, result='%s'}", success, result);
        }
    }

    /**
     * 实现BaseTool的run方法
     */
    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        onToolStart(this, toolInput, executionContext);
        
        try {
            PythonExecutionResult result = execute(toolInput);
            
            ToolExecuteResult toolResult = new ToolExecuteResult();
            toolResult.setOutput(result.getResult());
            toolResult.setInterrupted(!result.isSuccess());
            
            onToolEnd(this, toolInput, toolResult, executionContext);
            return toolResult;
            
        } catch (Exception e) {
            onToolError(this, e, executionContext);
            
            ToolExecuteResult errorResult = new ToolExecuteResult();
            errorResult.setOutput("Python执行工具发生错误: " + e.getMessage());
            errorResult.setInterrupted(true);
            
            return errorResult;
        }
    }

    // === 测试和演示方法 ===
    
    public static void main(String[] args) {
        System.out.println("=== Python执行工具测试 ===");
        
        PythonExecute pythonExecute = new PythonExecute();
        pythonExecute.setVerboseLogging(true);
        
        // 测试1：基本功能
        System.out.println("\n--- 测试1：基本打印功能 ---");
        String basicTest = "print('Hello, World!')";
        PythonExecutionResult basicResult = pythonExecute.execute(basicTest);
        System.out.println("结果: " + basicResult.toString());
        
        // 测试2：数学计算
        System.out.println("\n--- 测试2：数学计算 ---");
        String mathTest = String.format(LLMMATH_PYTHON_CODE, "2 + 3 * 5");
        PythonExecutionResult mathResult = pythonExecute.execute(mathTest);
        System.out.println("结果: " + mathResult.toString());
        
        // 测试3：JSON输入格式
        System.out.println("\n--- 测试3：JSON输入格式 ---");
        String jsonInput = "{\"code\":\"print('JSON输入测试成功')\",\"filename\":\"test.py\"}";
        PythonExecutionResult jsonResult = pythonExecute.execute(jsonInput);
        System.out.println("结果: " + jsonResult.toString());
        
        // 测试4：错误处理
        System.out.println("\n--- 测试4：错误处理 ---");
        String errorTest = "print('开始执行'); invalid_syntax_here; print('不会执行到这里')";
        PythonExecutionResult errorResult = pythonExecute.execute(errorTest);
        System.out.println("结果: " + errorResult.toString());
        
        System.out.println("\n=== 测试完成 ===");
    }
}
