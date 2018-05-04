package com.newaim.purchase.config;

import com.newaim.purchase.config.converter.StringToBigDecimalConverter;
import com.newaim.purchase.config.converter.StringToDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;


/**
 * Created by Mark on 2017/9/7.
 */
@Configuration
public class WebConfig{

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void addConversionConfig(){
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) requestMappingHandlerAdapter.getWebBindingInitializer();
        if(initializer != null){
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            //日期转换
            genericConversionService.addConverter(new StringToDateConverter());

            //金额转换
            genericConversionService.addConverter(new StringToBigDecimalConverter());
        }
    }

}
