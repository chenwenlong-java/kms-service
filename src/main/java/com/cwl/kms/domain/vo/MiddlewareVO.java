package com.cwl.kms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/9/15 15:16
 */
@Data
@Schema(description = "微服务初始化对象")
public class MiddlewareVO {
    @Schema(description = "中间件key")
    private String middlewareKey;

    @Schema(description = "中间件value")
    private String middlewareValue;
}
