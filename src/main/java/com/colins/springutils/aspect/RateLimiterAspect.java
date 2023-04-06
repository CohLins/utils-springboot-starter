package com.colins.springutils.aspect;

import cn.hutool.json.JSONUtil;
import com.colins.springutils.annotation.AnRateLimiter;
import com.colins.springutils.entity.R;
import com.colins.springutils.utils.ServletUtils;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Aspect
@Slf4j
@Order(1)
public class RateLimiterAspect {
    private  Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();


    @Pointcut("@annotation(com.colins.springutils.annotation.AnRateLimiter)")
    public void rateLimiterAspect() {

    }

    @Around("rateLimiterAspect()&&@annotation(anRateLimiter)")
    public Object aroundExec(ProceedingJoinPoint joinPoint,AnRateLimiter anRateLimiter) throws Throwable {
        // 获取request,response
        HttpServletRequest request = ServletUtils.getRequest();
        HttpServletResponse response = ServletUtils.getResponse();
        // 以url作为限流的Key
        String url = request.getRequestURI();
        if(anRateLimiter!=null){
            RateLimiter rateLimiter=null;
            if(!limitMap.containsKey(url)){
                rateLimiter=RateLimiter.create(anRateLimiter.createTokenRate());
                limitMap.put(url,rateLimiter);
            }
            boolean acquire=limitMap.get(url).tryAcquire(anRateLimiter.timeOut(),anRateLimiter.timeUnit());
            if(!acquire){
                ServletUtils.returnJson(response,JSONUtil.toJsonStr(R.fail(anRateLimiter.timeOutMsg())));
                return null;
            }
        }
        return joinPoint.proceed();
    }




}
