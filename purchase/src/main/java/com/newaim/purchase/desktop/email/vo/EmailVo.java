package com.newaim.purchase.desktop.email.vo;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.system.vo.AttachmentVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmailVo implements Serializable{

	private String id;
	private String sendName;
	private String sendEmail;
	private String recipientName;
	private String recipientEmail;
	private String ccName;
	private String ccEmail;
	private String bccName;
	private String bccEmail;
	private String title;
	private String content;
	private Integer isHtml;
	private Integer isRead;
	private Integer isReplied;
	private Integer attachmentCount;
	private Integer boxType;     //邮箱类型：０草稿箱、１收件箱、２发件箱、３垃圾箱
	private String emailTemplateId;
	private Date sendTime;
	private Date recipientTime;
	private String emailSettingId;
	private String emailSettingName;
	private String emailSettingEmail;

	private Integer onlyHead;
	private Integer color;
	private String uid;

	private Integer status;
	private Date createdAt;
	private Date updatedAt;
	private String creatorId;
	private String creatorCnName;
	private String creatorEnName;
	private String departmentId;
	private String departmentCnName;
	private String departmentEnName;

	private EmailSettingMiniVo emailSetting;

	//其它附件
	private List<AttachmentVo> attachments = Lists.newArrayList();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getBccName() {
		return bccName;
	}

	public void setBccName(String bccName) {
		this.bccName = bccName;
	}

	public String getBccEmail() {
		return bccEmail;
	}

	public void setBccEmail(String bccEmail) {
		this.bccEmail = bccEmail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Integer isHtml) {
		this.isHtml = isHtml;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Integer getIsReplied() {
		return isReplied;
	}

	public void setIsReplied(Integer isReplied) {
		this.isReplied = isReplied;
	}

	public Integer getAttachmentCount() {
		this.attachmentCount = this.attachments.size();
		return this.attachmentCount;
	}

	public void setAttachmentCount(Integer attachmentCount) {
		this.attachmentCount = attachmentCount;
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

	public String getEmailSettingName() {
		return emailSettingName;
	}

	public void setEmailSettingName(String emailSettingName) {
		this.emailSettingName = emailSettingName;
	}

	public String getEmailSettingEmail() {
		return emailSettingEmail;
	}

	public void setEmailSettingEmail(String emailSettingEmail) {
		this.emailSettingEmail = emailSettingEmail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOnlyHead() {
		return onlyHead;
	}

	public void setOnlyHead(Integer onlyHead) {
		this.onlyHead = onlyHead;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getRecipientTime() {
		return recipientTime;
	}

	public void setRecipientTime(Date recipientTime) {
		this.recipientTime = recipientTime;
	}

	public String getEmailTemplateId() {
		return emailTemplateId;
	}

	public void setEmailTemplateId(String emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
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

    public EmailSettingMiniVo getEmailSetting() {
        return emailSetting;
    }

    public void setEmailSetting(EmailSettingMiniVo emailSetting) {
        this.emailSetting = emailSetting;
    }

    public List<AttachmentVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentVo> attachments) {
		this.attachments = attachments;
		this.attachmentCount = attachments.size();
	}

}
