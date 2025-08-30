package com.alibaba.langengine.feishu.tools.schema;

import com.alibaba.langengine.core.tool.StructuredParameter;
import com.alibaba.langengine.core.tool.StructuredSchema;

import java.util.HashMap;
import java.util.Map;


public class FeishuMessageSchema extends StructuredSchema {

    public FeishuMessageSchema() {
        // 接收者ID参数
        StructuredParameter receiveIdParam = new StructuredParameter();
        receiveIdParam.setName("receive_id");
        receiveIdParam.setDescription("消息接收者的ID，可以是用户ID、邮箱、手机号或群聊ID");
        receiveIdParam.setRequired(true);
        
        Map<String, Object> receiveIdSchema = new HashMap<>();
        receiveIdSchema.put("type", "string");
        receiveIdParam.setSchema(receiveIdSchema);
        
        getParameters().add(receiveIdParam);

        // 接收者ID类型参数
        StructuredParameter receiveIdTypeParam = new StructuredParameter();
        receiveIdTypeParam.setName("receive_id_type");
        receiveIdTypeParam.setDescription("接收者ID的类型，可选值：open_id（默认）、user_id、email、mobile、chat_id");
        receiveIdTypeParam.setRequired(false);
        
        Map<String, Object> receiveIdTypeSchema = new HashMap<>();
        receiveIdTypeSchema.put("type", "string");
        receiveIdTypeSchema.put("enum", new String[]{"open_id", "user_id", "email", "mobile", "chat_id"});
        receiveIdTypeSchema.put("default", "open_id");
        receiveIdTypeParam.setSchema(receiveIdTypeSchema);
        
        getParameters().add(receiveIdTypeParam);

        // 消息类型参数
        StructuredParameter msgTypeParam = new StructuredParameter();
        msgTypeParam.setName("msg_type");
        msgTypeParam.setDescription("消息类型，可选值：text（文本消息，默认）、post（富文本消息）、interactive（交互式卡片）、image（图片）、file（文件）等");
        msgTypeParam.setRequired(false);
        
        Map<String, Object> msgTypeSchema = new HashMap<>();
        msgTypeSchema.put("type", "string");
        msgTypeSchema.put("enum", new String[]{"text", "post", "interactive", "image", "file", "audio", "video", "share_chat", "share_user"});
        msgTypeSchema.put("default", "text");
        msgTypeParam.setSchema(msgTypeSchema);
        
        getParameters().add(msgTypeParam);

        // 消息内容参数
        StructuredParameter contentParam = new StructuredParameter();
        contentParam.setName("content");
        contentParam.setDescription("消息内容。对于文本消息，可以是字符串；对于富文本和卡片消息，需要是JSON格式的对象");
        contentParam.setRequired(true);
        
        Map<String, Object> contentSchema = new HashMap<>();
        contentSchema.put("type", "object");
        contentSchema.put("description", "消息内容，根据msg_type的不同，格式也不同");
        
        // 添加内容示例
        Map<String, Object> examples = new HashMap<>();
        examples.put("text_example", "这是一条文本消息");
        examples.put("post_example", "{\"zh_cn\": {\"title\": \"标题\", \"content\": [[{\"tag\": \"text\", \"text\": \"富文本内容\"}]]}}");
        examples.put("interactive_example", "{\"elements\": [{\"tag\": \"div\", \"text\": {\"content\": \"卡片内容\", \"tag\": \"lark_md\"}}]}");
        contentSchema.put("examples", examples);
        
        contentParam.setSchema(contentSchema);
        
        getParameters().add(contentParam);
    }
}
