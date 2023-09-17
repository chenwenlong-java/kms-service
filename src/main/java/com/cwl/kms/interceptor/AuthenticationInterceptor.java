package com.cwl.kms.interceptor;

import com.cwl.kms.service.ServiceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: AuthenticationInterceptor
 * Package: com.cwl.kms.interceptor
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/17 22:04
 * @Version 1.0
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private ServiceAccountService accountService;

    @Value("${kms.account.key}")
    private String accountKey;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (pathMatcher.match("/kms/api/v1/microservice/**", requestURI)
                || pathMatcher.match("/kms/api/v1/middleware/**", requestURI)
                || pathMatcher.match("/kms/api/v1/serviceAccount/**", requestURI)) {
            return true;
        }
        // 使用对称加密 之后的SA
        String serviceAccount = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (pathMatcher.match("/kms/api/v1/bin/**", requestURI)) {
            String srcAppId = request.getHeader("appId"); // kms-bin
            String appId = accountService.decryptAES(serviceAccount, accountKey);
            if (appId.equals(srcAppId)) {
                return true;
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "service account not match");
            return false;
        }

        // 普通的SA ==> UUID
        if (accountService.notExist(serviceAccount)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "service account not exits");
            return false;
        }
        return true;
    }
}
