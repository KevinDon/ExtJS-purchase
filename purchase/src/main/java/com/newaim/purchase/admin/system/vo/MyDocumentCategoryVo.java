package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;

public class MyDocumentCategoryVo implements Serializable {

	private String id;
	
	private String title;
	
	private String path;
	
	private Integer status;
	
	private Integer sort;

	private List<MyDocumentCategoryDescVo> desc = Lists.newArrayList();

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
					title = desc.get(i).getTitle();
				}
			}
		}
		return title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MyDocumentCategoryDescVo> getDesc() {
		return desc;
	}

	public void setDesc(List<MyDocumentCategoryDescVo> desc) {
		this.desc = desc;
	}
}
