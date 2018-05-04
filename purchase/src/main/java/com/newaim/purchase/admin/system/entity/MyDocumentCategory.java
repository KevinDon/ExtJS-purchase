package com.newaim.purchase.admin.system.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.google.common.collect.Lists;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="na_files_category")
public class MyDocumentCategory implements Serializable {
	
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
			parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FC")})
	private String id;

	private Integer status;
	
	private Integer sort;
	
	private String path;

    @Transient
	private String title;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private List<MyDocumentCategoryDesc> desc = Lists.newArrayList();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<MyDocumentCategoryDesc> getDesc() {
		return desc;
	}

	public void setDesc(List<MyDocumentCategoryDesc> desc) {
		this.desc = desc;
	}

}
