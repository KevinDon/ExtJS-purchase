package com.newaim.core.jpa;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchFilter {

    public enum Operator {
        EQ, LK, GT, LT, GTE, LTE, LLK, RLK, NULL, NNULL, EPT, NEPT, NEQ, IN,GBY
    }

    public enum Type { S, N, D }

    public String fieldName;
    public Object value;
    public Operator operator;
    public String union="ADD";
    public Type type;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.type = Type.S;
    }
    
    public SearchFilter(String fieldName, Operator operator, Object value, String union) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.union = union;
        this.type = Type.S;
    }

    public SearchFilter(String fieldName, Operator operator, Object value, String union, Type type) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.union = union;
        this.type = type;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static LinkedHashMap<String, SearchFilter> parse(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = Maps.newLinkedHashMap();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();


            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "-");
            if (names.length < 3) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[0];
            Operator operator = Operator.valueOf(names[2]);
            Type type = Type.valueOf(names[1]);

            if (ObjectUtils.isEmpty(value) && operator != Operator.GBY) {
                continue;
            }

            SearchFilter filter = null;
            if (names.length == 4 && StringUtils.isNotBlank(names[3])) {
            	filter = new SearchFilter(filedName, operator, value, names[3], type);
            }else{
            	filter = new SearchFilter(filedName, operator, value, "ADD", type);
            }
            
            filters.put(key, filter);
        }

        return filters;
    }
}
