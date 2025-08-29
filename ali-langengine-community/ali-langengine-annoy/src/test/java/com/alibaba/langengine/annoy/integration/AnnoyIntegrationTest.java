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
package com.alibaba.langengine.annoy.integration;

import com.alibaba.langengine.annoy.model.AnnoyIndex;
import com.alibaba.langengine.annoy.model.AnnoyParam;
import com.alibaba.langengine.annoy.vectorstore.Annoy;
import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.core.indexes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Annoy集成测试")
class AnnoyIntegrationTest {

    @TempDir
    Path tempDir;

    private Annoy annoyVectorStore;
    private MockEmbeddings mockEmbeddings;

    @BeforeEach
    void setUp() {
        mockEmbeddings = new MockEmbeddings();
        
        AnnoyParam param = AnnoyParam.builder()
                .vectorDimension(3)
                .distanceMetric("euclidean")
                .nTrees(5)
                .batchSize(10)
                .build();
        
        annoyVectorStore = new Annoy(mockEmbeddings, "integration_test", param);
    }

    @Test
    @DisplayName("端到端测试：文档添加、索引构建、相似性搜索")
    void testEndToEndWorkflow() {
        // 1. 准备测试数据
        List<Document> documents = createTestDocuments();
        
        // 2. 添加文档
        annoyVectorStore.addDocuments(documents);
        assertEquals(3, annoyVectorStore.getDocumentCount());
        
        // 3. 构建索引
        annoyVectorStore.buildIndex();
        AnnoyIndex indexInfo = annoyVectorStore.getIndexInfo();
        assertTrue(indexInfo.isBuilt());
        assertTrue(indexInfo.getIndexFile().exists());
        
        // 4. 加载索引
        annoyVectorStore.loadIndex();
        assertTrue(indexInfo.isLoaded());
        assertTrue(indexInfo.isAvailable());
        
        // 5. 执行相似性搜索
        List<Document> results = annoyVectorStore.similaritySearch("similar to doc1", 2, null, null);
        
        // 6. 验证搜索结果
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 2);
        
