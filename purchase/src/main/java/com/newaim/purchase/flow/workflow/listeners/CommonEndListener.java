package com.newaim.purchase.flow.workflow.listeners;

import com.newaim.purchase.flow.workflow.entity.BusinessObject;
import com.newaim.purchase.flow.workflow.entity.FlowObject;
import org.activiti.engine.delegate.ExecutionListener;

import java.io.Serializable;

/**
 * Created by Mark on 2017/11/8.
 */
public abstract class CommonEndListener implements Serializable, ExecutionListener {

    /**
     * 设置业务表数据
     * @param businessObject
     * @param flowObject
     */
    protected void setBusinessObject(BusinessObject businessObject, FlowObject flowObject){
        businessObject.setId(null);
        businessObject.setBusinessId(flowObject.getId());
        businessObject.setCreatorId(flowObject.getCreatorId());
        businessObject.setCreatorCnName(flowObject.getCreatorCnName());
        businessObject.setCreatorEnName(flowObject.getCreatorEnName());
        businessObject.setDepartmentId(flowObject.getDepartmentId());
        businessObject.setDepartmentCnName(flowObject.getDepartmentCnName());
        businessObject.setDepartmentEnName(flowObject.getDepartmentEnName());
        businessObject.setReviewerId(flowObject.getReviewerId());
        businessObject.setReviewerCnName(flowObject.getReviewerCnName());
        businessObject.setReviewerEnName(flowObject.getReviewerEnName());
        businessObject.setStartTime(flowObject.getStartTime());
        businessObject.setEndTime(flowObject.getEndTime());
        businessObject.setStatus(flowObject.getStatus());
        businessObject.setFlowStatus(flowObject.getFlowStatus());
        businessObject.setCreatedAt(flowObject.getCreatedAt());
        businessObject.setUpdatedAt(flowObject.getUpdatedAt());
    }
}
