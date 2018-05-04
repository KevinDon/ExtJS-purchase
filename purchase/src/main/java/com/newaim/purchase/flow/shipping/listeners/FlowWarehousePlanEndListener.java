package com.newaim.purchase.flow.shipping.listeners;
import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.purchase.dao.CustomClearancePackingDao;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.shipping.dao.WarehousePlanDao;
import com.newaim.purchase.archives.flow.shipping.dao.WarehousePlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlan;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlanDetail;
import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlan;
import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlanDetail;
import com.newaim.purchase.flow.shipping.service.FlowWarehousePlanDetailService;
import com.newaim.purchase.flow.shipping.service.FlowWarehousePlanService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
 * 送仓计划正常结束监听器
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowWarehousePlanEndListener extends CommonEndListener {

    @Autowired
    private FlowWarehousePlanService flowWarehousePlanService;

    @Autowired
    private FlowWarehousePlanDetailService flowWarehousePlanDetailService;

   @Autowired
    private WarehousePlanDao warehousePlanDao;

   @Autowired
    private WarehousePlanDetailDao warehousePlanDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    @Autowired
    private CustomClearancePackingDao customClearancePackingDao;

    @Override
    public void notify(DelegateExecution execution)throws Exception{
        String businessId = execution.getProcessBusinessKey();
        //1. 通过表单id获取相关对象
        FlowWarehousePlan flowWarehousePlan = flowWarehousePlanService.getFlowWarehousePlan(businessId);
        List<FlowWarehousePlanDetail> details = flowWarehousePlanDetailService.findDetailsByBusinessId(businessId);
        //2. 保存相关数据到正式业务数据表
        WarehousePlan warehousePlan = BeanMapper.map(flowWarehousePlan,WarehousePlan.class);
        warehousePlan.setId(null);
        warehousePlan.setBusinessId(flowWarehousePlan.getId());
        warehousePlan.setFlowStatus(Constants.FlowStatus.PASS.code);
        warehousePlan.setEndTime(new Date());
        warehousePlanDao.save(warehousePlan);

        List<String> orderIds = Lists.newArrayList();
        if (details!=null){
            for (FlowWarehousePlanDetail detail : details){
                //2.2 拷贝明细数据到业务表
                WarehousePlanDetail warehousePlanDetail = BeanMapper.map(detail,WarehousePlanDetail.class);
                warehousePlanDetail.setWarehousePlanId(warehousePlan.getId());
                warehousePlanDetailDao.save(warehousePlanDetail);
                if(!orderIds.contains(warehousePlanDetail.getOrderId())){
                    orderIds.add(warehousePlanDetail.getOrderId());
                }
                List<CustomClearancePacking> packings = customClearancePackingDao.findPackingsByContainerNumber(warehousePlanDetail.getContainerNumber());
                if(packings != null && packings.size() > 0){
                    for (int i = 0; i < packings.size(); i++) {
                        CustomClearancePacking packing = packings.get(i);
                        packing.setFlagWarehousePlanStatus(1);
                        packing.setFlagAsnStatus(2);
                        customClearancePackingDao.save(packing);
                    }
                }
            }
        }
        //检查订单送仓计划标记位
        if(orderIds.size() > 0){
            for (int i = 0; i < orderIds.size(); i++) {
                List<CustomClearancePacking> notWarehousePlanPackings  = customClearancePackingDao.findNotWarehousePlanPackingsByOrderId(orderIds.get(i));
                if(notWarehousePlanPackings == null && notWarehousePlanPackings.isEmpty()){
                    PurchaseContract order = purchaseContractDao.findOne(orderIds.get(i));
                    order.setFlagWarehousePlanStatus(1);
                    order.setFlagWarehousePlanTime(new Date());
                    order.setFlagWarehousePlanId(warehousePlan.getId());
                    purchaseContractDao.save(order);
                }
            }
        }

    }


}
