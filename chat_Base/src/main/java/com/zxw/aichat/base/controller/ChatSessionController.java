package com.zxw.aichat.base.controller;

import com.zxw.aichat.base.annotation.ORpose;
import com.zxw.aichat.base.entity.ChatSession;
import com.zxw.aichat.base.service.ChatSessionService;
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
@Api(tags = "聊天会话接口")
@RestController
@RequestMapping("/base/sessions")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @ApiOperation("查询会话列表")
    @GetMapping
    public List<ChatSession> list(
            @ApiParam(value = "用户ID，不传则查询全部")
            @RequestParam(required = false) Long userId) {
        return chatSessionService.listSessions(userId);
    }

    @ApiOperation("查询会话详情")
    @GetMapping("/{id}")
    public ChatSession getById(
            @ApiParam(value = "会话ID", required = true)
            @PathVariable Long id) {
        return chatSessionService.getById(id);
    }

    @ApiOperation("新增会话")
    @PostMapping
    public Long create(
            @ApiParam(value = "会话信息", required = true)
            @RequestBody ChatSession session) {
        return chatSessionService.createSession(session);
    }

    @ApiOperation("更新会话")
    @PutMapping("/{id}")
    public Boolean update(
            @ApiParam(value = "会话ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "会话信息", required = true)
            @RequestBody ChatSession session) {
        return chatSessionService.updateSession(id, session);
    }

    @ApiOperation("删除会话")
    @DeleteMapping("/{id}")
    public Boolean delete(
            @ApiParam(value = "会话ID", required = true)
            @PathVariable Long id) {
        return chatSessionService.deleteSession(id);
    }
}
