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

import com.alibaba.langengine.core.embeddings.FakeEmbeddings;
import com.alibaba.langengine.core.indexes.Document;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled("需要 ScaNN 服务器运行")
public class ScannIntegrationTest {

    private Scann scann;
    private FakeEmbeddings fakeEmbeddings;
    private static final String INTEGRATION_INDEX_NAME = "integration_test_index";
    private static final String TEST_SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        // 创建高级配置的 ScaNN 实例
        ScannParam scannParam = new ScannParam();
        scannParam.setServerUrl(TEST_SERVER_URL);
        scannParam.setDimensions(768);
        scannParam.setIndexType("tree_ah");
        scannParam.setDistanceMeasure("dot_product");
        scannParam.setConnectionTimeout(10000);
        scannParam.setReadTimeout(30000);
        scannParam.setMaxConnections(50);
        scannParam.setBatchSize(50);
        scannParam.setEnableReordering(true);
        scannParam.setEnableParallelSearch(true);
        scannParam.setSearchThreads(4);
        scannParam.setQuantizationType("scalar");
        
        scann = new Scann(INTEGRATION_INDEX_NAME, scannParam);
        
        // 创建自定义的 FakeEmbeddings
        fakeEmbeddings = new FakeEmbeddings() {
            @Override
            public List<Document> embedDocument(List<Document> documents) {
                for (Document doc : documents) {
                    doc.setEmbedding(createConsistentEmbedding(doc.getPageContent()));
                }
                return documents;
            }
            
            @Override
            public List<String> embedQuery(String text, int recommend) {
                List<Double> embedding = createConsistentEmbedding(text);
                return Lists.newArrayList(embedding.toString());
            }
        };
        
