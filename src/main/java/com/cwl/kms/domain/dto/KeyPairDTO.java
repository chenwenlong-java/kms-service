package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/4 13:58
 */
@Data
public class KeyPairDTO {
    protected String privateKey;
    protected String publicKey;
}
