package com.alibaba.langengine.feishu.examples;

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.FeishuToolFactory;
import com.alibaba.langengine.feishu.tools.FeishuContactTool;
import com.alibaba.langengine.feishu.tools.FeishuDocumentTool;
import com.alibaba.langengine.feishu.tools.FeishuMeetingTool;
import com.alibaba.langengine.feishu.tools.FeishuMessageTool;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class FeishuToolsExample {

    private final FeishuToolFactory toolFactory;
    private final FeishuMessageTool messageTool;
    private final FeishuDocumentTool documentTool;
    private final FeishuMeetingTool meetingTool;
    private final FeishuContactTool contactTool;

    /**
     * 构造函数
     * 
     * @param appId 飞书应用ID
     * @param appSecret 飞书应用密钥
     */
    public FeishuToolsExample(String appId, String appSecret) {
        // 创建配置
        FeishuConfiguration config = new FeishuConfiguration(appId, appSecret);
        config.setDebug(true); // 启用调试模式
        
        // 创建工具工厂
        this.toolFactory = new FeishuToolFactory(config);
        
        // 创建各种工具
        this.messageTool = toolFactory.createMessageTool();
        this.documentTool = toolFactory.createDocumentTool();
        this.meetingTool = toolFactory.createMeetingTool();
        this.contactTool = toolFactory.createContactTool();
        
        log.info("FeishuToolsExample initialized successfully");
    }

    /**
     * 发送文本消息示例
     * 
     * @param userId 用户ID
     * @param message 消息内容
     * @return 执行结果
     */
    public ToolExecuteResult sendTextMessage(String userId, String message) {
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", userId);
        input.put("receive_id_type", "open_id");
        input.put("msg_type", "text");
        input.put("content", message);
        
        String inputJson = JSON.toJSONString(input);
        
        log.info("Sending text message to user: {}", userId);
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        if (!result.isInterrupted()) {
            log.info("Message sent successfully: {}", result.getOutput());
        } else {
            log.error("Failed to send message: {}", result.getOutput());
        }
        
        return result;
    }

    /**
     * 发送富文本消息示例
     * 
     * @param userId 用户ID
     * @param title 消息标题
     * @param content 消息内容
     * @return 执行结果
     */
    public ToolExecuteResult sendRichTextMessage(String userId, String title, String content) {
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", userId);
        input.put("receive_id_type", "open_id");
        input.put("msg_type", "post");
        
        // 构建富文本内容
        Map<String, Object> postContent = new HashMap<>();
        Map<String, Object> zhCn = new HashMap<>();
        zhCn.put("title", title);
        zhCn.put("content", new Object[][]{
            {Map.of("tag", "text", "text", content)}
        });
        postContent.put("zh_cn", zhCn);
        input.put("content", postContent);
        
        String inputJson = JSON.toJSONString(input);
        
        log.info("Sending rich text message to user: {}", userId);
        return messageTool.execute(inputJson);
    }

    /**
     * 创建文档示例
     * 
     * @param title 文档标题
     * @param content 文档内容
     * @return 执行结果
     */
    public ToolExecuteResult createDocument(String title, String content) {
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", title);
        input.put("content", content);
        
        String inputJson = JSON.toJSONString(input);
        
        log.info("Creating document with title: {}", title);
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        if (!result.isInterrupted()) {
            log.info("Document created successfully: {}", result.getOutput());
        } else {
            log.error("Failed to create document: {}", result.getOutput());
        }
        
        return result;
    }

    /**
     * 创建会议示例
     * 
     * @param topic 会议主题
     * @param startTime 开始时间
     * @param duration 会议时长（分钟）
     * @return 执行结果
     */
    public ToolExecuteResult createMeeting(String topic, String startTime, int duration) {
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", topic);
        input.put("start_time", startTime);
        input.put("duration", duration);
        input.put("description", "由AI助手创建的会议");
        input.put("need_password", false);
        input.put("auto_record", true);
        
        String inputJson = JSON.toJSONString(input);
        
        log.info("Creating meeting with topic: {}", topic);
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        if (!result.isInterrupted()) {
            log.info("Meeting created successfully: {}", result.getOutput());
        } else {
            log.error("Failed to create meeting: {}", result.getOutput());
        }
        
        return result;
    }

    /**
     * 搜索用户示例
     * 
     * @param email 用户邮箱
     * @return 执行结果
     */
    public ToolExecuteResult searchUserByEmail(String email) {
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        input.put("emails", new String[]{email});
        
        String inputJson = JSON.toJSONString(input);
        
        log.info("Searching user by email: {}", email);
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        if (!result.isInterrupted()) {
            log.info("User search completed: {}", result.getOutput());
        } else {
            log.error("Failed to search user: {}", result.getOutput());
        }
        
        return result;
    }

    /**
     * 综合示例：创建会议并发送通知
     * 
     * @param topic 会议主题
     * @param startTime 开始时间
     * @param attendeeEmail 参会者邮箱
     * @return 是否成功
     */
    public boolean createMeetingAndNotify(String topic, String startTime, String attendeeEmail) {
        try {
            // 1. 搜索用户
            log.info("Step 1: Searching for user by email: {}", attendeeEmail);
            ToolExecuteResult searchResult = searchUserByEmail(attendeeEmail);
            if (searchResult.isInterrupted()) {
                log.error("Failed to find user: {}", searchResult.getOutput());
                return false;
            }

            // 2. 创建会议
            log.info("Step 2: Creating meeting: {}", topic);
            ToolExecuteResult meetingResult = createMeeting(topic, startTime, 60);
            if (meetingResult.isInterrupted()) {
                log.error("Failed to create meeting: {}", meetingResult.getOutput());
                return false;
            }
            
            // 3. 解析会议信息
            Map<String, Object> meetingData = JSON.parseObject(meetingResult.getOutput(), Map.class);
            String meetingUrl = (String) meetingData.get("meeting_url");
            
            // 4. 发送会议通知
            log.info("Step 3: Sending meeting notification");
            String notificationMessage = String.format("您好！会议 '%s' 已创建，开始时间：%s，会议链接：%s", 
                    topic, startTime, meetingUrl);
            
            // 这里需要从搜索结果中提取用户ID
            // 实际使用时需要解析searchResult来获取用户的open_id
            String userId = "extracted_user_id"; // 实际应该从searchResult中提取
            
            ToolExecuteResult messageResult = sendTextMessage(userId, notificationMessage);
            if (messageResult.isInterrupted()) {
                log.error("Failed to send notification: {}", messageResult.getOutput());
                return false;
            }
            
            log.info("Meeting created and notification sent successfully");
            return true;
            
        } catch (Exception e) {
            log.error("Error in createMeetingAndNotify", e);
            return false;
        }
    }

    /**
     * 主方法，用于演示工具使用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 从环境变量获取配置
        String appId = System.getenv("FEISHU_APP_ID");
        String appSecret = System.getenv("FEISHU_APP_SECRET");
        
        if (appId == null || appSecret == null) {
            System.err.println("请设置环境变量 FEISHU_APP_ID 和 FEISHU_APP_SECRET");
            return;
        }
        
        // 创建示例实例
        FeishuToolsExample example = new FeishuToolsExample(appId, appSecret);
        
        // 演示各种功能
        System.out.println("=== 飞书工具集成示例 ===");
        
        // 1. 发送消息
        System.out.println("\n1. 发送文本消息");
        ToolExecuteResult messageResult = example.sendTextMessage("test_user_id", "Hello from LangEngine!");
        System.out.println("结果: " + messageResult.getOutput());
        
        // 2. 创建文档
        System.out.println("\n2. 创建文档");
        ToolExecuteResult documentResult = example.createDocument("AI生成文档", "这是由AI助手自动生成的文档内容。");
        System.out.println("结果: " + documentResult.getOutput());
        
        // 3. 创建会议
        System.out.println("\n3. 创建会议");
        ToolExecuteResult meetingResult = example.createMeeting("AI助手会议", "2024-12-01 14:00:00", 60);
        System.out.println("结果: " + meetingResult.getOutput());
        
        // 4. 搜索用户
        System.out.println("\n4. 搜索用户");
        ToolExecuteResult contactResult = example.searchUserByEmail("test@example.com");
        System.out.println("结果: " + contactResult.getOutput());
        
        System.out.println("\n=== 示例完成 ===");
    }
}
