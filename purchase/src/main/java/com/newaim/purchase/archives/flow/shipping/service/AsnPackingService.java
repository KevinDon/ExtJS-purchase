package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.AsnDao;
import com.newaim.purchase.archives.flow.shipping.dao.AsnPackingDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import com.newaim.purchase.archives.flow.shipping.entity.AsnPacking;
import com.newaim.purchase.archives.flow.shipping.vo.AsnPackingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class AsnPackingService extends ServiceBase {

    @Autowired
    private AsnPackingDao asnPackingDao;

    /**
     * entity转为Vo
     * @param asnPacking
     * @return
     */
    private AsnPackingVo convertToAsnPackingVo(AsnPacking asnPacking){
        AsnPackingVo vo = BeanMapper.map(asnPacking, AsnPackingVo.class);
        return vo;
    }




    /**
     * 根据asnId返回ASN
     * @param asnId
     * @return detail集合
     */
    public AsnPacking getPackingByAsnId(String asnId){
        List<AsnPacking> packings = asnPackingDao.findPackingsByAsnId(asnId);
        return packings != null && packings.size() > 0 ? packings.get(0) : null;
    }

    /**
     * 根据ID获取ASN柜信息
     * @param id
     * @return
     */
    public AsnPacking getAsnPacking(String id){
        return asnPackingDao.findOne(id);
    }

}
