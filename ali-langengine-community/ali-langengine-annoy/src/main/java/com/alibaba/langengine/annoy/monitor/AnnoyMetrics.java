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
package com.alibaba.langengine.annoy.monitor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Data
public class AnnoyMetrics {

    /**
     * 索引构建次数
     */
    private final AtomicLong indexBuildCount = new AtomicLong(0);

    /**
     * 索引构建总耗时（毫秒）
     */
    private final AtomicLong totalIndexBuildTime = new AtomicLong(0);

    /**
     * 搜索请求次数
     */
    private final AtomicLong searchRequestCount = new AtomicLong(0);

    /**
     * 搜索总耗时（毫秒）
     */
    private final AtomicLong totalSearchTime = new AtomicLong(0);

    /**
     * 文档添加次数
     */
    private final AtomicLong documentAddCount = new AtomicLong(0);

    /**
     * 添加的文档总数
     */
    private final AtomicLong totalDocumentsAdded = new AtomicLong(0);

    /**
     * 错误次数
     */
    private final AtomicLong errorCount = new AtomicLong(0);

    /**
     * 最后一次操作时间
     */
    private final AtomicReference<LocalDateTime> lastOperationTime = new AtomicReference<>(LocalDateTime.now());

    /**
     * 最后一次错误时间
     */
    private final AtomicReference<LocalDateTime> lastErrorTime = new AtomicReference<>();

    /**
     * 最后一次错误信息
     */
    private final AtomicReference<String> lastErrorMessage = new AtomicReference<>();

    /**
     * 记录索引构建
     */
    public void recordIndexBuild(long durationMs) {
        indexBuildCount.incrementAndGet();
        totalIndexBuildTime.addAndGet(durationMs);
        lastOperationTime.set(LocalDateTime.now());
        
        log.debug("Index build completed in {} ms", durationMs);
    }

    /**
     * 记录搜索操作
     */
    public void recordSearch(long durationMs, int resultCount) {
        searchRequestCount.incrementAndGet();
        totalSearchTime.addAndGet(durationMs);
        lastOperationTime.set(LocalDateTime.now());
        
        log.debug("Search completed in {} ms, returned {} results", durationMs, resultCount);
    }

    /**
     * 记录文档添加
     */
    public void recordDocumentAdd(int documentCount) {
        documentAddCount.incrementAndGet();
        totalDocumentsAdded.addAndGet(documentCount);
        lastOperationTime.set(LocalDateTime.now());
        
        log.debug("Added {} documents", documentCount);
    }

    /**
     * 记录错误
     */
    public void recordError(String errorMessage) {
        errorCount.incrementAndGet();
        lastErrorTime.set(LocalDateTime.now());
        lastErrorMessage.set(errorMessage);
        
        log.warn("Error recorded: {}", errorMessage);
    }

    /**
     * 获取平均索引构建时间
     */
    public double getAverageIndexBuildTime() {
        long buildCount = indexBuildCount.get();
        return buildCount > 0 ? (double) totalIndexBuildTime.get() / buildCount : 0.0;
    }

    /**
     * 获取平均搜索时间
     */
    public double getAverageSearchTime() {
        long searchCount = searchRequestCount.get();
        return searchCount > 0 ? (double) totalSearchTime.get() / searchCount : 0.0;
    }

    /**
     * 获取平均每次添加的文档数
     */
    public double getAverageDocumentsPerAdd() {
        long addCount = documentAddCount.get();
        return addCount > 0 ? (double) totalDocumentsAdded.get() / addCount : 0.0;
    }

    /**
     * 获取错误率
     */
    public double getErrorRate() {
        long totalOperations = indexBuildCount.get() + searchRequestCount.get() + documentAddCount.get();
        return totalOperations > 0 ? (double) errorCount.get() / totalOperations : 0.0;
    }

    /**
     * 重置所有指标
     */
    public void reset() {
        indexBuildCount.set(0);
        totalIndexBuildTime.set(0);
        searchRequestCount.set(0);
        totalSearchTime.set(0);
        documentAddCount.set(0);
        totalDocumentsAdded.set(0);
        errorCount.set(0);
        lastOperationTime.set(LocalDateTime.now());
        lastErrorTime.set(null);
        lastErrorMessage.set(null);
        
        log.info("Metrics reset");
    }

