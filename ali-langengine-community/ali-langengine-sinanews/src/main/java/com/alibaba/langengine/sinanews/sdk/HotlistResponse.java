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

/**
 * Hotlist Response
 * Represents the response from the Sinanews hotlist API
 */
public class HotlistResponse {
    private Integer status;
    private String info;
    private HotlistData data;

    public HotlistResponse() {
    }

    public HotlistResponse(Integer status, String info, HotlistData data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    // Getters and Setters

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public HotlistData getData() {
        return data;
    }

    public void setData(HotlistData data) {
        this.data = data;
    }
}