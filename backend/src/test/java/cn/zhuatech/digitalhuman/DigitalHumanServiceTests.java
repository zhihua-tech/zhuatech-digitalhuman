/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.digitalhuman;

import cn.zhuatech.digitalhuman.service.DigitalHumanService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class DigitalHumanServiceTests {
    private final DigitalHumanService service = new DigitalHumanService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void segmentsAuthorizedPresentationScript() {
        var result = service.plan(new DigitalHumanService.Request(
            "欢迎了解知华科技企业 AI 转型服务。我们从业务诊断开始，提供方案设计、实施与持续运营。现在可以预约一次需求沟通。",
            "avatar-enterprise-01", "voice-neutral-01", "16:9", true, true, true));
        assertThat(result.status()).isEqualTo("READY");
        assertThat(result.segments()).hasSize(3);
        assertThat(result.estimatedSeconds()).isPositive();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksMissingPortraitAuthorization() {
        var result = service.plan(new DigitalHumanService.Request(
            "这是一段用于测试企业数字人播报流程与合规门禁的完整演示口播稿。",
            "avatar-01", "voice-01", "9:16", false, true, true));
        assertThat(result.status()).isEqualTo("BLOCKED");
    }
}
