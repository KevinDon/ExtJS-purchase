package com.newaim.purchase.config.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 自定义数字格式化
 * Created by Mark on 2017/9/18.
 */
public class CustomNumberSerialize extends JsonSerializer<BigDecimal> {

    /**
     * 默认数字格式
     */
    private final static String DEFAULT_PATTERN = "#0.00";

    private String pattern = DEFAULT_PATTERN;

    public CustomNumberSerialize(){}

    public CustomNumberSerialize(boolean money){
        if(money){
            this.pattern = "###0.0000";
        }
    }

    public CustomNumberSerialize(boolean money, boolean format){
        if(money){
            if(format){
                this.pattern = "###0.0000";
            }else{
                this.pattern = "#0.0000";
            }
        }
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        DecimalFormat df = new DecimalFormat(pattern);
        gen.writeString(df.format(value));
    }
}
