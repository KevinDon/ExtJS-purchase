package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaProblemTeamDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaProblemTeamDao;
import com.newaim.purchase.desktop.sta.entity.*;
import com.newaim.purchase.desktop.sta.vo.StaProblemTeamVo;
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
public class StaProblemTeamService extends ServiceBase {

    @Autowired
    private StaProblemTeamDao staProblemTeamDao;

    @Autowired
    private ViewStaProblemTeamDao viewStaProblemTeamDao;

    public Page<StaProblemTeamVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaProblemTeam> spec = buildSpecification(params);
        Page<StaProblemTeam> p = staProblemTeamDao.findAll(spec, pageRequest);
        Page<StaProblemTeamVo> page = p.map(new Converter<StaProblemTeam, StaProblemTeamVo>() {
            @Override
            public StaProblemTeamVo convert(StaProblemTeam staProblemTeam) {
                return convertToStaProblemTeamVo(staProblemTeam);
            }
        });
        return page;
    }

    private StaProblemTeamVo convertToStaProblemTeamVo(StaProblemTeam staProblemTeam){
        StaProblemTeamVo vo = BeanMapper.map(staProblemTeam, StaProblemTeamVo.class);
        return vo;
    }

    private Specification<StaProblemTeam> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaProblemTeam> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaProblemTeam.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaProblemTeam getStaProblemTeam(String orderId){
        return staProblemTeamDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaProblemTeamVo get(String orderId){
        StaProblemTeamVo vo =convertToStaProblemTeamVo(getStaProblemTeam(orderId));
        return vo;
    }

    /**
     * 同步视图
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<StaProblemTeam> copyFromView(){
        List<ViewStaProblemTeam> viewStaProblemTeams = viewStaProblemTeamDao.findAll();
        List<StaProblemTeam> staProblemTeams = BeanMapper.mapList(viewStaProblemTeams,ViewStaProblemTeam.class, StaProblemTeam.class);
        if (staProblemTeams!=null){
            for (StaProblemTeam staProblemTeam : staProblemTeams){
                staProblemTeam.setId(null);
                staProblemTeamDao.save(staProblemTeam);
            }
        }
        return staProblemTeams;
    }

}
