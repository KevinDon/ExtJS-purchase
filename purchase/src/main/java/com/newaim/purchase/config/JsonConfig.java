package com.newaim.purchase.config;

import com.google.common.collect.Lists;
import com.newaim.purchase.config.converter.CustomMappingJackson2HttpMessageConverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;


/**
 * 自定义json输出时的格式转换器，增加日期的时区及格式自定义
 * Created by Mark on 2017/9/12.
 */
@Configuration
public class JsonConfig extends WebMvcConfigurerAdapter{


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        //替换原有的json转换器，增加日期转换
        List<MappingJackson2HttpMessageConverter> oriConverters = Lists.newArrayList();
        for (int i = 0; i < converters.size(); i++) {
            if(converters.get(i) instanceof MappingJackson2HttpMessageConverter){
                oriConverters.add((MappingJackson2HttpMessageConverter) converters.get(i));
            }
        }
        if(oriConverters != null && oriConverters.size() > 0){
            converters.removeAll(oriConverters);
        }
        converters.add(new CustomMappingJackson2HttpMessageConverter());
        super.extendMessageConverters(converters);
    }
}
