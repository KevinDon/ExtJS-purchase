package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlan;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanVo;
import org.apache.commons.beanutils.BeanMap;
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
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class OrderShippingPlanService extends ServiceBase {

    @Autowired
    private OrderShippingPlanDao orderShippingPlanDao;

    @Autowired
    private OrderShippingPlanDetailService orderShippingPlanDetailService;


    public Page<OrderShippingPlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<OrderShippingPlan> spec = buildSpecification(params);
        Page<OrderShippingPlan> p = orderShippingPlanDao.findAll(spec, pageRequest);
        Page<OrderShippingPlanVo> page = p.map(orderShippingPlan -> {
            OrderShippingPlanVo vo = convertToOrderShippingPlanVo(orderShippingPlan);
            vo.setDetails(orderShippingPlanDetailService.findDetailsVoByOrderShippingPlanId(vo.getId()));
            return vo;
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param orderShippingPlan
     * @return
     */
    private OrderShippingPlanVo convertToOrderShippingPlanVo(OrderShippingPlan orderShippingPlan){
        OrderShippingPlanVo vo = BeanMapper.map(orderShippingPlan, OrderShippingPlanVo.class);
        return vo;
    }

    /**
     * 根据id获取发货计划
     * @param id
     * @return 发货计划
     */
    public OrderShippingPlan getOrderShippingPlan(String id){
        return orderShippingPlanDao.findOne(id);
    }

    /**
     * 根据id获取发货计划
     * @param id
     * @return 发货计划
     */
    public OrderShippingPlanVo get(String id){
        OrderShippingPlanVo vo =convertToOrderShippingPlanVo(getOrderShippingPlan(id));
        return vo;
    }


    private Specification<OrderShippingPlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<OrderShippingPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderShippingPlan.class);
        return spec;
    }


    public List<OrderShippingPlanDetailVo> getDetail(String shippingOderId) {
        return BeanMapper.mapList(orderShippingPlanDetailService.getNotCostDetail(shippingOderId),OrderShippingPlanDetail.class, OrderShippingPlanDetailVo.class);
    }
    
    public List<OrderShippingPlanDetailVo> findDetailsByOrderShippingPlanId(String shippingOderId) {
    	return BeanMapper.mapList(orderShippingPlanDetailService.findDetailsByOrderShippingPlanId(shippingOderId),OrderShippingPlanDetail.class, OrderShippingPlanDetailVo.class);
    }


}
