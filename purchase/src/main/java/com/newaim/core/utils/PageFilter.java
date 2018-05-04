package com.newaim.core.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class PageFilter implements java.io.Serializable {

	private static final long serialVersionUID = 4567236814992823540L;
	
	private int page;// 当前页
	private int limit;// 每页显示记录数
	private int start;// 每页显示记录数
	private String sort;// 排序字段
	private String order;// asc/desc
	private int totalCounts; //查寻出的总记录条数
	private String[] filter;  //显示过滤字段
	private Map<String, String> condition = new HashMap<String, String>();  //特殊字段预设条件，即非数据表字段，如：统计字段
	
	private List<Map<String,String>> params = new ArrayList<Map<String, String>>();

	private String strSelect = "";
	private String strWhere = "";
	private String strOrderBy = "";
	private String strGroupBy = "";
	private String strLimit = "";
	private String entityName = "main";
	
	/**
	 * 默认解析参数
	 * @param request
	 */
	public PageFilter(HttpServletRequest request){
		this.setInit(this.entityName, request);
	}
	
	/**
	 * 增加额外过滤条件
	 * @param condition
	 * @param request
	 */
	public PageFilter(Map<String, String> condition, HttpServletRequest request){
		this.condition = condition;
		this.setInit(this.entityName, request);
	}
	
	/**
	 * 指定主表实体名
	 * @param entityName
	 * @param request
	 */
	public PageFilter(String entityName, HttpServletRequest request){
		this.entityName = entityName;
		this.setInit(this.entityName, request);
	}
	
	/**
	 * 指定主表实名及增加额外过滤参数
	 * @param entityName
	 * @param condition
	 * @param request
	 */
	public PageFilter(String entityName, Map<String, String> condition, HttpServletRequest request){
		this.condition = condition;
		this.entityName = entityName;
		this.setInit(this.entityName, request);
	}

	@SuppressWarnings("unchecked")
	private void setInit(String entityName, HttpServletRequest request) {
		
		Enumeration<String> paramEnu = request.getParameterNames();
		
		while (paramEnu.hasMoreElements()) {
			String paramName = (String) paramEnu.nextElement();
			//from 查询字段
			if (paramName.startsWith("Q-")) {
				String paramValue = (String) request.getParameter(paramName);
				addFilter(paramName, paramValue);
				
			//列出统计字段
			}else if (paramName.startsWith("OQ-")) {
				String paramValue = (String) request.getParameter(paramName);
				String[] fieldInfo = paramName.split("[-]");
				
				if(getCondition(fieldInfo[1], paramValue) != null && getCondition(fieldInfo[1], paramValue) != ""){
					if(this.strWhere != "") {
						this.strWhere += " AND ";
					}
					this.strWhere += getCondition(fieldInfo[1], paramValue)+ " "; 
				}
			}
		}
		
		//压入指定字段
		if(this.condition.size() > 0){
			for (Map.Entry<String, String> entry : this.condition.entrySet()) {
			    addFilter(entry.getKey(), entry.getValue());
			} 
		}
		
		if(request.getParameter("filter") != null){
			String strFilter = request.getParameter("filter");
			this.setFilter(strFilter.split(","));
		}
		
		if(request.getParameter("limit") != null){
			this.setLimit(Integer.parseInt(request.getParameter("limit")));
		}
		if(request.getParameter("start") != null){
			this.setStart(Integer.parseInt(request.getParameter("start")));
		}else{
			this.setStart(0);
		}
		
		if(request.getParameter("page") != null){
			this.setPage(Integer.parseInt(request.getParameter("page")));
		}else{
			this.setPage(1);
		}
		this.setLimit();
		
		if(request.getParameter("sort") != null){
			request.getParameter("sort");
			JsonObj jo = new JsonObj();
			List<?> param = jo.toList(request.getParameter("sort"));
			LinkedHashMap<String, String> strParam = (LinkedHashMap<String, String>) param.get(0);
			strParam.get("property");
			
			this.setSort(strParam.get("property"));
			this.setOrder(strParam.get("direction"));
			
			String strOrderBy = "";
			if(this.getSort().indexOf("_")>0 ){
				String[] names = this.getSort().split("[_]");
				if("dyn".equals(names[0])){
					strOrderBy = this.getSort();
				}else{
					strOrderBy = this.getSort().replace("_", ".");
				}
			}else{
				strOrderBy = this.entityName + "." + this.getSort();
			}
			
			this.setStrOrderBy( strOrderBy + " " + this.getOrder());
		}
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	@SuppressWarnings("rawtypes")
	public String getCondition(String key, String paramValue) {
		
		String strResult;
		strResult = condition.get(key).replaceAll(":"+key, paramValue);

		return strResult;
	}

	public void setCondition(String key, String value) {
		this.condition.put(key, value);
	}
	
	public void cleanCondistion(){
		this.condition.clear();
	}

	@SuppressWarnings("null")
	private void addFilter(String paramName, String paramValue) {
		Map<String,String> map = null;
		String[] fieldInfo = paramName.split("[-]");
		if (fieldInfo != null && fieldInfo.length == 4) {
			
			if(this.strWhere != "") {
				this.strWhere += " AND ";
			}
			
			this.strWhere += FieldCmdUtil.getPartHql( this.entityName, fieldInfo[3], fieldInfo[1], paramValue);
			
		} else if (fieldInfo != null && fieldInfo.length == 3) {
			map.put(paramName, paramValue);
			params.add(map);
			
			if(this.strWhere != "") {
				this.strWhere += " AND ";
			}
			
			this.strWhere += FieldCmdUtil.getPartHql( this.entityName, fieldInfo[3], fieldInfo[1], paramValue);
			
		}

	}
	
	/**
	 * get where string
	 * @param wantWhere
	 * @return
	 */
	public String getStrWhere(Boolean wantWhere) {
		if(this.strWhere != null && this.strWhere != ""){
			if(wantWhere == true){
				return " WHERE " + this.strWhere;
			}else{
				return " " + this.strWhere;
			}
		}else{
			return "";
		}
	}

	public PageFilter setStrWhere(String strWhere, Boolean emptyOldStr) {
		if(emptyOldStr.equals(false) && this.strWhere != null && this.strWhere != ""){
			this.strWhere = " (" + this.strWhere + ") AND " + strWhere;
		}else{
			this.strWhere = strWhere;
		}
		
		return this;
	}

	public String addStrWhere(String strWhere, String options){
		if(this.strWhere.equals("")){
			this.strWhere = strWhere;
		}else{
			this.strWhere += " " + options + " " + strWhere;
		}
		return this.strWhere;
	}
	
	public String getStrOrderBy() {
		if(this.strOrderBy != null && this.strOrderBy != ""){
			return " ORDER BY " + strOrderBy;
		}else{
			return "";
		}
	}

	public PageFilter setStrOrderBy(String strOrderBy) {
		this.strOrderBy = strOrderBy;
		
		return this;
	}
	
	/**
	 * 
	 * @param strOrderBy
	 * @param inBefore  是否插入在第一位 
	 * @return 
	 */
	public PageFilter addStrOrderBy(String strOrderBy, Boolean inBefore){
		if(this.strOrderBy != null && this.strOrderBy !=""){
			if(inBefore){
				this.strOrderBy = " " + strOrderBy + ", "+ this.strOrderBy;
			}else{
				this.strOrderBy += ", " + strOrderBy + " ";
			}
		}else{
			this.strOrderBy = " " + strOrderBy + " ";
		}
		return this;
	}

	public String getStrGroupBy() {
		if(this.strGroupBy != null){
			return strGroupBy;
		}else{
			return "";
		}
	}

	public PageFilter setStrGroupBy(String strGroupBy) {
		this.strGroupBy = strGroupBy;
		
		return this;
	}

	public String getStrLimit() {
		if(this.strLimit != null){
			return strLimit;
		}else{
			return "";
		}
	}

	public PageFilter setLimit(Integer limit){
		if(limit>0){
			this.strLimit += " limit " + this.getStart() +","+ this.getLimit();
		}else if(this.getLimit() >0 ){
			this.strLimit += " limit " + this.getStart() +","+ this.getLimit();
		}
		return this;
	}
	
	public PageFilter setLimit(){
		if(this.getLimit() >0 ){
			this.strLimit += " limit " + this.getStart() +","+ this.getLimit();
		}
		return this;
	}

	public int getTotalCounts() {
		return totalCounts;
	}
	
	public String getStrSelect() {
		if(strSelect.length() > 0){
			return strSelect;
		}else{
			if(this.filter != null && this.filter.length>0){
				for(int i=0; i<this.filter.length; i++){
					if(this.filter[i].indexOf(".")>0){
						String[] names = filter[i].split("[.]");
						if("dyn".equals(names[0])){
							//TODO 当前仅能在 list 的SQL中写入，后期应该完善成自动
						}else{
							this.strSelect += ", " + this.filter[i] + " as '" + this.filter[i].replace(".", "_") + "'";
						}
					}else{
						this.strSelect += ", " + this.entityName + "."+this.filter[i];
					}
				}
				this.strSelect = this.strSelect.substring(1);
				
				return this.strSelect;
			}else{
				return " * ";
			}
		}
	}

	public PageFilter setStrSelect(String strSelect) {
		this.strSelect = strSelect;
		
		return this;
	}

	public String[] getFilter() {
		return filter;
	}

	public PageFilter setFilter(String[] filter) {
		this.filter = filter;
		
		return this;
	}
	
}
