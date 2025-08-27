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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;


public class CodeExecutionResult {
    
    /**
     * 退出码
     */
    private int exitcode;
    
    /**
     * 标准输出
     */
    private String logs;
    
    /**
     * 错误输出
     */
    private String errorOutput;
    
    /**
     * 执行开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 执行耗时
     */
    private Duration executionTime;
    
    /**
     * 内存使用情况（MB）
     */
    private long memoryUsageMB;
    
    /**
     * 临时文件路径
     */
    private String tempFilePath;
    
    /**
     * 额外的元数据
     */
    private Map<String, Object> metadata;
    
    /**
     * 原始图片数据（如果有的话）
     */
    private String image;
    
    /**
     * 执行的代码
     */
    private String executedCode;

    // 默认构造函数
    public CodeExecutionResult() {
        this.metadata = new HashMap<>();
    }

    // 全参构造函数
    public CodeExecutionResult(int exitcode, String logs, String errorOutput, 
                               LocalDateTime startTime, LocalDateTime endTime, 
                               Duration executionTime, long memoryUsageMB,
                               String tempFilePath, Map<String, Object> metadata, 
                               String image, String executedCode) {
        this.exitcode = exitcode;
        this.logs = logs;
        this.errorOutput = errorOutput;
        this.startTime = startTime;
        this.endTime = endTime;
        this.executionTime = executionTime;
        this.memoryUsageMB = memoryUsageMB;
        this.tempFilePath = tempFilePath;
        this.metadata = metadata != null ? metadata : new HashMap<>();
        this.image = image;
        this.executedCode = executedCode;
    }

    // Getter和Setter方法
    public int getExitcode() {
        return exitcode;
    }

    public void setExitcode(int exitcode) {
        this.exitcode = exitcode;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput) {
        this.errorOutput = errorOutput;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }

    public long getMemoryUsageMB() {
        return memoryUsageMB;
    }

    public void setMemoryUsageMB(long memoryUsageMB) {
        this.memoryUsageMB = memoryUsageMB;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExecutedCode() {
        return executedCode;
    }

    public void setExecutedCode(String executedCode) {
        this.executedCode = executedCode;
    }
    
    /**
     * 执行是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccessful() {
        return exitcode == 0;
    }
    
    /**
     * 执行是否失败
     * 
     * @return 是否失败
     */
    public boolean isFailed() {
        return !isSuccessful();
    }
    
    /**
     * 是否有输出
     * 
     * @return 是否有输出
     */
    public boolean hasOutput() {
        return logs != null && !logs.trim().isEmpty();
    }
    
    /**
     * 是否有错误输出
     * 
     * @return 是否有错误输出
     */
    public boolean hasErrorOutput() {
        return errorOutput != null && !errorOutput.trim().isEmpty();
    }
    
    /**
     * 获取合并的输出（标准输出 + 错误输出）
     * 
     * @return 合并的输出
     */
    public String getCombinedOutput() {
        StringBuilder sb = new StringBuilder();
        if (hasOutput()) {
            sb.append(logs);
        }
        if (hasErrorOutput()) {
            if (sb.length() > 0) {
                sb.append("\n--- ERROR ---\n");
            }
            sb.append(errorOutput);
        }
        return sb.toString();
    }
    
    /**
     * 获取格式化的执行摘要
     * 
     * @return 执行摘要
     */
    public String getExecutionSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("执行结果: ").append(isSuccessful() ? "成功" : "失败").append("\n");
        sb.append("退出码: ").append(exitcode).append("\n");
        
        if (executionTime != null) {
            sb.append("执行时间: ").append(executionTime.toMillis()).append("ms").append("\n");
        }
        
        if (memoryUsageMB > 0) {
            sb.append("内存使用: ").append(memoryUsageMB).append("MB").append("\n");
        }
        
        if (hasOutput()) {
            sb.append("输出长度: ").append(logs.length()).append(" 字符").append("\n");
        }
        
        if (hasErrorOutput()) {
            sb.append("错误输出长度: ").append(errorOutput.length()).append(" 字符").append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * 计算执行时间
     */
    public void calculateExecutionTime() {
        if (startTime != null && endTime != null) {
            this.executionTime = Duration.between(startTime, endTime);
        }
    }
    
    /**
     * 创建成功的结果
     * 
     * @param output 输出
     * @return 成功的结果
     */
    public static CodeExecutionResult success(String output) {
        CodeExecutionResult result = new CodeExecutionResult();
        result.setExitcode(0);
        result.setLogs(output);
        result.setEndTime(LocalDateTime.now());
        return result;
    }
    
    /**
     * 创建失败的结果
     * 
     * @param exitCode 退出码
     * @param errorOutput 错误输出
     * @return 失败的结果
     */
    public static CodeExecutionResult failure(int exitCode, String errorOutput) {
        CodeExecutionResult result = new CodeExecutionResult();
        result.setExitcode(exitCode);
        result.setErrorOutput(errorOutput);
        result.setEndTime(LocalDateTime.now());
        return result;
    }
}
