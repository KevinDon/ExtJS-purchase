package com.newaim.purchase.admin.system.vo;

import java.util.Date;

public class AutoCodeVo {

    private String id;
    private String code;
    private String title;
    private String format;
    private Integer mainValue;
    private String mainValueCleanRule;
    private Integer subValue;
    private String subValueCleanRule;
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;
    private String description;
    private Date updatedAt;
    private Integer groupNo;
    private Integer sort;
    private String lastValue;
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getMainValue() {
        return mainValue;
    }

    public void setMainValue(Integer mainValue) {
        this.mainValue = mainValue;
    }

    public String getMainValueCleanRule() {
        return mainValueCleanRule;
    }

    public void setMainValueCleanRule(String mainValueCleanRule) {
        this.mainValueCleanRule = mainValueCleanRule;
    }

    public Integer getSubValue() {
        return subValue;
    }

    public void setSubValue(Integer subValue) {
        this.subValue = subValue;
    }

    public String getSubValueCleanRule() {
        return subValueCleanRule;
    }

    public void setSubValueCleanRule(String subValueCleanRule) {
        this.subValueCleanRule = subValueCleanRule;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
