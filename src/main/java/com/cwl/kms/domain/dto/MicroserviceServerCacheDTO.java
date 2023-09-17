package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/4 11:08
 */
@Data
public class MicroserviceServerCacheDTO {
    private String publicKey; // TODO 改成公钥
    private Long keyTTL;
    private Integer version;
}
