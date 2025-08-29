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
package com.alibaba.langengine.scann.vectorstore;

import com.alibaba.langengine.scann.config.ScannConfigLoader;
import lombok.Data;


@Data
public class ScannParam {

    /**
     * ScaNN 服务器地址
     */
    private String serverUrl;

    /**
     * 连接超时时间（毫秒）
     */
    private int connectionTimeout;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout;

    /**
     * 最大连接数
     */
    private int maxConnections;

    /**
     * 向量维度
     */
    private int dimensions;

    /**
     * 索引类型
     * 支持的类型：tree_ah, tree_x_hybrid, brute_force
     */
    private String indexType;

    /**
     * 距离度量类型
     * 支持的类型：dot_product, squared_l2, cosine
     */
    private String distanceMeasure;

    /**
     * 训练样本大小
     */
    private int trainingSampleSize;

    /**
     * 搜索时要检查的叶子节点数量
     */
    private int leavesToSearch;

    /**
     * 重排序候选数量
     */
    private int reorderNumNeighbors;

    /**
     * 是否启用重排序
     */
    private boolean enableReordering;

    /**
     * 量化类型
     * 支持的类型：none, scalar, product
     */
    private String quantizationType;

    /**
     * 量化维度（仅在 product 量化时使用）
     */
    private int quantizationDimensions;

    /**
     * 是否启用并行搜索
     */
    private boolean enableParallelSearch;

    /**
     * 搜索线程数
     */
    private int searchThreads;

    /**
     * 索引构建线程数
     */
    private int buildThreads;

    /**
     * 内存映射文件大小限制（字节）
     */
    private long memoryMappedFileSize;

    /**
     * 是否启用预取
     */
    private boolean enablePrefetch;

    /**
     * 批处理大小
     */
    private int batchSize;

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 是否自动保存索引
     */
    private boolean autoSaveIndex;

    /**
     * 索引保存间隔（秒）
     */
    private int saveInterval;

    /**
     * 构造函数
     */
    public ScannParam() {
        // 从配置文件加载默认值
        this.serverUrl = ScannConfigLoader.getString("scann.server.url", "http://localhost:8080");
        this.connectionTimeout = ScannConfigLoader.getInt("scann.server.connection.timeout", 30000);
        this.readTimeout = ScannConfigLoader.getInt("scann.server.read.timeout", 60000);
        this.maxConnections = ScannConfigLoader.getInt("scann.server.max.connections", 100);
        this.dimensions = ScannConfigLoader.getInt("scann.index.default.dimensions", 768);
        this.indexType = ScannConfigLoader.getString("scann.index.default.type", "tree_ah");
        this.distanceMeasure = ScannConfigLoader.getString("scann.index.default.distance.measure", "dot_product");
        this.trainingSampleSize = ScannConfigLoader.getInt("scann.index.training.sample.size", 100000);
        this.leavesToSearch = ScannConfigLoader.getInt("scann.index.leaves.to.search", 100);
        this.reorderNumNeighbors = ScannConfigLoader.getInt("scann.index.reorder.num.neighbors", 1000);
        this.enableReordering = ScannConfigLoader.getBoolean("scann.search.enable.reordering", true);
        this.quantizationType = ScannConfigLoader.getString("scann.quantization.type", "scalar");
        this.quantizationDimensions = ScannConfigLoader.getInt("scann.quantization.dimensions", 2);
        this.enableParallelSearch = ScannConfigLoader.getBoolean("scann.search.enable.parallel", true);
        this.searchThreads = ScannConfigLoader.getInt("scann.search.threads", 4);
        this.buildThreads = ScannConfigLoader.getInt("scann.search.build.threads", 8);
        this.memoryMappedFileSize = ScannConfigLoader.getLong("scann.performance.memory.mapped.file.size", 1024L * 1024L * 1024L);
        this.enablePrefetch = ScannConfigLoader.getBoolean("scann.performance.enable.prefetch", true);
        this.batchSize = ScannConfigLoader.getInt("scann.performance.batch.size", 1000);
        this.indexName = ScannConfigLoader.getString("scann.index.default.name", "default_index");
        this.datasetName = ScannConfigLoader.getString("scann.index.default.dataset", "default_dataset");
        this.autoSaveIndex = ScannConfigLoader.getBoolean("scann.index.auto.save", true);
        this.saveInterval = ScannConfigLoader.getInt("scann.index.save.interval", 300);
    }

    /**
     * 构造函数
     *
     * @param serverUrl 服务器地址
     * @param dimensions 向量维度
     */
    public ScannParam(String serverUrl, int dimensions) {
        // 先调用默认构造函数加载配置
        this();
        // 然后覆盖指定的值
        this.serverUrl = serverUrl;
        this.dimensions = dimensions;
    }

    /**
     * 构造函数
     *
     * @param serverUrl 服务器地址
     * @param dimensions 向量维度
     * @param indexType 索引类型
     * @param distanceMeasure 距离度量类型
     */
    public ScannParam(String serverUrl, int dimensions, String indexType, String distanceMeasure) {
        // 先调用默认构造函数加载配置
        this();
        // 然后覆盖指定的值
        this.serverUrl = serverUrl;
        this.dimensions = dimensions;
        this.indexType = indexType;
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * 验证参数有效性
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return serverUrl != null && !serverUrl.trim().isEmpty() 
               && dimensions > 0 
               && connectionTimeout > 0 
               && readTimeout > 0
               && maxConnections > 0
               && trainingSampleSize > 0
               && leavesToSearch > 0
               && reorderNumNeighbors > 0;
    }

    /**
     * 获取完整的服务器URL
     *
     * @param endpoint 端点路径
     * @return 完整URL
     */
    public String getFullUrl(String endpoint) {
        String baseUrl = serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        String path = endpoint.startsWith("/") ? endpoint : "/" + endpoint;
        return baseUrl + path;
    }
}
