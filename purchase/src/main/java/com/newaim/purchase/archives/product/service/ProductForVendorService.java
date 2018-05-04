package com.newaim.purchase.archives.product.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.ProductForVendorDao;
import com.newaim.purchase.archives.product.entity.ProductForVendor;
import com.newaim.purchase.archives.product.vo.ProductForVendorVo;
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
@Transactional(readOnly = true)
public class ProductForVendorService extends ServiceBase {

    @Autowired
    private ProductForVendorDao productForVendorDao;

    public Page<ProductForVendorVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {

        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ProductForVendor> spec = buildSpecification(params);
        Page<ProductForVendor> p = productForVendorDao.findAll(spec, pageRequest);
        Page<ProductForVendorVo> page = p.map(new Converter<ProductForVendor, ProductForVendorVo>() {
            @Override
            public ProductForVendorVo convert(ProductForVendor dv) {
                return BeanMapper.map(dv, ProductForVendorVo.class);
            }
        });

        return page;
    }

    public List<ProductForVendorVo> listByVendorId(String vendorId) {
        List<ProductForVendorVo> list = Lists.newArrayList();

        List<ProductForVendor> rows = productForVendorDao.findProductForVendorByVendorId(vendorId);
        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, ProductForVendor.class, ProductForVendorVo.class);
        }

        return list;
    }

    public ProductForVendorVo get(String id) {
        ProductForVendorVo o;
        ProductForVendor row = productForVendorDao.findProductForVendorById(id);
        o = BeanMapper.map(row, ProductForVendorVo.class);

        return o;
    }

    public ProductForVendor getProductForVendor(String id) {
        ProductForVendor o = productForVendorDao.findProductForVendorById(id);
        return o;
    }

    public ProductForVendor getProductForVendorByVendorId(String vendorId) {
        List<ProductForVendor> list = productForVendorDao.findProductForVendorByVendorId(vendorId);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    private Specification<ProductForVendor> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductForVendor> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductForVendor.class);
        return spec;
    }

    /**
     * 根据productId 获取产品list
     *
     * @param products
     */
    public List<ProductForVendorVo> listByProductId(List<ProductForVendorVo> products) {
        List<ProductForVendorVo> rd = Lists.newArrayList();

        if (!ListUtils.isEmpty(products)) {
            for (ProductForVendorVo pvv : products) {
                pvv = get(pvv.getId());
                rd.add(pvv);
            }
        }

        return rd;
    }
}
