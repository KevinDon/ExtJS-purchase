package com.newaim.purchase.flow.purchase.service;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.purchase.dao.FlowCustomClearancePackingDao;
import com.newaim.purchase.flow.purchase.entity.FlowCustomClearancePacking;
import com.newaim.purchase.flow.purchase.vo.FlowCustomClearancePackingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowCustomClearancePackingService extends ServiceBase {

    @Autowired
    private FlowCustomClearancePackingDao flowCustomClearancePackingDao;

    @Autowired
    private FlowCustomClearancePackingDetailService flowCustomClearancePackingDetailService;


    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowCustomClearancePackingVo> findPackingsVoByBusinessId(String businessId){
        List<FlowCustomClearancePacking> list = flowCustomClearancePackingDao.findPackingsByBusinessId(businessId);
        List<FlowCustomClearancePackingVo> voList = new ArrayList<FlowCustomClearancePackingVo>();
        for(int i = 0; i < list.size(); i++){
            FlowCustomClearancePackingVo vo = convertToFlowCustomClearancePackingVo(list.get(i));
            vo.setPackingList(flowCustomClearancePackingDetailService.findPackingDetailsVoByPackingId(vo.getId()));
            voList.add(vo);
        }

        return  voList;
    }

    private FlowCustomClearancePackingVo convertToFlowCustomClearancePackingVo(FlowCustomClearancePacking flowCustomClearancePacking){
        FlowCustomClearancePackingVo vo = BeanMapper.map(flowCustomClearancePacking, FlowCustomClearancePackingVo.class);
        return vo;
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowCustomClearancePacking> findPackingsByBusinessId(String businessId){
        return flowCustomClearancePackingDao.findPackingsByBusinessId(businessId);
    }

}
