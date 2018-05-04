package com.newaim.purchase.flow.inspection.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.inspection.dao.SampleQcDao;
import com.newaim.purchase.archives.flow.inspection.entity.SampleQc;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQc;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import com.newaim.purchase.flow.inspection.service.FlowSampleQcDetailService;
import com.newaim.purchase.flow.inspection.service.FlowSampleQcService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 样品质检流正常结束监听器.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowSampleQcEndListener extends CommonEndListener{

    @Autowired
    private FlowSampleQcService flowSampleQcService;

    @Autowired
    private FlowSampleQcDetailService flowSampleQcDetailService;

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private SampleQcDao sampleQcDao;

    @Autowired
    private ReportsService reportsService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关明细对象
        FlowSampleQc flowSampleQc = flowSampleQcService.getFlowSampleQc(businessId);
        List<FlowSampleQcDetail> details = flowSampleQcDetailService.findDetailsByBusinessId(businessId);
        //2. 修改产品的质检状态为完成，并根据安检状态和开发状态来转换新品
        if(details != null){
            Date now = new Date();
            for (FlowSampleQcDetail detail: details) {

                //复制明细表信息
                SampleQc sampleQc = BeanMapper.map(detail, SampleQc.class);
                //复制主表信息
                setBusinessObject(sampleQc, flowSampleQc);
                //设置明细信息
                sampleQc.setVendorId(flowSampleQc.getVendorId());
                sampleQc.setVendorCnName(flowSampleQc.getVendorCnName());
                sampleQc.setVendorEnName(flowSampleQc.getVendorEnName());
                sampleQc.setHandlerId(flowSampleQc.getHandlerId());
                sampleQc.setHandlerCnName(flowSampleQc.getHandlerCnName());
                sampleQc.setHandlerEnName(flowSampleQc.getHandlerEnName());
                sampleQc.setHandlerDepartmentId(flowSampleQc.getHandlerDepartmentId());
                sampleQc.setHandlerDepartmentCnName(flowSampleQc.getHandlerDepartmentCnName());
                sampleQc.setHandlerDepartmentEnName(flowSampleQc.getHandlerDepartmentEnName());
                sampleQc.setHandledAt(flowSampleQc.getHandledAt());
                sampleQc.setFlowStatus(Constants.FlowStatus.PASS.code);
                sampleQc.setHold(Constants.HoldStatus.UN_HOLD.code);
                sampleQc.setEndTime(now);
                sampleQcDao.save(sampleQc);

                //查询对应新品信息ID
                String productId = detail.getProductId();
                ProductVendorProp prop = productVendorPropDao.findProductVendorPropByProductId(productId);
                if(prop != null){
                    //设置质检状态通过
                    prop.setFlagQcStatus(Constants.NewProductStatus.QC_STATUS_PASS.code);
                    prop.setFlagQcId(sampleQc.getId());
                    prop.setFlagQcTime(now);
                    if(Constants.NewProductStatus.COMPLIANCE_STATUS_PASS.code.equals(prop.getFlagComplianceStatus())
                            && Constants.NewProductStatus.DEV_STATUS_PASS.code.equals(prop.getFlagQcStatus())){
                        //开发，安检通过时设置为正常产品
                        Msg.send(sampleQc.getCreatorId(), localeMessageSource.getMsgNewProductConvertTitle(sampleQc.getSku()), localeMessageSource.getMsgNewProductConvertContent(sampleQc.getSku()));
                    }
                    //保存状态
                    productVendorPropDao.save(prop);
                }
            }

            //更新相关报告的确认人
            reportsService.saveConfirmedByBusinessId(flowSampleQc.getId());
        }
    }
}
