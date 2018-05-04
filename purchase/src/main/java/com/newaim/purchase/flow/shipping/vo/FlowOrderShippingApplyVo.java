package com.newaim.purchase.flow.shipping.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.vo.UserVo;

public class FlowOrderShippingApplyVo implements Serializable{

    private String id;
    private BigDecimal totalContainerQty;//总柜量
    /**
     * 服务商相关字段
     */
    private String serviceProviderId;
    private String serviceProviderCnName;
    private String serviceProviderEnName;
    private String serviceProviderQuotationId;

    private Date readyDate;//预计完货时间
    private Date etd;//预计发运时间
    private Date eta;//预计到岸时间
    private String etds;//预计发运时间 从details里面拼接
    private String etas;//预计到岸时间 从details里面拼接
    private String orderTitles;//订单名称 从details里面拼接
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private Date startTime;//申请时间
    private Date endTime;//完成时间
    /**申请人ID*/
    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;
    /**部门ID*/
    private String departmentId;
    private String departmentCnName;
    private String departmentEnName;

    private Integer status;
    private Integer flowStatus;//流程状态
    /**处理人ID*/
    private String handlerId;
    private String handlerCnName;
    private String handlerEnName;
    /**处理部门ID*/
    private String handlerDepartmentId;
    private String handlerDepartmentCnName;
    private String handlerDepartmentEnName;
    private Date handledAt;//處理時間

    /**确认人信息*/
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
    private String orderNumbers;

    private List<FlowOrderShippingApplyDetailVo> details = Lists.newArrayList();
    private List<UserVo> flowNextHandler = Lists.newArrayList();
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    /**
     * 审核
     */
    @Transient
    private int approved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtds() {
		return etds;
	}

	public void setEtds(String etds) {
		this.etds = etds;
	}

	public String getEtas() {
		return etas;
	}

	public void setEtas(String etas) {
		this.etas = etas;
	}

	public BigDecimal getTotalContainerQty() {
        return totalContainerQty;
    }

    public void setTotalContainerQty(BigDecimal totalContainerQty) {
        this.totalContainerQty = totalContainerQty;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderCnName() {
        return serviceProviderCnName;
    }

    public void setServiceProviderCnName(String serviceProviderCnName) {
        this.serviceProviderCnName = serviceProviderCnName;
    }

    public String getServiceProviderEnName() {
        return serviceProviderEnName;
    }

    public void setServiceProviderEnName(String serviceProviderEnName) {
        this.serviceProviderEnName = serviceProviderEnName;
    }

    public String getServiceProviderQuotationId() {
        return serviceProviderQuotationId;
    }

    public void setServiceProviderQuotationId(String serviceProviderQuotationId) {
        this.serviceProviderQuotationId = serviceProviderQuotationId;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
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

    public List<FlowOrderShippingApplyDetailVo> getDetails() {
        return details;
    }

    public void setDetails(List<FlowOrderShippingApplyDetailVo> details) {
        this.details = details;
        List<String> etdList = Lists.newArrayList();
        List<String> etaList = Lists.newArrayList();
        List<String> orderTitleList = Lists.newArrayList();
        for(FlowOrderShippingApplyDetailVo obj:details){
        	if(obj.getEtd()!=null){
        		String etd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(obj.getEtd());
        		etdList.add(etd);
        	}
        	if(obj.getEta()!=null){
	        	String eta = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(obj.getEta());
	        	etaList.add(eta);
        	}
        	if(obj.getOrderTitle()!=null){
        		orderTitleList.add(obj.getOrderTitle());
        	}
        }
        this.etds=StringUtils.join(etdList.toArray(), ",");
        this.etas=StringUtils.join(etaList.toArray(), ",");
        this.orderTitles=StringUtils.join(orderTitleList.toArray(), ",");
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerCnName() {
        return reviewerCnName;
    }

    public void setReviewerCnName(String reviewerCnName) {
        this.reviewerCnName = reviewerCnName;
    }

    public String getReviewerEnName() {
        return reviewerEnName;
    }

    public void setReviewerEnName(String reviewerEnName) {
        this.reviewerEnName = reviewerEnName;
    }

    public String getReviewerDepartmentId() {
        return reviewerDepartmentId;
    }

    public void setReviewerDepartmentId(String reviewerDepartmentId) {
        this.reviewerDepartmentId = reviewerDepartmentId;
    }

    public String getReviewerDepartmentCnName() {
        return reviewerDepartmentCnName;
    }

    public void setReviewerDepartmentCnName(String reviewerDepartmentCnName) {
        this.reviewerDepartmentCnName = reviewerDepartmentCnName;
    }

    public String getReviewerDepartmentEnName() {
        return reviewerDepartmentEnName;
    }

    public void setReviewerDepartmentEnName(String reviewerDepartmentEnName) {
        this.reviewerDepartmentEnName = reviewerDepartmentEnName;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeCnName() {
        return assigneeCnName;
    }

    public void setAssigneeCnName(String assigneeCnName) {
        this.assigneeCnName = assigneeCnName;
    }

    public String getAssigneeEnName() {
        return assigneeEnName;
    }

    public void setAssigneeEnName(String assigneeEnName) {
        this.assigneeEnName = assigneeEnName;
    }

    public List<UserVo> getFlowNextHandler() {
        return flowNextHandler;
    }

    public void setFlowNextHandler(List<UserVo> flowNextHandler) {
        this.flowNextHandler = flowNextHandler;
    }

    public Integer getHold() {
        return hold;
    }

    public void setHold(Integer hold) {
        this.hold = hold;
    }

    public String getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(String orderNumbers) {
        this.orderNumbers = orderNumbers;
    }

	public String getOrderTitles() {
		return orderTitles;
	}

	public void setOrderTitles(String orderTitles) {
		this.orderTitles = orderTitles;
	}
}
