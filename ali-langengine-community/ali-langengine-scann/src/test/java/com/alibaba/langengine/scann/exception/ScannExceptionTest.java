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
package com.alibaba.langengine.scann.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ScaNN 异常类测试
 */
public class ScannExceptionTest {

    @Test
    @DisplayName("测试 ScannException 基础异常")
    public void testScannException() {
        // 测试消息构造函数
        ScannException exception1 = new ScannException("Test message");
        assertEquals("Test message", exception1.getMessage());
        assertNull(exception1.getCause());

        // 测试消息和原因构造函数
        RuntimeException cause = new RuntimeException("Original cause");
        ScannException exception2 = new ScannException("Test message", cause);
        assertEquals("Test message", exception2.getMessage());
        assertEquals(cause, exception2.getCause());

        // 测试原因构造函数
        ScannException exception3 = new ScannException(cause);
        assertEquals(cause, exception3.getCause());
    }

    @Test
    @DisplayName("测试 ScannConnectionException 连接异常")
    public void testScannConnectionException() {
        ScannConnectionException exception = new ScannConnectionException("Connection failed");
        assertEquals("Connection failed", exception.getMessage());
        assertTrue(exception instanceof ScannException);
    }

    @Test
    @DisplayName("测试 ScannIndexException 索引异常")
    public void testScannIndexException() {
        ScannIndexException exception = new ScannIndexException("Index operation failed");
        assertEquals("Index operation failed", exception.getMessage());
        assertTrue(exception instanceof ScannException);
    }

    @Test
    @DisplayName("测试 ScannSearchException 搜索异常")
    public void testScannSearchException() {
        ScannSearchException exception = new ScannSearchException("Search failed");
        assertEquals("Search failed", exception.getMessage());
        assertTrue(exception instanceof ScannException);
    }

    @Test
    @DisplayName("测试 ScannDocumentException 文档异常")
    public void testScannDocumentException() {
        ScannDocumentException exception = new ScannDocumentException("Document operation failed");
        assertEquals("Document operation failed", exception.getMessage());
        assertTrue(exception instanceof ScannException);
    }

    @Test
    @DisplayName("测试异常继承关系")
    public void testExceptionHierarchy() {
        // 所有自定义异常都应该继承自 ScannException
        assertTrue(ScannConnectionException.class.getSuperclass() == ScannException.class);
        assertTrue(ScannIndexException.class.getSuperclass() == ScannException.class);
        assertTrue(ScannSearchException.class.getSuperclass() == ScannException.class);
        assertTrue(ScannDocumentException.class.getSuperclass() == ScannException.class);

        // ScannException 应该继承自 RuntimeException
        assertTrue(ScannException.class.getSuperclass() == RuntimeException.class);
    }
}
