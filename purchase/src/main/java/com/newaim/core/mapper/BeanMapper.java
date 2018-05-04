package com.newaim.core.mapper;

import com.google.common.collect.Maps;
import com.newaim.core.utils.DateFormatUtil;
import com.newaim.purchase.archives.product.vo.ProductVendorPropVo;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单封装orika, 实现深度的BeanOfClasssA<->BeanOfClassB复制
 */
public class BeanMapper {

    private static MapperFacade mapper;

    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * 简单的复制出新类型对象.
     *
     * 通过source.getClass() 获得源Class
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 极致性能的复制出新类型对象.
     *
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> D map(S source, Type<S> sourceType, Type<D> destinationType) {
        return mapper.map(source, sourceType, destinationType);
    }

    /**
     * 简单的复制出新对象列表到ArrayList
     *
     * 不建议使用mapper.mapAsList(Iterable<S>,Class<D>)接口, sourceClass需要反射，实在有点慢
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
        return mapper.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    /**
     * 极致性能的复制出新类型对象到ArrayList.
     *
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return mapper.mapAsList(sourceList, sourceType, destinationType);
    }

    /**
     * 简单复制出新对象列表到数组
     *
     * 通过source.getComponentType() 获得源Class
     */
    public static <S, D> D[] mapArray(final D[] destination, final S[] source, final Class<D> destinationClass) {
        return mapper.mapAsArray(destination, source, destinationClass);
    }

