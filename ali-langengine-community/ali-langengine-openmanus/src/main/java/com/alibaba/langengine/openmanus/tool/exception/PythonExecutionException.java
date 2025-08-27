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
package com.alibaba.langengine.openmanus.tool.exception;


public class PythonExecutionException extends RuntimeException {
    
    private final ErrorType errorType;
    private final int exitCode;
    private final String pythonErrorOutput;
    
    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        SYNTAX_ERROR("语法错误"),
        RUNTIME_ERROR("运行时错误"),
        TIMEOUT_ERROR("超时错误"),
        SECURITY_ERROR("安全错误"),
        ENVIRONMENT_ERROR("环境错误"),
        UNKNOWN_ERROR("未知错误");
        
        private final String description;
        
        ErrorType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param errorType 错误类型
     * @param exitCode 退出码
     * @param pythonErrorOutput Python错误输出
     */
    public PythonExecutionException(String message, ErrorType errorType, int exitCode, String pythonErrorOutput) {
        super(message);
        this.errorType = errorType;
        this.exitCode = exitCode;
        this.pythonErrorOutput = pythonErrorOutput;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     * @param errorType 错误类型
     * @param exitCode 退出码
     * @param pythonErrorOutput Python错误输出
     */
    public PythonExecutionException(String message, Throwable cause, ErrorType errorType, int exitCode, String pythonErrorOutput) {
        super(message, cause);
        this.errorType = errorType;
        this.exitCode = exitCode;
        this.pythonErrorOutput = pythonErrorOutput;
    }
    
    /**
     * 获取错误类型
     * 
     * @return 错误类型
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * 获取退出码
     * 
     * @return 退出码
     */
    public int getExitCode() {
        return exitCode;
    }
    
    /**
     * 获取Python错误输出
     * 
     * @return Python错误输出
     */
    public String getPythonErrorOutput() {
        return pythonErrorOutput;
    }
    
    /**
     * 获取详细错误信息
     * 
     * @return 详细错误信息
     */
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Python执行异常: ").append(getMessage()).append("\n");
        sb.append("错误类型: ").append(errorType.getDescription()).append("\n");
        sb.append("退出码: ").append(exitCode).append("\n");
        if (pythonErrorOutput != null && !pythonErrorOutput.trim().isEmpty()) {
            sb.append("Python错误输出: ").append(pythonErrorOutput);
        }
        return sb.toString();
    }
    
    /**
     * 根据错误输出判断错误类型
     * 
     * @param errorOutput 错误输出
     * @param exitCode 退出码
     * @return 错误类型
     */
    public static ErrorType determineErrorType(String errorOutput, int exitCode) {
        if (errorOutput == null || errorOutput.trim().isEmpty()) {
            return ErrorType.UNKNOWN_ERROR;
        }
        
        String lowerError = errorOutput.toLowerCase();
        
        if (lowerError.contains("syntaxerror") || lowerError.contains("indentationerror")) {
            return ErrorType.SYNTAX_ERROR;
        }
        
        if (lowerError.contains("timeout") || lowerError.contains("time out")) {
            return ErrorType.TIMEOUT_ERROR;
        }
        
        if (lowerError.contains("permission") || lowerError.contains("access denied") || 
            lowerError.contains("security")) {
            return ErrorType.SECURITY_ERROR;
        }
        
        if (lowerError.contains("no module named") || lowerError.contains("command not found") ||
            lowerError.contains("python: not found")) {
            return ErrorType.ENVIRONMENT_ERROR;
        }
        
        if (exitCode != 0) {
            return ErrorType.RUNTIME_ERROR;
        }
        
        return ErrorType.UNKNOWN_ERROR;
    }
}
