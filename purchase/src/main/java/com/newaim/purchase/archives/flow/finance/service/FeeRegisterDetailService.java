package com.newaim.purchase.archives.flow.finance.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.finance.dao.FeeRegisterDetailDao;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegisterDetail;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FeeRegisterDetailService extends ServiceBase {

    @Autowired
    private FeeRegisterDetailDao feeRegisterDetailDao;

    /**
     * 根据feeRegisterId返回所有明细
     * @param feeRegisterId 业务ID
     * @return detailVo集合
     */
    public List<FeeRegisterDetailVo> findDetailsVoByFeeRegisterId(String feeRegisterId){
        return BeanMapper.mapList(feeRegisterDetailDao.findDetailsByFeeRegisterId(feeRegisterId), FeeRegisterDetail.class, FeeRegisterDetailVo.class);
    }

    /**
     * 根据feeRegisterId返回所有明细
     * @param feeRegisterId 业务ID
     * @param applyCost
     * @return detailVo集合
     */
    public List<FeeRegisterDetailVo> findDetailsByFeeRegisterIdAndApplyCost(String feeRegisterId, Integer applyCost){
        return BeanMapper.mapList(feeRegisterDetailDao.findDetailsByFeeRegisterIdAndApplyCost(feeRegisterId,applyCost), FeeRegisterDetail.class, FeeRegisterDetailVo.class);
    }

    /**
     * 根据feeRegisterId返回所有明细
     * @param feeRegisterId 业务ID
     * @return detail集合
     */
    public List<FeeRegisterDetail> findDetailsByFeeRegisterId(String feeRegisterId){
        return feeRegisterDetailDao.findDetailsByFeeRegisterId(feeRegisterId);
    }

    /**
     * 根据feeRegisterId和类型返回所有明细
     */
    public List<FeeRegisterDetailVo> findDetailsVoByFeeRegisterIdAndType(String feeRegisterId, Integer type){
        return BeanMapper.mapList(feeRegisterDetailDao.findDetailsByFeeRegisterIdAndType(feeRegisterId, type), FeeRegisterDetail.class, FeeRegisterDetailVo.class);
    }

    /**
     * 根据feeRegisterId和类型返回所有明细
     */
    public List<FeeRegisterDetail> findDetailsByFeeRegisterIdAndType(String feeRegisterId, Integer type){
        return feeRegisterDetailDao.findDetailsByFeeRegisterIdAndType(feeRegisterId,type);
    }

}
