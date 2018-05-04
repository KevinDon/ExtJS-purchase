package com.newaim.purchase.archives.vendor.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.service.ProductCategoryService;
import com.newaim.purchase.archives.vendor.dao.VendorProductCategoryUnionDao;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;
import com.newaim.purchase.archives.vendor.vo.VendorProductCategoryUnionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class VendorProductCategoryUnionService extends ServiceBase {

    @Autowired
    private VendorProductCategoryUnionDao vendorProductCategoryUnionDao;
    @Autowired
    private ProductCategoryService productCategoryService;

    public List<VendorProductCategoryUnionVo> listByVendorId(String vendorId) {
        List<VendorProductCategoryUnionVo> list = Lists.newArrayList();

        List<VendorProductCategoryUnion> rows = vendorProductCategoryUnionDao.findVendorProductCategoryUnionByVendorId(vendorId);

        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, VendorProductCategoryUnion.class, VendorProductCategoryUnionVo.class);
        }

        return list;

    }

    public List<VendorProductCategoryUnionVo> listByProductCategoryId(String productCategoryId) {
        List<VendorProductCategoryUnionVo> list = null;

        List<VendorProductCategoryUnion> rows = vendorProductCategoryUnionDao.findVendorProductCategoryUnionByProductCategoryId(productCategoryId);

        if (rows.size() > 0) {
            list = BeanMapper.mapList(rows, VendorProductCategoryUnion.class, VendorProductCategoryUnionVo.class);
        }

        return list;

    }

    /**
     * 根据ID查找产品分类信息
     *
     * @param id
     * @return
     */
    public VendorProductCategoryUnion getVendorProductCategoryUnion(String id) {
        return vendorProductCategoryUnionDao.findOne(id);
    }

    public VendorProductCategoryUnionVo get(String id) {
        VendorProductCategoryUnionVo vo = convertToVendorProductCategoryUnionVo(getVendorProductCategoryUnion(id));
        return vo;
    }

    /**
     * entity转换为vo
     *
     * @param union
     * @return
     */
    private VendorProductCategoryUnionVo convertToVendorProductCategoryUnionVo(VendorProductCategoryUnion union) {
        VendorProductCategoryUnionVo vo = BeanMapper.map(union, VendorProductCategoryUnionVo.class);
        return vo;
    }

    @Transactional
    public void save(VendorProductCategoryUnion o) {
        vendorProductCategoryUnionDao.save(o);
    }

    public void save(List<VendorProductCategoryUnion> list, String vendorId) {
        List<VendorProductCategoryUnion> o = Lists.newArrayList();

        deleteByVendorId(vendorId);
        if (StringUtils.isNotBlank(vendorId) && !StringUtils.isEmpty(vendorId)) {
            for (int i = 0; i < list.size(); i++) {
                VendorProductCategoryUnion e = new VendorProductCategoryUnion();
                e.setVendorId(vendorId);
                e.setProductCategoryId(list.get(i).getProductCategoryId());
                e.setAlias(list.get(i).getAlias());
                e.setOrderIndex(list.get(i).getOrderIndex());
                o.add(e);
            }
        }
        vendorProductCategoryUnionDao.save(o);
    }

    @Transactional
    public void deleteByVendorId(String vendorId) {
        vendorProductCategoryUnionDao.deleteVendorProductCategoryUnionByVendorId(vendorId);
    }

    @Transactional
    public void deleteByProductCategoryId(String productCategoryId) {
        vendorProductCategoryUnionDao.deleteVendorProductCategoryUnionByProductCategoryId(productCategoryId);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<VendorProductCategoryUnion> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<VendorProductCategoryUnion> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorProductCategoryUnion.class);
        return spec;
    }


    /**
     * @TODO 需要优化对象实例化
     * 根据供应商产品分类ID List 获取分类信息List
     *
     * @param productCategory
     * @return
     */
    public List<VendorProductCategoryUnionVo> listByProductCategoryId(List<VendorProductCategoryUnionVo> productCategory) {
        List<VendorProductCategoryUnionVo> rd = Lists.newArrayList();

        if (!ListUtils.isEmpty(productCategory)) {
            for(int i=0; i<productCategory.size(); i++) {
                VendorProductCategoryUnionVo vpcuv = new VendorProductCategoryUnionVo();
                BeanMapper.copyProperties(productCategory.get(i), vpcuv, true);
                vpcuv.setProductCategory(productCategoryService.get(vpcuv.getProductCategoryId()));
                vpcuv.setAlias(vpcuv.getAlias());
                vpcuv.setOrderIndex(vpcuv.getOrderIndex());
                vpcuv.setVendorId(vpcuv.getVendorId());
                vpcuv.setProductCategoryId(vpcuv.getProductCategoryId());
                rd.add(vpcuv);
            }

        }

        return rd;
    }
}
