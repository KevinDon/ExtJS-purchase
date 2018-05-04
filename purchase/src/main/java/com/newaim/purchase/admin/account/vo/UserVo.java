package com.newaim.purchase.admin.account.vo;

import com.newaim.purchase.admin.system.entity.Portal;
import com.newaim.purchase.config.json.JsonMoney;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Mark on 2017/8/10.
 */
public class UserVo implements Serializable {

    private String id;

    private String account;

    private String departmentId;

    private String cnName;

    private String enName;

    private String name;

    private String email;

    private String qq;

    private String wechat;

    private String skype;

    private String extension;

    private String phone;

    private Integer gender;

    private Integer status;

    private Integer setListRows;

    private String lang;

    private String timezone;

    private String dateFormat;

    private String password;

    private String salt;

    private String plainPassword;

    private Date joinusAt;

    private Date createdAt;


    //可用资源模板
    private List<ResourceMenuVo> resource;

    private List<String> roleIds;
    //角色
    private List<String> roles;

    private List<String> datas;

    private UserDepartmentVo department;

    //桌面
    private List<Portal> portals;
    //提示
    private Integer countMessageNew;
    private Integer countEmailNew;
    private Integer countTaskNew;
    //公用汇率
    @JsonMoney
    private BigDecimal audToRmb;
    @JsonMoney
    private BigDecimal audToUsd;


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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<ResourceMenuVo> getResource() {
        return resource;
    }

    public void setResource(List<ResourceMenuVo> resource) {
        this.resource = resource;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
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

    public UserDepartmentVo getDepartment() {
        return department;
    }

    public void setDepartment(UserDepartmentVo department) {
        this.department = department;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    /**
     * 部门中文名
     */
    public String getDepartmentCnName() {
        return department != null ? department.getCnName() : null;
    }

    /**
     * 部门英文名
     */
    public String getDepartmentEnName() {
        return department != null ? department.getEnName() : null;
    }

    public List<Portal> getPortals() {
        return portals;
    }

    public void setPortals(List<Portal> portals) {
        this.portals = portals;
    }

    public BigDecimal getAudToRmb() {
        return audToRmb;
    }

    public void setAudToRmb(BigDecimal audToRmb) {
        this.audToRmb = audToRmb;
    }

    public BigDecimal getAudToUsd() {
        return audToUsd;
    }

    public void setAudToUsd(BigDecimal audToUsd) {
        this.audToUsd = audToUsd;
    }

    public Integer getCountMessageNew() {
        return countMessageNew;
    }

    public void setCountMessageNew(Integer countMessageNew) {
        this.countMessageNew = countMessageNew;
    }

    public Integer getCountEmailNew() {
        return countEmailNew;
    }

    public void setCountEmailNew(Integer countEmailNew) {
        this.countEmailNew = countEmailNew;
    }

    public Integer getCountTaskNew() {
        return countTaskNew;
    }

    public void setCountTaskNew(Integer countTaskNew) {
        this.countTaskNew = countTaskNew;
    }
}
