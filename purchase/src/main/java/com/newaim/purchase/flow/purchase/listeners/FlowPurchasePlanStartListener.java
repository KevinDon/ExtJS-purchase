package com.newaim.purchase.flow.purchase.listeners;

import com.google.common.collect.Lists;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDao;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDao;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlan;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 采购计划流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowPurchasePlanStartListener implements Serializable, ExecutionListener{

    @Autowired
    private FlowPurchasePlanDao flowPurchasePlanDao;

    @Autowired
    private FlowPurchasePlanDetailDao flowPurchasePlanDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private FlowComplianceApplyDao flowComplianceApplyDao;

    @Autowired
    private FlowComplianceApplyDetailDao flowComplianceApplyDetailDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowPurchasePlan flowPurchasePlan = flowPurchasePlanDao.findOne(businessId);
        List<FlowPurchasePlanDetail> details = flowPurchasePlanDetailDao.findDetailsByBusinessId(businessId);
        //2. 采购计划发启成功，自动创建安检申请单
        List<FlowComplianceApplyDetail> flowComplianceApplyDetails = Lists.newArrayList();
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchasePlanDetail detail = details.get(i);
                Product product = productDao.findOne(detail.getProductId());
                Integer riskRating = product.getProp().getRiskRating();
                if(!Constants.RiskRating.isPass(riskRating)){
                    FlowComplianceApplyDetail flowComplianceApplyDetail = new FlowComplianceApplyDetail();
                    flowComplianceApplyDetail.setProductId(product.getId());
                    flowComplianceApplyDetail.setSku(product.getSku());
                    flowComplianceApplyDetail.setPrevRiskRating(riskRating);
                    flowComplianceApplyDetails.add(flowComplianceApplyDetail);
                }
            }
        }
        //如果需要做安检
        if(flowComplianceApplyDetails.size() > 0) {
            FlowComplianceApply flowComplianceApply = new FlowComplianceApply();
            flowComplianceApply.setVendorId(flowPurchasePlan.getVendorId());
            flowComplianceApply.setVendorCnName(flowPurchasePlan.getVendorCnName());
            flowComplianceApply.setVendorEnName(flowPurchasePlan.getVendorEnName());
            flowComplianceApply.setStatus(Constants.Status.DRAFT.code);
            flowComplianceApply.setFlowStatus(null);
            flowComplianceApply.setCreatedAt(new Date());
            flowComplianceApply.setCreatorId(flowPurchasePlan.getCreatorId());
            flowComplianceApply.setCreatorCnName(flowPurchasePlan.getCreatorCnName());
            flowComplianceApply.setCreatorEnName(flowPurchasePlan.getCreatorEnName());
            flowComplianceApply.setDepartmentId(flowPurchasePlan.getDepartmentId());
            flowComplianceApply.setDepartmentCnName(flowPurchasePlan.getDepartmentCnName());
            flowComplianceApply.setDepartmentEnName(flowPurchasePlan.getDepartmentEnName());
            flowComplianceApply.setHold(Constants.HoldStatus.UN_HOLD.code);
            flowComplianceApplyDao.save(flowComplianceApply);
            for (int i = 0; i < flowComplianceApplyDetails.size(); i++) {
                flowComplianceApplyDetails.get(i).setBusinessId(flowComplianceApply.getId());
            }
            flowComplianceApplyDetailDao.save(flowComplianceApplyDetails);
            Msg.send(flowPurchasePlan.getCreatorId(),"已成功创建安检申请单","已成功创建安检申请单");
        }
    }
}
