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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.core.indexes.Document;
import com.alibaba.langengine.scann.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Slf4j
@Data
public class ScannService {

    private final ScannParam scannParam;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String indexName;
    private final AtomicLong documentIdCounter;
    private final Map<String, String> documentIdMapping;

    /**
     * 构造函数
     *
     * @param indexName ScaNN 索引名称
     * @param scannParam ScaNN 参数配置
     */
    public ScannService(String indexName, ScannParam scannParam) {
        this.indexName = indexName;
        this.scannParam = scannParam;
        this.objectMapper = new ObjectMapper();
        this.documentIdCounter = new AtomicLong(0);
        this.documentIdMapping = new ConcurrentHashMap<>();
        
        // 配置 HTTP 客户端
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(scannParam.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(scannParam.getMaxConnections() / 4);
        
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(scannParam.getConnectionTimeout())
                .setSocketTimeout(scannParam.getReadTimeout())
                .setConnectionRequestTimeout(scannParam.getConnectionTimeout())
                .build();
        
        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 初始化 ScaNN 索引
     *
     * @throws Exception 初始化异常
     */
    public void init() throws Exception {
        log.info("Initializing ScaNN index: {}", indexName);
        
        // 检查索引是否存在
        if (!indexExists()) {
            createIndex();
        }
        
        log.info("ScaNN index {} initialized successfully", indexName);
    }

    /**
     * 检查索引是否存在
     *
     * @return 索引是否存在
     */
    private boolean indexExists() {
        try {
            String url = scannParam.getFullUrl("/api/v1/indexes/" + indexName);
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            EntityUtils.consume(response.getEntity());
            return statusCode == 200;
        } catch (IOException e) {
            log.warn("Network error while checking index existence: {}", e.getMessage());
            throw new ScannConnectionException("Failed to connect to ScaNN server while checking index existence", e);
        } catch (Exception e) {
            log.warn("Unexpected error while checking index existence: {}", e.getMessage());
            throw new ScannIndexException("Failed to check index existence", e);
        }
    }

    /**
     * 创建 ScaNN 索引
     *
     * @throws Exception 创建异常
     */
    private void createIndex() {
        log.info("Creating ScaNN index: {}", indexName);

        try {
            JSONObject indexConfig = new JSONObject();
            indexConfig.put("name", indexName);
            indexConfig.put("dimensions", scannParam.getDimensions());
            indexConfig.put("index_type", scannParam.getIndexType());
            indexConfig.put("distance_measure", scannParam.getDistanceMeasure());
            indexConfig.put("training_sample_size", scannParam.getTrainingSampleSize());
            indexConfig.put("leaves_to_search", scannParam.getLeavesToSearch());
            indexConfig.put("reorder_num_neighbors", scannParam.getReorderNumNeighbors());
            indexConfig.put("enable_reordering", scannParam.isEnableReordering());
            indexConfig.put("quantization_type", scannParam.getQuantizationType());
            indexConfig.put("enable_parallel_search", scannParam.isEnableParallelSearch());
            indexConfig.put("search_threads", scannParam.getSearchThreads());

            String url = scannParam.getFullUrl("/api/v1/indexes");
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(indexConfig.toJSONString(), StandardCharsets.UTF_8));

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (statusCode != 200 && statusCode != 201) {
                throw new ScannIndexException("Failed to create ScaNN index. Status: " + statusCode + ", Response: " + responseBody);
            }

            log.info("ScaNN index {} created successfully", indexName);
        } catch (IOException e) {
            log.error("Network error while creating index: {}", e.getMessage());
            throw new ScannConnectionException("Failed to connect to ScaNN server while creating index", e);
        } catch (ScannIndexException e) {
            throw e; // Re-throw ScaNN specific exceptions
        } catch (Exception e) {
            log.error("Unexpected error while creating index: {}", e.getMessage());
            throw new ScannIndexException("Failed to create ScaNN index", e);
        }
    }

    /**
     * 添加文档到 ScaNN 索引
     *
     * @param documents 文档列表
     */
    public void addDocuments(List<Document> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }
        
        try {
            log.info("Adding {} documents to ScaNN index: {}", documents.size(), indexName);

            // 分批处理文档
            List<List<Document>> batches = partitionDocuments(documents, scannParam.getBatchSize());

            for (List<Document> batch : batches) {
                addDocumentBatch(batch);
            }

            log.info("Successfully added {} documents to ScaNN index: {}", documents.size(), indexName);
        } catch (IOException e) {
            log.error("Network error while adding documents: {}", e.getMessage(), e);
            throw new ScannConnectionException("Failed to connect to ScaNN server while adding documents", e);
        } catch (ScannDocumentException e) {
            throw e; // Re-throw ScaNN specific exceptions
        } catch (Exception e) {
            log.error("Unexpected error while adding documents: {}", e.getMessage(), e);
            throw new ScannDocumentException("Failed to add documents to ScaNN index", e);
        }
    }

