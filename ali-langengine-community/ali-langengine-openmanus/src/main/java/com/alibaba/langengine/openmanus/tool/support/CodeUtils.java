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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CodeUtils {

    private static final String CODE_BLOCK_PATTERN = "```(\\w*)\n(.*?)\n```";
    private static final String SINGLE_LINE_CODE_PATTERN = "`([^`]+)`";
    private static final String UNKNOWN = "unknown";
    private static final int DEFAULT_TIMEOUT_SECONDS = 600;
    private static final String WORKING_DIR = Paths.get(System.getProperty("user.dir"), "extensions").toString();
    
    // 支持的编程语言映射
    private static final Map<String, String> LANGUAGE_EXTENSIONS = Map.of(
        "python", "py",
        "py", "py",
        "java", "java",
        "javascript", "js",
        "js", "js",
        "sh", "sh",
        "shell", "sh",
        "bash", "sh",
        "ps1", "ps1",
        "powershell", "ps1"
    );
    
    // 支持的语言命令映射
    private static final Map<String, List<String>> LANGUAGE_COMMANDS = Map.of(
        "python", List.of("python3", "python"),
        "py", List.of("python3", "python"),
        "java", List.of("java"),
        "javascript", List.of("node"),
        "js", List.of("node"),
        "sh", List.of("sh"),
        "shell", List.of("sh"),
        "bash", List.of("bash"),
        "ps1", List.of("powershell", "pwsh"),
        "powershell", List.of("powershell", "pwsh")
    );

    /**
     * 从文本中提取代码块
     * 
     * @param text 包含代码的文本
     * @param detectSingleLineCode 是否检测单行代码
     * @return 代码块列表，包含语言和代码内容
     */
    public static List<SimplePair<String, String>> extractCode(String text, boolean detectSingleLineCode) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<SimplePair<String, String>> extracted = new ArrayList<>();
        String content = contentStr(text);
        
        try {
            // 提取多行代码块
            extractMultiLineCode(content, extracted);
            
            // 如果需要，提取单行代码
            if (detectSingleLineCode && extracted.isEmpty()) {
                extractSingleLineCode(content, extracted);
            }
            
            // 如果没有找到任何代码块，将整个内容作为未知类型代码
            if (extracted.isEmpty()) {
                extracted.add(SimplePair.of(UNKNOWN, content.trim()));
            }
            
        } catch (Exception e) {
            System.err.println("Error extracting code from text: " + e.getMessage());
            throw new RuntimeException("Failed to extract code from text: " + e.getMessage(), e);
        }
        
        return extracted;
    }
    
    /**
     * 提取多行代码块
     */
    private static void extractMultiLineCode(String content, List<SimplePair<String, String>> extracted) {
        Pattern codeBlockPattern = Pattern.compile(CODE_BLOCK_PATTERN, Pattern.DOTALL);
        Matcher matcher = codeBlockPattern.matcher(content);
        
        while (matcher.find()) {
            String lang = matcher.group(1);
            String code = matcher.group(2);
            
            if (code != null && !code.trim().isEmpty()) {
                String normalizedLang = normalizeLang(lang);
                extracted.add(SimplePair.of(normalizedLang, code.trim()));
            }
        }
    }
    
    /**
     * 提取单行代码
     */
    private static void extractSingleLineCode(String content, List<SimplePair<String, String>> extracted) {
        Pattern singleLinePattern = Pattern.compile(SINGLE_LINE_CODE_PATTERN);
        Matcher matcher = singleLinePattern.matcher(content);
        
        while (matcher.find()) {
            String code = matcher.group(1);
            if (code != null && !code.trim().isEmpty()) {
                extracted.add(SimplePair.of(UNKNOWN, code.trim()));
            }
        }
    }
    
    /**
     * 标准化语言名称
     */
    private static String normalizeLang(String lang) {
        if (lang == null || lang.trim().isEmpty()) {
            return UNKNOWN;
        }
        return lang.toLowerCase().trim();
    }

    /**
     * 将内容对象转换为字符串
     * 支持字符串和结构化内容对象
     * 
     * @param content 内容对象
     * @return 字符串形式的内容
     */
    public static String contentStr(Object content) {
        if (content == null) {
            return "";
        }
        
        if (content instanceof String) {
            return (String) content;
        }
        
        try {
            StringBuilder result = new StringBuilder();
            
            if (content instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) content;
                
                for (Map<String, Object> item : itemList) {
                    if (item == null) {
                        continue;
                    }
                    
                    Object type = item.get("type");
                    if ("text".equals(type)) {
                        Object text = item.get("text");
                        if (text != null) {
                            result.append(text.toString());
                        }
                    } else if ("image_url".equals(type)) {
                        // 对于图像内容，添加占位符或说明
                        result.append("[图像内容]");
                    }
                }
            } else {
                // 对于其他类型的对象，尝试转换为字符串
                result.append(content.toString());
            }
            
            return result.toString();
            
        } catch (ClassCastException e) {
            System.err.println("Unexpected content format, falling back to toString(): " + e.getMessage());
            return content.toString();
        } catch (Exception e) {
            System.err.println("Error converting content to string: " + e.getMessage());
            return content.toString();
        }
    }

    /**
     * 执行代码（简化版本）
     * 
     * @param code 要执行的代码
     * @param lang 编程语言
     * @param filename 文件名（可选）
     * @param arm64 是否使用ARM64架构（仅macOS）
     * @param kwargs 额外参数
     * @return 执行结果
     */
    public static CodeExecutionResult executeCode(String code, String lang, String filename, Boolean arm64, Map<String, Object> kwargs) {
        System.out.println("Executing code: language=" + lang + ", filename=" + filename + ", arm64=" + arm64);
        
        // 验证输入参数
        if (code == null && filename == null) {
            throw new IllegalArgumentException("Either code or filename must be provided");
        }
        
        if (lang == null || lang.trim().isEmpty()) {
            throw new IllegalArgumentException("Language must be specified");
        }
        
        CodeExecutionResult result = new CodeExecutionResult();
        result.setStartTime(LocalDateTime.now());
        
        try {
            String workDir = (kwargs != null && kwargs.containsKey("work_dir")) ? 
                (String) kwargs.get("work_dir") : WORKING_DIR;
            
            // 生成文件名
            if (filename == null) {
                String codeHash = md5(code);
                String extension = getFileExtension(lang);
                filename = String.format("tmp_code_%s.%s", codeHash, extension);
            }
            
            String filepath = Paths.get(workDir, filename).toString();
            String fileDir = Paths.get(filepath).getParent().toString();
            
            // 确保目录存在
            Files.createDirectories(Paths.get(fileDir));
            
            // 写入代码到文件（使用UTF-8编码）
            if (code != null) {
                try (FileWriter writer = new FileWriter(filepath, java.nio.charset.StandardCharsets.UTF_8)) {
                    writer.write(code);
                }
            }
            
            System.out.println("Code file prepared: " + filepath);
            
            // 执行命令
            ExecuteCommandResult commandResult = executeCodeFile(lang, filepath, arm64);
            
            // 构建结果
            result.setExitcode(commandResult.getExitCode());
            result.setLogs(commandResult.getOutput());
            result.setEndTime(LocalDateTime.now());
            result.calculateExecutionTime();
            
            System.out.println("Code execution completed: exitCode=" + result.getExitcode() + 
                              ", hasOutput=" + result.hasOutput());
            
            return result;
            
        } catch (Exception e) {
            System.err.println("Error executing code: " + e.getMessage());
            CodeExecutionResult errorResult = new CodeExecutionResult();
            errorResult.setExitcode(-1);
            errorResult.setLogs("执行错误: " + e.getMessage());
            errorResult.setEndTime(LocalDateTime.now());
            return errorResult;
        }
    }
    
    /**
     * 执行代码文件
     */
    private static ExecuteCommandResult executeCodeFile(String lang, String filepath, Boolean arm64) {
        String normalizedLang = normalizeLang(lang);
        List<String> commands = new ArrayList<>();
        
        switch (normalizedLang) {
            case "python":
            case "py":
                if (arm64 != null && isMacOS()) {
                    commands.add("arch");
                    commands.add(arm64 ? "-arm64" : "-x86_64");
                }
                // 在Windows上尝试不同的Python命令
                if (isWindows()) {
                    commands.add("python");  // Windows通常使用python而不是python3
                } else {
                    commands.add("python3");
                }
                commands.add(filepath);
                break;
                
            case "sh":
            case "shell":
            case "bash":
                if (isWindows()) {
                    commands.add("cmd");
                    commands.add("/c");
                    commands.add("type"); // Windows的type命令相当于cat
                } else {
                    commands.add("sh");
                }
                commands.add(filepath);
                break;
                
            case "java":
                // 对于Java，需要先编译再执行
                throw new UnsupportedOperationException("Java execution not yet implemented");
                
            case "javascript":
            case "js":
                commands.add("node");
                commands.add(filepath);
                break;
                
            case "ps1":
            case "powershell":
                if (isWindows()) {
                    commands.add("powershell");
                    commands.add("-File");
                } else {
                    commands.add("pwsh");
                    commands.add("-File");
                }
                commands.add(filepath);
                break;
                
            default:
                throw new UnsupportedOperationException("Unsupported language: " + lang);
        }
        
        return executeCommand(commands.toArray(new String[0]));
    }
    
    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String language) {
        String normalizedLang = normalizeLang(language);
        return LANGUAGE_EXTENSIONS.getOrDefault(normalizedLang, "txt");
    }
    
    /**
     * 检查是否为Windows系统
     */
    private static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("windows");
    }
    
    /**
     * 检查是否为macOS系统
     */
    private static boolean isMacOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("mac") || osName.contains("darwin");
    }

    /**
     * 计算字符串的MD5哈希值
     * 
     * @param input 输入字符串
     * @return MD5哈希值
     */
    public static String md5(String input) {
        if (input == null) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，使用简单的哈希
            return String.valueOf(input.hashCode());
        }
    }

    /**
     * 执行系统命令
     * 
     * @param command 要执行的命令和参数
     * @return 执行结果
     */
    public static ExecuteCommandResult executeCommand(String... command) {
        if (command == null || command.length == 0) {
            throw new IllegalArgumentException("Command cannot be null or empty");
        }
        
        System.out.println("Executing command: " + Arrays.toString(command));
        
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(false); // 分别处理标准输出和错误输出
            
            Process process = processBuilder.start();
            
            // 同时读取标准输出和错误输出
            CompletableFuture<String> outputFuture = CompletableFuture.supplyAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    return readStream(reader);
                } catch (IOException e) {
                    System.err.println("Error reading process output: " + e.getMessage());
                    return "";
                }
            });
            
            CompletableFuture<String> errorFuture = CompletableFuture.supplyAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    return readStream(reader);
                } catch (IOException e) {
                    System.err.println("Error reading process error stream: " + e.getMessage());
                    return "";
                }
            });
            
            // 等待进程完成
            int exitCode = process.waitFor();
            
            // 获取输出结果
            String output = outputFuture.get();
            String errorOutput = errorFuture.get();
            
            System.out.println("Command completed: exitCode=" + exitCode + ", outputLength=" + 
                              output.length() + ", errorLength=" + errorOutput.length());
            
            // 构建结果
            ExecuteCommandResult result = new ExecuteCommandResult();
            result.setExitCode(exitCode);
            
            // 如果执行成功，返回标准输出；如果失败，返回错误输出
            if (exitCode == 0) {
                result.setOutput(output);
            } else {
                result.setOutput(errorOutput.isEmpty() ? output : errorOutput);
            }
            
            return result;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Command execution was interrupted: " + e.getMessage());
            
            ExecuteCommandResult result = new ExecuteCommandResult();
            result.setExitCode(-1);
            result.setOutput("命令执行被中断: " + e.getMessage());
            return result;
            
        } catch (Exception e) {
            System.err.println("Error executing command " + Arrays.toString(command) + ": " + e.getMessage());
            
            ExecuteCommandResult result = new ExecuteCommandResult();
            result.setExitCode(-1);
            result.setOutput("命令执行失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 读取输入流内容
     * 
     * @param reader 输入流读取器
     * @return 读取的内容
     */
    private static String readStream(BufferedReader reader) {
        if (reader == null) {
            return "";
        }
        
        try {
            List<String> lines = new ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            
            return lines.stream().collect(Collectors.joining("\n"));
            
        } catch (IOException e) {
            System.err.println("Error reading from stream: " + e.getMessage());
            return "";
        }
    }
}
