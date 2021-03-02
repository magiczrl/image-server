package com.cn.image.interceptor;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.image.athena.AthenaLog;
import com.cn.image.athena.AthenaLogReport;
import com.cn.image.athena.AthenaType;
import com.cn.image.common.RequestHolder;
import com.cn.image.common.ResponseHolder;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Aspect
@Component
@Order(1)
public class PrintParamInterceptor {
    private static Logger logger = LoggerFactory.getLogger(PrintParamInterceptor.class);

    public static final String EXEC = "execution(* com.cn.image.controller.*.*(..))";

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
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            LocalDateTime requestTime = LocalDateTime.now();
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            String signature = com.cn.image.common.tools.MethodSignature
                    .generateSignature(method);
            Object obj = point.proceed();
            printAthenaLog(request, signature, obj, requestTime);
            return obj;
        } finally {
            RequestHolder.clear();
            ResponseHolder.clear();
        }

    }

    private void printAthenaLog(HttpServletRequest httpServletRequest, String signature,
                                Object proceedObj, LocalDateTime requestTime) {
        try {
            AthenaLog athenaLog = new AthenaLog(httpServletRequest);
            // 设置Athena日志操作类型
            athenaLog.setOperType(AthenaType.get(signature));
            athenaLog.setAppType("3");
            // 设置Athena日志请求参数
            JSONObject requestJson = RequestHolder.get();
            if (requestJson == null) {
                requestJson = new JSONObject();
                Map<String, String[]> params = httpServletRequest.getParameterMap();
                if (params != null && !params.isEmpty()) {
                    for (Map.Entry<String, String[]> entry : params.entrySet()) {
                        requestJson.put(entry.getKey(), entry.getValue()[0]);
                    }
                }
            }
            athenaLog.setRequest(requestJson);
            // 设置Athena日志loginid
            athenaLog.setLoginId(requestJson == null ? null : requestJson.getString("mac"));
            // 设置Athena日志响应参数及响应码
            JSONObject responseJson = ResponseHolder.get();
            String operStatus = null;
            if (responseJson != null) {
                if (proceedObj instanceof String) {
                    JSONObject resultJson = JSONObject.parseObject((String) proceedObj);
                    operStatus = resultJson.getString("returnCode");
                } else {
                    JSONObject resultJson = (JSONObject) JSON.toJSON(proceedObj);
                    operStatus = resultJson.getString("returnCode");
                }
            } else {
                if (proceedObj != null) {
                    if (proceedObj instanceof String) {
                        try {
                            responseJson = JSONObject.parseObject((String) proceedObj);
                        } catch (Exception e) {
                            responseJson = new JSONObject();
                            responseJson.put("data", (String) proceedObj);
                        }
                    } else {
                        try {
                            responseJson = (JSONObject) JSON.toJSON(proceedObj);
                        } catch (Exception e) {
                            logger.warn("non json");
                        }
                    }
                    if (responseJson != null) {
                        operStatus = responseJson.getString("returnCode");
                    }
                }
            }
            athenaLog.setResponse(responseJson);
            athenaLog.setOperStatus(operStatus);
            AthenaLogReport.reportComplete(athenaLog, requestTime);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
