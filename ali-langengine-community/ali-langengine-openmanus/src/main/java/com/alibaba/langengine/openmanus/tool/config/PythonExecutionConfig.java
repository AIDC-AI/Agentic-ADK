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
package com.alibaba.langengine.openmanus.tool.config;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Data
@Builder
public class PythonExecutionConfig {
    
    /**
     * 默认超时时间
     */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(300);
    
    /**
     * 默认工作目录
     */
    public static final Path DEFAULT_WORK_DIR = Paths.get(System.getProperty("user.dir"), "extensions");
    
    /**
     * 默认Python命令
     */
    public static final String DEFAULT_PYTHON_COMMAND = System.getProperty("os.name").startsWith("Windows") ? "python" : "python3";
    
    /**
     * 执行超时时间
     */
    @NonNull
    @Builder.Default
    private final Duration timeout = DEFAULT_TIMEOUT;
    
    /**
     * 工作目录
     */
    @NonNull
    @Builder.Default
    private final Path workingDirectory = DEFAULT_WORK_DIR;
    
    /**
     * Python命令路径
     */
    @NonNull
    @Builder.Default
    private final String pythonCommand = DEFAULT_PYTHON_COMMAND;
    
    /**
     * 是否使用ARM64架构
     */
    @Builder.Default
    private final boolean useArm64 = false;
    
    /**
     * 是否启用安全模式（限制某些危险操作）
     */
    @Builder.Default
    private final boolean securityMode = true;
    
    /**
     * 最大内存限制（MB）
     */
    @Builder.Default
    private final int maxMemoryMB = 512;
    
    /**
     * 环境变量
     */
    @NonNull
    @Builder.Default
    private final Map<String, String> environmentVariables = Collections.emptyMap();
    
    /**
     * 禁用的模块列表（安全模式下生效）
     */
    @NonNull
    @Builder.Default
    private final List<String> disallowedModules = Collections.singletonList("os.system");
    
    /**
     * 是否保留临时文件（用于调试）
     */
    @Builder.Default
    private final boolean keepTempFiles = false;
    
    /**
     * 自定义Python路径（如果需要使用特定的Python环境）
     */
    private final String customPythonPath;
    
    /**
     * 最大输出长度（字符数）
     */
    @Builder.Default
    private final int maxOutputLength = 10000;
    
    /**
     * 是否启用详细日志
     */
    @Builder.Default
    private final boolean verboseLogging = false;
    
    /**
     * 验证配置的有效性
     * 
     * @throws IllegalArgumentException 如果配置无效
     */
    public void validate() {
        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("超时时间必须为正数");
        }
        
        if (maxMemoryMB <= 0) {
            throw new IllegalArgumentException("最大内存限制必须为正数");
        }
        
        if (maxOutputLength <= 0) {
            throw new IllegalArgumentException("最大输出长度必须为正数");
        }
        
        if (pythonCommand == null || pythonCommand.trim().isEmpty()) {
            throw new IllegalArgumentException("Python命令不能为空");
        }
    }
    
    /**
     * 创建默认配置
     * 
     * @return 默认配置
     */
    public static PythonExecutionConfig defaultConfig() {
        return PythonExecutionConfig.builder().build();
    }
    
    /**
     * 创建安全配置（更严格的安全限制）
     * 
     * @return 安全配置
     */
    public static PythonExecutionConfig secureConfig() {
        return PythonExecutionConfig.builder()
                .securityMode(true)
                .maxMemoryMB(256)
                .timeout(Duration.ofSeconds(60))
                .disallowedModules(List.of("os", "sys", "subprocess", "socket", "urllib"))
                .build();
    }
    
    /**
     * 创建开发配置（更宽松的限制，用于开发和调试）
     * 
     * @return 开发配置
     */
    public static PythonExecutionConfig developmentConfig() {
        return PythonExecutionConfig.builder()
                .securityMode(false)
                .maxMemoryMB(1024)
                .timeout(Duration.ofSeconds(600))
                .keepTempFiles(true)
                .verboseLogging(true)
                .disallowedModules(Collections.emptyList())
                .build();
    }
}
