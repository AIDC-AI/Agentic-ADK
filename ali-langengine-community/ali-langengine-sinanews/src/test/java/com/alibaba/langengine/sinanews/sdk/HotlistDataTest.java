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
 * Unit tests for HotlistData
 */
public class HotlistDataTest {

    @Test
    public void testDefaultConstructor() {
        // Execute
        HotlistData data = new HotlistData();
        
        // Verify
        assertNull(data.getData());
    }

    @Test
    public void testConstructorWithParameters() {
        // Prepare
        List<HotlistItem> items = new ArrayList<>();
        HotlistItem item = new HotlistItem();
        item.setTitle("Test News");
        items.add(item);
        
        // Execute
        HotlistData data = new HotlistData(items);
        
        // Verify
        assertEquals(items, data.getData());
        assertEquals(1, data.getData().size());
        assertEquals("Test News", data.getData().get(0).getTitle());
    }

    @Test
    public void testSetAndGetData() {
        // Prepare
        HotlistData data = new HotlistData();
        List<HotlistItem> items = new ArrayList<>();
        HotlistItem item = new HotlistItem();
        item.setTitle("Test News");
        items.add(item);
        
        // Execute
        data.setData(items);
        
        // Verify
        assertEquals(items, data.getData());
        assertEquals(1, data.getData().size());
        assertEquals("Test News", data.getData().get(0).getTitle());
    }
}