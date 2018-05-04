package com.newaim.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean获取工具
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null){
            SpringUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 通过bean名称获取Bean
     * @param name beanName
     */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 通过bean的class获取Bean
     * @param clazz beanClass
     * @param <T> Bena类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}
