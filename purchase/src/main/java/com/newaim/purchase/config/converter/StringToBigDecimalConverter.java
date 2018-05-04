package com.newaim.purchase.config.converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * 对金额等数字的处理
 */
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String str) {
        Assert.hasText(str, "Null or empty BigDecimal string.");
        BigDecimal bigDecimal = null;
        if(StringUtils.contains(str, ",")){ //金额
            String temp = StringUtils.removeAll(str, ",");
            bigDecimal = new BigDecimal(temp);
        }
        bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP); //四舍五入
        return bigDecimal;
    }
}
