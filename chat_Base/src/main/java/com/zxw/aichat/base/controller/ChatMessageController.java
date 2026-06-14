package com.zxw.aichat.base.controller;

import com.zxw.aichat.base.annotation.ORpose;
import com.zxw.aichat.base.entity.ChatMessage;
import com.zxw.aichat.base.service.ChatMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ORpose
@Api(tags = "聊天消息接口")
@RestController
@RequestMapping("/base/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @ApiOperation("查询消息列表")
    @GetMapping
    public List<ChatMessage> list(
            @ApiParam(value = "会话ID，不传则查询全部")
            @RequestParam(required = false) Long sessionId) {
        return chatMessageService.listMessages(sessionId);
    }

    @ApiOperation("查询消息详情")
    @GetMapping("/{id}")
    public ChatMessage getById(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        return chatMessageService.getById(id);
    }

    @ApiOperation("新增消息")
    @PostMapping
    public Long create(
            @ApiParam(value = "消息信息", required = true)
            @RequestBody ChatMessage message) {
        return chatMessageService.createMessage(message);
    }

    @ApiOperation("更新消息")
    @PutMapping("/{id}")
    public Boolean update(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "消息信息", required = true)
            @RequestBody ChatMessage message) {
        return chatMessageService.updateMessage(id, message);
    }

    @ApiOperation("删除消息")
    @DeleteMapping("/{id}")
    public Boolean delete(
            @ApiParam(value = "消息ID", required = true)
            @PathVariable Long id) {
        return chatMessageService.deleteMessage(id);
    }
}
