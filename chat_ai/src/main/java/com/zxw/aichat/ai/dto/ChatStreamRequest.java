package com.zxw.aichat.ai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@ApiModel(value = "ChatStreamRequest", description = "AI流式对话请求")
public class ChatStreamRequest {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "会话ID")
    private Long sessionId;

    @ApiModelProperty(value = "用户输入内容", required = true)
    @NotBlank(message = "消息内容不能为空")
    private String message;

    @ApiModelProperty(value = "模型编码，不传则使用配置默认模型")
    private String model;

    @ApiModelProperty(value = "系统提示词")
    private String systemPrompt;

    @ApiModelProperty(value = "上一轮OpenAI响应ID，用于续接上下文")
    private String previousResponseId;

    @ApiModelProperty(value = "温度参数")
    private Double temperature;

    @ApiModelProperty(value = "最大输出Token数")
    private Integer maxOutputTokens;
}
