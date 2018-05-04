package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaOrdersCycleDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaOrdersCycleDao;
import com.newaim.purchase.desktop.sta.entity.StaOrdersCycle;
import com.newaim.purchase.desktop.sta.vo.StaOrdersCycleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class StaOrdersCycleService extends ServiceBase {

    @Autowired
    private StaOrdersCycleDao staOrdersCycleDao;

    @Autowired
    private ViewStaOrdersCycleDao viewStaOrdersCycleDao;

    public Page<StaOrdersCycleVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaOrdersCycle> spec = buildSpecification(params);
        Page<StaOrdersCycle> p = staOrdersCycleDao.findAll(spec, pageRequest);
        Page<StaOrdersCycleVo> page = p.map(new Converter<StaOrdersCycle, StaOrdersCycleVo>() {
            @Override
            public StaOrdersCycleVo convert(StaOrdersCycle staOrdersCycle) {
                return convertToStaOrdersCycleVo(staOrdersCycle);
            }
        });
        return page;
    }

    private StaOrdersCycleVo convertToStaOrdersCycleVo(StaOrdersCycle staOrdersCycle){
        StaOrdersCycleVo vo = BeanMapper.map(staOrdersCycle, StaOrdersCycleVo.class);
        return vo;
    }

    private Specification<StaOrdersCycle> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaOrdersCycle> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaOrdersCycle.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaOrdersCycle getStaOrdersCycle(String orderId){
        return staOrdersCycleDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaOrdersCycleVo get(String orderId){
        StaOrdersCycleVo vo =convertToStaOrdersCycleVo(getStaOrdersCycle(orderId));
        return vo;
    }

    /**
     * 同步视图
     *
     * @param
     * @return
     */
    @Transactional
    public List<StaOrdersCycle> copyFromView() {
        try {
            staOrdersCycleDao.deleteFlowData();
            staOrdersCycleDao.updateFlowData();
            return staOrdersCycleDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }




}
