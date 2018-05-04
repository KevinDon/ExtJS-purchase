package com.newaim.purchase.archives.flow.shipping.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.WarehousePlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlan;
import com.newaim.purchase.archives.flow.shipping.vo.WarehousePlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WarehousePlanService extends ServiceBase {

    @Autowired
    private WarehousePlanDao warehousePlanDao;

    @Autowired
    private WarehousePlanDetailService warehousePlanDetailService;

    /**
     * 分页获取送仓计划
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<WarehousePlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<WarehousePlan> spec = buildSpecification(params);
        Page<WarehousePlan> p = warehousePlanDao.findAll(spec, pageRequest);
        Page<WarehousePlanVo> page = p.map(new Converter<WarehousePlan, WarehousePlanVo>() {
            @Override
            public WarehousePlanVo convert(WarehousePlan warehousePlan) {
                return convertToWarehousePlanVo(warehousePlan);
            }
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param warehousePlan
     * @return
     */
    private WarehousePlanVo convertToWarehousePlanVo(WarehousePlan warehousePlan){
        WarehousePlanVo vo = BeanMapper.map(warehousePlan, WarehousePlanVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<WarehousePlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<WarehousePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), WarehousePlan.class);
        return spec;
    }

    /**
     * 根据ID获取送仓计划
     * @param id
     * @return
     */
    public WarehousePlan getWarehousePlan(String id){
        return warehousePlanDao.findOne(id);
    }

}