        // 验证结果包含相似度信息
        for (Document doc : results) {
            assertNotNull(doc.getMetadata());
            assertTrue(doc.getMetadata().containsKey("similarity"));
            assertTrue(doc.getMetadata().containsKey("distance"));
            
            Number similarity = (Number) doc.getMetadata().get("similarity");
            Number distance = (Number) doc.getMetadata().get("distance");
            
            assertNotNull(similarity);
            assertNotNull(distance);
            assertTrue(similarity.doubleValue() >= 0 && similarity.doubleValue() <= 1);
            assertTrue(distance.doubleValue() >= 0);
        }
    }

    @Test
    @DisplayName("测试索引持久化和重新加载")
    void testIndexPersistenceAndReload() {
        // 1. 创建并构建索引
        List<Document> documents = createTestDocuments();
        annoyVectorStore.addDocuments(documents);
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        // 2. 执行搜索并记录结果
        List<Document> originalResults = annoyVectorStore.similaritySearch("test query", 2, null, null);
        
        // 3. 创建新的向量存储实例（模拟重启）
        AnnoyParam param = AnnoyParam.builder()
                .vectorDimension(3)
                .distanceMetric("euclidean")
                .nTrees(5)
                .build();
        
        Annoy newAnnoyVectorStore = new Annoy(mockEmbeddings, "integration_test", param);
        
        // 4. 重新添加相同的文档（模拟应用重启后重新加载数据的情况）
        newAnnoyVectorStore.addDocuments(documents);
        
        // 5. 验证索引状态
        AnnoyIndex newIndexInfo = newAnnoyVectorStore.getIndexInfo();
        assertNotNull(newIndexInfo);
        assertTrue(newIndexInfo.indexFileExists());
        
        // 6. 执行相同的搜索
        List<Document> newResults = newAnnoyVectorStore.similaritySearch("test query", 2, null, null);
        
        // 7. 验证结果一致性（注意：由于是近似搜索，结果可能略有不同）
        assertNotNull(newResults);
        assertEquals(originalResults.size(), newResults.size());
    }

    @Test
    @DisplayName("测试批量文档处理")
    void testBatchDocumentProcessing() {
        // 创建大量文档
        List<Document> largeBatch = createLargeDocumentBatch(100);
        
        // 批量添加
        annoyVectorStore.addDocuments(largeBatch);
        assertEquals(100, annoyVectorStore.getDocumentCount());
        
        // 构建和加载索引
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        // 验证搜索性能
        long startTime = System.currentTimeMillis();
        List<Document> results = annoyVectorStore.similaritySearch("batch test", 10, null, null);
        long endTime = System.currentTimeMillis();
        
        assertNotNull(results);
        assertTrue(results.size() <= 10);
        
        // 搜索应该在合理时间内完成（这里设置为1秒）
        assertTrue(endTime - startTime < 1000, "Search took too long: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("测试不同距离度量的搜索结果")
    void testDifferentDistanceMetrics() {
        List<Document> documents = createTestDocuments();
        
        // 测试不同的距离度量
        String[] metrics = {"euclidean", "angular", "manhattan"};
        
        for (String metric : metrics) {
            AnnoyParam param = AnnoyParam.builder()
                    .vectorDimension(3)
                    .distanceMetric(metric)
                    .nTrees(5)
                    .build();
            
            Annoy vectorStore = new Annoy(mockEmbeddings, "test_" + metric, param);
            vectorStore.addDocuments(documents);
            vectorStore.buildIndex();
            vectorStore.loadIndex();
            
            List<Document> results = vectorStore.similaritySearch("test query", 2, null, null);
            
            assertNotNull(results, "Results should not be null for metric: " + metric);
            assertFalse(results.isEmpty(), "Results should not be empty for metric: " + metric);
            
            // 清理
            vectorStore.deleteIndex();
        }
    }

    @Test
    @DisplayName("测试距离过滤功能")
    void testDistanceFiltering() {
        List<Document> documents = createTestDocuments();
        annoyVectorStore.addDocuments(documents);
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        // 不使用距离过滤
        List<Document> allResults = annoyVectorStore.similaritySearch("test query", 10, null, null);
        
        // 使用严格的距离过滤
        List<Document> filteredResults = annoyVectorStore.similaritySearch("test query", 10, 0.1, null);
        
        // 过滤后的结果应该不多于原始结果
        assertTrue(filteredResults.size() <= allResults.size());
        
        // 验证过滤后的结果都满足距离要求
        for (Document doc : filteredResults) {
            Double distance = (Double) doc.getMetadata().get("distance");
            assertNotNull(distance);
            assertTrue(distance <= 0.1, "Distance " + distance + " should be <= 0.1");
        }
    }

    @Test
    @DisplayName("测试索引清空和重建")
    void testIndexClearAndRebuild() {
        // 初始构建
        List<Document> documents = createTestDocuments();
        annoyVectorStore.addDocuments(documents);
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        assertEquals(3, annoyVectorStore.getDocumentCount());
        
        // 清空索引
        annoyVectorStore.clearIndex();
        assertEquals(0, annoyVectorStore.getDocumentCount());
        
        // 添加新文档
        List<Document> newDocuments = createDifferentTestDocuments();
        annoyVectorStore.addDocuments(newDocuments);
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        assertEquals(2, annoyVectorStore.getDocumentCount());
        
        // 验证搜索返回新文档
        List<Document> results = annoyVectorStore.similaritySearch("new content", 5, null, null);
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @DisplayName("测试并发访问安全性")
    void testConcurrentAccess() throws InterruptedException {
        List<Document> documents = createTestDocuments();
        annoyVectorStore.addDocuments(documents);
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
        
        // 创建多个线程同时执行搜索
        Thread[] threads = new Thread[5];
        boolean[] results = new boolean[5];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    List<Document> searchResults = annoyVectorStore.similaritySearch(
                            "concurrent test " + threadIndex, 3, null, null);
                    results[threadIndex] = searchResults != null && !searchResults.isEmpty();
                } catch (Exception e) {
                    results[threadIndex] = false;
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // 验证所有搜索都成功
        for (boolean result : results) {
            assertTrue(result, "Concurrent search should succeed");
        }
    }

    /**
     * 创建测试文档
     */
    private List<Document> createTestDocuments() {
        Document doc1 = new Document();
        doc1.setUniqueId("doc1");
        doc1.setPageContent("This is the first test document");
        
        Document doc2 = new Document();
        doc2.setUniqueId("doc2");
        doc2.setPageContent("This is the second test document");
        
        Document doc3 = new Document();
        doc3.setUniqueId("doc3");
        doc3.setPageContent("This is the third test document");
        
        return Arrays.asList(doc1, doc2, doc3);
    }

    /**
     * 创建不同的测试文档
     */
    private List<Document> createDifferentTestDocuments() {
        Document doc1 = new Document();
        doc1.setUniqueId("new_doc1");
        doc1.setPageContent("This is new content for testing");
        
        Document doc2 = new Document();
        doc2.setUniqueId("new_doc2");
        doc2.setPageContent("Another new document with different content");
        
        return Arrays.asList(doc1, doc2);
    }

    /**
     * 创建大批量文档
     */
    private List<Document> createLargeDocumentBatch(int count) {
        Document[] documents = new Document[count];
        for (int i = 0; i < count; i++) {
            Document doc = new Document();
            doc.setUniqueId("batch_doc_" + i);
            doc.setPageContent("Batch document content number " + i);
            documents[i] = doc;
        }
        return Arrays.asList(documents);
    }

    /**
     * 模拟的Embeddings实现，用于测试
     */
    private static class MockEmbeddings extends Embeddings {
        @Override
        public String getModelType() {
            return "mock";
        }

        @Override
        public List<Document> embedDocument(List<Document> documents) {
            for (Document doc : documents) {
                // 基于文档内容生成简单的向量
                String content = doc.getPageContent();
                double[] vector = generateVectorFromContent(content);
                doc.setEmbedding(Arrays.asList(vector[0], vector[1], vector[2]));
            }
            return documents;
        }

        @Override
        public List<String> embedQuery(String text, int recommend) {
            double[] vector = generateVectorFromContent(text);
            String vectorStr = "[" + vector[0] + ", " + vector[1] + ", " + vector[2] + "]";
            return Arrays.asList(vectorStr);
        }

        private double[] generateVectorFromContent(String content) {
            // 简单的哈希函数生成向量
            int hash = content.hashCode();
            return new double[] {
                (hash % 100) / 100.0,
                ((hash / 100) % 100) / 100.0,
                ((hash / 10000) % 100) / 100.0
            };
        }
    }
}
