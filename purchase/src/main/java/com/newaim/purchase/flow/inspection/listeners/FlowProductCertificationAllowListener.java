package com.newaim.purchase.flow.inspection.listeners;

import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.dao.ProductCertificateDao;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品认证流.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowProductCertificationAllowListener extends CommonEndListener{

    @Autowired
    private ProductCertificateDao productCertificateDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        List<ProductCertificate> productCertificates = productCertificateDao.findByBusinessIdAndStatus(businessId, Constants.Status.DRAFT.code);
        if(productCertificates != null && productCertificates.size() > 0){
            for (int i = 0; i < productCertificates.size(); i++) {
                ProductCertificate productCertificate = productCertificates.get(i);
                productCertificate.setStatus(Constants.Status.NORMAL.code);
                productCertificateDao.save(productCertificate);
            }
        }
        List<ProductCertificate> data = productCertificateDao.findByBusinessIdAndStatus(businessId, Constants.Status.NORMAL.code);
        if(data == null || data.isEmpty()){
            throw new RuntimeException("product certificate file not submit!");
        }
    }
}
