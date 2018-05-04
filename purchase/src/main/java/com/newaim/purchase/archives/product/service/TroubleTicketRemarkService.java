package com.newaim.purchase.archives.product.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.TroubleTicketRemarkDao;
import com.newaim.purchase.archives.product.entity.TroubleTicketRemark;
import com.newaim.purchase.archives.product.vo.TroubleTicketRemarkVo;
import org.docx4j.wml.Tr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TroubleTicketRemarkService extends ServiceBase{

    @Autowired
    private TroubleTicketRemarkDao troubleTicketRemarkDao;

    public List<TroubleTicketRemark> findRemarksByTroubleTicketId(String troubleTicketId){
        return troubleTicketRemarkDao.findRemarksByTroubleTicketId(troubleTicketId);
    }

    public List<TroubleTicketRemarkVo> findRemarksVoByTroubleTicketId(String troubleTicketId){
        return BeanMapper.mapList(troubleTicketRemarkDao.findRemarksByTroubleTicketId(troubleTicketId), TroubleTicketRemark.class, TroubleTicketRemarkVo.class);
    }

    public TroubleTicketRemark getTroubleTicketRemark(String id){
        return troubleTicketRemarkDao.findOne(id);
    }

    public TroubleTicketRemarkVo get(String id){
        return BeanMapper.map(getTroubleTicketRemark(id), TroubleTicketRemarkVo.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TroubleTicketRemark remark){
        troubleTicketRemarkDao.save(remark);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        troubleTicketRemarkDao.delete(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTroubleTicketId(String troubleTicketId){
        troubleTicketRemarkDao.deleteByTroubleTicketId(troubleTicketId);
    }

}
