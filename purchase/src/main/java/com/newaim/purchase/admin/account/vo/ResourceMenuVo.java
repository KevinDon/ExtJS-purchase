package com.newaim.purchase.admin.account.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newaim.core.utils.SessionUtils;

import java.io.Serializable;
import java.util.List;


public class ResourceMenuVo implements Serializable{
	private String id;

	private String parentId;
		
	private String title;
	
	private String text;
	
	private String iconCls;
	
	@JsonIgnore
	private String cnName;
	
	@JsonIgnore
	private String enName;
	
	private Integer leaf;
	
	private Integer level;
	
	private Integer type;
	
	private String url;
	
	private Integer sort;
	
	@JsonIgnore
	private String itemIcon;
	
	
	private List<ResourceMenuVo> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCnName() {
		return cnName;
	}
	
	public String getTitle() {
		return title;
	}


	public String getText() {
		return text;
	}

	public void setCnName(String cnName) {
		UserVo user = SessionUtils.currentUserVo();
		
		if(user.getLang().equals("zh_CN")){
			title = cnName;
			text = cnName;
		}else if(user.getLang().equals("en_AU")){
			title = enName;
			text = enName;
		}
		
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		UserVo user = SessionUtils.currentUserVo();
		
		if(user.getLang().equals("zh_CN")){
			title = cnName;
			text = cnName;
		}else if(user.getLang().equals("en_AU")){
			title = enName;
			text = enName;
		}
		this.enName = enName;
	}

	public Integer getLeaf() {
		return leaf;
	}

	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getItemIcon() {
		return itemIcon;
	}

	public void setItemIcon(String itemIcon) {
		this.setIconCls(itemIcon);
		this.itemIcon = itemIcon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<ResourceMenuVo> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceMenuVo> children) {
		this.children = children;
	}

	
}
