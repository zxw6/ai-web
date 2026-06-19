package com.zxw.aichat.ai.service.impl;

import com.zxw.aichat.ai.client.OpenAiStreamClient;
import com.zxw.aichat.ai.dto.ChatStreamRequest;
import com.zxw.aichat.ai.service.ChatStreamService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatStreamServiceImpl implements ChatStreamService {

    // 这个 Client 负责真正调用 OpenAI 接口，并返回模型生成的文本片段 Flux<String>。
    private final OpenAiStreamClient openAiStreamClient;

    // 构造方法注入 OpenAiStreamClient，由 Spring 自动创建并传进来。
    public ChatStreamServiceImpl(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    /**
     * 把 OpenAI 返回的纯文本流包装成前端/Apifox 能识别的 SSE 事件流。
     *
     * <p>OpenAiStreamClient 返回的是 Flux<String>，例如：
     * "你"、"好"、"，"、"我是 AI"。
     *
     * <p>这里会把每一段文本包装成：
     * event: message
     * data: 文本片段
     *
     * <p>全部结束后再补一个：
     * event: done
     * data: [DONE]
     *
     * <p>如果中间出错，则返回：
     * event: error
     * data: 错误信息
     */
    @Override
    public Flux<ServerSentEvent<String>> stream(ChatStreamRequest request) {
        return openAiStreamClient.streamText(request)
                // 每收到 OpenAI 的一小段文字，就包装成 event: message 发给前端。
                .map(delta -> ServerSentEvent.builder(delta).event("message").build())
                // OpenAI 正常结束后，额外发一个 done 事件，方便前端知道流已经结束。
                .concatWithValues(ServerSentEvent.builder("[DONE]").event("done").build())
                // 如果调用 OpenAI 或解析流时出错，不让接口直接崩掉，而是返回 event: error。
                .onErrorResume(error -> Flux.just(ServerSentEvent.builder(error.getMessage()).event("error").build()));
    }
}
