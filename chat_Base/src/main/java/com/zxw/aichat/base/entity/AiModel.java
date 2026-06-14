package com.zxw.aichat.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(value = "AiModel", description = "AI模型配置")
@TableName("ai_model")
public class AiModel {

    @ApiModelProperty(value = "模型配置ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务商编码，例如 openai")
    private String provider;

    @ApiModelProperty(value = "模型编码，例如 gpt-4o-mini")
    private String modelCode;

    @ApiModelProperty(value = "模型显示名称")
    private String modelName;

    @ApiModelProperty(value = "是否启用：1启用，0禁用")
    private Integer enabled;

    @ApiModelProperty(value = "是否默认模型：1默认，0非默认")
    private Integer defaultFlag;

    @ApiModelProperty(value = "排序值")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "最大输出Token数")
    private Integer maxOutputTokens;

    @ApiModelProperty(value = "温度参数")
    private BigDecimal temperature;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
