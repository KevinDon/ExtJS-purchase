package com.newaim.core.utils;

import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 *  统一结果变量
 */
public class RestResult implements Serializable {

	private static final long serialVersionUID = -8625820686486530414L;
	
	/**
	 * 成功
	 */
	public static final String CODE_SUCCESS = "00";
	
	/**
	 * 失败
	 */
	public static final String CODE_FAILURE = "01";

	private Boolean success = false;
	
	/**
	 * 执行结果是否成功,成功为true
	 */
	private String code;
	
	/**
	 * 执行结果提示
	 */
	private String msg;
	
	/**
	 * data 执行结果数据，如有数据可以写入
	 */
	private Object data;
	
	private String result;
	
	private Long tc = 0L;
	
	private Integer hasMore = 0;
	
	
	public RestResult(){
		this.setData(null);
		this.setMsg("");
		this.setTc(0);
		this.setCode("");
		this.setHasMore(0);
		this.setResult("");
	}

	public RestResult setPage(Page page){
		this.data = page.getContent();
		this.hasMore = page.hasNext() ? 1 : 0;
		this.tc = page.getTotalElements();
		return this;
	}

	public Boolean getSuccess() {
		return success;
	}

	public RestResult setSuccess(Boolean success) {
		this.success = success;
		this.code = success ? CODE_SUCCESS : CODE_FAILURE;
		return this;
	}

	public String getCode() {
		return code;
	}

	public RestResult setCode(String code) {
		this.code = code;
		
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public RestResult setMsg(String msg) {
		this.msg = msg;
		
		return this;
	}

	public Object getData() {
		return data;
	}

	public RestResult setData(Object data) {
		this.data = data;
		
		return this;
	}

	public Long getTc() {
		return tc;
	}

	public RestResult setTc(long l) {
		this.tc = l;
		return this;
	}

	public Integer getHasMore() {
		return hasMore;
	}

	public void setHasMore(Integer hasMore) {
		this.hasMore = hasMore;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
