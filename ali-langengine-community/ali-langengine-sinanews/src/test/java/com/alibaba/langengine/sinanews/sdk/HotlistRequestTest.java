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
package com.alibaba.langengine.sinanews.sdk;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for HotlistRequest
 */
public class HotlistRequestTest {

    @Test
    public void testDefaultConstructor() {
        // Execute
        HotlistRequest request = new HotlistRequest();
        
        // Verify
        assertNull(request.getNewsId());
    }

    @Test
    public void testConstructorWithNewsId() {
        // Prepare
        String newsId = "test-news-id";
        
        // Execute
        HotlistRequest request = new HotlistRequest(newsId);
        
        // Verify
        assertEquals(newsId, request.getNewsId());
    }

    @Test
    public void testSetAndGetNewsId() {
        // Prepare
        HotlistRequest request = new HotlistRequest();
        String newsId = "test-news-id";
        
        // Execute
        request.setNewsId(newsId);
        
        // Verify
        assertEquals(newsId, request.getNewsId());
    }
}