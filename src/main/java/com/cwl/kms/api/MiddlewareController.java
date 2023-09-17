package com.cwl.kms.api;

import com.cwl.kms.domain.vo.MiddlewareVO;
import com.cwl.kms.domain.vo.base.ResponseDataWrapper;
import com.cwl.kms.service.MiddlewareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: MiddlewareController
 * Package: com.cwl.kms.api
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:52
 * @Version 1.0
 */
@Tag(name = "中间件", description = "中间件服务接口")
@RestController
@RequestMapping("/kms/api/v1/middleware")
public class MiddlewareController {

    @Autowired
    private MiddlewareService middlewareService;

    @Operation(summary = "中间件初始化账号密码接口, 提供给中间件做免密服务")
    @PostMapping("/init")
    public ResponseDataWrapper<Integer> init(@Parameter(description = "服务账号") @RequestParam String serviceAccount,
                                             @Parameter(description = "中间件初始化的key-value, 支持多个") @RequestBody List<MiddlewareVO> middlewareVOList) {
        return ResponseDataWrapper.success(middlewareService.init(serviceAccount, middlewareVOList));
    }

    @Operation(summary = "认证SDK根据中间的提供的key, 获取公钥加密之后的密码")
    @GetMapping("/getValue")
    public ResponseDataWrapper<String> getValue(@RequestParam(value = "key") String key) {
        return ResponseDataWrapper.success(middlewareService.getValue(key));
    }

    @Operation(summary = "认证SDK根据ServiceAccount获取私钥, 用于加密中间件传输的密码")
    @GetMapping("/getPrivateKey")
    public ResponseDataWrapper<String> getPrivateKey(@RequestParam(value = "serviceAccount") String serviceAccount) {
        return ResponseDataWrapper.success(middlewareService.getPrivateKey(serviceAccount));
    }
}
