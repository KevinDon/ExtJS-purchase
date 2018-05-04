package com.newaim.purchase.flow.purchase.listeners;

import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDao;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;
import com.newaim.purchase.flow.inspection.service.FlowComplianceApplyService;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
import com.newaim.purchase.flow.purchase.entity.FlowNewProductDetail;
import com.newaim.purchase.flow.purchase.service.FlowNewProductDetailService;
import com.newaim.purchase.flow.purchase.service.FlowNewProductService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新品开发发启监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowNewProductStartListener extends CommonEndListener{

    @Autowired
    private FlowNewProductService flowNewProductService;

    @Autowired
    private FlowNewProductDetailService flowNewProductDetailService;

    @Autowired
    private FlowComplianceApplyDao flowComplianceApplyDao;

    @Autowired
    private FlowComplianceApplyDetailDao flowComplianceApplyDetailDao;

    @Autowired
    private ProductService productService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相细对象
        FlowNewProduct flowNewProduct = flowNewProductService.getFlowNewProduct(businessId);
        List<FlowNewProductDetail> newProductDetails = flowNewProductDetailService.findDetailsByBusinessId(flowNewProduct.getId());
        //新品开发申请发启成功，自动创建安检申请单
        FlowComplianceApply flowComplianceApply = new FlowComplianceApply();
        flowComplianceApply.setVendorId(flowNewProduct.getVendorId());
        flowComplianceApply.setVendorCnName(flowNewProduct.getVendorCnName());
        flowComplianceApply.setVendorEnName(flowNewProduct.getVendorEnName());
        flowComplianceApply.setStatus(Constants.Status.DRAFT.code);
        flowComplianceApply.setFlowStatus(null);
        flowComplianceApply.setCreatedAt(new Date());
        flowComplianceApply.setCreatorId(flowNewProduct.getCreatorId());
        flowComplianceApply.setCreatorCnName(flowNewProduct.getCreatorCnName());
        flowComplianceApply.setCreatorEnName(flowNewProduct.getCreatorEnName());
        flowComplianceApply.setDepartmentId(flowNewProduct.getDepartmentId());
        flowComplianceApply.setDepartmentCnName(flowNewProduct.getDepartmentCnName());
        flowComplianceApply.setDepartmentEnName(flowNewProduct.getDepartmentEnName());
        flowComplianceApply.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowComplianceApplyDao.save(flowComplianceApply);
        if (newProductDetails!=null){
            for (FlowNewProductDetail newProductDetail : newProductDetails){
                if (newProductDetail.getNewProduct()==1){
                    FlowComplianceApplyDetail applyDetail = new FlowComplianceApplyDetail();
                    applyDetail.setProductId(newProductDetail.getProductId());
                    applyDetail.setBusinessId(flowComplianceApply.getId());
                    Product product = productService.getProduct(applyDetail.getProductId());
                    applyDetail.setSku(product.getSku());
                    flowComplianceApplyDetailDao.save(applyDetail);
                }
            }
        }
        Msg.send(flowNewProduct.getCreatorId(),"已成功创建安检申请单","已成功创建安检申请单");
    }
}
