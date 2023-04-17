package com.colins.springutils.config;


import com.colins.springutils.aspect.*;
import com.colins.springutils.desensitization.DesensitizationTypeHandler;
import com.colins.springutils.desensitization.impl.EntityTypeHandler;
import com.colins.springutils.encryption.MybatisFieldDecryInterceptor;
import com.colins.springutils.encryption.MybatisFieldEncryptInterceptor;
import com.colins.springutils.log.MybatisLogInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({UtilsConfig.class})
public class UtilsAutoConfiguration {



    @Bean
    @ConditionalOnProperty(value = "my-utils.config.sql-log" ,havingValue = "true" )
    public Interceptor mybatisLogInterceptor(){
        return new MybatisLogInterceptor();
    }

    @Bean
    public RequestLogAspect requestLogAspect(){
        return new RequestLogAspect();
    }

    @Bean
    public Interceptor fieldDecryInterceptor(UtilsConfig utilsConfig){
        return new MybatisFieldDecryInterceptor(utilsConfig);
    }

    @Bean
    public Interceptor fieldEncryptInterceptor(UtilsConfig utilsConfig){
        return new MybatisFieldEncryptInterceptor(utilsConfig);
    }


    @Bean
    @ConditionalOnMissingBean(value = DesensitizationTypeHandler.class)
    public DesensitizationTypeHandler typeHandler(){
        return new EntityTypeHandler();
    }

    @Bean
    public DataDesensitizationAspect dataDesensitizationAspect(DesensitizationTypeHandler typeHandler){
        return new DataDesensitizationAspect(typeHandler);
    }

    @Bean
    public HystrixAspect hystrixAspect(){
        return new HystrixAspect();
    }

    @Bean
    public RateLimiterAspect rateLimiterAspect(){
        return new RateLimiterAspect();
    }

    @Bean
    public RetryAspect retryAspect(){
        return new RetryAspect();
    }
}
