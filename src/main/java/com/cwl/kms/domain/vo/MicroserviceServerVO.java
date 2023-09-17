package com.cwl.kms.domain.vo;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/9/15 15:16
 */
@Data
public class MicroserviceServerVO {
    private String publicKey;
    private Integer version;
    private Long keyTTL;
}
