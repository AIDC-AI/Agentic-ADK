package com.alibaba.langengine.feishu;

import com.alibaba.langengine.core.tool.BaseTool;
import com.alibaba.langengine.feishu.tools.FeishuContactTool;
import com.alibaba.langengine.feishu.tools.FeishuDocumentTool;
import com.alibaba.langengine.feishu.tools.FeishuMeetingTool;
import com.alibaba.langengine.feishu.tools.FeishuMessageTool;
import com.alibaba.langengine.feishu.util.FeishuConfigLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FeishuToolFactory {

    private final FeishuConfiguration configuration;

    /**
     * 构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuToolFactory(FeishuConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("FeishuConfiguration cannot be null");
        }
        if (!configuration.isValid()) {
            throw new IllegalArgumentException("Invalid FeishuConfiguration: " + configuration);
        }
        
        this.configuration = configuration;
        log.info("FeishuToolFactory initialized with configuration: {}", configuration);
    }

    /**
     * 创建消息工具
     * 
     * @return 飞书消息工具
     */
    public FeishuMessageTool createMessageTool() {
        return new FeishuMessageTool(configuration);
    }

    /**
     * 创建文档工具
     * 
     * @return 飞书文档工具
     */
    public FeishuDocumentTool createDocumentTool() {
        return new FeishuDocumentTool(configuration);
    }

    /**
     * 创建会议工具
     * 
     * @return 飞书会议工具
     */
    public FeishuMeetingTool createMeetingTool() {
        return new FeishuMeetingTool(configuration);
    }

    /**
     * 创建通讯录工具
     * 
     * @return 飞书通讯录工具
     */
    public FeishuContactTool createContactTool() {
        return new FeishuContactTool(configuration);
    }

    /**
     * 创建所有飞书工具
     * 
     * @return 所有飞书工具的列表
     */
    public List<BaseTool> createAllTools() {
        List<BaseTool> tools = new ArrayList<>();
        
        tools.add(createMessageTool());
        tools.add(createDocumentTool());
        tools.add(createMeetingTool());
        tools.add(createContactTool());
        
        log.info("Created {} Feishu tools", tools.size());
        return tools;
    }

    /**
     * 创建指定类型的工具
     * 
     * @param toolType 工具类型
     * @return 对应的工具实例
     * @throws IllegalArgumentException 不支持的工具类型
     */
    public BaseTool createTool(FeishuToolType toolType) {
        if (toolType == null) {
            throw new IllegalArgumentException("ToolType cannot be null");
        }
        
        switch (toolType) {
            case MESSAGE:
                return createMessageTool();
            case DOCUMENT:
                return createDocumentTool();
            case MEETING:
                return createMeetingTool();
            case CONTACT:
                return createContactTool();
            default:
                throw new IllegalArgumentException("Unsupported tool type: " + toolType);
        }
    }

    /**
     * 获取配置
     * 
     * @return 飞书配置
     */
    public FeishuConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * 飞书工具类型枚举
     */
    public enum FeishuToolType {
        /**
         * 消息工具
         */
        MESSAGE("message", "飞书消息工具"),
        
        /**
         * 文档工具
         */
        DOCUMENT("document", "飞书文档工具"),
        
        /**
         * 会议工具
         */
        MEETING("meeting", "飞书会议工具"),
        
        /**
         * 通讯录工具
         */
        CONTACT("contact", "飞书通讯录工具");

        private final String code;
        private final String description;

        FeishuToolType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        /**
         * 获取工具类型代码
         * 
         * @return 工具类型代码
         */
        public String getCode() {
            return code;
        }

        /**
         * 获取工具类型描述
         * 
         * @return 工具类型描述
         */
        public String getDescription() {
            return description;
        }

        /**
         * 根据代码获取工具类型
         * 
         * @param code 工具类型代码
         * @return 对应的工具类型
         * @throws IllegalArgumentException 不支持的工具类型代码
         */
        public static FeishuToolType fromCode(String code) {
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("Tool type code cannot be null or empty");
            }
            
            for (FeishuToolType type : values()) {
                if (type.code.equalsIgnoreCase(code.trim())) {
                    return type;
                }
            }
            
            throw new IllegalArgumentException("Unsupported tool type code: " + code);
        }

        @Override
        public String toString() {
            return "FeishuToolType{" +
                    "code='" + code + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    /**
     * 创建默认的飞书工具工厂
     * 
     * @return 使用默认配置的工具工厂
     */
    public static FeishuToolFactory createDefault() {
        try {
            FeishuConfiguration defaultConfig = FeishuConfigLoader.loadConfiguration();
            return new FeishuToolFactory(defaultConfig);
        } catch (Exception e) {
            // 如果无法加载配置，创建一个带有测试值的配置
            FeishuConfiguration testConfig = new FeishuConfiguration();
            testConfig.setAppId("test_app_id");
            testConfig.setAppSecret("test_app_secret");
            return new FeishuToolFactory(testConfig);
        }
    }

    /**
     * 创建带应用凭证的飞书工具工厂
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @return 工具工厂
     */
    public static FeishuToolFactory create(String appId, String appSecret) {
        FeishuConfiguration config = new FeishuConfiguration(appId, appSecret);
        return new FeishuToolFactory(config);
    }

    /**
     * 创建带完整配置的飞书工具工厂
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param baseUrl 基础URL
     * @return 工具工厂
     */
    public static FeishuToolFactory create(String appId, String appSecret, String baseUrl) {
        FeishuConfiguration config = new FeishuConfiguration(appId, appSecret, baseUrl);
        return new FeishuToolFactory(config);
    }

    @Override
    public String toString() {
        return "FeishuToolFactory{" +
                "configuration=" + configuration +
                '}';
    }
}
