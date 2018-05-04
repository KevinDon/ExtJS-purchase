package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingPlanDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingPlanDetail;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingPlanDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bryan 2017/11/2.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowOrderShippingPlanDetailService extends ServiceBase {

    @Autowired
    private FlowOrderShippingPlanDetailDao flowOrderShippingPlanDetailDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderShippingPlanDetailVo> findDetailsVoByBusinessId(String businessId){
        List<FlowOrderShippingPlanDetailVo> data = BeanMapper.mapList(flowOrderShippingPlanDetailDao.findDetailsByBusinessId(businessId), FlowOrderShippingPlanDetail.class, FlowOrderShippingPlanDetailVo.class);
        if(data != null && data.size() > 0){
            for (int i = 0; i < data.size(); i++) {
                FlowOrderShippingPlanDetailVo detail = data.get(i);
                if(StringUtils.isNotBlank(detail.getOrderId())){
                    PurchaseContract order = purchaseContractDao.findOne(detail.getOrderId());
                    if(order != null){
                        detail.setCurrency(order.getCurrency());
                        detail.setRateAudToRmb(order.getRateAudToRmb());
                        detail.setRateAudToUsd(order.getRateAudToUsd());
                        detail.setTotalPriceAud(order.getTotalPriceAud());
                        detail.setTotalPriceRmb(order.getTotalPriceRmb());
                        detail.setTotalPriceUsd(order.getTotalPriceUsd());
                    }
                }
            }
        }
        return data;
    }

    /**
     * 通过订单id查询第一条发货计划明细
     * @param orderId 订单id
     * @return 发货计划明细
     */
    public FlowOrderShippingPlanDetail findTopFlowOrderShippingPlanDetailByOrderId(String orderId) {
        return flowOrderShippingPlanDetailDao.findTopByOrderId(orderId);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderShippingPlanDetail> findDetailsByBusinessId(String businessId){
        return flowOrderShippingPlanDetailDao.findDetailsByBusinessId(businessId);
    }



}
