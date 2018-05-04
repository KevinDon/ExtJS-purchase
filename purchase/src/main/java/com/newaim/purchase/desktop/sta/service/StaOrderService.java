package com.newaim.purchase.desktop.sta.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.desktop.sta.dao.StaOrderDao;
import com.newaim.purchase.desktop.sta.dao.ViewStaOrderDao;
import com.newaim.purchase.desktop.sta.entity.StaOrder;
import com.newaim.purchase.desktop.sta.vo.StaOrderVo;
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
public class StaOrderService extends ServiceBase {

    @Autowired
    private StaOrderDao staOrderDao;

    @Autowired
    private ViewStaOrderDao viewStaOrderDao;

    public Page<StaOrderVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<StaOrder> spec = buildSpecification(params);
        Page<StaOrder> p = staOrderDao.findAll(spec, pageRequest);
        Page<StaOrderVo> page = p.map(new Converter<StaOrder, StaOrderVo>() {
            @Override
            public StaOrderVo convert(StaOrder staOrder) {
                return convertToStaOrderVo(staOrder);
            }
        });
        return page;
    }

    private StaOrderVo convertToStaOrderVo(StaOrder staOrder){
        StaOrderVo vo = BeanMapper.map(staOrder, StaOrderVo.class);
        return vo;
    }

    private Specification<StaOrder> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<StaOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), StaOrder.class);
        return spec;
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaOrder getStaOrder(String orderId){
        return staOrderDao.findOne(orderId);
    }

    /**
     * 根据orderId获取订单报表信息
     * @param orderId
     * @return
     */
    public StaOrderVo get(String orderId){
        StaOrderVo vo =convertToStaOrderVo(getStaOrder(orderId));
        return vo;
    }

    /**
     * 同步视图
     *
     * @param
     * @return
     */
    @Transactional
    public List<StaOrder> copyFromView() {
        try {
            staOrderDao.deleteFlowData();
            staOrderDao.updateFlowData();
            return staOrderDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
