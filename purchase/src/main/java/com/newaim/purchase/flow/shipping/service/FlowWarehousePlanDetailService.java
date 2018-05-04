package com.newaim.purchase.flow.shipping.service;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.shipping.dao.FlowWarehousePlanDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlanDetail;
import com.newaim.purchase.flow.shipping.vo.FlowWarehousePlanDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowWarehousePlanDetailService extends ServiceBase {

    @Autowired
    private FlowWarehousePlanDetailDao flowWarehousePlanDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowWarehousePlanDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowWarehousePlanDetailDao.findDetailsByBusinessId(businessId), FlowWarehousePlanDetail.class, FlowWarehousePlanDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowWarehousePlanDetail> findDetailsByBusinessId(String businessId){
        return flowWarehousePlanDetailDao.findDetailsByBusinessId(businessId);
    }

    public List<FlowWarehousePlanDetail> findDetailsByContainerNumber(String containerNumber){
        return flowWarehousePlanDetailDao.findDetailsByContainerNumber(containerNumber);
    }
    public List<FlowWarehousePlanDetailVo> findDetailsVoByContainerNumber(String containerNumber){
        return BeanMapper.mapList(findDetailsByContainerNumber(containerNumber), FlowWarehousePlanDetail.class, FlowWarehousePlanDetailVo.class);
    }


}
