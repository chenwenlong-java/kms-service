package com.cwl.kms.api;

import com.cwl.kms.domain.vo.base.ResponseDataWrapper;
import com.cwl.kms.service.ServiceAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ServiceAccountController
 * Package: com.cwl.kms.api
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:55
 * @Version 1.0
 */
@Tag(name = "服务账号", description = "用于向KMS获取服务账号的接口")
@RestController
@RequestMapping("/kms/api/v1/serviceAccount")
public class ServiceAccountController {

    @Autowired
    private ServiceAccountService accountService;

    @Value("${kms.account.key}")
    private String accountKey;

    @Operation(summary = "用于提供给中间件/NavigatorConsole获取服务账号的接口, KMS会根据Paladin配置的Allow列表来判定是否允许访问")
    @GetMapping("/getServiceAccount")
    public ResponseDataWrapper<String> getServiceAccount(@RequestParam String appId) {
        return ResponseDataWrapper.success(accountService.selectByAppId(appId));
    }

    @Operation(summary ="用于获取对称加密之后的服务账号, 目前提供给Caster使用, 避免服务账号的明文传输导致初始安全问题的产生")
    @GetMapping("/getEncryptAccount")
    public ResponseDataWrapper<String> getEncryptAccount(@RequestParam String appId) {
        return ResponseDataWrapper.success(accountService.encryptAES(appId, accountKey));
    }
}
