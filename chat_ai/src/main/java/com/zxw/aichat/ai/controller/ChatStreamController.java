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
// 这个 Controller 的所有接口都以 /ai/chat 开头。
// 再加上 application.yml 里的 context-path: /api，
// 所以完整访问路径是：http://localhost:8082/api/ai/chat/stream
@RequestMapping("/ai/chat")
public class ChatStreamController {

    // Controller 不直接调用 OpenAI，把业务处理交给 Service。
    private final ChatStreamService chatStreamService;

    // 构造方法注入 ChatStreamService，Spring 会自动把实现类 ChatStreamServiceImpl 注入进来。
    public ChatStreamController(ChatStreamService chatStreamService) {
        this.chatStreamService = chatStreamService;
    }

    @ApiOperation("流式发送消息")
    // produces = text/event-stream 表示这个接口返回的是 SSE 流，不是普通 JSON。
    // Apifox 里看响应时要用 Raw/流式视图，不要按普通 JSON 去解析。
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@ApiParam(value = "AI流式对话请求", required = true)
            // @Valid 会触发 ChatStreamRequest 里的参数校验，例如 message 不能为空。
            // @RequestBody 表示请求参数来自 JSON Body。
            @Valid @RequestBody ChatStreamRequest request) {
        // 把请求交给 Service，Service 会返回一条一条的 SSE 事件。
        return chatStreamService.stream(request);
    }
}
