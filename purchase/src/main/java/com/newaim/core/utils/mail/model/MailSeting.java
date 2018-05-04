package com.newaim.core.utils.mail.model;

import com.newaim.purchase.desktop.email.entity.EmailSetting;

import java.io.Serializable;

public class MailSeting implements Serializable {

    // 邮件服务器的IP、端口
    protected String sendHost;
    protected String sendPort;

    // 邮件接收服务器的IP、端口和协议
    protected String receiveHost;
    protected String receivePort;
    protected String protocal;

    // 是否是SSL
    protected Boolean SSL = false;

    // 是否需要身份验证
    protected Boolean validate = true;

    // 登陆邮件服务器的用户名和密码
    protected String mailAddress;
    protected String password;

    // 用户昵称
    protected String nickName;

    // 是否收取附件
    protected Boolean isHandleAttach = true;

    // 是否删除服务器上的邮件
    protected Boolean isDeleteRemote = false;

    public final static String SMTP_PROTOCAL = "smtp";
    public final static String POP3_PROTOCAL = "pop3";
    public final static String IMAP_PROTOCAL = "imap";

    public MailSeting(){}

    /**
     * 初始化邮件发送服务器
     * @param es
     */
    public MailSeting(EmailSetting es){
        if(es.getType() == 1) {
            this.setProtocal(MailSeting.SMTP_PROTOCAL);
            this.setSendHost(es.getServiceSmtp());
            this.setSendPort(es.getServiceSmtpPort());
            this.setMailAddress(es.getSmtpAccount());
            this.setPassword(es.getSmtpPassword());
            this.setSSL(es.getServiceSmtpSsl() == 1);
            this.setValidate(true);
            this.setNickName(es.getSendName());
        }else if(es.getType() == 2) {
            this.setProtocal(MailSeting.IMAP_PROTOCAL);
        }
    }

    public MailSeting(EmailSetting es, String receive){
        if(es.getType() == 1) {
            this.setProtocal(MailSeting.POP3_PROTOCAL);
            this.setReceiveHost(es.getServicePop());
            this.setReceivePort(es.getServicePopPort());
            this.setMailAddress(es.getPopAccount());
            this.setPassword(es.getPopPassword());
            this.setSSL(es.getServicePopSsl() == 1);
            this.setValidate(true);
            this.setIsDeleteRemote(false);
        }else if(es.getType() == 2) {
            this.setProtocal(MailSeting.IMAP_PROTOCAL);
        }
    }

    public Boolean getSSL() {
        return SSL;
    }

    public void setSSL(Boolean SSL) {
        this.SSL = SSL;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getReceiveHost() {
        return receiveHost.trim();
    }

    public void setReceiveHost(String receiveHost) {
        this.receiveHost = receiveHost;
    }

    public String getReceivePort() {
        return receivePort.trim();
    }

    public void setReceivePort(String receivePort) {
        this.receivePort = receivePort;
    }

    public Boolean getIsHandleAttach() {
        return isHandleAttach;
    }

    public void setIsHandleAttach(Boolean isHandleAttach) {
        this.isHandleAttach = isHandleAttach;
    }

    public String getSendHost() {
        return sendHost.trim();
    }

    public void setSendHost(String sendHost) {
        this.sendHost = sendHost;
    }

    public String getSendPort() {
        return sendPort.trim();
    }

    public void setSendPort(String sendPort) {
        this.sendPort = sendPort;
    }

    public Boolean getIsDeleteRemote() {
        return isDeleteRemote;
    }

    public void setIsDeleteRemote(Boolean isDeleteRemote) {
        this.isDeleteRemote = isDeleteRemote;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}