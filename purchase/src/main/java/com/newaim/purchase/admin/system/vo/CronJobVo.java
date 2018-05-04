package com.newaim.purchase.admin.system.vo;

import java.util.Date;

public class CronJobVo {

    private String id;
    private String code;
    private String name;
    private String type;
    private String year;
    private String dayOfWeek;
    private String month;
    private String dayOfMonth;
    private String hour;
    private String minute;
    private String second;
    private String dayOfWeekUnit;
    private String monthUnit;
    private String dayOfMonthUnit;
    private String hourUnit;
    private String minuteUnit;
    private String remindType;
    private String roleCodes;
    private String isSystem;
    private Integer status;
    private Date updatedAt;
    private Integer isStarted;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getDayOfWeekUnit() {
        return dayOfWeekUnit;
    }

    public void setDayOfWeekUnit(String dayOfWeekUnit) {
        this.dayOfWeekUnit = dayOfWeekUnit;
    }

    public String getMonthUnit() {
        return monthUnit;
    }

    public void setMonthUnit(String monthUnit) {
        this.monthUnit = monthUnit;
    }

    public String getDayOfMonthUnit() {
        return dayOfMonthUnit;
    }

    public void setDayOfMonthUnit(String dayOfMonthUnit) {
        this.dayOfMonthUnit = dayOfMonthUnit;
    }

    public String getHourUnit() {
        return hourUnit;
    }

    public void setHourUnit(String hourUnit) {
        this.hourUnit = hourUnit;
    }

    public String getMinuteUnit() {
        return minuteUnit;
    }

    public void setMinuteUnit(String minuteUnit) {
        this.minuteUnit = minuteUnit;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(String roleCodes) {
        this.roleCodes = roleCodes;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Integer isStarted) {
        this.isStarted = isStarted;
    }
}
