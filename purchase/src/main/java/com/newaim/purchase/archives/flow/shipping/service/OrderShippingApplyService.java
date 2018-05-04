package com.newaim.purchase.archives.flow.shipping.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApply;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingApplyVo;
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
public class OrderShippingApplyService extends ServiceBase {

    @Autowired
    private OrderShippingApplyDao orderShippingApplyDao;

    @Autowired
    private OrderShippingApplyDetailService orderShippingApplyDetailService;

    /**
     * 发货确认信息List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<OrderShippingApplyVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<OrderShippingApply> spec = buildSpecification(params);
        Page<OrderShippingApply> p = orderShippingApplyDao.findAll(spec, pageRequest);
        Page<OrderShippingApplyVo> page = p.map(new Converter<OrderShippingApply, OrderShippingApplyVo>() {
            @Override
            public OrderShippingApplyVo convert(OrderShippingApply orderShippingApply) {
                return convertToOrderShippingApplyVo(orderShippingApply);
            }
        });
        return page;
    }

    private OrderShippingApplyVo convertToOrderShippingApplyVo(OrderShippingApply orderShippingApply){
        OrderShippingApplyVo vo = BeanMapper.map(orderShippingApply, OrderShippingApplyVo.class);
        return vo;
    }

    /**
     * 根据id获取发货确认
     * @param id
     * @return 发货确认
     */
    public OrderShippingApply getOrderShippingApply(String id){
        return orderShippingApplyDao.findOne(id);
    }

    /**
     * 根据id获取发货确认
     * @param id
     * @return 发货确认
     */
    public OrderShippingApplyVo get(String id){
        OrderShippingApplyVo vo =convertToOrderShippingApplyVo(getOrderShippingApply(id));
        return vo;
    }



    private Specification<OrderShippingApply> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<OrderShippingApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderShippingApply.class);
        return spec;
    }

}
