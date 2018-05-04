package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.purchase.Constants;
import com.newaim.purchase.Constants.NewProductStatus;
import com.newaim.purchase.archives.flow.purchase.dao.NewProductDao;
import com.newaim.purchase.archives.flow.purchase.entity.NewProduct;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.service.FlowNewProductDetailService;
import com.newaim.purchase.flow.purchase.service.FlowNewProductService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 新品开发流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowNewProductEndListener extends CommonEndListener{

    @Autowired
    private FlowNewProductService flowNewProductService;

    @Autowired
    private FlowNewProductDetailService flowNewProductDetailService;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private NewProductDao newProductDao;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private ReportsService reportsService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相细对象
        FlowNewProduct flowNewProduct = flowNewProductService.getFlowNewProduct(businessId);
        List<FlowNewProductDetail> details = flowNewProductDetailService.findDetailsByBusinessId(businessId);

        if(details != null){
            Date now = new Date();
            for (FlowNewProductDetail detail: details) {
                //查询对应新品信息ID
                String productId = detail.getProductId();
                Product product = productDao.findOne(productId);
                //2.2 拷贝明细数据到业务表
                NewProduct newProduct = BeanMapper.map(detail, NewProduct.class);
                setBusinessObject(newProduct, flowNewProduct);
                //2.2.1 拷贝其余数据
                newProduct.setVendorId(flowNewProduct.getVendorId());
                newProduct.setVendorCnName(flowNewProduct.getVendorCnName());
                newProduct.setVendorEnName(flowNewProduct.getVendorEnName());
                newProduct.setFlowStatus(Constants.FlowStatus.PASS.code);
                newProduct.setHold(Constants.HoldStatus.UN_HOLD.code);
                newProduct.setEndTime(now);
                newProductDao.save(newProduct);
                //2.1 修改产品的开发状态为完成，并根据安检状态和质检状态来转换新品
                ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(productId);
                if(prop != null){
                    //2.1.1 新品通过后，将报价更新到产品档案
                    prop.setRateAudToRmb(newProduct.getRateAudToRmb());
                    prop.setRateAudToUsd(newProduct.getRateAudToUsd());
                    prop.setCurrency(newProduct.getCurrency());
                    prop.setProductPredictProfitAud(detail.getProductPredictProfitAud());
                    prop.setProductPredictProfitRmb(detail.getProductPredictProfitRmb());
                    prop.setProductPredictProfitUsd(detail.getProductPredictProfitUsd());
                    prop.setQuotationCurrency(newProduct.getCurrency());
                    prop.setQuotationPriceAud(newProduct.getPriceAud());
                    prop.setQuotationPriceRmb(newProduct.getPriceRmb());
                    prop.setQuotationPriceUsd(newProduct.getPriceUsd());
                    prop.setQuotationRateAudToUsd(newProduct.getRateAudToUsd());
                    prop.setQuotationRateAudToRmb(newProduct.getRateAudToRmb());
                    //设置开发状态通过
                    prop.setFlagDevStatus(NewProductStatus.DEV_STATUS_PASS.code);
                    prop.setFlagDevId(newProduct.getId());
                    prop.setFlagDevTime(now);
                    //新品时才转换
                    if(Product.NEW_PRODUCT.equals(product.getNewProduct()) && NewProductStatus.COMPLIANCE_STATUS_PASS.code.equals(prop.getFlagComplianceStatus())
                            && NewProductStatus.QC_STATUS_PASS.code.equals(prop.getFlagQcStatus())){
                        //安检，质检通过时设置为正常产品
                        Msg.send(newProduct.getCreatorId(), localeMessageSource.getMsgNewProductConvertTitle(product.getSku()), localeMessageSource.getMsgNewProductConvertContent(product.getSku()));
                    }
                    //保存状态
                    productVendorPropDao.save(prop);
                }
            }

            //更新相关报告的确认人
            reportsService.saveConfirmedByBusinessId(flowNewProduct.getId());
        }
    }
}
