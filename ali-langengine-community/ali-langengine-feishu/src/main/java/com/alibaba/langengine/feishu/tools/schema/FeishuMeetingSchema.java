package com.alibaba.langengine.feishu.tools.schema;

import com.alibaba.langengine.core.tool.StructuredParameter;
import com.alibaba.langengine.core.tool.StructuredSchema;

import java.util.HashMap;
import java.util.Map;


public class FeishuMeetingSchema extends StructuredSchema {

    public FeishuMeetingSchema() {
        // 操作类型参数
        StructuredParameter operationParam = new StructuredParameter();
        operationParam.setName("operation");
        operationParam.setDescription("会议操作类型，可选值：create（创建会议）、get/query（查询会议）、list（获取会议列表）");
        operationParam.setRequired(true);
        
        Map<String, Object> operationSchema = new HashMap<>();
        operationSchema.put("type", "string");
        operationSchema.put("enum", new String[]{"create", "get", "query", "list"});
        operationParam.setSchema(operationSchema);
        
        getParameters().add(operationParam);

        // 会议预约ID参数
        StructuredParameter reserveIdParam = new StructuredParameter();
        reserveIdParam.setName("reserve_id");
        reserveIdParam.setDescription("会议预约ID，查询会议时必需");
        reserveIdParam.setRequired(false);
        
        Map<String, Object> reserveIdSchema = new HashMap<>();
        reserveIdSchema.put("type", "string");
        reserveIdParam.setSchema(reserveIdSchema);
        
        getParameters().add(reserveIdParam);

        // 会议主题参数
        StructuredParameter topicParam = new StructuredParameter();
        topicParam.setName("topic");
        topicParam.setDescription("会议主题，创建会议时必需");
        topicParam.setRequired(false);
        
        Map<String, Object> topicSchema = new HashMap<>();
        topicSchema.put("type", "string");
        topicParam.setSchema(topicSchema);
        
        getParameters().add(topicParam);

        // 开始时间参数
        StructuredParameter startTimeParam = new StructuredParameter();
        startTimeParam.setName("start_time");
        startTimeParam.setDescription("会议开始时间，创建会议时必需。支持格式：'yyyy-MM-dd HH:mm:ss'、'yyyy-MM-dd HH:mm'、时间戳等");
        startTimeParam.setRequired(false);
        
        Map<String, Object> startTimeSchema = new HashMap<>();
        startTimeSchema.put("type", "string");
        startTimeParam.setSchema(startTimeSchema);
        
        getParameters().add(startTimeParam);

        // 会议时长参数
        StructuredParameter durationParam = new StructuredParameter();
        durationParam.setName("duration");
        durationParam.setDescription("会议时长（分钟），创建会议时可选，默认60分钟");
        durationParam.setRequired(false);
        
        Map<String, Object> durationSchema = new HashMap<>();
        durationSchema.put("type", "integer");
        durationSchema.put("minimum", 1);
        durationSchema.put("maximum", 1440); // 最大24小时
        durationSchema.put("default", 60);
        durationParam.setSchema(durationSchema);
        
        getParameters().add(durationParam);

        // 会议描述参数
        StructuredParameter descriptionParam = new StructuredParameter();
        descriptionParam.setName("description");
        descriptionParam.setDescription("会议描述，创建会议时可选");
        descriptionParam.setRequired(false);
        
        Map<String, Object> descriptionSchema = new HashMap<>();
        descriptionSchema.put("type", "string");
        descriptionParam.setSchema(descriptionSchema);
        
        getParameters().add(descriptionParam);

        // 邀请人员参数
        StructuredParameter inviteesParam = new StructuredParameter();
        inviteesParam.setName("invitees");
        inviteesParam.setDescription("邀请人员列表，创建会议时可选，包含用户ID或邮箱地址");
        inviteesParam.setRequired(false);
        
        Map<String, Object> inviteesSchema = new HashMap<>();
        inviteesSchema.put("type", "array");
        Map<String, Object> itemsSchema = new HashMap<>();
        itemsSchema.put("type", "string");
        inviteesSchema.put("items", itemsSchema);
        inviteesParam.setSchema(inviteesSchema);
        
        getParameters().add(inviteesParam);

        // 是否需要密码参数
        StructuredParameter needPasswordParam = new StructuredParameter();
        needPasswordParam.setName("need_password");
        needPasswordParam.setDescription("是否需要会议密码，创建会议时可选，默认false");
        needPasswordParam.setRequired(false);
        
        Map<String, Object> needPasswordSchema = new HashMap<>();
        needPasswordSchema.put("type", "boolean");
        needPasswordSchema.put("default", false);
        needPasswordParam.setSchema(needPasswordSchema);
        
        getParameters().add(needPasswordParam);

        // 是否自动录制参数
        StructuredParameter autoRecordParam = new StructuredParameter();
        autoRecordParam.setName("auto_record");
        autoRecordParam.setDescription("是否自动录制会议，创建会议时可选，默认false");
        autoRecordParam.setRequired(false);
        
        Map<String, Object> autoRecordSchema = new HashMap<>();
        autoRecordSchema.put("type", "boolean");
        autoRecordSchema.put("default", false);
        autoRecordParam.setSchema(autoRecordSchema);
        
        getParameters().add(autoRecordParam);

        // 页面大小参数（用于列表查询）
        StructuredParameter pageSizeParam = new StructuredParameter();
        pageSizeParam.setName("page_size");
        pageSizeParam.setDescription("每页返回的会议数量，获取会议列表时可选，默认20，最大100");
        pageSizeParam.setRequired(false);
        
        Map<String, Object> pageSizeSchema = new HashMap<>();
        pageSizeSchema.put("type", "integer");
        pageSizeSchema.put("minimum", 1);
        pageSizeSchema.put("maximum", 100);
        pageSizeSchema.put("default", 20);
        pageSizeParam.setSchema(pageSizeSchema);
        
        getParameters().add(pageSizeParam);

        // 页面令牌参数（用于分页）
        StructuredParameter pageTokenParam = new StructuredParameter();
        pageTokenParam.setName("page_token");
        pageTokenParam.setDescription("分页令牌，获取会议列表时可选，用于获取下一页数据");
        pageTokenParam.setRequired(false);
        
        Map<String, Object> pageTokenSchema = new HashMap<>();
        pageTokenSchema.put("type", "string");
        pageTokenParam.setSchema(pageTokenSchema);
        
        getParameters().add(pageTokenParam);
    }
}
