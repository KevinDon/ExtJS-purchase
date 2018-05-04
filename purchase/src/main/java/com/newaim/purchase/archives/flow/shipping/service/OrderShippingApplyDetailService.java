package com.newaim.purchase.archives.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.shipping.dao.OrderShippingApplyDetailDao;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingApplyDetail;
import com.newaim.purchase.archives.flow.shipping.entity.OrderShippingPlanDetail;
import com.newaim.purchase.archives.flow.shipping.vo.OrderShippingApplyDetailVo;
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
public class OrderShippingApplyDetailService extends ServiceBase {

    @Autowired
    private OrderShippingApplyDetailDao orderShippingApplyDetailDao;

    /**
     * 根据orderShippingApplyId返回所有明细
     * @param orderShippingApplyId
     * @return detailVo集合
     */
    public List<OrderShippingApplyDetailVo> findDetailsVoByOrderShippingApplyId(String orderShippingApplyId){
        return BeanMapper.mapList(orderShippingApplyDetailDao.findDetailsByOrderShippingApplyId(orderShippingApplyId), OrderShippingApplyDetail.class, OrderShippingApplyDetailVo.class);
    }

    /**
     * 根据orderShippingApplyId返回所有明细
     * @param orderShippingApplyId
     * @return detailVo集合
     */
    public List<OrderShippingApplyDetail> findDetailsByOrderShippingApplyId(String orderShippingApplyId){
        return orderShippingApplyDetailDao.findDetailsByOrderShippingApplyId(orderShippingApplyId);
    }


    /**
     * 发货确认详细信息List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<OrderShippingApplyDetailVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<OrderShippingApplyDetail> spec = buildSpecification(params);
        Page<OrderShippingApplyDetail> p = orderShippingApplyDetailDao.findAll(spec, pageRequest);
        Page<OrderShippingApplyDetailVo> page = p.map(new Converter<OrderShippingApplyDetail, OrderShippingApplyDetailVo>() {
            @Override
            public OrderShippingApplyDetailVo convert(OrderShippingApplyDetail orderShippingApplyDetail) {
                return convertToOrderShippingApplyVo(orderShippingApplyDetail);
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param orderShippingApplyDetail
     * @return
     */
    private OrderShippingApplyDetailVo convertToOrderShippingApplyVo(OrderShippingApplyDetail orderShippingApplyDetail) {
        OrderShippingApplyDetailVo vo = BeanMapper.map(orderShippingApplyDetail, OrderShippingApplyDetailVo.class);
        return vo;
    }

    private Specification<OrderShippingApplyDetail> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<OrderShippingApplyDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), OrderShippingApplyDetail.class);
        return spec;
    }

    /**
     * 根据ID查找发货确认详细信息
     * @param id
     * @return
     */
    public OrderShippingApplyDetail getOrderShippingApplyDetail(String id) {
        return orderShippingApplyDetailDao.findOne(id);
    }


}
