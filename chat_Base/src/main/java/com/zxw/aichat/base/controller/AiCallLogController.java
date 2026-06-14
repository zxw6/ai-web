package com.zxw.aichat.base.controller;

import com.zxw.aichat.base.annotation.ORpose;
import com.zxw.aichat.base.entity.AiCallLog;
import com.zxw.aichat.base.service.AiCallLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ORpose
@Api(tags = "AI调用日志接口")
@RestController
@RequestMapping("/base/call-logs")
public class AiCallLogController {

    private final AiCallLogService aiCallLogService;

    public AiCallLogController(AiCallLogService aiCallLogService) {
        this.aiCallLogService = aiCallLogService;
    }

    @ApiOperation("查询调用日志列表")
    @GetMapping
    public List<AiCallLog> list(
            @ApiParam(value = "用户ID")
            @RequestParam(required = false) Long userId,
            @ApiParam(value = "会话ID")
            @RequestParam(required = false) Long sessionId) {
        return aiCallLogService.listCallLogs(userId, sessionId);
    }

    @ApiOperation("查询调用日志详情")
    @GetMapping("/{id}")
    public AiCallLog getById(
            @ApiParam(value = "调用日志ID", required = true)
            @PathVariable Long id) {
        return aiCallLogService.getById(id);
    }

    @ApiOperation("新增调用日志")
    @PostMapping
    public Long create(
            @ApiParam(value = "调用日志信息", required = true)
            @RequestBody AiCallLog callLog) {
        return aiCallLogService.createCallLog(callLog);
    }
}
