package com.zxw.aichat.ai.service.impl;

import com.zxw.aichat.ai.client.OpenAiStreamClient;
import com.zxw.aichat.ai.dto.ChatStreamRequest;
import com.zxw.aichat.ai.service.ChatStreamService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatStreamServiceImpl implements ChatStreamService {

    private final OpenAiStreamClient openAiStreamClient;

    public ChatStreamServiceImpl(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    @Override
    public Flux<ServerSentEvent<String>> stream(ChatStreamRequest request) {
        return openAiStreamClient.streamText(request)
                .map(delta -> ServerSentEvent.builder(delta).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").event("done").build())
                .onErrorResume(error -> Flux.just(ServerSentEvent.builder(error.getMessage()).event("error").build()));
    }
}
