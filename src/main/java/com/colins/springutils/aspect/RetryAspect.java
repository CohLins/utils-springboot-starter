package com.colins.springutils.aspect;

import cn.hutool.json.JSONUtil;
import com.colins.springutils.annotation.AnRetry;
import com.colins.springutils.entity.R;
import com.colins.springutils.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;


@Aspect
@Slf4j
public class RetryAspect {

    @Pointcut("@annotation(com.colins.springutils.annotation.AnRetry)")
    public void retryAspect() {

    }

    @Around("retryAspect()&&@annotation(retry)")
    public Object aroundExec(ProceedingJoinPoint joinPoint, AnRetry retry) throws Throwable {
        int attempts = 0;
        while (attempts < retry.retryTimes()) {
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                if (!retry.retryFor().isInstance(e)) {
                    throw e;
                }
                attempts++;
            }
        }
        ServletUtils.returnJson(ServletUtils.getResponse(), JSONUtil.toJsonStr(R.fail(retry.retryOverMsg())));
        return null;
    }
}
