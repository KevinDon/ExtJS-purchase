package com.newaim.purchase.flow.workflow.service;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderService;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.flow.workflow.entity.FlowObject;
import com.newaim.purchase.flow.workflow.entity.FlowServiceProviderObject;
import com.newaim.purchase.flow.workflow.entity.FlowServiceProviderWithCurrencyObject;
import com.newaim.purchase.flow.workflow.entity.FlowVendorObject;
import com.newaim.purchase.flow.workflow.entity.FlowVendorWithCurrencyObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class FlowServiceBase {

    @Autowired
    protected UserService userService;

    @Autowired
    protected ServiceProviderService serviceProviderService;

    @Autowired
    protected VendorService vendorService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 设置流程表单通用创建人信息
     * @param object 表单对象
     */
    protected void setFlowCreatorInfo(FlowObject object){
        UserVo user = SessionUtils.currentUserVo();
        object.setCreatedAt(new Date());
        object.setStatus(Constants.Status.DRAFT.code);
        object.setCreatorId(user.getId());
        object.setCreatorCnName(user.getCnName());
        object.setCreatorEnName(user.getEnName());
        object.setDepartmentId(user.getDepartmentId());
        object.setDepartmentCnName(user.getDepartmentCnName());
        object.setDepartmentEnName(user.getDepartmentEnName());
        //默认处理人为创建人
        object.setAssigneeId(user.getId());
        object.setAssigneeCnName(user.getCnName());
        object.setAssigneeEnName(user.getEnName());
        //默认非挂起
        object.setHold(2);
    }

    /**
     * 设置供应商信息
     * @param object
     */
    protected void setFlowVendorInfo(FlowVendorObject object){
        if(StringUtils.isNotBlank(object.getVendorId())){
            Vendor vendor = vendorService.getVendor(object.getVendorId());
            if(vendor != null){
                object.setVendorCnName(vendor.getCnName());
                object.setVendorEnName(vendor.getEnName());
                if(object instanceof FlowVendorWithCurrencyObject){
                    ((FlowVendorWithCurrencyObject) object).setCurrency(vendor.getCurrency());
                }
            }
        }
    }

    /**
     *  设置服务商相关信息，新增或更新时
     * @param object 表单对象
     */
    protected void setFlowServiceProviderInfo(FlowServiceProviderObject object){
        if(StringUtils.isNotBlank(object.getServiceProviderId())){
            ServiceProvider serviceProvider = serviceProviderService.getServiceProvider(object.getServiceProviderId());
            if(serviceProvider != null){
                object.setServiceProviderCnName(serviceProvider.getCnName());
                object.setServiceProviderEnName(serviceProvider.getEnName());
                if(object instanceof FlowServiceProviderWithCurrencyObject){
                    ((FlowServiceProviderWithCurrencyObject) object).setCurrency(serviceProvider.getCurrency());
                }
            }
        }
    }

    /**
     * 另存时清理信息
     * @param object
     */
    protected void cleanInfoForSaveAs(FlowObject object){
        object.setId(null);
        object.setUpdatedAt(null);
        object.setProcessInstanceId(null);
        object.setFlowStatus(null);
        object.setStartTime(null);
        object.setEndTime(null);
    }
}
