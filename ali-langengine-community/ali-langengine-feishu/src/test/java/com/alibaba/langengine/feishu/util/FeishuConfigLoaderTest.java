package com.alibaba.langengine.feishu.util;

import com.alibaba.langengine.feishu.FeishuConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FeishuConfigLoaderTest {

    private String originalAppId;
    private String originalAppSecret;
    private String originalBaseUrl;
    private String originalDebug;

    @BeforeEach
    void setUp() {
        // 保存原始环境变量
        originalAppId = System.getenv("FEISHU_APP_ID");
        originalAppSecret = System.getenv("FEISHU_APP_SECRET");
        originalBaseUrl = System.getenv("FEISHU_BASE_URL");
        originalDebug = System.getenv("FEISHU_DEBUG");
    }

    @AfterEach
    void tearDown() {
        // 清理系统属性
        System.clearProperty("feishu.app.id");
        System.clearProperty("feishu.app.secret");
        System.clearProperty("feishu.base.url");
        System.clearProperty("feishu.connect.timeout");
        System.clearProperty("feishu.read.timeout");
        System.clearProperty("feishu.debug");
        System.clearProperty("feishu.max.retries");
        System.clearProperty("feishu.retry.interval");
    }

    @Test
    void testLoadFromEnvironment() {
        // 由于无法直接设置环境变量，这里主要测试方法不会抛出异常
        FeishuConfiguration config = FeishuConfigLoader.loadFromEnvironment();
        
        assertThat(config).isNotNull();
        // 如果没有设置环境变量，配置可能无效，但不应该抛出异常
    }

    @Test
    void testLoadFromSystemProperties() {
        // 设置系统属性
        System.setProperty("feishu.app.id", "test_app_id");
        System.setProperty("feishu.app.secret", "test_app_secret");
        System.setProperty("feishu.base.url", "https://test.feishu.cn");
        System.setProperty("feishu.connect.timeout", "15000");
        System.setProperty("feishu.read.timeout", "25000");
        System.setProperty("feishu.debug", "true");
        System.setProperty("feishu.max.retries", "5");
        System.setProperty("feishu.retry.interval", "2000");
        
        FeishuConfiguration config = FeishuConfigLoader.loadFromSystemProperties();
        
        assertThat(config).isNotNull();
        assertThat(config.getAppId()).isEqualTo("test_app_id");
        assertThat(config.getAppSecret()).isEqualTo("test_app_secret");
        assertThat(config.getBaseUrl()).isEqualTo("https://test.feishu.cn");
        assertThat(config.getConnectTimeout()).isEqualTo(15000);
        assertThat(config.getReadTimeout()).isEqualTo(25000);
        assertThat(config.isDebug()).isTrue();
        assertThat(config.getMaxRetries()).isEqualTo(5);
        assertThat(config.getRetryInterval()).isEqualTo(2000);
    }

    @Test
    void testLoadFromSystemPropertiesWithInvalidValues() {
        // 设置无效的系统属性
        System.setProperty("feishu.app.id", "test_app_id");
        System.setProperty("feishu.app.secret", "test_app_secret");
        System.setProperty("feishu.connect.timeout", "invalid_number");
        System.setProperty("feishu.read.timeout", "another_invalid");
        System.setProperty("feishu.max.retries", "not_a_number");
        System.setProperty("feishu.retry.interval", "also_invalid");
        
        FeishuConfiguration config = FeishuConfigLoader.loadFromSystemProperties();
        
        assertThat(config).isNotNull();
        assertThat(config.getAppId()).isEqualTo("test_app_id");
        assertThat(config.getAppSecret()).isEqualTo("test_app_secret");
        // 无效的数值应该使用默认值
        assertThat(config.getConnectTimeout()).isEqualTo(30000); // 默认值
        assertThat(config.getReadTimeout()).isEqualTo(30000); // 默认值
        assertThat(config.getMaxRetries()).isEqualTo(3); // 默认值
        assertThat(config.getRetryInterval()).isEqualTo(1000); // 默认值
    }

    @Test
    void testLoadFromNonExistentFile() {
        // 测试加载不存在的配置文件
        FeishuConfiguration config = FeishuConfigLoader.loadFromFile("non-existent-config.properties");
        
        assertThat(config).isNotNull();
        // 应该回退到环境变量加载
    }

    @Test
    void testLoadConfiguration() {
        // 测试综合配置加载
        FeishuConfiguration config = FeishuConfigLoader.loadConfiguration();
        
        assertThat(config).isNotNull();
        // 配置应该包含默认值
        assertThat(config.getBaseUrl()).isNotNull();
        assertThat(config.getConnectTimeout()).isGreaterThan(0);
        assertThat(config.getReadTimeout()).isGreaterThan(0);
    }

    @Test
    void testValidateConfigurationWithValidConfig() {
        // 测试验证有效配置
        FeishuConfiguration config = new FeishuConfiguration("valid_app_id", "valid_app_secret");
        
        boolean isValid = FeishuConfigLoader.validateConfiguration(config);
        
        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateConfigurationWithInvalidConfig() {
        // 测试验证无效配置
        FeishuConfiguration config = new FeishuConfiguration();
        config.setAppId("");
        config.setAppSecret("");
        
        boolean isValid = FeishuConfigLoader.validateConfiguration(config);
        
        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateConfigurationWithNullConfig() {
        // 测试验证空配置
        boolean isValid = FeishuConfigLoader.validateConfiguration(null);
        
        assertThat(isValid).isFalse();
    }

    @Test
    void testLoadAndValidateConfigurationWithValidConfig() {
        // 设置有效的系统属性
        System.setProperty("feishu.app.id", "valid_app_id");
        System.setProperty("feishu.app.secret", "valid_app_secret");
        
        FeishuConfiguration config = FeishuConfigLoader.loadAndValidateConfiguration();
        
        assertThat(config).isNotNull();
        assertThat(config.isValid()).isTrue();
    }

    @Test
    void testLoadAndValidateConfigurationWithInvalidConfig() {
        // 清除所有可能的配置源
        System.clearProperty("feishu.app.id");
        System.clearProperty("feishu.app.secret");
        
        // 应该抛出异常，因为配置无效
        assertThatThrownBy(() -> FeishuConfigLoader.loadAndValidateConfiguration())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Failed to load valid Feishu configuration");
    }

    @Test
    void testPrintConfiguration() {
        // 测试打印配置（主要测试不会抛出异常）
        FeishuConfiguration config = new FeishuConfiguration("test_app_id_12345678", "test_app_secret_87654321");
        
        assertThatCode(() -> FeishuConfigLoader.printConfiguration(config))
                .doesNotThrowAnyException();
    }

    @Test
    void testPrintNullConfiguration() {
        // 测试打印空配置
        assertThatCode(() -> FeishuConfigLoader.printConfiguration(null))
                .doesNotThrowAnyException();
    }

    @Test
    void testConfigurationPriority() {
        // 测试配置优先级：系统属性 > 环境变量 > 配置文件
        
        // 设置系统属性（最高优先级）
        System.setProperty("feishu.app.id", "system_app_id");
        System.setProperty("feishu.app.secret", "system_app_secret");
        System.setProperty("feishu.debug", "true");
        
        FeishuConfiguration config = FeishuConfigLoader.loadConfiguration();
        
        assertThat(config).isNotNull();
        assertThat(config.getAppId()).isEqualTo("system_app_id");
        assertThat(config.getAppSecret()).isEqualTo("system_app_secret");
        assertThat(config.isDebug()).isTrue();
    }
}
