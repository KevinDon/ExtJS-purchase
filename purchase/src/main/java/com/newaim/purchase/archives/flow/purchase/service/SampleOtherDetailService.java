package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.purchase.dao.SampleOtherDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.SampleOtherDetail;
import com.newaim.purchase.archives.flow.purchase.vo.SampleOtherDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SampleOtherDetailService {

    @Autowired
    private SampleOtherDetailDao sampleOtherDetailDao;

    /**
     * 查找样品其它费用明细
     * @param sampleId
     * @return
     */
    public List<SampleOtherDetail> findOtherDetailsBySampleId(String sampleId){
        return sampleOtherDetailDao.findOtherDetailsBySampleId(sampleId);
    }

    /**
     * 查找样品其它费用明细
     * @param orderId
     * @return
     */
    public List<SampleOtherDetailVo> findOtherDetailVosBySampleId(String orderId){
        List<SampleOtherDetail> data = findOtherDetailsBySampleId(orderId);
        List<SampleOtherDetailVo> result = BeanMapper.mapList(data, SampleOtherDetail.class, SampleOtherDetailVo.class);
        return result;
    }


}
