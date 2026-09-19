/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.digitalhuman.service;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class DigitalHumanService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result plan(Request request) {
        String normalized = request.script().trim().replaceAll("\\s+", " ");
        String[] sentences = normalized.split("(?<=[。！？!?；;])");
        List<Segment> segments = new ArrayList<>();
        int sequence = 1;
        int totalCharacters = 0;
        for (String sentence : sentences) {
            if (sentence.isBlank()) continue;
            totalCharacters += sentence.length();
            segments.add(new Segment(sequence++, sentence.trim(), estimateSeconds(sentence),
                sequence % 3 == 0 ? "中景 + 要点字幕" : "半身正面 + 品牌背景"));
        }
        if (segments.isEmpty()) {
            segments.add(new Segment(1, normalized, estimateSeconds(normalized), "半身正面 + 品牌背景"));
            totalCharacters = normalized.length();
        }

        double totalSeconds = segments.stream().mapToDouble(Segment::seconds).sum();
        List<String> checks = new ArrayList<>();
        checks.add(request.avatarAuthorized() ? "数字人形象与肖像授权已确认" : "未确认形象或肖像授权");
        checks.add(request.voiceAuthorized() ? "播报音色授权已确认" : "未确认播报音色授权");
        checks.add(totalCharacters >= 30 ? "口播稿信息量满足演示要求" : "建议补充口播稿内容");
        checks.add(request.disclosureEnabled() ? "已开启 AI 生成内容标识" : "建议开启 AI 生成内容标识");

        boolean authorized = request.avatarAuthorized() && request.voiceAuthorized();
        String status = !authorized ? "BLOCKED" : totalCharacters < 30 ? "NEEDS_SCRIPT" : "READY";
        return new Result(status, segments.size(), Math.round(totalSeconds * 10.0) / 10.0,
            List.copyOf(segments), List.copyOf(checks),
            Map.of("script", normalized, "avatarId", request.avatarId(), "voiceId", request.voiceId(),
                "aspectRatio", request.aspectRatio(), "disclosureEnabled", request.disclosureEnabled()),
            "LOCAL_PRESENTATION_PLANNER");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private double estimateSeconds(String text) {
        return Math.max(2.0, Math.round(text.length() / 3.8 * 10.0) / 10.0);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank @Size(max = 5000) String script,
                          @NotBlank String avatarId,
                          @NotBlank String voiceId,
                          @NotBlank String aspectRatio,
                          boolean avatarAuthorized,
                          boolean voiceAuthorized,
                          boolean disclosureEnabled) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Segment(int sequence, String text, double seconds, String visual) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String status, int segmentCount, double estimatedSeconds,
                         List<Segment> segments, List<String> checks,
                         Map<String, Object> providerPayload, String executionMode) {}
}
