package com.newaim.purchase.flow.inspection.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.inspection.dao.ProductCertificationApplyDao;
import com.newaim.purchase.archives.product.dao.ProductCertificateDao;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertification;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertificationDetail;
import com.newaim.purchase.archives.flow.inspection.entity.ProductCertificationApply;
import com.newaim.purchase.flow.inspection.service.FlowProductCertificationDetailService;
import com.newaim.purchase.flow.inspection.service.FlowProductCertificationService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 产品认证流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowProductCertificationEndListener extends CommonEndListener{

    @Autowired
    private FlowProductCertificationService flowProductCertificationService;

    @Autowired
    private FlowProductCertificationDetailService flowProductCertificationDetailService;

    @Autowired
    private ProductCertificationApplyDao productCertificationApplyDao;

    @Autowired
    private ProductCertificateDao productCertificateDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相细对象
        FlowProductCertification flowProductCertification = flowProductCertificationService.getFlowProductCertification(businessId);
        List<FlowProductCertificationDetail> details = flowProductCertificationDetailService.findDetailsByBusinessId(businessId);

        if(details != null){
            for (FlowProductCertificationDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                ProductCertificationApply productCertificationApply = BeanMapper.map(detail, ProductCertificationApply.class);
                setBusinessObject(productCertificationApply, flowProductCertification);
                //2.2.1 拷贝其余数据
                productCertificationApply.setVendorId(flowProductCertification.getVendorId());
                productCertificationApply.setVendorCnName(flowProductCertification.getVendorCnName());
                productCertificationApply.setVendorEnName(flowProductCertification.getVendorEnName());
                productCertificationApply.setHandlerId(flowProductCertification.getHandlerId());
                productCertificationApply.setHandlerCnName(flowProductCertification.getHandlerCnName());
                productCertificationApply.setHandlerEnName(flowProductCertification.getHandlerEnName());
                productCertificationApply.setHandlerDepartmentId(flowProductCertification.getHandlerDepartmentId());
                productCertificationApply.setHandlerDepartmentCnName(flowProductCertification.getHandlerDepartmentCnName());
                productCertificationApply.setHandlerDepartmentEnName(flowProductCertification.getHandlerDepartmentEnName());
                productCertificationApply.setHandledAt(flowProductCertification.getHandledAt());
                productCertificationApply.setFlowStatus(Constants.FlowStatus.PASS.code);
                productCertificationApply.setEndTime(new Date());
                productCertificationApply.setHold(Constants.HoldStatus.UN_HOLD.code);
                productCertificationApplyDao.save(productCertificationApply);

                if(StringUtils.isNotBlank(productCertificationApply.getProductCertificateId())){
                    ProductCertificate productCertificate = productCertificateDao.findOne(productCertificationApply.getProductCertificateId());
                    if(productCertificate != null){
                        productCertificate.setStatus(Constants.Status.NORMAL.code);
                        productCertificateDao.save(productCertificate);
                    }
                }
            }
        }
    }
}
