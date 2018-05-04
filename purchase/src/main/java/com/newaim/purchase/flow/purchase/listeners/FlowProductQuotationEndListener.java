package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.ProductQuotationDao;
import com.newaim.purchase.archives.flow.purchase.entity.ProductQuotation;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotation;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotationDetail;
import com.newaim.purchase.flow.purchase.service.FlowProductQuotationDetailService;
import com.newaim.purchase.flow.purchase.service.FlowProductQuotationService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 采购询价流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowProductQuotationEndListener extends CommonEndListener{

    @Autowired
    private FlowProductQuotationService flowProductQuotationService;

    @Autowired
    private FlowProductQuotationDetailService flowProductQuotationDetailService;

    @Autowired
    private ProductQuotationDao productQuotationDao;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowProductQuotation flowProductQuotation = flowProductQuotationService.getFlowProductQuotation(businessId);
        List<FlowProductQuotationDetail> details = flowProductQuotationDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        if(details != null){
            for (FlowProductQuotationDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                ProductQuotation productQuotation = BeanMapper.map(detail, ProductQuotation.class);
                setBusinessObject(productQuotation, flowProductQuotation);
                productQuotation.setDetailBusinessId(detail.getId());
                //2.2.1 拷贝其余数据
                productQuotation.setVendorId(flowProductQuotation.getVendorId());
                productQuotation.setCurrency(flowProductQuotation.getCurrency());
                productQuotation.setVendorCnName(flowProductQuotation.getVendorCnName());
                productQuotation.setVendorEnName(flowProductQuotation.getVendorEnName());
                productQuotation.setEffectiveDate(flowProductQuotation.getEffectiveDate());
                productQuotation.setValidUntil(flowProductQuotation.getValidUntil());
                productQuotation.setFlowStatus(Constants.FlowStatus.PASS.code);
                //设置产品状态
                Product product = productDao.findOne(detail.getProductId());
                if(product != null){
                    productQuotation.setProductStatus(product.getStatus());
                }
                productQuotation.setIsApply(2);
                productQuotation.setHold(Constants.HoldStatus.UN_HOLD.code);
                productQuotation.setEndTime(new Date());
                productQuotationDao.save(productQuotation);
                //采购询价通过后，确定报价被应用，并且回写到产品档案
                ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(productQuotation.getProductId());
                if (prop!=null){
                    prop.setIsApply(productQuotation.getIsApply());
                    productVendorPropDao.save(prop);
                }
            }
        }
    }
}
