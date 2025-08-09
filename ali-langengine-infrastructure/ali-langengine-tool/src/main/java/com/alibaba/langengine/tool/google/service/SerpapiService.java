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
package com.alibaba.langengine.tool.google.service;

import com.alibaba.langengine.core.model.fastchat.service.RetrofitInitService;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.net.Proxy;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * serpapi服务
 *
 * @author xiaoxuan.lp
 */
@Data
public class SerpapiService extends RetrofitInitService<SerpapiApi> {

    public SerpapiService() {
        super();
    }

    public SerpapiService(String serverUrl, Duration timeout, boolean authentication, String token) {
        super(serverUrl, timeout, authentication, token);
    }

    public SerpapiService(String serverUrl, Duration timeout, boolean authentication, String token, Proxy proxy) {
        super(serverUrl, timeout, authentication, token, proxy);
    }

    @Override
    public Class<SerpapiApi> getServiceApiClass() {
        return SerpapiApi.class;
    }

    public OkHttpClient defaultClient(Duration timeout, Proxy proxy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(100, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);
        // contribute by dapeng.fdp
        if(proxy != null) {
            builder.proxy(proxy);
        }
        return builder.build();
    }

    public Map<String, Object> search(String q, Integer start, Integer num, String serpapiKey) {
        return execute(getApi().search(q, start, num, "google", serpapiKey));
    }
}
