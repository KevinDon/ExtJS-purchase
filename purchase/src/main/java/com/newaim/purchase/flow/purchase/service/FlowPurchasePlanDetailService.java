package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowPurchasePlanDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowPurchasePlanDetail;
import com.newaim.purchase.flow.purchase.vo.FlowPurchasePlanDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowPurchasePlanDetailService extends ServiceBase {

    @Autowired
    private FlowPurchasePlanDetailDao flowPurchasePlanDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowPurchasePlanDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowPurchasePlanDetailDao.findDetailsByBusinessId(businessId), FlowPurchasePlanDetail.class, FlowPurchasePlanDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowPurchasePlanDetail> findDetailsByBusinessId(String businessId){
        return flowPurchasePlanDetailDao.findDetailsByBusinessId(businessId);
    }

    /**
     * 通过产品id查找历史报价
     * @param productId 产品id
     */
    public List<FlowPurchasePlanDetailVo> findDetailsVoByProductId(String productId){
        return BeanMapper.mapList(flowPurchasePlanDetailDao.findDetailsByProductId(productId), FlowPurchasePlanDetail.class, FlowPurchasePlanDetailVo.class);
    }




}
