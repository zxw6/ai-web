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

    private static final String OUTPUT_TEXT_DELTA = "response.output_text.delta";
    private static final String COMPLETED = "response.completed";
    private static final String ERROR = "error";

    private final WebClient openAiWebClient;
    private final OpenAiProperties properties;
    private final ObjectMapper objectMapper;

    public OpenAiStreamClient(WebClient openAiWebClient, OpenAiProperties properties, ObjectMapper objectMapper) {
        this.openAiWebClient = openAiWebClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public Flux<String> streamText(ChatStreamRequest request) {
        return openAiWebClient.post()
                .uri("/responses")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(buildRequestBody(request))
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })
                .handle((event, sink) -> {
                    String eventName = event.event();
                    String data = event.data();
                    if (!StringUtils.hasText(data) || COMPLETED.equals(eventName)) {
                        return;
                    }
                    if (ERROR.equals(eventName)) {
                        sink.error(new IllegalStateException(data));
                        return;
                    }
                    if (OUTPUT_TEXT_DELTA.equals(eventName)) {
                        String delta = readDelta(data);
                        if (StringUtils.hasText(delta)) {
                            sink.next(delta);
                        }
                    }
                });
    }

    private Map<String, Object> buildRequestBody(ChatStreamRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", StringUtils.hasText(request.getModel()) ? request.getModel() : properties.getModel());
        body.put("input", buildInput(request));
        body.put("stream", true);

        if (StringUtils.hasText(request.getPreviousResponseId())) {
            body.put("previous_response_id", request.getPreviousResponseId());
        }
        if (request.getTemperature() != null) {
            body.put("temperature", request.getTemperature());
        }
        if (request.getMaxOutputTokens() != null) {
            body.put("max_output_tokens", request.getMaxOutputTokens());
        }
        return body;
    }

    private List<Map<String, String>> buildInput(ChatStreamRequest request) {
        List<Map<String, String>> input = new ArrayList<>();
        if (StringUtils.hasText(request.getSystemPrompt())) {
            input.add(message("system", request.getSystemPrompt()));
        }
        input.add(message("user", request.getMessage()));
        return input;
    }

    private Map<String, String> message(String role, String content) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

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
