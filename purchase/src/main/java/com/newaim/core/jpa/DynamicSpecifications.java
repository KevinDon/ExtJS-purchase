package com.newaim.core.jpa;

import com.google.common.collect.Lists;
import com.newaim.core.utils.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;


public class DynamicSpecifications {

    public static <T, K> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<K> entityClazz) {

        return new Specification<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (filters != null && !filters.isEmpty()) {
                    Predicate result = null;
                    List<Predicate> predicates = Lists.newArrayList();
                    List<Predicate> predicatesInner =  Lists.newArrayList();
                    String lastUnion = "";
                    Integer totalItems = filters.size();
                    Integer index = 0;

                    for (SearchFilter filter : filters) {
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        index ++;
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }

                        Predicate predicate = null;
                        switch (filter.operator) {
                            case EQ:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.equal(expression, Integer.parseInt(filter.value.toString())));
                                    }else if(filter.type.equals(SearchFilter.Type.D)) {
                                        predicate = (builder.equal(expression, DateFormatUtil.parseDateTimeWithUserTimeZone(filter.value.toString().replace("T", " "))));
                                    }else{
                                        predicate = (builder.equal(expression, filter.value));
                                    }
                                }catch (Exception e){}

                                break;
                            case NEQ:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.notEqual(expression, Integer.parseInt(filter.value.toString())));
                                    }else{
                                        predicate = (builder.notEqual(expression, filter.value));
                                    }
                                }catch (Exception e){}

                                break;
                            case LK:
                                if(filter.type.equals(SearchFilter.Type.S)){
                                    predicate = (builder.like(expression, "%" + filter.value + "%"));
                                }

                                break;
                            case LLK:
                                if(filter.type.equals(SearchFilter.Type.S)){
                                    predicate = (builder.like(expression, filter.value + "%"));
                                }
                                break;
                            case RLK:
                                if(filter.type.equals(SearchFilter.Type.S)){
                                    predicate = (builder.like(expression, "%" + filter.value));
                                }
                                break;
                            case GT:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.greaterThan(expression, (Comparable) Integer.valueOf(filter.value.toString())));
                                    }else if(filter.type.equals(SearchFilter.Type.D)) {
                                        predicate = (builder.greaterThan(expression, (Comparable) DateFormatUtil.parseDateTimeWithUserTimeZone(filter.value.toString().replace("T", " "))));
                                    }else{
                                        predicate = (builder.greaterThan(expression, (Comparable) filter.value));
                                    }
                                }catch (Exception e){}
                                break;
                            case LT:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.lessThan(expression, (Comparable) Integer.valueOf(filter.value.toString())));
                                    }else if(filter.type.equals(SearchFilter.Type.D)) {
                                        predicate = (builder.lessThan(expression, (Comparable) DateFormatUtil.parseDateTimeWithUserTimeZone(filter.value.toString().replace("T", " "))));
                                    }else{
                                        predicate = (builder.lessThan(expression, (Comparable) filter.value));
                                    }
                                }catch (Exception e){}
                                break;
                            case GTE:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.greaterThanOrEqualTo(expression, (Comparable) Integer.valueOf(filter.value.toString())));
                                    }else if(filter.type.equals(SearchFilter.Type.D)) {
                                        predicate = (builder.greaterThanOrEqualTo(expression, (Comparable) DateFormatUtil.parseDateTimeWithUserTimeZone(filter.value.toString().replace("T", " "))));
                                    }else{
                                        predicate = (builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                    }
                                }catch (Exception e){}
                                break;
                            case LTE:
                                try {
                                    if(filter.type.equals(SearchFilter.Type.N)) {
                                        predicate = (builder.lessThanOrEqualTo(expression, (Comparable) Integer.valueOf(filter.value.toString())));
                                    }else if(filter.type.equals(SearchFilter.Type.D)) {
                                        predicate = (builder.lessThanOrEqualTo(expression, (Comparable) DateFormatUtil.parseDateTimeWithUserTimeZone(filter.value.toString().replace("T", " "))));
                                    }else{
                                        predicate = (builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                    }
                                }catch (Exception e){}
                                break;
                            case NULL:
                                predicate = (builder.isNull(expression));
                                break;
                            case NNULL:
                                predicate = (builder.isNotNull(expression));
                                break;
                            case EPT:
                                predicate = (builder.isEmpty(expression));
                                break;
                            case NEPT:
                                predicate = (builder.isNotEmpty(expression));
                                break;
                            case IN:
                                if(filter.type.equals(SearchFilter.Type.S)) {
                                    predicate = (expression.in(filter.value.toString().split(",")));
                                }else if(filter.type.equals(SearchFilter.Type.N)){
                                    String[] values = filter.value.toString().split(",");
                                    Integer[] iValues = new Integer[values.length];
                                    for (int i = 0; i < values.length; i++) {
                                        iValues[i] = Integer.valueOf(values[i]);
                                    }
                                    predicate = (expression.in(iValues));
                                }
                                break;
                            case GBY:
                                query.groupBy(expression);
                                break;
                        }

                        if(predicate != null){

                            if(!"".equals(lastUnion) && !filter.union.equals(lastUnion)) {
                                if (lastUnion.equals("OR")) {
                                    predicates.add(builder.and(builder.or(predicatesInner.toArray(new Predicate[predicatesInner.size()]))));
                                } else if (lastUnion.equals("ADD")) {
                                    predicates.add(builder.and(predicatesInner.toArray(new Predicate[predicatesInner.size()])));
                                }

                                predicatesInner = Lists.newArrayList();
                                predicatesInner.add(predicate);
                                lastUnion = filter.union;

                                //多条件时，最后一个条件
                                if (!"".equals(lastUnion) && totalItems == index) {
                                    if (lastUnion.equals("OR")) {
                                        predicates.add(builder.and(builder.or(predicatesInner.toArray(new Predicate[predicatesInner.size()]))));
                                    } else if (lastUnion.equals("ADD")) {
                                        predicates.add(builder.and(predicatesInner.toArray(new Predicate[predicatesInner.size()])));
                                    }
                                }
                            }else if("".equals(lastUnion) && totalItems == index) {
                                //只有一个条件
                                predicatesInner.add(predicate);
                                lastUnion = filter.union;
                                predicates.add(builder.and(predicatesInner.toArray(new Predicate[predicatesInner.size()])));

                            }else if(totalItems == index){
                                //最后一个条件
                                predicatesInner.add(predicate);
                                if (lastUnion.equals("OR")) {
                                    predicates.add(builder.and(builder.or(predicatesInner.toArray(new Predicate[predicatesInner.size()]))));
                                } else if (lastUnion.equals("ADD")) {
                                    predicates.add(builder.and(predicatesInner.toArray(new Predicate[predicatesInner.size()])));
                                }

                            }else{
                                predicatesInner.add(predicate);
                                lastUnion = filter.union;
                            }
                        }

                    }

                    if(predicates.size() >0 ) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }

                }

                return builder.conjunction();
            }
        };
    }
}
