/**
 * Copyright (C) 2024 AIDC-AI
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.openmanus;

import com.alibaba.langengine.core.chatmodel.BaseChatModel;
import com.alibaba.langengine.core.util.WorkPropertiesUtils;
import com.alibaba.langengine.dashscope.DashScopeModelName;
import com.alibaba.langengine.dashscope.model.DashScopeOpenAIChatModel;
import com.alibaba.langengine.openai.model.ChatModelOpenAI;
import com.alibaba.langengine.openai.model.OpenAIModelConstants;

public class OpenManusConfiguration {

    public static String APPBUILDER_APIID;
    public static String APPBUILDER_AK;
    
    public static BaseChatModel manusChatModel;
    public static BaseChatModel planningChatModel;

    static {
        try {
            APPBUILDER_APIID = WorkPropertiesUtils.getFirstAvailable("aidc_appbuilder_appid");
        } catch (Exception e) {
            APPBUILDER_APIID = null;
        }
        try {
            APPBUILDER_AK = WorkPropertiesUtils.getFirstAvailable("aidc_appbuilder_ak");
        } catch (Exception e) {
            APPBUILDER_AK = null;
        }
        
        try {
            manusChatModel = new DashScopeOpenAIChatModel();
            manusChatModel.setModel(DashScopeModelName.QWEN25_MAX);
            manusChatModel.setTemperature(0d);
            manusChatModel.setToolChoice("required");
        } catch (Exception e) {
            System.err.println("Failed to initialize manusChatModel: " + e.getMessage());
            manusChatModel = null;
        }
        
        try {
            planningChatModel = new DashScopeOpenAIChatModel();
            planningChatModel.setModel(DashScopeModelName.QWEN25_MAX);
            planningChatModel.setTemperature(0d);
            planningChatModel.setToolChoice("required");
        } catch (Exception e) {
            System.err.println("Failed to initialize planningChatModel: " + e.getMessage());
            planningChatModel = null;
        }
    }

    public static BaseChatModel getManusChatModel() {
        if (manusChatModel == null) {
            try {
                manusChatModel = new DashScopeOpenAIChatModel();
                manusChatModel.setModel(DashScopeModelName.QWEN25_MAX);
                manusChatModel.setTemperature(0d);
                manusChatModel.setToolChoice("required");
            } catch (Exception e) {
                System.err.println("Warning: Could not create manusChatModel: " + e.getMessage());
            }
        }
        return manusChatModel;
    }

    public static BaseChatModel getPlanningChatModel() {
        if (planningChatModel == null) {
            try {
                planningChatModel = new DashScopeOpenAIChatModel();
                planningChatModel.setModel(DashScopeModelName.QWEN25_MAX);
                planningChatModel.setTemperature(0d);
                planningChatModel.setToolChoice("required");
            } catch (Exception e) {
                System.err.println("Warning: Could not create planningChatModel: " + e.getMessage());
            }
        }
        return planningChatModel;
    }
}
