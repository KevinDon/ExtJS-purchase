package com.newaim.purchase.archives.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.purchase.dao.PurchasePlanDetailDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchasePlanDetail;
import com.newaim.purchase.archives.flow.purchase.vo.PurchasePlanDetailVo;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PurchasePlanDetailService extends ServiceBase {

    @Autowired
    private PurchasePlanDetailDao purchasePlanDetailDao;

    @Autowired
    private ProductService productService;

    public Page<PurchasePlanDetailVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<PurchasePlanDetail> spec = buildSpecification(params);
        Page<PurchasePlanDetail> p = purchasePlanDetailDao.findAll(spec, pageRequest);
        Page<PurchasePlanDetailVo> page = p.map(new Converter<PurchasePlanDetail, PurchasePlanDetailVo>() {
            @Override
            public PurchasePlanDetailVo convert(PurchasePlanDetail purchasePlanDetail) {
                return convertToPurchasePlanDetailVo(purchasePlanDetail);
            }
        });
        return page;
    }

    private PurchasePlanDetailVo convertToPurchasePlanDetailVo(PurchasePlanDetail purchasePlanDetail){
        PurchasePlanDetailVo vo = BeanMapper.map(purchasePlanDetail, PurchasePlanDetailVo.class);
        return vo;
    }

    private Specification<PurchasePlanDetail> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PurchasePlanDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), PurchasePlanDetail.class);
        return spec;
    }

    /**
     * 根据id获取采购计划信息
     * @param id
     * @return
     */
    public PurchasePlanDetail getPurchasePlanDetail(String id){
        return purchasePlanDetailDao.findOne(id);
    }

    /**
     * 根据id获取采购计划信息
     * @param id
     * @return
     */
    public PurchasePlanDetailVo get(String id){
        PurchasePlanDetailVo vo =convertToPurchasePlanDetailVo(getPurchasePlanDetail(id));
        return vo;
    }




}
