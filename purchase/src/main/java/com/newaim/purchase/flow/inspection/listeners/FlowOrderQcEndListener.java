package com.newaim.purchase.flow.inspection.listeners;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.archives.flow.inspection.dao.OrderQcDao;
import com.newaim.purchase.archives.flow.inspection.entity.OrderQc;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQc;
import com.newaim.purchase.flow.inspection.entity.FlowOrderQcDetail;
import com.newaim.purchase.flow.inspection.service.FlowOrderQcDetailService;
import com.newaim.purchase.flow.inspection.service.FlowOrderQcService;
import com.newaim.purchase.flow.purchase.dao.FlowPurchaseContractDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchaseContract;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单质检正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowOrderQcEndListener extends CommonEndListener {

    @Autowired
    private FlowOrderQcService flowOrderQcService;

    @Autowired
    private FlowOrderQcDetailService flowOrderQcDetailService;

    @Autowired
    private OrderQcDao orderQcDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private FlowPurchaseContractDao flowPurchaseContractDao;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相细对象
        FlowOrderQc flowOrderQc = flowOrderQcService.getFlowOrderQc(businessId);
        List<FlowOrderQcDetail> details = flowOrderQcDetailService.findDetailsByBusinessId(businessId);

        StringBuilder jobNumbers = new StringBuilder("");
        if(details != null){
            for (FlowOrderQcDetail detail: details) {
                //2.2 拷贝明细数据到业务表
                OrderQc orderQc = BeanMapper.map(detail, OrderQc.class);
                setBusinessObject(orderQc, flowOrderQc);
                //2.2.1 拷贝其余数据
                orderQc.setVendorId(flowOrderQc.getVendorId());
                orderQc.setVendorCnName(flowOrderQc.getVendorCnName());
                orderQc.setVendorEnName(flowOrderQc.getVendorEnName());
                orderQc.setHandlerId(flowOrderQc.getHandlerId());
                orderQc.setHandlerCnName(flowOrderQc.getHandlerCnName());
                orderQc.setHandlerEnName(flowOrderQc.getHandlerEnName());
                orderQc.setHandlerDepartmentId(flowOrderQc.getHandlerDepartmentId());
                orderQc.setHandlerDepartmentCnName(flowOrderQc.getHandlerDepartmentCnName());
                orderQc.setHandlerDepartmentEnName(flowOrderQc.getHandlerDepartmentEnName());
                orderQc.setHandledAt(flowOrderQc.getHandledAt());
                orderQc.setFlowStatus(Constants.FlowStatus.PASS.code);
                orderQc.setHold(Constants.HoldStatus.UN_HOLD.code);
                orderQc.setEndTime(new Date());
                orderQcDao.save(orderQc);
                PurchaseContract order = purchaseContractDao.findOne(orderQc.getOrderId());
                if(order != null){
                    order.setFlagOrderQcId(orderQc.getOrderId());
                    order.setFlagOrderQcStatus(1);
                    order.setFlagOrderQcTime(new Date());
                    //标记已质检
                    order.setIsNeededQc(1);
                    purchaseContractDao.save(order);

                    FlowPurchaseContract flowOrder = flowPurchaseContractDao.findOne(order.getBusinessId());
                    if(flowOrder != null){
                        flowOrder.setIsNeededQc(1);
                        flowPurchaseContractDao.save(flowOrder);
                    }

                    jobNumbers.append(",").append(order.getOrderNumber());
                }
            }
            List<User> users = userService.findUserByRoleCode("shipping");
            for (int i = 0; i < users.size(); i++) {
                Msg.send(users.get(i).getId(), "Order release", "HI （" + users.get(i).getAccount() + "），\n" +
                        "\n" +
                        "S/O of order (" + jobNumbers.substring(1) + ") can be release. Thanks. \n" +
                        "\n" +
                        "Best Regards,\n" +
                        "Buyer");
            }
            //更新相关报告的确认人
            reportsService.saveConfirmedByBusinessId(flowOrderQc.getId());
        }
    }

}
