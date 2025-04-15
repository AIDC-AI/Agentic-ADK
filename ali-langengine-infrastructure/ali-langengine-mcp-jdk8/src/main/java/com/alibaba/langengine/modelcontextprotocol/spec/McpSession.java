/*
 * Copyright 2025 Alibaba Group Holding Ltd.
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
package com.alibaba.langengine.modelcontextprotocol.spec;

import lombok.Data;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a Model Control Protocol (MCP) session that handles communication between
 * clients and the server. This interface provides methods for sending requests and
 * notifications, as well as managing the session lifecycle.
 *
 * <p>
 * The session operates asynchronously using CompletableFuture for
 * non-blocking operations. It supports both request-response patterns and one-way
 * notifications.
 * </p>
 *
 * JDK 1.8 compatible version.
 *
 * @author Christian Tzolov
 * @author Dariusz Jędrzejczyk
 */
public interface McpSession {

    /**
     * Sends a request to the model counterparty and expects a response of type T.
     *
     * <p>
     * This method handles the request-response pattern where a response is expected from
     * the client or server. The response type is determined by the provided
     * TypeReference.
     * </p>
     * @param <T> the type of the expected response
     * @param method the name of the method to be called on the counterparty
     * @param requestParams the parameters to be sent with the request
     * @param typeRef the TypeReference describing the expected response type
     * @return a CompletableFuture that will emit the response when received
     */
    <T> CompletableFuture<T> sendRequest(String method, Object requestParams, TypeReference<T> typeRef);

    /**
     * Sends a notification to the model client or server without parameters.
     *
     * <p>
     * This method implements the notification pattern where no response is expected from
     * the counterparty. It's useful for fire-and-forget scenarios.
     * </p>
     * @param method the name of the notification method to be called on the server
     * @return a CompletableFuture that completes when the notification has been sent
     */
    default CompletableFuture<Void> sendNotification(String method) {
        return sendNotification(method, null);
    }

    /**
     * Sends a notification to the model client or server with parameters.
     *
     * <p>
     * Similar to {@link #sendNotification(String)} but allows sending additional
     * parameters with the notification.
     * </p>
     * @param method the name of the notification method to be sent to the counterparty
     * @param params a map of parameters to be sent with the notification
     * @return a CompletableFuture that completes when the notification has been sent
     */
    CompletableFuture<Void> sendNotification(String method, Map<String, Object> params);

    /**
     * Closes the session and releases any associated resources asynchronously.
     * @return a {@link CompletableFuture<Void>} that completes when the session has been closed.
     */
    CompletableFuture<Void> closeGracefully();

    /**
     * Closes the session and releases any associated resources.
     */
    void close();
}
