package com.cn.image.interceptor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cn.image.common.ActionResponse;
import com.cn.image.common.Constants;
import com.cn.image.common.ResMsg;
import com.cn.image.context.CacheWrapper;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Aspect
@Component
@Order(20)
public class LoginInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    public static final String EXEC = "execution(* com.cn.image.controller.*.*(..))";

    @Autowired
    private CacheWrapper cacheWrapper;

    /**
     *
     */
    @Pointcut(EXEC)
    public void interceptor() {
    }

    /**
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("interceptor()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Auth auth = method.getAnnotation(Auth.class);
        // 此类请求无需拦截，直接放行
        if (auth == null) {
            logger.info("{} 未配置Auth Annotation，不需要校验登录状态，直接放行", method.getName());
            return point.proceed();
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String accessToken = request.getHeader("token");

        logger.info("methodName: {}, [LoginInterceptor] accessToken: {} ",
                point.getSignature().getDeclaringTypeName() + "." + method.getName(), accessToken);
        try {
            Validate.notBlank(accessToken);
            Validate.notBlank(cacheWrapper.get(Constants.CACHE_TOKEN_KEY + accessToken));
        } catch (Exception e) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getResponse();
            response.setStatus(401);
            ActionResponse ar = new ActionResponse();
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "登录回话过期或无效，请重新登录");
            return ar;
        }
        return point.proceed();
    }

}
