package com.newaim.purchase.flow.inspection.listeners;

import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContractDetail;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import com.newaim.purchase.flow.inspection.service.FlowOrderQcDetailService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单质检正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowOrderQcStartListener extends CommonEndListener {

    @Autowired
    private FlowOrderQcDetailService flowOrderQcDetailService;

    @Autowired
    private PurchaseContractDetailDao purchaseContractDetailDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相细对象
        List<FlowOrderQcDetail> details = flowOrderQcDetailService.findDetailsByBusinessId(businessId);
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                FlowOrderQcDetail detail = details.get(i);
                List<PurchaseContractDetail> orderDetails = purchaseContractDetailDao.findDetailsByOrderId(detail.getOrderId());
                if(orderDetails != null && orderDetails.size() > 0){
                    for (int j = 0; j < orderDetails.size(); j++) {
                        Product product = productDao.findOne(orderDetails.get(j).getProductId());
                        if(product != null){
                            // 清零产品的订单累计数(前两单必检)
                            Integer qcIndex = product.getQcIndex();
                            if(qcIndex == null){
                                product.setQcIndex(1);
                            }else if(qcIndex > 2){
                                product.setQcIndex(2);
                            }
                            productDao.save(product);
                        }
                    }
                }
            }
        }
    }

}
