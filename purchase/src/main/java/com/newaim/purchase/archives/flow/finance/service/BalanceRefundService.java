package com.newaim.purchase.archives.flow.finance.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.finance.vo.BalanceRefundVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.apache.commons.lang3.StringUtils;
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
public class BalanceRefundService extends FlowServiceBase {

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private VendorService vendorService;

    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<BalanceRefundVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<BalanceRefund> spec = buildSpecification(params);
        Page<BalanceRefund> p = balanceRefundDao.findAll(spec, pageRequest);
        Page<BalanceRefundVo> page = p.map(new Converter<BalanceRefund, BalanceRefundVo>() {
            @Override
            public BalanceRefundVo convert(BalanceRefund flowBalanceRefund) {
                return convertToBalanceRefundVo(flowBalanceRefund);
            }
        });
        return page;
    }

    /**
     * 转换实体类为Vo
     * @param flowBalanceRefund
     * @return
     */
    private BalanceRefundVo convertToBalanceRefundVo(BalanceRefund flowBalanceRefund){
        BalanceRefundVo vo = BeanMapper.map(flowBalanceRefund, BalanceRefundVo.class);
        return vo;
    }

    private Specification<BalanceRefund> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<BalanceRefund> spec = DynamicSpecifications.bySearchFilter(filters.values(), BalanceRefund.class);
        return spec;
    }

    public BalanceRefund getBalanceRefund(String id){
        return balanceRefundDao.findOne(id);
    }

    /**
     * 获取差额退款信息
     * @param id
     * @return
     */
    public BalanceRefundVo get(String id){
        BalanceRefundVo vo =convertToBalanceRefundVo(getBalanceRefund(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        return vo;
    }

}
