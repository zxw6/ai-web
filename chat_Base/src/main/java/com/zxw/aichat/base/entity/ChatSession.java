package com.zxw.aichat.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(value = "ChatSession", description = "聊天会话")
@TableName("chat_session")
public class ChatSession {

    @ApiModelProperty(value = "会话ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "会话标题")
    private String title;

    @ApiModelProperty(value = "模型编码")
    private String modelCode;

    @ApiModelProperty(value = "系统提示词")
    private String systemPrompt;

    @ApiModelProperty(value = "消息数量")
    private Integer messageCount;

    @ApiModelProperty(value = "最后消息时间")
    private LocalDateTime lastMessageTime;

    @ApiModelProperty(value = "是否置顶：1置顶，0不置顶")
    private Integer pinned;

    @ApiModelProperty(value = "是否归档：1归档，0正常")
    private Integer archived;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "逻辑删除：0正常，1删除")
    @TableLogic
    private Integer deleted;
}
