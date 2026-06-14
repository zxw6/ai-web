package com.zxw.aichat.ai.controller;

import com.zxw.aichat.ai.dto.ChatStreamRequest;
import com.zxw.aichat.ai.service.ChatStreamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Api(tags = "AI流式对话接口")
@RestController
@RequestMapping("/ai/chat")
public class ChatStreamController {

    private final ChatStreamService chatStreamService;

    public ChatStreamController(ChatStreamService chatStreamService) {
        this.chatStreamService = chatStreamService;
    }

    @ApiOperation("流式发送消息")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(
            @ApiParam(value = "AI流式对话请求", required = true)
            @Valid @RequestBody ChatStreamRequest request) {
        return chatStreamService.stream(request);
    }
}
