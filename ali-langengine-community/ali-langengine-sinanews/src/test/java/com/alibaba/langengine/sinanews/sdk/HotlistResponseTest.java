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
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for HotlistResponse
 */
public class HotlistResponseTest {

    @Test
    public void testDefaultConstructor() {
        // Execute
        HotlistResponse response = new HotlistResponse();
        
        // Verify
        assertNull(response.getStatus());
        assertNull(response.getInfo());
        assertNull(response.getData());
    }

    @Test
    public void testConstructorWithParameters() {
        // Prepare
        Integer status = 0;
        String info = "success";
        HotlistData data = new HotlistData();
        
        // Execute
        HotlistResponse response = new HotlistResponse(status, info, data);
        
        // Verify
        assertEquals(status, response.getStatus());
        assertEquals(info, response.getInfo());
        assertEquals(data, response.getData());
    }

    @Test
    public void testSetAndGetStatus() {
        // Prepare
        HotlistResponse response = new HotlistResponse();
        Integer status = 0;
        
        // Execute
        response.setStatus(status);
        
        // Verify
        assertEquals(status, response.getStatus());
    }

    @Test
    public void testSetAndGetInfo() {
        // Prepare
        HotlistResponse response = new HotlistResponse();
        String info = "success";
        
        // Execute
        response.setInfo(info);
        
        // Verify
        assertEquals(info, response.getInfo());
    }

    @Test
    public void testSetAndGetData() {
        // Prepare
        HotlistResponse response = new HotlistResponse();
        HotlistData data = new HotlistData();
        List<HotlistItem> items = new ArrayList<>();
        HotlistItem item = new HotlistItem();
        item.setTitle("Test News");
        items.add(item);
        data.setData(items);
        
        // Execute
        response.setData(data);
        
        // Verify
        assertEquals(data, response.getData());
        assertEquals(1, response.getData().getData().size());
        assertEquals("Test News", response.getData().getData().get(0).getTitle());
    }
}