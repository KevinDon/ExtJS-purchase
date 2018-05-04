package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.TroubleTicketProductDetailDao;
import com.newaim.purchase.archives.product.entity.TroubleTicketProductDetail;
import com.newaim.purchase.archives.product.vo.TroubleTicketProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TroubleTicketProductDetailService extends ServiceBase{

    @Autowired
    private TroubleTicketProductDetailDao troubleTicketProductDetailDao;

    public List<TroubleTicketProductDetail> findDetailsByTroubleTicketProductId(String troubleTicketProductId){
        return troubleTicketProductDetailDao.findDetailsByTroubleTicketProductId(troubleTicketProductId);
    }

    public List<TroubleTicketProductDetailVo> findDetailsVoByTroubleTicketProductId(String troubleTicketProductId){
        return BeanMapper.mapList(troubleTicketProductDetailDao.findDetailsByTroubleTicketProductId(troubleTicketProductId), TroubleTicketProductDetail.class, TroubleTicketProductDetailVo.class);
    }

}
