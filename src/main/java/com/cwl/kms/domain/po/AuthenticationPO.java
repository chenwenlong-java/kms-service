package com.cwl.kms.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author FastJson
 * @Create 2023/8/1 16:24
 */
@Data
public class AuthenticationPO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 应用唯一标识
     */
    private String appId;

    /**
     * 环境标识
     */
    private String deployEnv;

    /**
     * 提交人信息
     */
    private String submitter;

    /**
     * Token过期时间; 默认值10分钟
     */
    private Integer tokenTTL;

    /**
     * 审批状态: 0 -待审批; 1 -审批通过; 2 -审批驳回
     */
    private Integer approvalState;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 更新时间
     */
    private LocalDateTime mtime;

    /**
     * 是否开启认证 0 - 关闭 1 - 开启
     */
    private Boolean enabled;

}
