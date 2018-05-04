package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.vo.FlowAsnPackingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowAsnPackingService extends ServiceBase {

    @Autowired
    private FlowAsnPackingDao flowAsnPackingDao;

    @Autowired
    private FlowAsnPackingDetailService flowAsnPackingDetailService;

    /**
     * 根据businessId返回所有明细
     * @param businessId
     * @return
     */
    public List<FlowAsnPackingVo> findPackingsVoByBusinessId(String businessId){
        List<FlowAsnPacking> list = findPackingsByBusinessId(businessId);
        List<FlowAsnPackingVo> voList = new ArrayList<FlowAsnPackingVo>();
        for(int i = 0; i < list.size(); i++){
            FlowAsnPackingVo vo = convertToFlowAsnPackingVo(list.get(i));
            vo.setAsnPackingDetails(flowAsnPackingDetailService.findPackingDetailsVoByAsnPackingId(vo.getId()));
            voList.add(vo);
        }
        return  voList;
    }

    private FlowAsnPackingVo convertToFlowAsnPackingVo(FlowAsnPacking flowAsnPacking){
        FlowAsnPackingVo vo = BeanMapper.map(flowAsnPacking, FlowAsnPackingVo.class);
        return vo;
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowAsnPacking> findPackingsByBusinessId(String businessId){
        return flowAsnPackingDao.findPackingsByBusinessId(businessId);
    }

}
