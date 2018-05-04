package com.newaim.core.utils.mail;

import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.InternetAddress;

/**
 * 邮箱地址集类
 */
public class InternetEmailAddress {

    protected InternetAddress[] address = new InternetAddress[]{};

    public InternetAddress[] getAddress() {
        return address;
    }

    public void setAddress(InternetAddress[] address) {
        this.address = address;
    }

    /**
     * 给地址赋值
     * @param address
     */
    public InternetEmailAddress(EmailAddress address) {
        try {
            this.address = new InternetAddress[]{address.toInternetAddress()};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 给地址赋值
     * @param address
     */
    public InternetEmailAddress(String address, String name) {
        try {
            this.address = new InternetAddress[]{new EmailAddress(address, name).toInternetAddress()};
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拆分字符串联系人，　将name:email拆分
     * @param strNameAndAddress     String  "name:email,name:email,..."
     */
    public InternetEmailAddress(String strNameAndAddress){
        if(StringUtils.isNotBlank(strNameAndAddress)){
            String[] arr = strNameAndAddress.split(",");// 拆分名字和地址
            try {
                if(arr.length>1){
                    this.address = new InternetAddress[arr.length];

                    for (int i=0; i< arr.length; i++){
                        this.address[i] =  new EmailAddress(arr[i]).toInternetAddress();
                    }
                }else{
                    this.address =  new InternetAddress[]{new EmailAddress(arr[0]).toInternetAddress()};
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
