package com.newaim.core.utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SuppressWarnings("serial") 

public class JsonObj extends ObjectMapper{

	private boolean success = false;
	private String msg = "";
	private Object data = null;	
	private Integer tc=0;

	public JsonObj(){
		setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
	
	public JsonObj(boolean forceLazyLoading) {
		Hibernate5Module mod = new Hibernate5Module();
		mod.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, forceLazyLoading);
		registerModule(mod);
	}
	
	/**
	 * 带时间格式化 的构造方法
	 * @param dateFormat  格式如 yyyy-MM-dd
	 * 
	 */
	public JsonObj(String dateFormat) {
		setDateFormat(new SimpleDateFormat(dateFormat));
	}

	/**
	 * 构造方法
	 * 
	 * @param forceLazyLoading  是否懒加载 true 为懒加载，否则false
	 * @param dateFormat 格式如： yyyy-MM-dd
	 */
	public JsonObj(boolean forceLazyLoading, String dateFormat) {
		this(forceLazyLoading);
		setDateFormat(new SimpleDateFormat(dateFormat));
	}
	
	
	/**
	 * 把Object转化为Json字符串
	 * 
	 * @param object
	 *            can be pojo entity,list,map etc.
	 */
	public String toJson(Object object) {
		try {
			return this.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析对象错误");
		}
	}

	/**
	 * Json转List
	 * 
	 * @param json
	 *            json字符串
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> toList(String json) {
		try {
			return this.readValue(json, List.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}
	
	public <T>T toBeanObject(String content, Class<T> valueType) {
		try {
			return this.readValue(content, valueType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}

	/**
	 * json转换为java对象
	 * 
	 * @param json
	 *            字符串
	 * @param clazz
	 *            对象的class
	 * @return 返回对象
	 */
	public <T> T toObject(String json, Class<T> clazz) {
		try {
			return this.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析json错误");
		}
	}

	/**
	 * 返回分页列表的Json格式
	 * 
	 * @param list
	 * @param totalCounts
	 * @return
	 */
	public String toPageJson(List<?> list, Integer totalCounts) {
		StringBuffer sb = new StringBuffer("{success:"+this.getSuccess()+",totalCounts:")
				.append(totalCounts).append(",msg:\""+this.getMsg()+"\",")
				.append(",result:");
		
		sb.append(toJson(list));
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 返回分页列表的Json格式
	 * 
	 * @param list
	 * @param totalCounts
	 * @return
	 */
	public String toListJson(List<?> list) {
		StringBuffer sb = new StringBuffer("{success:"+this.getSuccess()+",totalCounts:")
				.append(list.size()).append(",msg:'"+this.getMsg()+"',")
				.append(",result:");
		sb.append(toJson(list));
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 返回带有结果的Json格式
	 * 
	 * @param object
	 *            can be pojo entity,list,map etc.
	 * @return {success:true,data:['...']}
	 */
	public String toDataJson(Object object) {
		StringBuffer sb = new StringBuffer("{success:"+this.getSuccess())
				.append(",msg:\""+this.getMsg()+"\",data:");
		
		sb.append(toJson(object));
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 返回带有结果的Json格式
	 * 
	 * @param object
	 *            can be pojo entity,list,map etc.
	 * @return {success:true,result:['...']}
	 */
	public String toResultJson(Object object) {
		StringBuffer sb = new StringBuffer("{success:"+this.getSuccess())
				.append(",msg:\""+this.getMsg()+"\",result:");
		
		sb.append(toJson(object));
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 返回数组
	 * @param list
	 * @return [[...],[...]]
	 */
	public String toStoreDataJson(List<?> list){
		StringBuffer sb = new StringBuffer();
		sb.append(toJson(list));
		return sb.toString();
	}
	
	
	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Integer getTotalCounts() {
		return tc;
	}

	public void setTotalCounts(Integer totalCounts) {
		this.tc = totalCounts;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.tc = data.size();
		this.data = data;
	}
	
}
