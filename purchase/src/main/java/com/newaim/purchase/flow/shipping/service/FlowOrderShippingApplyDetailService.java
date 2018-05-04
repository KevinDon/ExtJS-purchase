package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.shipping.dao.FlowOrderShippingApplyDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowOrderShippingApplyDetail;
import com.newaim.purchase.flow.shipping.vo.FlowOrderShippingApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowOrderShippingApplyDetailService extends ServiceBase {

    @Autowired
    private FlowOrderShippingApplyDetailDao flowOrderShippingApplyDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderShippingApplyDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowOrderShippingApplyDetailDao.findDetailsByBusinessId(businessId), FlowOrderShippingApplyDetail.class, FlowOrderShippingApplyDetailVo.class);
    }
    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowOrderShippingApplyDetail> findDetailsByBusinessId(String businessId){
        return flowOrderShippingApplyDetailDao.findDetailsByBusinessId(businessId);
    }


}
