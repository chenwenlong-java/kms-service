package com.cwl.kms.domain.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author FastJson
 * @Create 2023/8/1 16:31
 */
@Data
public class MiddlewarePO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 中间件唯一标识
     */
    private String middlewareKey;
    /**
     * 中间件密码
     */
    private String middlewareValue;
    /**
     * 中间件的appId
     */
    private String appId;
}
