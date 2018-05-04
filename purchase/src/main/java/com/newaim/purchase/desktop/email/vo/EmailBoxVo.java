package com.newaim.purchase.desktop.email.vo;

import java.io.Serializable;
import java.util.Date;

public class EmailBoxVo implements Serializable{

	private String id;
	private String cnName;
	private String enName;
	private Integer number;
	private Integer noRead;
	private Integer boxType;     //邮箱类型：０草稿箱、１收件箱、２发件箱、３垃圾箱
	private String emailSettingId;
	private String lastUid;         //最后下载的UID

	private Integer status;
	private Date createdAt;
	private Date updatedAt;
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNoRead() {
		return noRead;
	}

	public void setNoRead(Integer noRead) {
		this.noRead = noRead;
	}

	public Integer getBoxType() {
		return boxType;
	}

	public void setBoxType(Integer boxType) {
		this.boxType = boxType;
	}

	public String getEmailSettingId() {
		return emailSettingId;
	}

	public void setEmailSettingId(String emailSettingId) {
		this.emailSettingId = emailSettingId;
	}

	public String getLastUid() {
		return lastUid;
	}

	public void setLastUid(String lastUid) {
		this.lastUid = lastUid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorCnName() {
		return creatorCnName;
	}

	public void setCreatorCnName(String creatorCnName) {
		this.creatorCnName = creatorCnName;
	}

	public String getCreatorEnName() {
		return creatorEnName;
	}

	public void setCreatorEnName(String creatorEnName) {
		this.creatorEnName = creatorEnName;
	}

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }
}
