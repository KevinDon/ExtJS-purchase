package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaCostDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaCostDao;
import com.newaim.purchase.desktop.sta.entity.StaCost;
import com.newaim.purchase.desktop.sta.vo.StaCostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class StaCostService extends ServiceBase {

    @Autowired
    private StaCostDao staCostDao;

    @Autowired
    private ViewStaCostDao viewStaCostDao;

    public Page<StaCostVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaCost> spec = buildSpecification(params);
        Page<StaCost> p = staCostDao.findAll(spec, pageRequest);
        Page<StaCostVo> page = p.map(new Converter<StaCost, StaCostVo>() {
            @Override
            public StaCostVo convert(StaCost staCost) {
                return convertToStaCostVo(staCost);
            }
        });
        return page;
    }

    private StaCostVo convertToStaCostVo(StaCost staCost){
        StaCostVo vo = BeanMapper.map(staCost, StaCostVo.class);
        return vo;
    }

    private Specification<StaCost> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaCost> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaCost.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaCost getStaCost(String orderId){
        return staCostDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaCostVo get(String orderId){
        StaCostVo vo =convertToStaCostVo(getStaCost(orderId));
        return vo;
    }

    /**
     * 同步视图
     * @param
     * @return
     */
    @Transactional
    public List<StaCost> copyFromView() {
        try {
            staCostDao.deleteFlowData();
            staCostDao.updateFlowData();
            return staCostDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



}