    /**
     * 分批处理文档
     *
     * @param documents 文档列表
     * @param batchSize 批处理大小
     * @return 分批后的文档列表
     */
    private List<List<Document>> partitionDocuments(List<Document> documents, int batchSize) {
        List<List<Document>> batches = new ArrayList<>();
        for (int i = 0; i < documents.size(); i += batchSize) {
            int end = Math.min(i + batchSize, documents.size());
            batches.add(documents.subList(i, end));
        }
        return batches;
    }

    /**
     * 添加文档批次
     *
     * @param documents 文档批次
     * @throws Exception 添加异常
     */
    private void addDocumentBatch(List<Document> documents) throws IOException, ScannDocumentException {
        JSONObject request = new JSONObject();
        JSONArray vectors = new JSONArray();
        JSONArray metadata = new JSONArray();
        
        for (Document document : documents) {
            // 生成或获取文档ID
            String docId = getOrGenerateDocumentId(document);
            
            // 构建向量数据
            JSONObject vectorData = new JSONObject();
            vectorData.put("id", docId);
            vectorData.put("vector", document.getEmbedding());
            vectors.add(vectorData);
            
            // 构建元数据
            JSONObject metaData = new JSONObject();
            metaData.put("id", docId);
            metaData.put("content", document.getPageContent());
            metaData.put("unique_id", document.getUniqueId());
            if (document.getMetadata() != null) {
                metaData.putAll(document.getMetadata());
            }
            metadata.add(metaData);
        }
        
        request.put("vectors", vectors);
        request.put("metadata", metadata);
        
        String url = scannParam.getFullUrl("/api/v1/indexes/" + indexName + "/documents");
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setEntity(new StringEntity(request.toJSONString(), StandardCharsets.UTF_8));
        
        HttpResponse response = httpClient.execute(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        
        if (statusCode != 200 && statusCode != 201) {
            throw new ScannDocumentException("Failed to add document batch. Status: " + statusCode + ", Response: " + responseBody);
        }
    }

    /**
     * 获取或生成文档ID
     *
     * @param document 文档
     * @return 文档ID
     */
    private String getOrGenerateDocumentId(Document document) {
        if (StringUtils.isNotEmpty(document.getUniqueId())) {
            return document.getUniqueId();
        }

        String contentHash = String.valueOf(document.getPageContent().hashCode());
        return documentIdMapping.computeIfAbsent(contentHash,
                k -> "doc_" + documentIdCounter.incrementAndGet());
    }

    /**
     * 执行相似性搜索
     *
     * @param queryVector 查询向量
     * @param k 返回结果数量
     * @param maxDistanceValue 最大距离值
     * @return 搜索结果文档列表
     */
    public List<Document> similaritySearch(List<Double> queryVector, int k, Double maxDistanceValue) {
        try {
            log.debug("Performing similarity search in ScaNN index: {} with k={}", indexName, k);

            JSONObject searchRequest = new JSONObject();
            searchRequest.put("vector", queryVector);
            searchRequest.put("k", k);
            searchRequest.put("leaves_to_search", scannParam.getLeavesToSearch());
            searchRequest.put("reorder_num_neighbors", scannParam.getReorderNumNeighbors());

            if (maxDistanceValue != null) {
                searchRequest.put("max_distance", maxDistanceValue);
            }

            String url = scannParam.getFullUrl("/api/v1/indexes/" + indexName + "/search");
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(searchRequest.toJSONString(), StandardCharsets.UTF_8));

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (statusCode != 200) {
                log.error("ScaNN search failed with status {}: {}", statusCode, responseBody);
                return new ArrayList<>();
            }

            return parseSearchResponse(responseBody);

        } catch (Exception e) {
            log.error("Failed to perform similarity search: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析搜索响应
     *
     * @param responseBody 响应体
     * @return 文档列表
     */
    private List<Document> parseSearchResponse(String responseBody) {
        try {
            JSONObject response = JSON.parseObject(responseBody);
            JSONArray results = response.getJSONArray("results");

            if (results == null || results.isEmpty()) {
                return new ArrayList<>();
            }

            List<Document> documents = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JSONObject result = results.getJSONObject(i);
                Document document = parseSearchResult(result);
                if (document != null) {
                    documents.add(document);
                }
            }

            return documents;
        } catch (Exception e) {
            log.error("Failed to parse search response: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 解析单个搜索结果
     *
     * @param result 搜索结果JSON对象
     * @return 文档对象
     */
    private Document parseSearchResult(JSONObject result) {
        try {
            Document document = new Document();

            // 设置基本信息
            String docId = result.getString("id");
            Double score = result.getDouble("score");
            Double distance = result.getDouble("distance");

            document.setUniqueId(docId);
            document.setScore(score != null ? score : (distance != null ? 1.0 / (1.0 + distance) : 0.0));

            // 获取元数据
            JSONObject metadata = result.getJSONObject("metadata");
            if (metadata != null) {
                document.setPageContent(metadata.getString("content"));

                Map<String, Object> metaMap = new HashMap<>();
                for (String key : metadata.keySet()) {
                    if (!"content".equals(key) && !"unique_id".equals(key)) {
                        metaMap.put(key, metadata.get(key));
                    }
                }
                document.setMetadata(metaMap);
            }

            return document;
        } catch (Exception e) {
            log.error("Failed to parse search result: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 删除文档
     *
     * @param documentIds 文档ID列表
     */
    public void deleteDocuments(List<String> documentIds) {
        if (CollectionUtils.isEmpty(documentIds)) {
            return;
        }

        try {
            log.info("Deleting {} documents from ScaNN index: {}", documentIds.size(), indexName);

            JSONObject deleteRequest = new JSONObject();
            deleteRequest.put("ids", documentIds);

            String url = scannParam.getFullUrl("/api/v1/indexes/" + indexName + "/documents");
            HttpDelete request = new HttpDelete(url);
            request.setHeader("Content-Type", "application/json");

            // HttpDelete 不支持 body，使用 HttpPost 模拟
            HttpPost deletePost = new HttpPost(url + "/delete");
            deletePost.setHeader("Content-Type", "application/json");
            deletePost.setEntity(new StringEntity(deleteRequest.toJSONString(), StandardCharsets.UTF_8));

            HttpResponse response = httpClient.execute(deletePost);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (statusCode != 200) {
                log.error("Failed to delete documents: {}", responseBody);
                throw new RuntimeException("Failed to delete documents: " + responseBody);
            }

            log.info("Successfully deleted {} documents from ScaNN index: {}", documentIds.size(), indexName);
        } catch (Exception e) {
            log.error("Failed to delete documents: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete documents", e);
        }
    }

    /**
     * 获取索引统计信息
     *
     * @return 索引统计信息
     */
    public Map<String, Object> getIndexStats() {
        try {
            String url = scannParam.getFullUrl("/api/v1/indexes/" + indexName + "/stats");
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (statusCode != 200) {
                log.error("Failed to get index stats: {}", responseBody);
                return new HashMap<>();
            }

            JSONObject stats = JSON.parseObject(responseBody);
            return stats.getInnerMap();
        } catch (Exception e) {
            log.error("Failed to get index stats: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * 关闭服务，释放资源
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
            log.info("ScaNN service closed successfully");
        } catch (IOException e) {
            log.error("Failed to close ScaNN service: {}", e.getMessage(), e);
        }
    }
}
