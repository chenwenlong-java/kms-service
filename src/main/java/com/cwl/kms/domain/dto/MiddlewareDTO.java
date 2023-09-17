package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/2 20:03
 */
@Data
public class MiddlewareDTO {
    private Long id;
    private String middlewareKey;
    private String middlewareValue;
    private String serviceAccount;
    private Integer middlewareType;
}
