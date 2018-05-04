package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingPlanDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingPlanDetailVo;
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

/**
 * Created by bryan 2017/11/2.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class OrderShippingPlanDetailService extends ServiceBase {

    @Autowired
    private OrderShippingPlanDetailDao orderShippingPlanDetailDao;

    /**
     * 根据业务id返回所有明细
     * @param orderShippingPlanId 业务ID
     * @return detailVo集合
     */
    public List<OrderShippingPlanDetailVo> findDetailsVoByOrderShippingPlanId(String orderShippingPlanId){
        return BeanMapper.mapList(orderShippingPlanDetailDao.findDetailsByOrderShippingPlanId(orderShippingPlanId), OrderShippingPlanDetail.class, OrderShippingPlanDetailVo.class);
    }

    /**
     * 根据业务id返回所有明细
     * @param orderShippingPlanId 业务ID
     * @return detailVo集合
     */
    public List<OrderShippingPlanDetail> findDetailsByOrderShippingPlanId(String orderShippingPlanId){
        return orderShippingPlanDetailDao.findDetailsByOrderShippingPlanId(orderShippingPlanId);
    }

    public Page<OrderShippingPlanDetailVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<OrderShippingPlanDetail> spec = buildSpecification(params);
        Page<OrderShippingPlanDetail> p = orderShippingPlanDetailDao.findAll(spec, pageRequest);
        Page<OrderShippingPlanDetailVo> page = p.map(new Converter<OrderShippingPlanDetail, OrderShippingPlanDetailVo>() {
            @Override
            public OrderShippingPlanDetailVo convert(OrderShippingPlanDetail orderShippingPlanDetail) {
                return convertToOrderShippingPlanVo(orderShippingPlanDetail);
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     *
     * @param orderShippingPlanDetail
     * @return
     */
    private OrderShippingPlanDetailVo convertToOrderShippingPlanVo(OrderShippingPlanDetail orderShippingPlanDetail) {
        OrderShippingPlanDetailVo vo = BeanMapper.map(orderShippingPlanDetail, OrderShippingPlanDetailVo.class);
        return vo;
    }

    public OrderShippingPlanDetail getOrderShippingPlanDetail(String id) {
        return orderShippingPlanDetailDao.findOne(id);
    }

    /**
     * 通过订单id查询第一条发货计划明细
     * @param orderId 订单id
     * @return 发货计划明细
     */
    public OrderShippingPlanDetail findTopOrderShippingPlanDetailByOrderId(String orderId) {
        return orderShippingPlanDetailDao.findTopByOrderId(orderId);
    }

    private Specification<OrderShippingPlanDetail> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<OrderShippingPlanDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderShippingPlanDetail.class);
        return spec;
    }

    public List<OrderShippingPlanDetail> getNotCostDetail(String shippingPlanId) {
       return orderShippingPlanDetailDao.getNotCostDetail(shippingPlanId);

    }

}
