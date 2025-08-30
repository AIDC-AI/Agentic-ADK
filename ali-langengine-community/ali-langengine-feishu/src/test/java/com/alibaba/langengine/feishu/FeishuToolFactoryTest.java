package com.alibaba.langengine.feishu;

import com.alibaba.langengine.core.tool.BaseTool;
import com.alibaba.langengine.feishu.tools.FeishuContactTool;
import com.alibaba.langengine.feishu.tools.FeishuDocumentTool;
import com.alibaba.langengine.feishu.tools.FeishuMeetingTool;
import com.alibaba.langengine.feishu.tools.FeishuMessageTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FeishuToolFactoryTest {

    private FeishuConfiguration configuration;
    private FeishuToolFactory factory;

    @BeforeEach
    void setUp() {
        configuration = new FeishuConfiguration("test_app_id", "test_app_secret");
        factory = new FeishuToolFactory(configuration);
    }

    @Test
    void testConstructorWithNullConfiguration() {
        // 测试空配置构造函数
        assertThatThrownBy(() -> new FeishuToolFactory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FeishuConfiguration cannot be null");
    }

    @Test
    void testConstructorWithInvalidConfiguration() {
        // 测试无效配置构造函数
        FeishuConfiguration invalidConfig = new FeishuConfiguration();
        invalidConfig.setAppId("");
        invalidConfig.setAppSecret("");
        
        assertThatThrownBy(() -> new FeishuToolFactory(invalidConfig))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid FeishuConfiguration");
    }

    @Test
    void testConstructorWithValidConfiguration() {
        // 测试有效配置构造函数
        assertThat(factory).isNotNull();
        assertThat(factory.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testCreateMessageTool() {
        // 测试创建消息工具
        FeishuMessageTool messageTool = factory.createMessageTool();
        
        assertThat(messageTool).isNotNull();
        assertThat(messageTool.getName()).isEqualTo("feishu_send_message");
        assertThat(messageTool.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testCreateDocumentTool() {
        // 测试创建文档工具
        FeishuDocumentTool documentTool = factory.createDocumentTool();
        
        assertThat(documentTool).isNotNull();
        assertThat(documentTool.getName()).isEqualTo("feishu_document_operation");
        assertThat(documentTool.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testCreateMeetingTool() {
        // 测试创建会议工具
        FeishuMeetingTool meetingTool = factory.createMeetingTool();
        
        assertThat(meetingTool).isNotNull();
        assertThat(meetingTool.getName()).isEqualTo("feishu_meeting_operation");
        assertThat(meetingTool.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testCreateContactTool() {
        // 测试创建通讯录工具
        FeishuContactTool contactTool = factory.createContactTool();
        
        assertThat(contactTool).isNotNull();
        assertThat(contactTool.getName()).isEqualTo("feishu_contact_query");
        assertThat(contactTool.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testCreateAllTools() {
        // 测试创建所有工具
        List<BaseTool> tools = factory.createAllTools();
        
        assertThat(tools).isNotNull();
        assertThat(tools).hasSize(4);
        
        // 验证工具类型
        assertThat(tools.get(0)).isInstanceOf(FeishuMessageTool.class);
        assertThat(tools.get(1)).isInstanceOf(FeishuDocumentTool.class);
        assertThat(tools.get(2)).isInstanceOf(FeishuMeetingTool.class);
        assertThat(tools.get(3)).isInstanceOf(FeishuContactTool.class);
        
        // 验证所有工具都使用相同配置
        for (BaseTool tool : tools) {
            if (tool instanceof FeishuMessageTool) {
                assertThat(((FeishuMessageTool) tool).getConfiguration()).isEqualTo(configuration);
            } else if (tool instanceof FeishuDocumentTool) {
                assertThat(((FeishuDocumentTool) tool).getConfiguration()).isEqualTo(configuration);
            } else if (tool instanceof FeishuMeetingTool) {
                assertThat(((FeishuMeetingTool) tool).getConfiguration()).isEqualTo(configuration);
            } else if (tool instanceof FeishuContactTool) {
                assertThat(((FeishuContactTool) tool).getConfiguration()).isEqualTo(configuration);
            }
        }
    }

    @Test
    void testCreateToolByType() {
        // 测试根据类型创建工具
        BaseTool messageTool = factory.createTool(FeishuToolFactory.FeishuToolType.MESSAGE);
        BaseTool documentTool = factory.createTool(FeishuToolFactory.FeishuToolType.DOCUMENT);
        BaseTool meetingTool = factory.createTool(FeishuToolFactory.FeishuToolType.MEETING);
        BaseTool contactTool = factory.createTool(FeishuToolFactory.FeishuToolType.CONTACT);
        
        assertThat(messageTool).isInstanceOf(FeishuMessageTool.class);
        assertThat(documentTool).isInstanceOf(FeishuDocumentTool.class);
        assertThat(meetingTool).isInstanceOf(FeishuMeetingTool.class);
        assertThat(contactTool).isInstanceOf(FeishuContactTool.class);
    }

    @Test
    void testCreateToolWithNullType() {
        // 测试使用空类型创建工具
        assertThatThrownBy(() -> factory.createTool(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ToolType cannot be null");
    }

    @Test
    void testCreateDefault() {
        // 测试创建默认工厂
        FeishuToolFactory defaultFactory = FeishuToolFactory.createDefault();
        
        assertThat(defaultFactory).isNotNull();
        assertThat(defaultFactory.getConfiguration()).isNotNull();
    }

    @Test
    void testCreateWithAppIdAndSecret() {
        // 测试使用应用ID和密钥创建工厂
        FeishuToolFactory customFactory = FeishuToolFactory.create("custom_app_id", "custom_app_secret");
        
        assertThat(customFactory).isNotNull();
        assertThat(customFactory.getConfiguration().getAppId()).isEqualTo("custom_app_id");
        assertThat(customFactory.getConfiguration().getAppSecret()).isEqualTo("custom_app_secret");
    }

    @Test
    void testCreateWithFullParameters() {
        // 测试使用完整参数创建工厂
        FeishuToolFactory customFactory = FeishuToolFactory.create("custom_app_id", "custom_app_secret", "https://custom.feishu.cn");
        
        assertThat(customFactory).isNotNull();
        assertThat(customFactory.getConfiguration().getAppId()).isEqualTo("custom_app_id");
        assertThat(customFactory.getConfiguration().getAppSecret()).isEqualTo("custom_app_secret");
        assertThat(customFactory.getConfiguration().getBaseUrl()).isEqualTo("https://custom.feishu.cn");
    }

    @Test
    void testToolTypeEnum() {
        // 测试工具类型枚举
        FeishuToolFactory.FeishuToolType[] types = FeishuToolFactory.FeishuToolType.values();
        
        assertThat(types).hasSize(4);
        assertThat(types).contains(
                FeishuToolFactory.FeishuToolType.MESSAGE,
                FeishuToolFactory.FeishuToolType.DOCUMENT,
                FeishuToolFactory.FeishuToolType.MEETING,
                FeishuToolFactory.FeishuToolType.CONTACT
        );
    }

    @Test
    void testToolTypeFromCode() {
        // 测试根据代码获取工具类型
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("message")).isEqualTo(FeishuToolFactory.FeishuToolType.MESSAGE);
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("document")).isEqualTo(FeishuToolFactory.FeishuToolType.DOCUMENT);
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("meeting")).isEqualTo(FeishuToolFactory.FeishuToolType.MEETING);
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("contact")).isEqualTo(FeishuToolFactory.FeishuToolType.CONTACT);
        
        // 测试大小写不敏感
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("MESSAGE")).isEqualTo(FeishuToolFactory.FeishuToolType.MESSAGE);
        assertThat(FeishuToolFactory.FeishuToolType.fromCode("Message")).isEqualTo(FeishuToolFactory.FeishuToolType.MESSAGE);
    }

    @Test
    void testToolTypeFromCodeWithInvalidCode() {
        // 测试使用无效代码获取工具类型
        assertThatThrownBy(() -> FeishuToolFactory.FeishuToolType.fromCode("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported tool type code: invalid");
        
        assertThatThrownBy(() -> FeishuToolFactory.FeishuToolType.fromCode(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tool type code cannot be null or empty");
        
        assertThatThrownBy(() -> FeishuToolFactory.FeishuToolType.fromCode(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tool type code cannot be null or empty");
    }

    @Test
    void testToolTypeProperties() {
        // 测试工具类型属性
        FeishuToolFactory.FeishuToolType messageType = FeishuToolFactory.FeishuToolType.MESSAGE;
        
        assertThat(messageType.getCode()).isEqualTo("message");
        assertThat(messageType.getDescription()).isEqualTo("飞书消息工具");
        assertThat(messageType.toString()).contains("message");
        assertThat(messageType.toString()).contains("飞书消息工具");
    }

    @Test
    void testFactoryToString() {
        // 测试工厂toString方法
        String result = factory.toString();
        
        assertThat(result).contains("FeishuToolFactory{");
        assertThat(result).contains("configuration=");
    }

    @Test
    void testMultipleToolCreation() {
        // 测试多次创建工具
        FeishuMessageTool tool1 = factory.createMessageTool();
        FeishuMessageTool tool2 = factory.createMessageTool();
        
        // 应该创建不同的实例
        assertThat(tool1).isNotSameAs(tool2);
        
        // 但使用相同的配置
        assertThat(tool1.getConfiguration()).isEqualTo(tool2.getConfiguration());
    }

    @Test
    void testFactoryWithDifferentConfigurations() {
        // 测试不同配置的工厂
        FeishuConfiguration config1 = new FeishuConfiguration("app1", "secret1");
        FeishuConfiguration config2 = new FeishuConfiguration("app2", "secret2");
        
        FeishuToolFactory factory1 = new FeishuToolFactory(config1);
        FeishuToolFactory factory2 = new FeishuToolFactory(config2);
        
        FeishuMessageTool tool1 = factory1.createMessageTool();
        FeishuMessageTool tool2 = factory2.createMessageTool();
        
        assertThat(tool1.getConfiguration()).isEqualTo(config1);
        assertThat(tool2.getConfiguration()).isEqualTo(config2);
        assertThat(tool1.getConfiguration()).isNotEqualTo(tool2.getConfiguration());
    }
}
