package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaOrderDao;
import com.newaim.purchase.desktop.sta.dao.StaProblemDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaProblemDao;
import com.newaim.purchase.desktop.sta.entity.*;
import com.newaim.purchase.desktop.sta.vo.StaOrderVo;
import com.newaim.purchase.desktop.sta.vo.StaProblemVo;
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
public class StaProblemService extends ServiceBase {

    @Autowired
    private StaProblemDao staProblemDao;

    @Autowired
    private ViewStaProblemDao viewStaProblemDao;

    public Page<StaProblemVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaProblem> spec = buildSpecification(params);
        Page<StaProblem> p = staProblemDao.findAll(spec, pageRequest);
        Page<StaProblemVo> page = p.map(new Converter<StaProblem, StaProblemVo>() {
            @Override
            public StaProblemVo convert(StaProblem staProblem) {
                return convertToStaProblemVo(staProblem);
            }
        });
        return page;
    }

    private StaProblemVo convertToStaProblemVo(StaProblem staProblem){
        StaProblemVo vo = BeanMapper.map(staProblem, StaProblemVo.class);
        return vo;
    }

    private Specification<StaProblem> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaProblem> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaProblem.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaProblem getStaProblem(String orderId){
        return staProblemDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaProblemVo get(String orderId){
        StaProblemVo vo =convertToStaProblemVo(getStaProblem(orderId));
        return vo;
    }

    /**
     * 同步视图
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<StaProblem> copyFromView(){
        List<ViewStaProblem> viewStaProblems = viewStaProblemDao.findAll();
        List<StaProblem> staProblems = BeanMapper.mapList(viewStaProblems,ViewStaProblem.class, StaProblem.class);
        if (staProblems!=null){
            for (StaProblem staProblem : staProblems){
                staProblem.setId(null);
                staProblemDao.save(staProblem);
            }
        }
        return staProblems;
    }


}
