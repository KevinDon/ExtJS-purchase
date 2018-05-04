package com.newaim.purchase.flow.inspection.entity;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.flow.workflow.entity.FlowVendorObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "na_flow_compliance_apply")
public class FlowComplianceApply implements FlowVendorObject, Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FCA")})
    private String id;

    private String vendorId;
    private String vendorCnName;
    private String vendorEnName;


    private Date startTime;//申请时间
    private Date endTime;//完成时间
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间

    private String creatorId;//申请人ID
    private String creatorCnName;
    private String creatorEnName;

    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;


    private Integer status;
    private Integer flowStatus;//流程状态
    private String processInstanceId;//流程实例ID

    private String handlerId;//处理人ID
    private String handlerCnName;
    private String handlerEnName;

    private String handlerDepartmentId;//处理部门ID
    private String handlerDepartmentCnName;
    private String handlerDepartmentEnName;

    private Date handledAt;

    private String reviewerId;
    private String reviewerCnName;
    private String reviewerEnName;
    private String reviewerDepartmentId;
    private String reviewerDepartmentCnName;
    private String reviewerDepartmentEnName;

    /**当前处理人*/
    private String assigneeId;
    private String assigneeCnName;
    private String assigneeEnName;

    /**挂起状态*/
    private Integer hold;

    @Transient
    private int approved;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getVendorId() {
        return vendorId;
    }

    @Override
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String getVendorCnName() {
        return vendorCnName;
    }

    @Override
    public void setVendorCnName(String vendorCnName) {
        this.vendorCnName = vendorCnName;
    }

    @Override
    public String getVendorEnName() {
        return vendorEnName;
    }

    @Override
    public void setVendorEnName(String vendorEnName) {
        this.vendorEnName = vendorEnName;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorCnName() {
        return creatorCnName;
    }

    @Override
    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    @Override
    public String getCreatorEnName() {
        return creatorEnName;
    }

    @Override
    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }

    @Override
    public String getDepartmentId() {
        return departmentId;
    }

    @Override
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getDepartmentCnName() {
        return departmentCnName;
    }

    @Override
    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    @Override
    public String getDepartmentEnName() {
        return departmentEnName;
    }

    @Override
    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Integer getFlowStatus() {
        return flowStatus;
    }

    @Override
    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerCnName() {
        return handlerCnName;
    }

    public void setHandlerCnName(String handlerCnName) {
        this.handlerCnName = handlerCnName;
    }

    public String getHandlerEnName() {
        return handlerEnName;
    }

    public void setHandlerEnName(String handlerEnName) {
        this.handlerEnName = handlerEnName;
    }

    public String getHandlerDepartmentId() {
        return handlerDepartmentId;
    }

    public void setHandlerDepartmentId(String handlerDepartmentId) {
        this.handlerDepartmentId = handlerDepartmentId;
    }

    public String getHandlerDepartmentCnName() {
        return handlerDepartmentCnName;
    }

    public void setHandlerDepartmentCnName(String handlerDepartmentCnName) {
        this.handlerDepartmentCnName = handlerDepartmentCnName;
    }

    public String getHandlerDepartmentEnName() {
        return handlerDepartmentEnName;
    }

    public void setHandlerDepartmentEnName(String handlerDepartmentEnName) {
        this.handlerDepartmentEnName = handlerDepartmentEnName;
    }

    public Date getHandledAt() {
        return handledAt;
    }

    public void setHandledAt(Date handledAt) {
        this.handledAt = handledAt;
    }

    @Override
    public String getReviewerId() {
        return reviewerId;
    }

    @Override
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    @Override
    public String getReviewerCnName() {
        return reviewerCnName;
    }

    @Override
    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    @Override
    public String getReviewerEnName() {
        return reviewerEnName;
    }

    @Override
    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    @Override
    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    @Override
    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    @Override
    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    @Override
    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    @Override
    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    @Override
    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    @Override
    public String getAssigneeId() {
        return assigneeId;
    }

    @Override
    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    @Override
    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    @Override
    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    @Override
    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    @Override
    public Integer getHold() {
        return hold;
    }

    @Override
    public void setHold(Integer hold) {
        this.hold = hold;
    }

    @Transient
    public String getVendorName() {
        if(SessionUtils.isCnLang()){
            return  this.vendorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.vendorEnName;
        }
        return null;
    }

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.getCreatorCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getCreatorEnName();
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.getDepartmentCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getDepartmentEnName();
        }
        return null;
    }

    @Transient
    public String getHandlerDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.getHandlerDepartmentCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getHandlerDepartmentEnName();
        }
        return null;
    }

    @Transient
    public String getHandlerName() {
        if(SessionUtils.isCnLang()){
            return  this.getHandlerCnName();
        }else if(SessionUtils.isEnLang()){
            return this.getHandlerEnName();
        }
        return null;
    }


}