    /**
     * 获取指标摘要
     */
    public String getSummary() {
        return String.format(
            "AnnoyMetrics{" +
            "indexBuilds=%d, avgBuildTime=%.2fms, " +
            "searches=%d, avgSearchTime=%.2fms, " +
            "documentAdds=%d, totalDocs=%d, " +
            "errors=%d, errorRate=%.2f%%, " +
            "lastOperation=%s}",
            indexBuildCount.get(), getAverageIndexBuildTime(),
            searchRequestCount.get(), getAverageSearchTime(),
            documentAddCount.get(), totalDocumentsAdded.get(),
            errorCount.get(), getErrorRate() * 100,
            lastOperationTime.get()
        );
    }

    /**
     * 获取详细报告
     */
    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Annoy Performance Metrics ===\n");
        report.append(String.format("Index Builds: %d\n", indexBuildCount.get()));
        report.append(String.format("Total Index Build Time: %d ms\n", totalIndexBuildTime.get()));
        report.append(String.format("Average Index Build Time: %.2f ms\n", getAverageIndexBuildTime()));
        report.append("\n");
        
        report.append(String.format("Search Requests: %d\n", searchRequestCount.get()));
        report.append(String.format("Total Search Time: %d ms\n", totalSearchTime.get()));
        report.append(String.format("Average Search Time: %.2f ms\n", getAverageSearchTime()));
        report.append("\n");
        
        report.append(String.format("Document Add Operations: %d\n", documentAddCount.get()));
        report.append(String.format("Total Documents Added: %d\n", totalDocumentsAdded.get()));
        report.append(String.format("Average Documents Per Add: %.2f\n", getAverageDocumentsPerAdd()));
        report.append("\n");
        
        report.append(String.format("Errors: %d\n", errorCount.get()));
        report.append(String.format("Error Rate: %.2f%%\n", getErrorRate() * 100));
        report.append(String.format("Last Operation: %s\n", lastOperationTime.get()));
        
        LocalDateTime lastError = lastErrorTime.get();
        if (lastError != null) {
            report.append(String.format("Last Error: %s\n", lastError));
            report.append(String.format("Last Error Message: %s\n", lastErrorMessage.get()));
        }
        
        return report.toString();
    }

    /**
     * 检查性能健康状况
     */
    public HealthStatus checkHealth() {
        double errorRate = getErrorRate();
        double avgSearchTime = getAverageSearchTime();
        
        if (errorRate > 0.1) { // 错误率超过10%
            return HealthStatus.UNHEALTHY;
        }
        
        if (avgSearchTime > 1000) { // 平均搜索时间超过1秒
            return HealthStatus.DEGRADED;
        }
        
        return HealthStatus.HEALTHY;
    }

    /**
     * 健康状态枚举
     */
    public enum HealthStatus {
        HEALTHY("健康"),
        DEGRADED("性能下降"),
        UNHEALTHY("不健康");

        private final String description;

        HealthStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 性能监控器
     */
    public static class PerformanceMonitor {
        private final AnnoyMetrics metrics;
        private final long startTime;

        public PerformanceMonitor(AnnoyMetrics metrics) {
            this.metrics = metrics;
            this.startTime = System.currentTimeMillis();
        }

        /**
         * 结束监控并记录索引构建
         */
        public void endIndexBuild() {
            long duration = System.currentTimeMillis() - startTime;
            metrics.recordIndexBuild(duration);
        }

        /**
         * 结束监控并记录搜索
         */
        public void endSearch(int resultCount) {
            long duration = System.currentTimeMillis() - startTime;
            metrics.recordSearch(duration, resultCount);
        }

        /**
         * 记录错误
         */
        public void recordError(String errorMessage) {
            metrics.recordError(errorMessage);
        }
    }

    /**
     * 创建性能监控器
     */
    public PerformanceMonitor createMonitor() {
        return new PerformanceMonitor(this);
    }
}
