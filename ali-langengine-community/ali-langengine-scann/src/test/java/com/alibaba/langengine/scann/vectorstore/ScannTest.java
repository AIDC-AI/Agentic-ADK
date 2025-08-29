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
import com.alibaba.langengine.scann.exception.ScannSearchException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ScannTest {

    private Scann scann;
    private FakeEmbeddings fakeEmbeddings;
    private static final String TEST_INDEX_NAME = "test_knowledge_index";
    private static final String TEST_SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        // 创建测试用的 embedding 模型
        fakeEmbeddings = new FakeEmbeddings();
        
        // 创建 ScaNN 参数配置
        ScannParam scannParam = new ScannParam();
        scannParam.setServerUrl(TEST_SERVER_URL);
        scannParam.setDimensions(768);
        scannParam.setIndexType("tree_ah");
        scannParam.setDistanceMeasure("dot_product");
        scannParam.setConnectionTimeout(10000);
        scannParam.setReadTimeout(30000);
        
        // 创建 ScaNN 实例
        scann = new Scann(TEST_INDEX_NAME, scannParam);
        scann.setEmbedding(fakeEmbeddings);
    }

    @AfterEach
    public void tearDown() {
        if (scann != null) {
            scann.close();
        }
    }

    @Test
    @DisplayName("测试 ScaNN 构造函数")
    public void testConstructor() {
        // 测试基本构造函数
        Scann basicScann = new Scann("basic_index");
        assertNotNull(basicScann);
        assertEquals("basic_index", basicScann.getIndexName());
        assertNotNull(basicScann.getScannParam());
        
        // 测试带参数的构造函数
        ScannParam param = new ScannParam("http://test:8080", 512);
        Scann paramScann = new Scann("param_index", param);
        assertNotNull(paramScann);
        assertEquals("param_index", paramScann.getIndexName());
        assertEquals("http://test:8080", paramScann.getScannParam().getServerUrl());
        assertEquals(512, paramScann.getScannParam().getDimensions());
        
        // 测试完整参数构造函数
        Scann fullScann = new Scann("full_index", "http://full:8080", 1024, "brute_force", "cosine");
        assertNotNull(fullScann);
        assertEquals("full_index", fullScann.getIndexName());
        assertEquals("http://full:8080", fullScann.getScannParam().getServerUrl());
        assertEquals(1024, fullScann.getScannParam().getDimensions());
        assertEquals("brute_force", fullScann.getScannParam().getIndexType());
        assertEquals("cosine", fullScann.getScannParam().getDistanceMeasure());
    }

    @Test
    @DisplayName("测试构造函数参数验证")
    public void testConstructorValidation() {
        // 测试空索引名称
        assertThrows(IllegalArgumentException.class, () -> {
            new Scann("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Scann(null);
        });
        
        // 测试无效参数
        ScannParam invalidParam = new ScannParam();
        invalidParam.setServerUrl("");
        invalidParam.setDimensions(-1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Scann("test", invalidParam);
        });
    }

    @Test
    @DisplayName("测试 ScaNN 参数配置")
    public void testScannParam() {
        ScannParam param = new ScannParam();
        
        // 测试默认值
        assertEquals("http://localhost:8080", param.getServerUrl());
        assertEquals(768, param.getDimensions());
        assertEquals("tree_ah", param.getIndexType());
        assertEquals("dot_product", param.getDistanceMeasure());
        assertEquals(30000, param.getConnectionTimeout());
        assertEquals(60000, param.getReadTimeout());
        assertEquals(100, param.getMaxConnections());
        
        // 测试设置值
        param.setServerUrl("http://custom:9090");
        param.setDimensions(512);
        param.setIndexType("brute_force");
        param.setDistanceMeasure("cosine");
        param.setConnectionTimeout(5000);
        param.setReadTimeout(10000);
        param.setMaxConnections(50);
        
        assertEquals("http://custom:9090", param.getServerUrl());
        assertEquals(512, param.getDimensions());
        assertEquals("brute_force", param.getIndexType());
        assertEquals("cosine", param.getDistanceMeasure());
        assertEquals(5000, param.getConnectionTimeout());
        assertEquals(10000, param.getReadTimeout());
        assertEquals(50, param.getMaxConnections());
        
        // 测试参数验证
        assertTrue(param.isValid());
        
        param.setDimensions(-1);
        assertFalse(param.isValid());
        
        param.setDimensions(512);
        param.setServerUrl("");
        assertFalse(param.isValid());
    }

    @Test
    @DisplayName("测试 URL 构建")
    public void testUrlBuilding() {
        ScannParam param = new ScannParam();
        param.setServerUrl("http://localhost:8080");
        
        assertEquals("http://localhost:8080/api/v1/test", param.getFullUrl("/api/v1/test"));
        assertEquals("http://localhost:8080/api/v1/test", param.getFullUrl("api/v1/test"));
        
        param.setServerUrl("http://localhost:8080/");
        assertEquals("http://localhost:8080/api/v1/test", param.getFullUrl("/api/v1/test"));
        assertEquals("http://localhost:8080/api/v1/test", param.getFullUrl("api/v1/test"));
    }

    @Test
    @DisplayName("测试添加文档 - 基本功能")
    @Disabled("需要 ScaNN 服务器运行")
    public void testAddDocuments() {
        // 准备测试文档
        List<Document> documents = createTestDocuments();
        
        // 测试添加文档
        assertDoesNotThrow(() -> {
            scann.addDocuments(documents);
        });
    }

    @Test
    @DisplayName("测试添加文档 - 空列表")
    public void testAddDocumentsEmpty() {
        // 测试空文档列表
        assertDoesNotThrow(() -> {
            scann.addDocuments(null);
            scann.addDocuments(Lists.newArrayList());
        });
    }

    @Test
    @DisplayName("测试相似性搜索 - 基本功能")
    @Disabled("需要 ScaNN 服务器运行")
    public void testSimilaritySearch() {
        // 先添加一些测试文档
        List<Document> documents = createTestDocuments();
        scann.addDocuments(documents);
        
        // 执行相似性搜索
        List<Document> results = scann.similaritySearch("hello world", 5);
        assertNotNull(results);
        assertTrue(results.size() <= 5);
    }

    @Test
    @DisplayName("测试相似性搜索 - 参数验证")
    public void testSimilaritySearchValidation() {
        // 测试空查询
        List<Document> results = scann.similaritySearch("", 5);
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        results = scann.similaritySearch(null, 5);
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        // 测试无效 k 值
        results = scann.similaritySearch("test", 0);
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        results = scann.similaritySearch("test", -1);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("测试相似性搜索 - 无 embedding 模型")
    public void testSimilaritySearchNoEmbedding() {
        scann.setEmbedding(null);

        // 现在应该抛出 ScannSearchException 而不是返回空列表
        assertThrows(ScannSearchException.class, () -> {
            scann.similaritySearch("test query", 5);
        });
    }

    @Test
    @DisplayName("测试删除文档")
    @Disabled("需要 ScaNN 服务器运行")
    public void testDeleteDocuments() {
        List<String> documentIds = Lists.newArrayList("doc1", "doc2", "doc3");
        
        assertDoesNotThrow(() -> {
            scann.deleteDocuments(documentIds);
        });
        
        // 测试空列表
        assertDoesNotThrow(() -> {
            scann.deleteDocuments(null);
            scann.deleteDocuments(Lists.newArrayList());
        });
    }

    @Test
    @DisplayName("测试获取索引统计信息")
    @Disabled("需要 ScaNN 服务器运行")
    public void testGetIndexStats() {
        Map<String, Object> stats = scann.getIndexStats();
        assertNotNull(stats);
    }

    @Test
    @DisplayName("测试初始化")
    @Disabled("需要 ScaNN 服务器运行")
    public void testInit() {
        assertDoesNotThrow(() -> {
            scann.init();
        });
    }

    @Test
    @DisplayName("测试关闭")
    public void testClose() {
        assertDoesNotThrow(() -> {
            scann.close();
        });
    }

    /**
     * 创建测试文档
     *
     * @return 测试文档列表
     */
    private List<Document> createTestDocuments() {
        List<Document> documents = Lists.newArrayList();
        
        // 文档1
        Document doc1 = new Document();
        doc1.setUniqueId("doc1");
        doc1.setPageContent("Hello world, this is a test document.");
        Map<String, Object> metadata1 = new HashMap<>();
        metadata1.put("category", "test");
        metadata1.put("source", "unit_test");
        doc1.setMetadata(metadata1);
        documents.add(doc1);
        
        // 文档2
        Document doc2 = new Document();
        doc2.setUniqueId("doc2");
        doc2.setPageContent("ScaNN is a high-performance vector similarity search library.");
        Map<String, Object> metadata2 = new HashMap<>();
        metadata2.put("category", "technology");
        metadata2.put("source", "documentation");
        doc2.setMetadata(metadata2);
        documents.add(doc2);
        
        // 文档3
        Document doc3 = new Document();
        doc3.setUniqueId("doc3");
        doc3.setPageContent("Machine learning and artificial intelligence are transforming the world.");
        Map<String, Object> metadata3 = new HashMap<>();
        metadata3.put("category", "ai");
        metadata3.put("source", "article");
        doc3.setMetadata(metadata3);
        documents.add(doc3);
        
        return documents;
    }
}
