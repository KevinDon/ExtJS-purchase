package com.newaim.purchase.config.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.config.json.JsonMoney;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Mark on 2017/9/12.
 */
public class CustomMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final MediaType TEXT_EVENT_STREAM = new MediaType("text", "event-stream");

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        JsonEncoding encoding = getJsonEncoding(contentType);
        ObjectMapper objectMapper = new ObjectMapper();
        try {//简单设置临时日期转换器
            Subject subject = SecurityUtils.getSubject();
            if(subject != null){
                UserVo user = SessionUtils.currentUserVo();
                String zoneInfo = user.getTimezone();
                String dateFormat = user.getDateFormat();
                dateFormat = DateFormatUtil.getFormDateFormatMapping(dateFormat);
                SimpleDateFormat df = new SimpleDateFormat(dateFormat);//日期格式
                df.setTimeZone(TimeZone.getTimeZone(zoneInfo));//时区
                objectMapper.setDateFormat(df);
                objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public Object findSerializer(Annotated am) {
                        if(am instanceof AnnotatedMethod){
                            if(BigDecimal.class.equals(((AnnotatedMethod) am).getRawReturnType())){ //数字格式化
                                if(!am.hasAnnotation(JsonMoney.class)){
                                    return new CustomNumberSerialize();
                                }else{ //金额
                                    JsonMoney jsonMoney = am.getAnnotation(JsonMoney.class);
                                    return new CustomNumberSerialize(true, jsonMoney.format());
                                }
                            }
                        }
                        return super.findSerializer(am);
                    }
                });
//                objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//                    @Override
//                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
//                        gen.writeString("");
//                    }
//                });
            }
        } catch (Exception e){}
        this.setObjectMapper(objectMapper);
        JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            writePrefix(generator, object);

            Class<?> serializationView = null;
            FilterProvider filters = null;
            Object value = object;
            JavaType javaType = null;
            if (object instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = getJavaType(type, null);
            }
            ObjectWriter objectWriter;
            if (serializationView != null) {
                objectWriter = this.objectMapper.writerWithView(serializationView);
            }
            else if (filters != null) {
                objectWriter = this.objectMapper.writer(filters);
            }
            else {
                objectWriter = this.objectMapper.writer();
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.forType(javaType);
            }
            SerializationConfig config = objectWriter.getConfig();
            if (contentType != null && contentType.isCompatibleWith(TEXT_EVENT_STREAM) &&
                    config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
                prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
                objectWriter = objectWriter.with(prettyPrinter);
            }
            objectWriter.writeValue(generator, value);

            writeSuffix(generator, object);
            generator.flush();

        }
        catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
        }
    }
}
