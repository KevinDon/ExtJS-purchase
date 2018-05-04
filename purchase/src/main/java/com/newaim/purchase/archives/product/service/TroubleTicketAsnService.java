package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.TroubleTicketAsnDao;
import com.newaim.purchase.archives.product.entity.TroubleTicketAsn;
import com.newaim.purchase.archives.product.vo.TroubleTicketAsnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TroubleTicketAsnService extends ServiceBase{

    @Autowired
    private TroubleTicketAsnDao troubleTicketAsnDao;

    public List<TroubleTicketAsn> findTroubleTicketAsnsByTroubleTicketId(String troubleTicketId){
        return troubleTicketAsnDao.findTroubleTicketAsnsByTroubleTicketId(troubleTicketId);
    }

    public List<TroubleTicketAsnVo> findTroubleTicketAsnsVoByTroubleTicketId(String troubleTicketId){
        return BeanMapper.mapList(troubleTicketAsnDao.findTroubleTicketAsnsByTroubleTicketId(troubleTicketId), TroubleTicketAsn.class, TroubleTicketAsnVo.class);
    }

}
