package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.WarehousePlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlanDetail;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WarehousePlanDetailService extends ServiceBase {

    @Autowired
    private WarehousePlanDetailDao warehousePlanDetailDao;


    /**
     * 根据送仓计划ID查找明细
     * @param warehousePlanId
     * @return
     */
    public List<WarehousePlanDetailVo> findDetailsVoByWarehousePlanId(String warehousePlanId){
        return BeanMapper.mapList(findDetailsByWarehousePlanId(warehousePlanId), WarehousePlanDetail.class, WarehousePlanDetailVo.class);
    }

    public List<WarehousePlanDetail> findDetailsByWarehousePlanId(String warehousePlanId){
        return warehousePlanDetailDao.findDetailsByWarehousePlanId(warehousePlanId);
    }

    public List<WarehousePlanDetail> findWarehousePlanDetails(){
        return warehousePlanDetailDao.findAll();
    }


    private WarehousePlanDetailVo convertToWarehousePlanDetailVo(WarehousePlanDetail warehousePlanDetail){
        WarehousePlanDetailVo vo = BeanMapper.map(warehousePlanDetail, WarehousePlanDetailVo.class);
        return vo;
    }

    private Specification<WarehousePlanDetail> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<WarehousePlanDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), WarehousePlanDetail.class);
        return spec;
    }




}

