package com.colins.springutils.encryption;


import com.colins.springutils.annotation.EncryptAndDecry;
import com.colins.springutils.config.UtilsConfig;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class MybatisFieldEncryptInterceptor implements Interceptor {

    private final static Logger log = LoggerFactory.getLogger(MybatisFieldEncryptInterceptor.class);

    private final UtilsConfig utilsConfig;

    public MybatisFieldEncryptInterceptor(UtilsConfig utilsConfig){
        this.utilsConfig = utilsConfig;
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();

        // 获取类名
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);

        // 动态加载类并获取类中的方法
        final Method[] methods = Class.forName(className).getMethods();

        // 遍历类的所有方法并找到此次调用的方法
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName) && method.isAnnotationPresent(EncryptAndDecry.class)) {
                EncryptAndDecry annotation = method.getAnnotation(EncryptAndDecry.class);
                if (annotation.dbFieldName().length > 0) {
                    Constructor<? extends IEncryptAndDecryStrategy> constructor = annotation.strategy().getConstructor(UtilsConfig.class);
                    IEncryptAndDecryStrategy iEncryptAndDecryStrategy = constructor.newInstance(utilsConfig);
                    valueEncrypt(configuration, boundSql, annotation.dbFieldName(), iEncryptAndDecryStrategy);
                }
            }
        }

        return invocation.proceed();
    }

    /**
     * 1. return target;  直接返回目标对象，相当于当前Interceptor没起作用，不会调用上面的intercept()方法
     * 2. return Plugin.wrap(target, this);  返回代理对象，会调用上面的intercept()方法
     *
     * @param target 目标对象
     * @return 目标对象或者代理对象
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 用于获取在Configuration初始化当前的Interceptor时时候设置的一些参数
     *
     * @param properties Properties参数
     */
    @Override
    public void setProperties(Properties properties) {
    }


    /**
     * 组装完整的sql语句 -- 把对应的参数都代入到sql语句里面
     *
     * @param configuration Configuration
     * @param boundSql      BoundSql
     * @return sql完整语句
     */
    private void valueEncrypt(Configuration configuration, BoundSql boundSql, String[] dbFieldName, IEncryptAndDecryStrategy strategy) {
        List<String> encryptField = Arrays.asList(dbFieldName);
        // 获取mapper里面方法上的参数
        Object sqlParameter = boundSql.getParameterObject();
        // sql语句里面需要的参数 -- 真实需要用到的参数 因为sqlParameter里面的每个参数不一定都会用到
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        MetaObject metaObject = configuration.newMetaObject(sqlParameter);
        if (parameterMappings.size() > 0 && sqlParameter != null) {
            for (ParameterMapping parameterMapping : parameterMappings) {
                // 一个一个把对应的值替换进去 按顺序把?替换成对应的值
                String propertyName = parameterMapping.getProperty();
                if (encryptField.contains(propertyName)) {
                    //加密
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        metaObject.setValue(propertyName, encrypt(obj, strategy));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        metaObject.setValue(propertyName, encrypt(obj, strategy));
                    }
                }

            }
        }
    }

    private static Object encrypt(Object obj, IEncryptAndDecryStrategy strategy) {
        if (obj!=null && obj instanceof String) {
            return strategy.encrypt(obj.toString());
        }
        return obj;
    }
}
