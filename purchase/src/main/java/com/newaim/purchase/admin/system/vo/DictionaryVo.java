package com.newaim.purchase.admin.system.vo;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;

import java.io.Serializable;
import java.util.List;

public class DictionaryVo implements Serializable{

	private String id;
	
	private String title;
	
	private String codeMain;
	
	private String codeSub;
	
	private String categoryId;
	
	private String categoryName;
	
	private Integer sort;
	
	private Integer status;
	
	private Integer custom;
	
	private Integer type;

	private List<DictionaryDescVo> desc = Lists.newArrayList();
	
	private List<DictionaryCategoryDescVo> category = Lists.newArrayList();
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		UserVo user = SessionUtils.currentUserVo();
		
		if(desc.size()>0){
			for(int i=0; i<desc.size(); i++){
				if(desc.get(i).getLang().equals(user.getLang())){
					title = desc.get(i).getName();
				}
			}
		}
		return title;
	}
	
	public String getCodeMain() {
		return codeMain;
	}

	public void setCodeMain(String codeMain) {
		this.codeMain = codeMain;
	}

	public String getCodeSub() {
		return codeSub;
	}

	public void setCodeSub(String codeSub) {
		this.codeSub = codeSub;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCustom() {
		return custom;
	}

	public void setCustom(Integer custom) {
		this.custom = custom;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<DictionaryDescVo> getDesc() {
		return desc;
	}

	public void setDesc(List<DictionaryDescVo> desc) {
		this.desc = desc;
	}

	public List<DictionaryCategoryDescVo> getCategory() {
		return category;
	}

	public void setCategory(List<DictionaryCategoryDescVo> category) {
		this.category = category;
	}

}
