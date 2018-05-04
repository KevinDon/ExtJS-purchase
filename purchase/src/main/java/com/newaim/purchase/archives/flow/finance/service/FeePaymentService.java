package com.newaim.purchase.archives.flow.finance.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.finance.dao.FeePaymentDao;
import com.newaim.purchase.archives.flow.finance.entity.FeePayment;
import com.newaim.purchase.archives.flow.finance.entity.FeeRegister;
import com.newaim.purchase.archives.flow.finance.vo.FeePaymentVo;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FeePaymentService extends ServiceBase {

    @Autowired
    private FeePaymentDao feePaymentDao;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 分页查询正式费用登记数据
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FeePaymentVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FeePayment> spec = buildSpecification(params);
        Page<FeePayment> p = feePaymentDao.findAll(spec, pageRequest);
        Page<FeePaymentVo> page = p.map(new Converter<FeePayment, FeePaymentVo>() {
            @Override
            public FeePaymentVo convert(FeePayment feePayment) {
                FeePaymentVo vo = convertToFeePaymentVo(feePayment);
                vo.setAttachments(attachmentService.listByBusinessId(vo.getBusinessId()));
                return vo;
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param feePayment
     * @return
     */
    private FeePaymentVo convertToFeePaymentVo(FeePayment feePayment){
        FeePaymentVo vo = BeanMapper.map(feePayment, FeePaymentVo.class);
        return vo;
    }

    /**
     * 动态建立关系
     * @param searchParams
     * @return
     */
    private Specification<FeePayment> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FeePayment> spec = DynamicSpecifications.bySearchFilter(filters.values(), FeePayment.class);
        return spec;
    }

    /**
     * 根据ID获取费用支付
     * @param id
     * @return
     */
    public FeePayment getFeePayment(String id){
        return feePaymentDao.findOne(id);
    }

    /**
     * 根据ID获取费用支付
     * @param id
     * @return
     */
    public FeePaymentVo get(String id){
        FeePaymentVo vo =convertToFeePaymentVo(getFeePayment(id));
        vo.setAttachments(attachmentService.listByBusinessId(vo.getBusinessId()));
        return vo;
    }


    public List<FeePayment> findBalancesByOrderIds(List<String> orderIds){
        if(!ListUtils.isEmpty(orderIds)) {
            return feePaymentDao.findBalancesByOrderIds(orderIds);
        }else {
            return Lists.newArrayList();
        }
    }

    public List<FeePaymentVo> findBalancesVoByOrderIds(List<String> orderIds){
        return BeanMapper.mapList(findBalancesByOrderIds(orderIds), FeePayment.class, FeePaymentVo.class);
    }

}
