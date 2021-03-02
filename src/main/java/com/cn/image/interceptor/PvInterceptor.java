package com.cn.image.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Aspect
@Component
@Order(5)
public class PvInterceptor {

    private static Logger logger = LoggerFactory.getLogger("SYSTEM_LOG");

    public static final String EXEC = "execution(* com.cn.image.controller.*.*(..))";

    private static Map<String, AtomicInteger> CLICK_TIMES_MAP = new HashMap<String, AtomicInteger>();

    public static int PERIOD = 180;

    /**
     * 
     */
    @Pointcut(EXEC)
    public void interceptor() {
    }

    static {
        ScheduledExecutorService executor = Executors
                .newSingleThreadScheduledExecutor(new ThreadFactory() {

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "pool-pv-thread");
                        t.setDaemon(true);
                        return t;
                    }
                });

        executor.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                try {
                    if (CLICK_TIMES_MAP.isEmpty()) {
                        logger.error("无URL调用");
                        return;
                    }

                    for (String path : CLICK_TIMES_MAP.keySet()) {
                        AtomicInteger clickNum = CLICK_TIMES_MAP.get(path);
                        int value = clickNum.getAndSet(0);
                        float tps = (float) value / PERIOD;
                        if (tps > 1) {
                            logger.info("URL[{}]调用频率 = {} records/sec", path,
                                    (float) value / PERIOD);
                        }
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }

            }
        }, PERIOD, PERIOD, TimeUnit.SECONDS);
    }

    /**
     * 
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("interceptor()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        logger.debug("===============进入pv拦截器===============");
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RequestMapping rmM = method.getAnnotation(RequestMapping.class);
        if (rmM == null) {
            return point.proceed();
        }

        String[] valueArr = rmM.value();
        if (valueArr == null || valueArr.length < 1) {
            return point.proceed();
        }

        String path = valueArr[0];

        try {
            @SuppressWarnings("rawtypes")
            Class clazz = method.getDeclaringClass();
            @SuppressWarnings("unchecked")
            RequestMapping rmC = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (rmC != null) {
                valueArr = rmC.value();
                if (valueArr != null && valueArr.length >= 1) {
                    path = valueArr[0] + path;
                }
            }
        } catch (Exception e) {
        }

        AtomicInteger clickNum = CLICK_TIMES_MAP.get(path);
        if (clickNum == null) {
            clickNum = new AtomicInteger(0);
            CLICK_TIMES_MAP.put(path, clickNum);
        }

        clickNum.incrementAndGet();
        return point.proceed();
    }

}
