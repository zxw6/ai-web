package com.zxw.aichat.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ApiResponse", description = "统一接口响应")
public class ApiResponse<T> {

    @ApiModelProperty(value = "业务状态码：0成功，其他失败")
    private String code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("0", "成功", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("0", "成功", null);
    }

    public static ApiResponse<Void> fail(String msg) {
        return new ApiResponse<>("500", msg, null);
    }
}
