package com.newaim.purchase.flow.workflow.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用业务表接口
 */
public interface BusinessObject{

    /**
     * 获取id
     * @return id
     */
    String getId();

    /**
     * 设置id
     * @param id id
     */
    void setId(String id);

    /**
     * 设置申请单流程状态
     * @param flowStatus 申请单流程状态
     */
    void setFlowStatus(Integer flowStatus);

    /**
     * 获取流程状态
     * @return 流程状态
     */
    Integer getFlowStatus();

    /**
     * 设置申请单状态
     * @param status 申请单状态
     */
    void setStatus(Integer status);

    /**
     * 获取申请单状态
     * @return 申请单状态
     */
    Integer getStatus();

    /**
     * 获取创建时间
     * @return 创建时间
     */
    Date getCreatedAt();

    /**
     * 设置创建时间
     * @param createdAt 创建时间
     */
    void setCreatedAt(Date createdAt);

    /**
     * 获取更新时间
     * @return 更新时间
     */
    Date getUpdatedAt();

    /**
     * 设置更新时间
     * @param updatedAt 更新时间
     */
    void setUpdatedAt(Date updatedAt);

    /**
     * 设置启动时间
     * @param date 启动时间
     */
    void setStartTime(Date date);

    /**
     * 获取流程发起时间
     * @return 流程发起时间
     */
    Date getStartTime();

    /**
     * 设置结束时间
     * @param date 结束时间
     */
    void setEndTime(Date date);

    /**
     * 获取流程结束时间
     * @return 流程结束时间
     */
    Date getEndTime();

    /**
     * 设置创建人ID
     * @param creatorId 创建人ID
     */
    void setCreatorId(String creatorId);

    /**
     * 获取创建人ID
     * @return 创建人ID
     */
    String getCreatorId();

    /**
     * 设置创建人中文名
     * @param creatorCnName 创建人中文名
     */
    void setCreatorCnName(String creatorCnName);

    /**
     * 获取创建中文名
     * @return 创建中文名
     */
    String getCreatorCnName();

    /**
     * 设置创建人英文名
     * @param creatorEnName 创建人英文名
     */
    void setCreatorEnName(String creatorEnName);

    /**
     * 获取创建人英文名
     * @return 创建人英文名
     */
    String getCreatorEnName();

    /**
     * 设置所属部门id
     * @param departmentId 所属部门id
     */
    void setDepartmentId(String departmentId);

    /**
     * 获取所属部门id
     * @return 所属部门id
     */
    String getDepartmentId();


    /**
     * 设置所属部门中文名
     * @param departmentCnName 所属部门中文名
     */
    void setDepartmentCnName(String departmentCnName);

    /**
     * 获取所属部门中文名
     * @return 所属部门中文名
     */
    String getDepartmentCnName();

    /**
     * 设置所属部门英文名
     * @param departmentEnName 所属部门英文名
     */
    void setDepartmentEnName(String departmentEnName);

    /**
     * 获取所属部门英文名
     * @return 所属部门英文名
     */
    String getDepartmentEnName();

    /**
     * 设置流程实例ID
     * @param processInstanceId 流程实例ID
     */
    void setProcessInstanceId(String processInstanceId);

    /**
     * 获取流程实例ID
     * @return 流程实例ID
     */
    String getProcessInstanceId();

    /**
     * 获取申请单id
     * @return 申请单id
     */
    String getBusinessId();

    /**
     * 设置申请单id
     * @param businessId 申请单id
     */
    void setBusinessId(String businessId);

    /**
     * 获取确认人id
     * @return 确认人id
     */
    String getReviewerId();

    /**
     * 设置确认人id
     * @param reviewerId 确认人id
     */
    void setReviewerId(String reviewerId);

    /**
     * 获取确认人中文名
     * @return 确认人中文名
     */
    String getReviewerCnName();

    /**
     * 设置确认人中文名
     * @param reviewerCnName 确认人中文名
     */
    void setReviewerCnName(String reviewerCnName);

    /**
     * 获取确认人英文名
     * @return 确认人英文名
     */
    String getReviewerEnName();

    /**
     * 设置确认人英文名
     * @param reviewerEnName 确认人英文名
     */
    void setReviewerEnName(String reviewerEnName);

    /**
     * 获取确认人部门id
     * @return 确认人部门id
     */
    String getReviewerDepartmentId();

    /**
     * 设置确认人部门id
     * @param reviewerDepartmentId 确认人部门id
     */
    void setReviewerDepartmentId(String reviewerDepartmentId);

    /**
     * 获取确认人部门中文名
     * @return 确认人部门中文名
     */
    String getReviewerDepartmentCnName();

    /**
     * 获取确认人部门中文名
     * @param reviewerDepartmentCnName 确认人部门中文名
     */
    void setReviewerDepartmentCnName(String reviewerDepartmentCnName);

    /**
     * 获取确认人部门英文名
     * @return 确认人部门英文名
     */
    String getReviewerDepartmentEnName();

    /**
     * 设置确认人部门英文名
     * @param reviewerDepartmentEnName 确认人部门英文名
     */
    void setReviewerDepartmentEnName(String reviewerDepartmentEnName);

    /**
     * 获取挂起状态
     * @return 挂起状态
     */
    Integer getHold();

    /**
     * 设置挂起状态
     * @param hold 挂起状态
     */
    void setHold(Integer hold);
}
