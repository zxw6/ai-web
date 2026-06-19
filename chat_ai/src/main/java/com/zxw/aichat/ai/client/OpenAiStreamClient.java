package com.zxw.aichat.ai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxw.aichat.ai.config.OpenAiProperties;
import com.zxw.aichat.ai.dto.ChatStreamRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class OpenAiStreamClient {

    /*
     * OpenAI Responses API 在开启 stream=true 后，会用 SSE 返回很多事件。
     * 这里只关心三类事件：
     * 1. response.output_text.delta：模型新生成的一小段文本
     * 2. response.completed：整次回答结束
     * 3. error：模型接口返回错误
     */
    private static final String OUTPUT_TEXT_DELTA = "response.output_text.delta";
    private static final String COMPLETED = "response.completed";
    private static final String ERROR = "error";

    // 已在 WebClientConfig 中配置好 baseUrl 和 Authorization，这里直接调用 OpenAI 接口。
    private final WebClient openAiWebClient;

    // 读取 application-dev.yml 里的 openai.model、base-url、api-key 等配置。
    private final OpenAiProperties properties;

    // 用来解析 OpenAI SSE 事件里的 JSON 字符串。
    private final ObjectMapper objectMapper;

    public OpenAiStreamClient(WebClient openAiWebClient, OpenAiProperties properties, ObjectMapper objectMapper) {
        this.openAiWebClient = openAiWebClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * 调用 OpenAI 流式接口，并把 OpenAI 返回的 SSE 事件转换成纯文本流。
     *
     * <p>方法返回 Flux<String>，意思是“未来会陆续产生很多段 String”。
     * Controller 会把这些 String 再包装成浏览器能接收的 SSE 响应。</p>
     */
    public Flux<String> streamText(ChatStreamRequest request) {
        return openAiWebClient.post()
                // OpenAI Responses API 的路径，最终请求地址是：openai.base-url + /responses。
                .uri("/responses")
                // 告诉 OpenAI：我希望你用 text/event-stream 的形式流式返回。
                .accept(MediaType.TEXT_EVENT_STREAM)
                // 把前端传来的 ChatStreamRequest 转成 OpenAI 需要的 JSON 请求体。
                .bodyValue(buildRequestBody(request))
                .retrieve()
                // OpenAI 返回的是 SSE，所以这里按 ServerSentEvent<String> 一条条读取。
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })
                .handle((event, sink) -> {
                    String eventName = event.event();
                    String data = event.data();

                    // 没有内容，或者回答完成了，就不再往下游发送文本。
                    if (!StringUtils.hasText(data) || COMPLETED.equals(eventName)) {
                        return;
                    }

                    // OpenAI 返回 error 事件时，把错误传给下游，最终接口会结束并报错。
                    if (ERROR.equals(eventName)) {
                        sink.error(new IllegalStateException(data));
                        return;
                    }

                    // delta 事件才是真正的“模型刚生成的一小段文字”。
                    if (OUTPUT_TEXT_DELTA.equals(eventName)) {
                        String delta = readDelta(data);
                        if (StringUtils.hasText(delta)) {
                            // 把这一小段文字推给 Service/Controller，前端就能实时看到输出。
                            sink.next(delta);
                        }
                    }
                });
    }

    /**
     * 构造 OpenAI Responses API 的请求体。
     *
     * <p>最终大概长这样：
     * {
     *   "model": "gpt-4o-mini",
     *   "input": [{"role": "user", "content": "你好"}],
     *   "stream": true
     * }</p>
     */
    private Map<String, Object> buildRequestBody(ChatStreamRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        // 前端传了 model 就用前端的；没传就用配置文件里的默认模型。
        body.put("model", StringUtils.hasText(request.getModel()) ? request.getModel() : properties.getModel());

        // input 是消息列表，里面可以包含 system 提示词和 user 问题。
        body.put("input", buildInput(request));

        // 开启流式输出，否则 OpenAI 会等完整回答生成完才一次性返回。
        body.put("stream", true);

        // 如果要接着上一轮 response 继续上下文，就传 previous_response_id。
        if (StringUtils.hasText(request.getPreviousResponseId())) {
            body.put("previous_response_id", request.getPreviousResponseId());
        }

        // temperature 控制回答随机性：越低越稳定，越高越发散。
        if (request.getTemperature() != null) {
            body.put("temperature", request.getTemperature());
        }

        // 限制模型最多输出多少 token，防止回答过长。
        if (request.getMaxOutputTokens() != null) {
            body.put("max_output_tokens", request.getMaxOutputTokens());
        }
        return body;
    }

    /**
     * 把系统提示词和用户输入拼成 OpenAI 的 input 消息数组。
     */
    private List<Map<String, String>> buildInput(ChatStreamRequest request) {
        List<Map<String, String>> input = new ArrayList<>();

        // systemPrompt 是给模型看的“角色设定/回答规则”，用户一般看不到。
        if (StringUtils.hasText(request.getSystemPrompt())) {
            input.add(message("system", request.getSystemPrompt()));
        }

        // 用户本次真正发送的问题。
        input.add(message("user", request.getMessage()));
        return input;
    }

    /**
     * 创建一条 OpenAI 消息。
     *
     * @param role 角色，例如 system、user
     * @param content 消息内容
     */
    private Map<String, String> message(String role, String content) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    /**
     * 从 OpenAI 的 delta 事件 JSON 中取出真正新增的文本。
     *
     * <p>OpenAI 事件 data 大概长这样：
     * {"type":"response.output_text.delta","delta":"你好"}
     * 这里最终只返回 "你好"。</p>
     */
    private String readDelta(String data) {
        try {
            JsonNode root = objectMapper.readTree(data);
            JsonNode delta = root.get("delta");
            return delta == null || delta.isNull() ? null : delta.asText();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse OpenAI stream event: " + data, e);
        }
    }
}
