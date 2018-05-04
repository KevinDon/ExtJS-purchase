package com.newaim.purchase.admin.system.vo;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;

import java.io.Serializable;
import java.util.List;

public class DictionaryValueVo implements Serializable{

	private String id;
	
	private String dictId;
	
	private String value;
	
	private Integer sort;
	
	private Integer status;
	
	private Integer custom;
	
	private Integer isDefault;

    private String param1;

    private String param2;

	private List<DictionaryValueDescVo> desc = Lists.newArrayList();
		
	private String title;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public String getTitle() {
		UserVo user = SessionUtils.currentUserVo();
		
		if(desc.size()>0){
			for(int i=0; i<desc.size(); i++){
				if(desc.get(i).getLang().equals(user.getLang())){
					title = desc.get(i).getText();
				}
			}
		}
		return title;
	}

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public List<DictionaryValueDescVo> getDesc() {
		return desc;
	}

	public void setDesc(List<DictionaryValueDescVo> desc) {
		this.desc = desc;
	}

}
