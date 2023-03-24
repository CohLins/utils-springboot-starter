package com.colins.springutils.aspect;


import com.colins.springutils.desensitization.DesensitizationTypeHandler;
import com.colins.springutils.utils.DesensitizationUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@Aspect
public class DataDesensitizationAspect {

    private DesensitizationTypeHandler typeHandler;

    public DataDesensitizationAspect(DesensitizationTypeHandler typeHandler){
        this.typeHandler=typeHandler;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.colins.springutils.annotation.DataDesensitization)")
    public void DataDesensitizationAspect() {

    }

    /**
     * 脱敏 , 并处理特殊类型数据
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around(value = "DataDesensitizationAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
        // 执行拦截的方法
        Object result = joinPoint.proceed();

        // 处理集合类型
        if (result instanceof List) {
            List<Object> collection = (List<Object>) result;
            for (Object object : collection) {
                // 重新赋值
                object = DesensitizationUtils.desensitize(object);
            }
            return result;
        }
        // 自定义处理 拓展
        return typeHandler.resultDateHandler(result);
    }
}
