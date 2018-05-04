package com.newaim.purchase.admin.system.vo;

import java.io.Serializable;
import java.util.Date;

public class BuyerInfoVo implements Serializable{

    private String id;
    private String name;
    private String cnName;//买方中文名
    private String enName;//买方英文名
    private String cnAddress;//买方中文地址
    private String enAddress;//买方英文地址
    private String phone;//买方电话
    private String fax;//买方传真
    private String contactCnName;//买方联系人中文名
    private String contactEnName;//买方联系人英文名
    private String email;//买方邮件
    private Integer status;//状态
    private String creatorId;//创建人ID
    private String creatorCnName;//创建人中文名
    private String creatorEnName;//创建人英文名
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private String departmentId;//部门ID
    private String departmentCnName;//部门中文名
    private String departmentEnName;//部门英文名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCnAddress() {
        return cnAddress;
    }

    public void setCnAddress(String cnAddress) {
        this.cnAddress = cnAddress;
    }

    public String getEnAddress() {
        return enAddress;
    }

    public void setEnAddress(String enAddress) {
        this.enAddress = enAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getContactCnName() {
        return contactCnName;
    }

    public void setContactCnName(String contactCnName) {
        this.contactCnName = contactCnName;
    }

    public String getContactEnName() {
        return contactEnName;
    }

    public void setContactEnName(String contactEnName) {
        this.contactEnName = contactEnName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
