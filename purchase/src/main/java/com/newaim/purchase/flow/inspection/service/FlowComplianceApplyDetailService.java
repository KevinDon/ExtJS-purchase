package com.newaim.purchase.flow.inspection.service;


import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;
import com.newaim.purchase.flow.inspection.vo.FlowComplianceApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowComplianceApplyDetailService extends ServiceBase {

    @Autowired
    private FlowComplianceApplyDetailDao flowComplianceApplyDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detailVo集合
     */
    public List<FlowComplianceApplyDetailVo> findDetailsVoByBusinessId(String businessId){
        return BeanMapper.mapList(flowComplianceApplyDetailDao.findDetailsByBusinessId(businessId), FlowComplianceApplyDetail.class, FlowComplianceApplyDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param businessId 业务ID
     * @return detail集合
     */
    public List<FlowComplianceApplyDetail> findDetailsByBusinessId(String businessId){
        return flowComplianceApplyDetailDao.findDetailsByBusinessId(businessId);
    }

}
