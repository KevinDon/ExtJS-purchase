package com.newaim.core.utils.mail.model;

import com.newaim.core.utils.mail.EmailAddress;
import com.newaim.purchase.desktop.email.entity.Email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mail implements Serializable{
	/**
	 * 邮件唯一ID
	 */
	protected String messageId ;
	/**
	 * 发件人显示名
	 */
	protected String senderName ;
	/**
	 * 发件人地址
	 */
	protected String senderAddress;
	/**
	 * 邮件主题
	 */
	protected String subject;
	/**
	 * 邮件内容
	 */
	protected String content;
	/**
	 * 发送时间
	 */
	protected Date sendDate ;
	/**
	 * 收件人显示名
	 */
	protected String receiverName ;
	/**
	 * 收件人地址
	 */
	protected String receiverAddresses;
	/**
	 * 抄送人地址
     * 格式：name:email,name:email
	 */
	protected String copyToAddresses;
	/**
	 * 暗送人地址
     * 格式：name:email,name:email
	 */
	protected String bcCAddresses;
	/**
	 * 邮件附件
	 */
	protected List<MailAttachment> attachments = new ArrayList<MailAttachment>();

    /**
     * 邮件在远程服务器上排序位置
     */
	protected Integer uidNumber;

	public Mail(){}
	public Mail(Email o){

		EmailAddress to = new EmailAddress(o.getRecipientEmail());
        EmailAddress cc = new EmailAddress(o.getCcEmail());
        EmailAddress bcc = new EmailAddress(o.getBccEmail());

        this.setReceiverAddresses(to.getAddress());
        this.setCopyToAddresses(cc.getAddress());
        this.setBcCAddresses(bcc.getAddress());
		this.setReceiverName(to.getName());
		this.setSendDate(o.getSendTime());
		this.setSubject(o.getTitle());
		this.setContent(o.getContent());
		this.setSenderAddress(o.getSendEmail());
		this.setSenderName(o.getSendName());
	}
	
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBcCAddresses() {
		return bcCAddresses;
	}
	public void setBcCAddresses(String bcCAddresses) {
		this.bcCAddresses = bcCAddresses;
	}
	public List<MailAttachment> getMailAttachments() {
		return attachments;
	}
	public void setMailAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getCopyToAddresses() {
		return copyToAddresses;
	}
	public void setCopyToAddresses(String copyToAddresses) {
		this.copyToAddresses = copyToAddresses;
	}
	public String getReceiverAddresses() {
		return receiverAddresses;
	}
	public void setReceiverAddresses(String receiverAddresses) {
		this.receiverAddresses = receiverAddresses;
	}
	public String getUID() {
		return messageId;
	}
	public void setUID(String uID) {
		messageId = uID;
	}
	public Integer getUidNumber() {
		return uidNumber;
	}
	public void setUidNumber(Integer uidNumber) {
		this.uidNumber = uidNumber;
	}
}