package com.newaim.purchase.archives.flow.purchase.service;


import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.flow.purchase.dao.ProductQuotationDao;
import com.newaim.purchase.archives.flow.purchase.entity.ProductQuotation;
import com.newaim.purchase.archives.flow.purchase.vo.ProductQuotationVo;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductQuotationService extends ServiceBase {

    @Autowired
    private ProductQuotationDao productQuotationDao;

    @Autowired
    private ProductService productService;

    public List<ProductQuotationVo> listAllByVendor(String vendorId, String type){
        UserVo user = SessionUtils.currentUserVo();
        List<ProductQuotation> data = Lists.newArrayList();
        if(StringUtils.isNotBlank(type)){
            if(StringUtils.equals(type, "1")){
                data = productQuotationDao.findAllLatestByVendor(vendorId, user.getId());
            }
        } else {
            data = productQuotationDao.findAllByVendor(vendorId, user.getId());
        }
        List<ProductQuotationVo> result =  BeanMapper.mapList(data, ProductQuotation.class, ProductQuotationVo.class);
        if(result != null && result.size() > 0){
            for (int i = 0; i < result.size(); i++) {
                ProductQuotationVo vo = result.get(i);
                ProductVo productVo = productService.get(result.get(i).getProductId());
                vo.setProduct(productVo);
            }
        }
        return result;
    }

    /**
     * 获取所有正式的采购询价信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<ProductQuotationVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ProductQuotation> spec = buildSpecification(params);
        Page<ProductQuotation> p = productQuotationDao.findAll(spec, pageRequest);
        Page<ProductQuotationVo> page = p.map(productQuotation -> {
            ProductQuotationVo vo = convertToProductQuotationVo(productQuotation);
            ProductVo productVo = productService.get(vo.getProductId());
            vo.setProduct(productVo);
            return vo;
        });
        return page;
    }

    private ProductQuotationVo convertToProductQuotationVo(ProductQuotation productQuotation){
        ProductQuotationVo vo = BeanMapper.map(productQuotation, ProductQuotationVo.class);
        return vo;
    }

    private Specification<ProductQuotation> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductQuotation> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductQuotation.class);
        return spec;
    }

    /**
     * 根据ID获取采购询价
     * @param id
     * @return
     */
    public ProductQuotation getProductQuotation(String id){
        return productQuotationDao.findOne(id);
    }

    /**
     * 根据ID获取采购询价
     * @param id
     * @return
     */
    public ProductQuotationVo get(String id){
        ProductQuotationVo vo =convertToProductQuotationVo(getProductQuotation(id));
        return vo;
    }

    /**
     * 通过产品ID查找所有历史报价明细
     * @param productId
     * @return
     */
    public List<ProductQuotation> findProductQuotationsByProductId(String productId){
        return productQuotationDao.findProductQuotationsByProductId(productId);
    }

    /**
     * 通过产品ID查找所有历史报价明细
     * @param productId
     * @return
     */
    public List<ProductQuotationVo> findProductQuotationsVoByProductId(String productId){
        return BeanMapper.mapList(productQuotationDao.findProductQuotationsByProductId(productId), ProductQuotation.class, ProductQuotationVo.class);
    }

    /**
     * 通过productId禁用相关报价单的产品状态
     * @param productId
     */
    @Transactional(rollbackFor = Exception.class)
    public void disableProductStatus(String productId){
        List<ProductQuotation> productQuotations = productQuotationDao.findProductQuotationsByProductId(productId);
        if (productQuotations != null){
            for (ProductQuotation productQuotation : productQuotations){
                productQuotation.setProductStatus(Constants.Status.DISABLED.code);
            }
        }
        productQuotationDao.save(productQuotations);
    }

}
