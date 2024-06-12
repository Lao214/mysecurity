package com.example.mysecurity.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 应用程序上下文工具
 * 程序启动后，会创建ApplicationContext对象
 * ApplicationContextAware能感知到ApplicationContext对象
 * 自动调用setApplicationContext方法
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    //系统的IOC容器
    private static ApplicationContext applicationContext = null;

    //感知到上下文后，自动调用，获得上下文
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }

    //返回对象
    public static <T> T getBean(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }
}