    /**
     * 极致性能的复制出新类型对象到数组
     *
     * 预先通过BeanMapper.getType() 静态获取并缓存Type类型，在此处传入
     */
    public static <S, D> D[] mapArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return mapper.mapAsArray(destination, source, sourceType, destinationType);
    }

    /**
     * 预先获取orika转换所需要的Type，避免每次转换.
     */
    public static <E> Type<E> getType(final Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }
    
    
    
    
    
    //=================================================================
    
    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map toMap(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
    	
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else if("".equals(result)) {
                    returnMap.put(propertyName, "");
                } else{
                	returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }

    /**
     * 转换成模板变量
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map toMapForVar(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {

        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(("${"+ propertyName + "}").toString(), result);
                } else if("".equals(result)) {
                    returnMap.put(("${"+ propertyName + "}").toString(), "");
                } else{
                    returnMap.put(("${"+ propertyName + "}").toString(), null);
                }
            }
        }
        return returnMap;
    }
    
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @param index 第几行
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object toBean(Class type, Map map, int index) throws IntrospectionException, IllegalAccessException,
    InstantiationException, InvocationTargetException {

    	return toBean(type, map, false, index);

    }
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InstantiationException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
	public static Object toBean(Class type, Map map) throws IntrospectionException, IllegalAccessException,
	InstantiationException, InvocationTargetException {
    	
    	return toBean(type, map, false, 0);
    	
    }
    
    /**
     * 
     * @param type
     * @param map
     * @param ignoreNull
     * @param index
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    @SuppressWarnings({ "rawtypes" })
	public static Object toBean(Class type, Map map, Boolean ignoreNull, int index) throws IntrospectionException, IllegalAccessException,
		InstantiationException, InvocationTargetException {
    	
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            String PropertyType = propertyDescriptors[i].getPropertyType().toString();

            if (map.containsKey(propertyName) || propertyName.equals("dyn_num") ) {
        		Object value = null;
        		try{ 
        			if(propertyName.equals("dyn_num")){
        				value = (index + 1);
        			}else if(map.get(propertyName) != null && !"".equals(map.get(propertyName))){
	        			if(PropertyType.equals("class java.lang.Integer")) {
							value = Integer.parseInt(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.String")) {
							value = map.get(propertyName);
						} else if(PropertyType.equals("class java.lang.Long")) {
							value = Long.parseLong(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.Boolean")) {
							value = Boolean.parseBoolean(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.Float")) {
							value = Float.parseFloat(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.util.Date")) {
                            try {
                                System.out.print(map.get(propertyName).toString());
                                if (map.get(propertyName) instanceof java.util.Date) {
                                    value = map.get(propertyName);
                                } else {
                                    value = DateFormatUtil.parseDateTime(map.get(propertyName).toString());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
							value = map.get(propertyName);
						}
	        			
	        		}else if("".equals(map.get(propertyName))){
	        			if(PropertyType.equals("class java.lang.Integer")) {
							value = 0;
						} else if(PropertyType.equals("class java.lang.String")) {
							value = null;
						} else if(PropertyType.equals("class java.lang.Long")) {
							value = 0L;
						} else if(PropertyType.equals("class java.lang.Boolean")) {
							value = false;
						} else if(PropertyType.equals("class java.lang.Float")) {
							value = 0F;
						} else if(PropertyType.equals("class java.util.Date")) {
							value = null;
						} else {
							value = null;
						}
	        		}
        		
	            	descriptor.getWriteMethod().invoke(obj, value);
            	}catch(Exception ex){
            		
            	}
            	 
            }
        }
        
        return obj;
    }
    
    @SuppressWarnings("rawtypes")
	public static Object copyProperties(Object source, Object target, Boolean ignoreNull){
    	
    	try {
    		
			Map map = BeanMapper.toMap(source);
			
			BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass()); // 获取类属性
    		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
    		
    		for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                String PropertyType = propertyDescriptors[i].getPropertyType().toString();

                if (map.containsKey(propertyName) ) {
            		Object value = null;
            		
            		if(map.get(propertyName) != null && !"".equals(map.get(propertyName))){
            			
            			if(PropertyType.equals("class java.lang.Integer")) {
							value = Integer.parseInt(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.String")) {
							value = map.get(propertyName);
						} else if(PropertyType.equals("class java.lang.Long")) {
							value = Long.parseLong(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.Boolean")) {
							value = Boolean.parseBoolean(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.lang.Float")) {
							value = Float.parseFloat(map.get(propertyName).toString());
						} else if(PropertyType.equals("class java.util.Date"))
//	            			try {
//								value = DateFormatUtil.parseDateTime(map.get(propertyName).toString());
//							} catch (ParseException e) {
//								e.printStackTrace();
//							}
						{
							value = map.get(propertyName);
						} else {
							value = map.get(propertyName);
						}
            			
            		}else if("".equals(map.get(propertyName))){
            			
            			if(PropertyType.equals("class java.lang.Integer")) {
							value = 0;
						} else if(PropertyType.equals("class java.lang.String")) {
							value = null;
						} else if(PropertyType.equals("class java.lang.Long")) {
							value = 0L;
						} else if(PropertyType.equals("class java.lang.Boolean")) {
							value = false;
						} else if(PropertyType.equals("class java.lang.Float")) {
							value = 0F;
						} else if(PropertyType.equals("class java.util.Date")) {
							value = null;
						} else {
							value = null;
						}
            		}

	            	if(ignoreNull==false || value != null){
	            		descriptor.getWriteMethod().invoke(target, value);
	            	}

                }
            }
		} catch (Exception e) {
			
		}
    	
    	return target;
    }

    /**
     * @TODO　未完善
     * 对象比较并且获取差异属性Map
     * @param obj1
     * @param obj2
     * @return
     */
    public static Map<String, Object> compare(Object obj1, Object obj2) {
	    Map<String, Object> result = Maps.newHashMap();

        try {
            BeanInfo obj1Class = Introspector.getBeanInfo(obj1.getClass()); // 获取类属性
            BeanInfo obj2Class = Introspector.getBeanInfo(obj2.getClass()); // 获取类属性
            Map obj1Map = BeanMapper.toMap(obj1);
            Map obj2Map = BeanMapper.toMap(obj2);


        } catch (IntrospectionException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }
}
