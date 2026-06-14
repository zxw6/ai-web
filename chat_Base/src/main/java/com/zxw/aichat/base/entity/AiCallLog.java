package com.zxw.aichat.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(value = "AiCallLog", description = "AI调用日志")
@TableName("ai_call_log")
public class AiCallLog {

    @ApiModelProperty(value = "调用日志ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "会话ID")
    private Long sessionId;

    @ApiModelProperty(value = "用户请求消息ID")
    private Long requestMessageId;

    @ApiModelProperty(value = "AI回复消息ID")
    private Long responseMessageId;

    @ApiModelProperty(value = "服务商编码，例如 openai")
    private String provider;

    @ApiModelProperty(value = "模型编码")
    private String modelCode;

    @ApiModelProperty(value = "模型服务返回的响应ID")
    private String responseId;

    @ApiModelProperty(value = "输入Token数")
    private Integer promptTokens;

    @ApiModelProperty(value = "输出Token数")
    private Integer completionTokens;

    @ApiModelProperty(value = "总Token数")
    private Integer totalTokens;

    @ApiModelProperty(value = "请求耗时，单位毫秒")
    private Long latencyMs;

    @ApiModelProperty(value = "是否流式响应：1是，0否")
    @TableField("stream")
    private Integer stream;

    @ApiModelProperty(value = "调用状态：1成功，0失败")
    private Integer status;

    @ApiModelProperty(value = "错误编码")
    private String errorCode;

    @ApiModelProperty(value = "错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
