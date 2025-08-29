/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.annoy.util;

import com.alibaba.langengine.annoy.exception.AnnoyException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


public class AnnoyUtils {

    /**
     * 验证向量维度
     */
    public static void validateVectorDimension(List<?> vector, int expectedDimension) {
        if (vector == null) {
            throw new AnnoyException.ParameterValidationException("Vector cannot be null");
        }
        
        if (vector.size() != expectedDimension) {
            throw new AnnoyException.ParameterValidationException(
                String.format("Vector dimension mismatch: expected %d, got %d", 
                    expectedDimension, vector.size()));
        }
    }

    /**
     * 验证距离度量类型
     */
    public static void validateDistanceMetric(String metric) {
        if (StringUtils.isEmpty(metric)) {
            throw new AnnoyException.ParameterValidationException("Distance metric cannot be null or empty");
        }
        
        String normalizedMetric = metric.toLowerCase().trim();
        if (!isValidDistanceMetric(normalizedMetric)) {
            throw new AnnoyException.ParameterValidationException(
                "Invalid distance metric: " + metric + 
                ". Supported metrics: angular, euclidean, manhattan, hamming, dot");
        }
    }

    /**
     * 检查距离度量类型是否有效
     */
    public static boolean isValidDistanceMetric(String metric) {
        if (StringUtils.isEmpty(metric)) {
            return false;
        }
        
        String normalizedMetric = metric.toLowerCase().trim();
        return "angular".equals(normalizedMetric) ||
               "euclidean".equals(normalizedMetric) ||
               "manhattan".equals(normalizedMetric) ||
               "hamming".equals(normalizedMetric) ||
               "dot".equals(normalizedMetric) ||
               "cosine".equals(normalizedMetric); // cosine is alias for angular
    }

    /**
     * 生成唯一的索引ID
     */
    public static String generateIndexId() {
        return "annoy_" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成索引文件路径
     */
    public static String generateIndexFilePath(String indexPath, String indexId) {
        if (StringUtils.isEmpty(indexPath)) {
            throw new AnnoyException.ParameterValidationException("Index path cannot be null or empty");
        }
        
        if (StringUtils.isEmpty(indexId)) {
            throw new AnnoyException.ParameterValidationException("Index ID cannot be null or empty");
        }
        
        return Paths.get(indexPath, indexId + ".ann").toString();
    }

    /**
     * 创建目录
     */
    public static void createDirectoryIfNotExists(String directoryPath) {
        if (StringUtils.isEmpty(directoryPath)) {
            return;
        }
        
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new AnnoyException.FileOperationException(
                "Failed to create directory: " + directoryPath, e);
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        
        try {
            File file = new File(filePath);
            return file.exists() && file.delete();
        } catch (Exception e) {
            throw new AnnoyException.FileOperationException(
                "Failed to delete file: " + filePath, e);
        }
    }

    /**
     * 检查文件是否存在
     */
    public static boolean fileExists(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        
        return new File(filePath).exists();
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return 0;
        }
        
        File file = new File(filePath);
        return file.exists() ? file.length() : 0;
    }

    /**
     * 验证正整数参数
     */
    public static void validatePositiveInteger(Integer value, String paramName) {
        if (value == null || value <= 0) {
            throw new AnnoyException.ParameterValidationException(
                paramName + " must be a positive integer");
        }
    }

    /**
     * 验证正长整数参数
     */
    public static void validatePositiveLong(Long value, String paramName) {
        if (value == null || value <= 0) {
            throw new AnnoyException.ParameterValidationException(
                paramName + " must be a positive long");
        }
    }

    /**
     * 验证非空字符串参数
     */
    public static void validateNonEmptyString(String value, String paramName) {
        if (StringUtils.isEmpty(value)) {
            throw new AnnoyException.ParameterValidationException(
                paramName + " cannot be null or empty");
        }
    }

    /**
     * 安全地转换Double列表为Float列表
     */
    public static List<Float> convertDoubleListToFloatList(List<Double> doubleList) {
        if (doubleList == null) {
            return null;
        }
        
        return doubleList.stream()
                .map(Double::floatValue)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 安全地转换Float列表为Double列表
     */
    public static List<Double> convertFloatListToDoubleList(List<Float> floatList) {
        if (floatList == null) {
            return null;
        }
        
        return floatList.stream()
                .map(Float::doubleValue)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 计算向量的L2范数
     */
    public static double calculateL2Norm(List<Float> vector) {
        if (vector == null || vector.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Float value : vector) {
            if (value != null) {
                sum += value * value;
            }
        }
        
        return Math.sqrt(sum);
    }

    /**
     * 归一化向量
     */
    public static List<Float> normalizeVector(List<Float> vector) {
        if (vector == null || vector.isEmpty()) {
            return vector;
        }
        
        double norm = calculateL2Norm(vector);
        if (norm == 0.0) {
            return vector; // 避免除零
        }
        
        return vector.stream()
                .map(value -> value != null ? (float) (value / norm) : 0.0f)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 格式化时间间隔
     */
    public static String formatDuration(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " ms";
        } else if (milliseconds < 60 * 1000) {
            return String.format("%.2f s", milliseconds / 1000.0);
        } else if (milliseconds < 60 * 60 * 1000) {
            return String.format("%.2f min", milliseconds / (60.0 * 1000.0));
        } else {
            return String.format("%.2f h", milliseconds / (60.0 * 60.0 * 1000.0));
        }
    }

    /**
     * 验证索引ID格式
     */
    public static void validateIndexId(String indexId) {
        validateNonEmptyString(indexId, "Index ID");
        
        // 检查是否包含非法字符
        if (!indexId.matches("[a-zA-Z0-9_-]+")) {
            throw new AnnoyException.ParameterValidationException(
                "Index ID can only contain alphanumeric characters, underscore, and hyphen");
        }
        
        // 检查长度限制
        if (indexId.length() > 100) {
            throw new AnnoyException.ParameterValidationException(
                "Index ID cannot be longer than 100 characters");
        }
    }

    /**
     * 获取系统信息摘要
     */
    public static String getSystemInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return String.format(
            "System Info - Max Memory: %s, Used Memory: %s, Free Memory: %s, Processors: %d",
            formatFileSize(maxMemory),
            formatFileSize(usedMemory),
            formatFileSize(freeMemory),
            runtime.availableProcessors()
        );
    }
}
