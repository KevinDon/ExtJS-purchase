package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.core.service.LocaleMessageSource;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import com.newaim.purchase.flow.purchase.service.FlowPurchasePlanDetailService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 采购计划流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowPurchasePlanCheckComplianceListener implements Serializable, ExecutionListener{

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private FlowPurchasePlanDetailService flowPurchasePlanDetailService;

    @Autowired
    private ProductDao productDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        List<FlowPurchasePlanDetail> details = flowPurchasePlanDetailService.findDetailsByBusinessId(businessId);
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowPurchasePlanDetail detail = details.get(i);
                Product product = productDao.findOne(detail.getProductId());
                Integer riskRating = product.getProp().getRiskRating();
                if(!Constants.RiskRating.isPass(riskRating)){
                    throw new Exception(localeMessageSource.getMsgRiskRatingNotPass());
                }
            }
        }
    }
}