        scann.setEmbedding(fakeEmbeddings);
    }

    @AfterEach
    public void tearDown() {
        if (scann != null) {
            scann.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("集成测试 - 完整工作流程")
    public void testCompleteWorkflow() {
        // 1. 初始化索引
        assertDoesNotThrow(() -> {
            scann.init();
        });
        
        // 2. 添加文档
        List<Document> documents = createLargeTestDataset(100);
        assertDoesNotThrow(() -> {
            scann.addDocuments(documents);
        });
        
        // 3. 执行搜索
        List<Document> results = scann.similaritySearch("machine learning", 10);
        assertNotNull(results);
        assertTrue(results.size() <= 10);
        
        // 4. 验证搜索结果质量
        for (Document doc : results) {
            assertNotNull(doc.getPageContent());
            assertNotNull(doc.getUniqueId());
            assertNotNull(doc.getScore());
            assertTrue(doc.getScore() >= 0.0);
        }
        
        // 5. 获取统计信息
        Map<String, Object> stats = scann.getIndexStats();
        assertNotNull(stats);
        
        // 6. 删除部分文档
        List<String> idsToDelete = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            idsToDelete.add("large_doc_" + i);
        }
        assertDoesNotThrow(() -> {
            scann.deleteDocuments(idsToDelete);
        });
        
        // 7. 验证删除后的搜索结果
        List<Document> resultsAfterDelete = scann.similaritySearch("machine learning", 10);
        assertNotNull(resultsAfterDelete);
    }

    @Test
    @Order(2)
    @DisplayName("集成测试 - 大规模数据处理")
    public void testLargeScaleDataProcessing() {
        scann.init();
        
        // 创建大量文档
        List<Document> largeDataset = createLargeTestDataset(1000);
        
        // 测试大批量添加
        long startTime = System.currentTimeMillis();
        scann.addDocuments(largeDataset);
        long endTime = System.currentTimeMillis();

        System.out.println("Added " + largeDataset.size() + " documents in " + (endTime - startTime) + " ms");
        
        // 测试多次搜索性能
        for (int i = 0; i < 10; i++) {
            String query = "test query " + i;
            List<Document> results = scann.similaritySearch(query, 20);
            assertNotNull(results);
            assertTrue(results.size() <= 20);
        }
    }

    @Test
    @Order(3)
    @DisplayName("集成测试 - 并发操作")
    public void testConcurrentOperations() throws InterruptedException {
        scann.init();
        
        int threadCount = 5;
        int documentsPerThread = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        // 并发添加文档
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    List<Document> documents = Lists.newArrayList();
                    for (int i = 0; i < documentsPerThread; i++) {
                        Document doc = new Document();
                        doc.setUniqueId("thread_" + threadId + "_doc_" + i);
                        doc.setPageContent("Concurrent test document from thread " + threadId + " doc " + i);
                        doc.setEmbedding(createConsistentEmbedding(doc.getPageContent()));
                        documents.add(doc);
                    }
                    scann.addDocuments(documents);
                } catch (Exception e) {
                    fail("Concurrent document addition failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有线程完成
        assertTrue(latch.await(60, TimeUnit.SECONDS));
        
        // 并发搜索测试
        CountDownLatch searchLatch = new CountDownLatch(threadCount);
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    List<Document> results = scann.similaritySearch("concurrent test " + threadId, 5);
                    assertNotNull(results);
                } catch (Exception e) {
                    fail("Concurrent search failed: " + e.getMessage());
                } finally {
                    searchLatch.countDown();
                }
            });
        }
        
        assertTrue(searchLatch.await(30, TimeUnit.SECONDS));
        executor.shutdown();
    }

    @Test
    @Order(4)
    @DisplayName("集成测试 - 不同距离度量")
    public void testDifferentDistanceMeasures() {
        // 测试点积距离
        testWithDistanceMeasure("dot_product");
        
        // 测试余弦相似度
        testWithDistanceMeasure("cosine");
        
        // 测试欧几里得距离
        testWithDistanceMeasure("squared_l2");
    }

    @Test
    @Order(5)
    @DisplayName("集成测试 - 不同索引类型")
    public void testDifferentIndexTypes() {
        // 测试树形索引
        testWithIndexType("tree_ah");
        
        // 测试混合索引
        testWithIndexType("tree_x_hybrid");
        
        // 测试暴力搜索
        testWithIndexType("brute_force");
    }

    @Test
    @Order(6)
    @DisplayName("集成测试 - 错误处理和恢复")
    public void testErrorHandlingAndRecovery() {
        scann.init();
        
        // 测试添加无效文档
        List<Document> invalidDocuments = Lists.newArrayList();
        Document invalidDoc = new Document();
        invalidDoc.setPageContent("Invalid document without embedding");
        // 故意不设置 embedding
        invalidDocuments.add(invalidDoc);
        
        assertThrows(RuntimeException.class, () -> {
            scann.addDocuments(invalidDocuments);
        });
        
        // 测试恢复 - 添加有效文档
        List<Document> validDocuments = createTestDocumentsWithEmbeddings(5);
        assertDoesNotThrow(() -> {
            scann.addDocuments(validDocuments);
        });
        
        // 验证系统仍然可以正常工作
        List<Document> results = scann.similaritySearch("test", 3);
        assertNotNull(results);
    }

    /**
     * 测试特定距离度量
     *
     * @param distanceMeasure 距离度量类型
     */
    private void testWithDistanceMeasure(String distanceMeasure) {
        ScannParam param = new ScannParam();
        param.setServerUrl(TEST_SERVER_URL);
        param.setDimensions(768);
        param.setDistanceMeasure(distanceMeasure);
        
        String indexName = "test_" + distanceMeasure + "_" + UUID.randomUUID().toString().substring(0, 8);
        Scann testScann = new Scann(indexName, param);
        testScann.setEmbedding(fakeEmbeddings);
        
        try {
            testScann.init();
            
            List<Document> documents = createTestDocumentsWithEmbeddings(10);
            testScann.addDocuments(documents);
            
            List<Document> results = testScann.similaritySearch("test query", 5);
            assertNotNull(results);
            
        } finally {
            testScann.close();
        }
    }

    /**
     * 测试特定索引类型
     *
     * @param indexType 索引类型
     */
    private void testWithIndexType(String indexType) {
        ScannParam param = new ScannParam();
        param.setServerUrl(TEST_SERVER_URL);
        param.setDimensions(768);
        param.setIndexType(indexType);
        
        String indexName = "test_" + indexType + "_" + UUID.randomUUID().toString().substring(0, 8);
        Scann testScann = new Scann(indexName, param);
        testScann.setEmbedding(fakeEmbeddings);
        
        try {
            testScann.init();
            
            List<Document> documents = createTestDocumentsWithEmbeddings(10);
            testScann.addDocuments(documents);
            
            List<Document> results = testScann.similaritySearch("test query", 5);
            assertNotNull(results);
            
        } finally {
            testScann.close();
        }
    }

    /**
     * 创建大型测试数据集
     *
     * @param size 数据集大小
     * @return 测试文档列表
     */
    private List<Document> createLargeTestDataset(int size) {
        List<Document> documents = Lists.newArrayList();
        
        String[] categories = {"technology", "science", "business", "health", "education"};
        String[] sources = {"article", "paper", "blog", "news", "documentation"};
        
        for (int i = 0; i < size; i++) {
            Document doc = new Document();
            doc.setUniqueId("large_doc_" + i);
            doc.setPageContent(generateTestContent(i));
            doc.setEmbedding(createConsistentEmbedding(doc.getPageContent()));
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("category", categories[i % categories.length]);
            metadata.put("source", sources[i % sources.length]);
            metadata.put("index", i);
            metadata.put("timestamp", System.currentTimeMillis());
            doc.setMetadata(metadata);
            
            documents.add(doc);
        }
        
        return documents;
    }

    /**
     * 创建带有嵌入向量的测试文档
     *
     * @param count 文档数量
     * @return 测试文档列表
     */
    private List<Document> createTestDocumentsWithEmbeddings(int count) {
        List<Document> documents = Lists.newArrayList();
        
        for (int i = 0; i < count; i++) {
            Document doc = new Document();
            doc.setUniqueId("integration_doc_" + i);
            doc.setPageContent("Integration test document number " + i + " with content about machine learning and AI.");
            doc.setEmbedding(createConsistentEmbedding(doc.getPageContent()));
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("test_type", "integration");
            metadata.put("doc_number", i);
            doc.setMetadata(metadata);
            
            documents.add(doc);
        }
        
        return documents;
    }

    /**
     * 生成测试内容
     *
     * @param index 索引
     * @return 测试内容
     */
    private String generateTestContent(int index) {
        String[] topics = {
            "machine learning algorithms and neural networks",
            "artificial intelligence and deep learning frameworks",
            "natural language processing and text analysis",
            "computer vision and image recognition systems",
            "data science and statistical modeling techniques",
            "cloud computing and distributed systems architecture",
            "software engineering best practices and design patterns",
            "database optimization and query performance tuning",
            "cybersecurity and information protection strategies",
            "mobile application development and user experience design"
        };
        
        String baseTopic = topics[index % topics.length];
        return "Document " + index + ": This document discusses " + baseTopic + 
               " with detailed explanations and practical examples. " +
               "It covers various aspects including implementation details, " +
               "performance considerations, and real-world applications.";
    }

    /**
     * 创建一致性嵌入向量（基于内容哈希）
     *
     * @param content 文档内容
     * @return 一致性向量
     */
    private List<Double> createConsistentEmbedding(String content) {
        List<Double> embedding = Lists.newArrayList();
        
        // 使用内容哈希创建一致性向量
        int hash = content.hashCode();
        java.util.Random random = new java.util.Random(hash);
        
        for (int i = 0; i < 768; i++) {
            embedding.add(random.nextGaussian() * 0.1); // 标准正态分布
        }
        
        // 归一化向量
        double norm = Math.sqrt(embedding.stream().mapToDouble(x -> x * x).sum());
        if (norm > 0) {
            for (int i = 0; i < embedding.size(); i++) {
                embedding.set(i, embedding.get(i) / norm);
            }
        }
        
        return embedding;
    }
}
