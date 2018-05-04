package com.newaim.purchase.admin.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mark on 2017/8/10.
 */
@Entity
@Table(name = "na_account_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@Parameter(name = "prefix", value = "USER")})
    private String id;

    private String account;

    private String cnName;

    private String enName;

    private String password;

    private String salt;

    private String email;

    private String qq;

    private String skype;

    private String wechat;

    private Integer status;

    private Integer gender;

    private Integer setListRows;

    //手机号
    private String phone;

    //分机号
    private String extension;

    private String departmentId;

    private Date joinusAt;

    private Date createdAt;

    private String lang;

    private String timezone;

    private String dateFormat;

    //不持久化到数据库，在json字符串中不显示
    @Transient
    @JsonIgnore
    private String plainPassword;

    @ManyToOne
    @JoinColumn(name = "departmentId", insertable = false, updatable = false)
    private Department department;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    @Transient
    public String getName() {
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.CHINA.equals(locale)) {
            return this.getCnName();
        } else {
            return this.getEnName();
        }
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getSalt() {
        return salt;
    }


    public void setSalt(String salt) {
        this.salt = salt;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getQq() {
        return qq;
    }


    public void setQq(String qq) {
        this.qq = qq;
    }


    public String getSkype() {
        return skype;
    }


    public void setSkype(String skype) {
        this.skype = skype;
    }


    public String getWechat() {
        return wechat;
    }


    public void setWechat(String wechat) {
        this.wechat = wechat;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getGender() {
        return gender;
    }


    public void setGender(Integer gender) {
        this.gender = gender;
    }


    public Integer getSetListRows() {
        return setListRows;
    }


    public void setSetListRows(Integer setListRows) {
        this.setListRows = setListRows;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getExtension() {
        return extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getDepartmentId() {
        return departmentId;
    }


    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }


    public Date getJoinusAt() {
        return joinusAt;
    }


    public void setJoinusAt(Date joinusAt) {
        this.joinusAt = joinusAt;
    }


    public Date getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public String getLang() {
        return lang;
    }


    public void setLang(String lang) {
        this.lang = lang;
    }


    public String getTimezone() {
        return timezone;
    }


    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


    public String getDateFormat() {
        return dateFormat;
    }


    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    public String getPlainPassword() {
        return plainPassword;
    }


    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


}
