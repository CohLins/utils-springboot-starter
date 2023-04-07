package com.colins.springutils.aspect;

import cn.hutool.json.JSONUtil;
import com.colins.springutils.annotation.AnHystrix;
import com.colins.springutils.entity.HystrixEntity;
import com.colins.springutils.entity.HystrixWindowArray;
import com.colins.springutils.entity.R;
import com.colins.springutils.utils.HystrixUtils;
import com.colins.springutils.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Aspect
@Slf4j
@Order(2)
public class HystrixAspect {

    private static ConcurrentHashMap<String, HystrixWindowArray> windowMap = new ConcurrentHashMap<>(32);


    @Pointcut("@annotation(com.colins.springutils.annotation.AnHystrix)")
    public void hystrixAspect() {

    }

    @Around("hystrixAspect()&&@annotation(hystrix)")
    public Object aroundExec(ProceedingJoinPoint joinPoint, AnHystrix hystrix) throws Throwable {

        int cpuLimit = hystrix.cpuLimit() <= 0 || hystrix.cpuLimit() > 100 ? HystrixUtils.DEFAULT_CPU_LIMIT : hystrix.cpuLimit();
        int heapMemoryLimit = hystrix.heapMemoryLimit() <= 0 || hystrix.heapMemoryLimit() > 100 ? HystrixUtils.DEFAULT_HEAP_MEMORY_LIMIT : hystrix.heapMemoryLimit();
        if (HystrixUtils.getCurrentCpu() >= cpuLimit || HystrixUtils.getCurrentHeapMemory() >= heapMemoryLimit) {
            return this.hystrixResult(hystrix.hystrixMsg());
        }
        if (hystrix.errorNumLimit() > 0 || (hystrix.errorRatioLimit() > 0 && hystrix.errorRatioLimit() <= 100)) {
            // 异常相关熔断判断
            int windowTimeInMs = hystrix.windowTimeInMs() < 1000 ? HystrixUtils.DEFAULT_WINDOW_TIME_LIMIT : hystrix.windowTimeInMs();
            int windowCount = hystrix.windowCount() < 2 ? HystrixUtils.DEFAULT_WINDOW_COUNT_LIMIT : hystrix.windowCount();
            HystrixWindowArray windowArray = getWindowArray(windowCount, windowTimeInMs);
            List<HystrixEntity> windowValues = windowArray.values();
            // 方法执行前 异常指标就超了就直接返回了
            if ((hystrix.errorRatioLimit() > 0 && hystrix.errorRatioLimit() <= 100 && HystrixUtils.getCurrentErrorRatio(windowValues) > hystrix.errorRatioLimit())
                    || (hystrix.errorNumLimit() > 0 && HystrixUtils.getCurrentErrorCount(windowValues) > hystrix.errorNumLimit())) {
                return this.hystrixResult(hystrix.hystrixMsg());
            }
            Object result=null;
            try {
                 result=joinPoint.proceed();
            } catch (Exception e) {
                windowArray.currentWindow().getHystrixEntity().addErrorCount();
                throw e;
            } finally {
                windowArray.currentWindow().getHystrixEntity().addRequestCount();
            }
            return result;
        }
        return joinPoint.proceed();
    }


    private Object hystrixResult(String hystrixMsg) {
        ServletUtils.returnJson(ServletUtils.getResponse(), JSONUtil.toJsonStr(R.fail(hystrixMsg)));
        return null;
    }

    private HystrixWindowArray getWindowArray(int windowCount, int windowTimeInMs) {
        // 以url作为熔断滑动窗口 key
        String url = ServletUtils.getRequest().getRequestURI();
        HystrixWindowArray hystrixWindowArray = windowMap.get(url);
        if (hystrixWindowArray == null) {
            hystrixWindowArray = new HystrixWindowArray(windowCount, windowTimeInMs);
            windowMap.put(url, hystrixWindowArray);
        }
        return hystrixWindowArray;
    }
}
