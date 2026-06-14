package com.zxw.aichat.ai.service;

import com.zxw.aichat.ai.dto.ChatStreamRequest;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ChatStreamService {

    Flux<ServerSentEvent<String>> stream(ChatStreamRequest request);
}
