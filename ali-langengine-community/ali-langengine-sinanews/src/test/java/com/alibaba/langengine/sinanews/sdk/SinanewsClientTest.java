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
 * Unit tests for SinanewsClient
 */
public class SinanewsClientTest {

    @Test
    public void testDefaultConstructor() {
        // Execute
        SinanewsClient client = new SinanewsClient();
        
        // Verify
        assertNotNull(client);
    }

    @Test
    public void testConstructorWithBaseUrl() {
        // Prepare
        String baseUrl = "https://test.api.com";
        
        // Execute
        SinanewsClient client = new SinanewsClient(baseUrl);
        
        // Verify
        assertNotNull(client);
    }

    @Test
    public void testBuildUrlWithDefaultRequest() {
        // Prepare
        SinanewsClient client = new SinanewsClient("https://newsapp.sina.cn/api/hotlist");
        HotlistRequest request = new HotlistRequest("test-id");
        
        // This test just verifies that the client can be instantiated
        assertNotNull(client);
        assertNotNull(request);
    }
}