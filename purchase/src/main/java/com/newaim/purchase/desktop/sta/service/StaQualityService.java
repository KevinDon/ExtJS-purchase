package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaQualityDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaQualityDao;
import com.newaim.purchase.desktop.sta.entity.*;
import com.newaim.purchase.desktop.sta.vo.StaQualityVo;
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
public class StaQualityService extends ServiceBase {

    @Autowired
    private StaQualityDao staQualityDao;

    @Autowired
    private ViewStaQualityDao viewStaQualityDao;

    public Page<StaQualityVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaQuality> spec = buildSpecification(params);
        Page<StaQuality> p = staQualityDao.findAll(spec, pageRequest);
        Page<StaQualityVo> page = p.map(new Converter<StaQuality, StaQualityVo>() {
            @Override
            public StaQualityVo convert(StaQuality staQuality) {
                return convertToStaQualityVo(staQuality);
            }
        });
        return page;
    }

    private StaQualityVo convertToStaQualityVo(StaQuality staQuality){
        StaQualityVo vo = BeanMapper.map(staQuality, StaQualityVo.class);
        return vo;
    }

    private Specification<StaQuality> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaQuality> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaQuality.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaQuality getStaQuality(String orderId){
        return staQualityDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaQualityVo get(String orderId){
        StaQualityVo vo =convertToStaQualityVo(getStaQuality(orderId));
        return vo;
    }

    /**
     * 同步视图
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<StaQuality> copyFromView(){
        List<ViewStaQuality> viewStaQualities = viewStaQualityDao.findAll();
        List<StaQuality> staQualities = BeanMapper.mapList(viewStaQualities,ViewStaQuality.class, StaQuality.class);
        if (staQualities!=null){
            for (StaQuality staQuality : staQualities){
                staQuality.setId(null);
                staQualityDao.save(staQuality);
            }
        }
        return staQualities;
    }

}
