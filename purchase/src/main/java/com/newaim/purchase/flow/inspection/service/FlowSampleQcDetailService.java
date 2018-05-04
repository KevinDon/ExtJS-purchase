package com.newaim.purchase.flow.inspection.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import com.newaim.purchase.flow.inspection.vo.FlowSampleQcDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSampleQcDetailService extends ServiceBase {

    @Autowired
    private FlowSampleQcDetailDao flowSampleQcDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowSampleQcDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowSampleQcDetailDao.findDetailsByBusinessId(businessId), FlowSampleQcDetail.class, FlowSampleQcDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowSampleQcDetail> findDetailsByBusinessId(String businessId){
        return flowSampleQcDetailDao.findDetailsByBusinessId(businessId);
    }

}
