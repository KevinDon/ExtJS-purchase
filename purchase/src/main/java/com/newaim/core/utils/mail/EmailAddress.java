package com.newaim.core.utils.mail;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

/**
 * 邮箱地址类
 */
public class EmailAddress {
    /**
     * 地址
     */
    protected String address;
    /**
     * 名称
     */
    protected String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 给地址赋值
     * @param address
     * @param name
     */
    public EmailAddress(String address, String name) {
        this.address = address;
        this.name = name;
    }

    /**
     * 拆分字符串联系人，　将name:email拆分
     * @param nameAndAddress     String  "name:email"
     */
    public EmailAddress(String nameAndAddress){
        if(StringUtils.isNotBlank(nameAndAddress)){
            String[] arr = nameAndAddress.split(":");// 拆分名字和地址
            if(arr.length>1){
                this.name = arr[0].trim();
                this.address = arr[1].trim();
            }else{
                this.address = arr[0].trim();
            }
        }
    }

    public InternetAddress toInternetAddress() throws Exception {
        if (name != null && !name.trim().equals("")) {
            return new InternetAddress(address, MimeUtility.encodeWord(name,"utf-8", "Q"));
        }else if(StringUtils.isNotBlank(address)){
            return new InternetAddress(address);
        }
        return null;
    }
}
