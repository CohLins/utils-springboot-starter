package com.colins.springutils.config;


import com.colins.springutils.aspect.DataDesensitizationAspect;
import com.colins.springutils.aspect.RequestLogAspect;
import com.colins.springutils.desensitization.DesensitizationTypeHandler;
import com.colins.springutils.desensitization.impl.EntityTypeHandler;
import com.colins.springutils.encryption.MybatisFieldDecryInterceptor;
import com.colins.springutils.encryption.MybatisFieldEncryptInterceptor;
import com.colins.springutils.log.MybatisLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({MybatisUtilsConfig.class})
public class MybatisUtilsAutoConfiguration {



    @Bean
    @ConditionalOnProperty(prefix = "my-utils.config.sqlLog" ,havingValue = "true" )
    public MybatisLogInterceptor mybatisLogInterceptor(){
        return new MybatisLogInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "my-utils.config.requestLog" ,havingValue = "true" )
    public RequestLogAspect requestLogAspect(){
        return new RequestLogAspect();
    }

    @Bean
    public MybatisFieldDecryInterceptor fieldDecryInterceptor(MybatisUtilsConfig utilsConfig){
        return new MybatisFieldDecryInterceptor(utilsConfig);
    }

    @Bean
    public MybatisFieldEncryptInterceptor fieldEncryptInterceptor(MybatisUtilsConfig utilsConfig){
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
}
