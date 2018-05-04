package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.dao.SampleDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.SampleDetail;
import com.newaim.purchase.archives.flow.purchase.vo.SampleDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SampleDetailService extends ServiceBase {

    @Autowired
    private SampleDetailDao sampleDetailDao;


    public List<SampleDetail> findDetailsBySampleId(String sampleId){
        return sampleDetailDao.findBySampleId(sampleId);
    }

    public List<SampleDetailVo> findDetailVosBySampleId(String sampleId){
        return BeanMapper.mapList(findDetailsBySampleId(sampleId), SampleDetail.class, SampleDetailVo.class);
    }

    /**
     * 根据id获取样品明细
     * @param id
     * @return
     */
    public SampleDetail getSampleDetail(String id){
        return sampleDetailDao.findOne(id);
    }


}
