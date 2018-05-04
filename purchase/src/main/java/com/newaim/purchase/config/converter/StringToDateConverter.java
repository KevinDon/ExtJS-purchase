package com.newaim.purchase.config.converter;

import com.newaim.core.utils.DateFormatUtil;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 通用字符转日期转换器, 根据用户日期格式设置转换
 * Created by Mark on 2017/9/7.
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String dateStr) {
        Date date = null;
        if(StringUtils.isBlank(dateStr)){
            return date;
        }
        try {
            UserVo user = SessionUtils.currentUserVo();
            String timezone = user.getTimezone();
            String dateFormat = user.getDateFormat();
            dateFormat = DateFormatUtil.getFormDateFormatMapping(dateFormat);
            if(StringUtils.indexOf(dateStr," ") == -1){//不带时间时的转换
                dateFormat = StringUtils.substring(dateFormat,0, StringUtils.indexOf(dateFormat, " "));
            }
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            df.setTimeZone(TimeZone.getTimeZone(timezone));
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return date;
    }
}
