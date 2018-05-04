package com.newaim.purchase.desktop.email.vo;

import java.io.Serializable;
import java.util.Date;

public class EmailSettingVo implements Serializable{

	private String id;
	private String title;
	private String email;
	private Integer shared;   //共享范围，数据字典值
	private Integer onlyHead;      //仅下载邮件头

	private String servicePop;
	private String servicePopPort;
	private Integer servicePopSsl;
	private String popAccount;
	private String popPassword;

	private String serviceSmtp;
	private String serviceSmtpPort;
	private Integer serviceSmtpSsl;
	private String smtpAccount;
	private String smtpPassword;
	private Integer serviceSmtpValidate;

	private String serviceImap;
	private String serviceImapPort;
	private Integer serviceImapSsl;
	private String imapAccount;
	private String imapPassword;

	private Integer type;   //邮箱类型:1: POP3、SMTP，2:IMAP
	private Integer isDefault;   //默认发件箱
	private String sendName;
	private String signature;

	private Integer sort;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getShared() {
		return shared;
	}

	public void setShared(Integer shared) {
		this.shared = shared;
	}

	public Integer getOnlyHead() {
		return onlyHead;
	}

	public void setOnlyHead(Integer onlyHead) {
		this.onlyHead = onlyHead;
	}

	public String getServicePop() {
		return servicePop;
	}

	public void setServicePop(String servicePop) {
		this.servicePop = servicePop;
	}

	public String getServicePopPort() {
		return servicePopPort;
	}

	public void setServicePopPort(String servicePopPort) {
		this.servicePopPort = servicePopPort;
	}

	public Integer getServicePopSsl() {
		return servicePopSsl;
	}

	public void setServicePopSsl(Integer servicePopSsl) {
		this.servicePopSsl = servicePopSsl;
	}

	public String getPopAccount() {
		return popAccount;
	}

	public void setPopAccount(String popAccount) {
		this.popAccount = popAccount;
	}

	public String getPopPassword() {
		return popPassword;
	}

	public void setPopPassword(String popPassword) {
		this.popPassword = popPassword;
	}

	public String getServiceSmtp() {
		return serviceSmtp;
	}

	public void setServiceSmtp(String serviceSmtp) {
		this.serviceSmtp = serviceSmtp;
	}

	public String getServiceSmtpPort() {
		return serviceSmtpPort;
	}

	public void setServiceSmtpPort(String serviceSmtpPort) {
		this.serviceSmtpPort = serviceSmtpPort;
	}

	public Integer getServiceSmtpSsl() {
		return serviceSmtpSsl;
	}

	public void setServiceSmtpSsl(Integer serviceSmtpSsl) {
		this.serviceSmtpSsl = serviceSmtpSsl;
	}

	public String getSmtpAccount() {
		return smtpAccount;
	}

	public void setSmtpAccount(String smtpAccount) {
		this.smtpAccount = smtpAccount;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public Integer getServiceSmtpValidate() {
		return serviceSmtpValidate;
	}

	public void setServiceSmtpValidate(Integer serviceSmtpValidate) {
		this.serviceSmtpValidate = serviceSmtpValidate;
	}

	public String getServiceImap() {
		return serviceImap;
	}

	public void setServiceImap(String serviceImap) {
		this.serviceImap = serviceImap;
	}

	public String getServiceImapPort() {
		return serviceImapPort;
	}

	public void setServiceImapPort(String serviceImapPort) {
		this.serviceImapPort = serviceImapPort;
	}

	public Integer getServiceImapSsl() {
		return serviceImapSsl;
	}

	public void setServiceImapSsl(Integer serviceImapSsl) {
		this.serviceImapSsl = serviceImapSsl;
	}

	public String getImapAccount() {
		return imapAccount;
	}

	public void setImapAccount(String imapAccount) {
		this.imapAccount = imapAccount;
	}

	public String getImapPassword() {
		return imapPassword;
	}

	public void setImapPassword(String imapPassword) {
		this.imapPassword = imapPassword;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
