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
 * Unit tests for HotlistItem
 */
public class HotlistItemTest {

    @Test
    public void testDefaultConstructor() {
        // Execute
        HotlistItem item = new HotlistItem();
        
        // Verify
        assertNull(item.getId());
        assertNull(item.getTitle());
        assertNull(item.getUrl());
        assertNull(item.getDesc());
        assertNull(item.getLogo());
        assertNull(item.getType());
        assertNull(item.getHotnum());
    }

    @Test
    public void testSetAndGetId() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String id = "test-id";
        
        // Execute
        item.setId(id);
        
        // Verify
        assertEquals(id, item.getId());
    }

    @Test
    public void testSetAndGetTitle() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String title = "Test News";
        
        // Execute
        item.setTitle(title);
        
        // Verify
        assertEquals(title, item.getTitle());
    }

    @Test
    public void testSetAndGetUrl() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String url = "http://test.com";
        
        // Execute
        item.setUrl(url);
        
        // Verify
        assertEquals(url, item.getUrl());
    }

    @Test
    public void testSetAndGetDesc() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String desc = "Test description";
        
        // Execute
        item.setDesc(desc);
        
        // Verify
        assertEquals(desc, item.getDesc());
    }

    @Test
    public void testSetAndGetLogo() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String logo = "http://test.com/logo.png";
        
        // Execute
        item.setLogo(logo);
        
        // Verify
        assertEquals(logo, item.getLogo());
    }

    @Test
    public void testSetAndGetType() {
        // Prepare
        HotlistItem item = new HotlistItem();
        String type = "news";
        
        // Execute
        item.setType(type);
        
        // Verify
        assertEquals(type, item.getType());
    }

    @Test
    public void testSetAndGetHotnum() {
        // Prepare
        HotlistItem item = new HotlistItem();
        Integer hotnum = 100;
        
        // Execute
        item.setHotnum(hotnum);
        
        // Verify
        assertEquals(hotnum, item.getHotnum());
    }
}