package com.colins.springutils.encryption;


import com.colins.springutils.annotation.EncryptAndDecry;
import com.colins.springutils.config.MybatisUtilsConfig;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class MybatisFieldDecryInterceptor implements Interceptor {
    private final static Logger log = LoggerFactory.getLogger(MybatisFieldDecryInterceptor.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private final MybatisUtilsConfig mybatisUtilsConfig;

    public MybatisFieldDecryInterceptor(MybatisUtilsConfig mybatisUtilsConfig){
        this.mybatisUtilsConfig=mybatisUtilsConfig;
    }



    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取到返回结果
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaResultSetHandler = MetaObject.forObject(resultSetHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
        MappedStatement mappedStatement = (MappedStatement) metaResultSetHandler.getValue("mappedStatement");

        EncryptAndDecry annotation = getEncryptAndDecryAnnotation(mappedStatement);
        Object returnValue = invocation.proceed();
        if (annotation != null && returnValue != null) {
            Constructor<? extends IEncryptAndDecryStrategy> constructor = annotation.strategy().getConstructor(MybatisUtilsConfig.class);
            IEncryptAndDecryStrategy iEncryptAndDecryStrategy = constructor.newInstance(mybatisUtilsConfig);
            List<String> decryptField = Arrays.asList(annotation.dbFieldName());
            // 对结果进行处理
            if (returnValue instanceof ArrayList<?>) {
                List<?> list = (ArrayList<?>) returnValue;
                for (int index = 0; index < list.size(); index++) {
                    Object returnItem = list.get(index);
                    if (returnItem instanceof String) {
                        List<String> stringList = (List<String>) list;
                        stringList.set(index, decrypt(returnItem,iEncryptAndDecryStrategy).toString());
                    } else {
                        MetaObject metaReturnItem = MetaObject.forObject(returnItem, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
                        decryptField.forEach(item->{
                            Object fieldValue = metaReturnItem.getValue(item);
                            if (fieldValue!=null && fieldValue instanceof String) {
                                metaReturnItem.setValue(item,decrypt(fieldValue,iEncryptAndDecryStrategy));
                            }
                        });
                    }
                }
            }
        }
        return returnValue;
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



    private EncryptAndDecry getEncryptAndDecryAnnotation(MappedStatement mappedStatement) {
        EncryptAndDecry encryptResultFieldAnnotation = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            final Method[] method = Class.forName(className).getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(EncryptAndDecry.class)) {
                    encryptResultFieldAnnotation = me.getAnnotation(EncryptAndDecry.class);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encryptResultFieldAnnotation;
    }

    private static Object decrypt(Object obj, IEncryptAndDecryStrategy strategy) {
        if (obj!=null && obj instanceof String) {
            return strategy.decrypt(obj.toString());
        }
        return obj;
    }
}
