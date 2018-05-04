package com.newaim.core.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 辅助类为发件人信息验证类
 */
public class MyAuthenticator extends Authenticator {
    private String username;
    private String password;

    /**
     * @param username
     * @param password
     */
    public MyAuthenticator(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}