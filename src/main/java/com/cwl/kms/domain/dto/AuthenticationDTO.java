package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/3 11:22
 */
@Data
public class AuthenticationDTO {
    private Long id;
    private String appId;
    private Integer tokenTTL;
}
