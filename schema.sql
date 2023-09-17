CREATE TABLE `asymmetric_keys`
(
    `id`               bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `private_key`      varchar(2048) NOT NULL DEFAULT '' COMMENT '私钥：使用Base64编码后存储',
    `public_key`       varchar(2048) NOT NULL DEFAULT '' COMMENT '公钥：使用Base64编码后存储',
    `ttl`              bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '密钥的有效时间戳；-1 表示永不过期【用在中间件场景】',
    `last_clock_cycle` bigint(20) NOT NULL DEFAULT '0' COMMENT '上一个时钟周期，用于计算加一个时钟周期',
    `key_type`         tinyint(1) NOT NULL DEFAULT '0' COMMENT '密钥类型：0 -表示微服务类型；1 -表示中间件类型',
    `version`          int(11) unsigned NOT NULL DEFAULT '1' COMMENT '密钥的版本',
    `service_id`       bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '关联服务的ID；可能来自中间件或者微服务',
    `ctime`            datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`            datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `is_deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 -否; 1 -是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_service_id_version` (`service_id`,`version`) USING BTREE,
    KEY                `ix_mtime` (`mtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='密钥表';


CREATE TABLE `authentication`
(
    `id`             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键自增ID',
    `ctime`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `app_id`         varchar(128) NOT NULL DEFAULT '' COMMENT '微服务的唯一标识；不允许为空；添加唯一索引',
    `token_ttl`      tinyint(3) unsigned NOT NULL DEFAULT '10' COMMENT 'Token过期时间，默认值为10分钟',
    `enabled`        tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启认证，1 -表示开启，0 -表示关闭',
    `submitter`      varchar(128) NOT NULL DEFAULT '' COMMENT '提交人信息',
    `deploy_env`     varchar(5)   NOT NULL DEFAULT '' COMMENT '环境信息',
    `approval_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审批状态: 0 -待审批; 1 -审批通过; 2 -审批驳回',
    `is_deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 -否; 1 -是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_id` (`app_id`) USING BTREE,
    KEY              `ix_mtime` (`mtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='认证表';


CREATE TABLE `middleware`
(
    `id`               bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID自增长',
    `middleware_key`   varchar(255) NOT NULL DEFAULT '' COMMENT '中间件用于换取value的key,目前是中间件连接串',
    `middleware_value` varchar(512) NOT NULL DEFAULT '' COMMENT '使用公钥加密之后的密文',
    `app_id`           varchar(128) NOT NULL DEFAULT '' COMMENT '中间件应用ID',
    `ctime`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`            datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `is_deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 -否; 1 -是',
    PRIMARY KEY (`id`),
    KEY                `ix_mtime` (`mtime`),
    KEY                `ix_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中间件表';

CREATE TABLE `service_account`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID自增长',
    `service_account` varchar(64)  NOT NULL DEFAULT '' COMMENT '账户唯一标识',
    `app_id`          varchar(128) NOT NULL DEFAULT '' COMMENT '应用唯一标识',
    `enabled`         tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1 -是 0 -否',
    `ctime`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `mtime`           datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `is_deleted`      tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 -否; 1 -是',
    PRIMARY KEY (`id`),
    KEY               `ix_mtime` (`mtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';