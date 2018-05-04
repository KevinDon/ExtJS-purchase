package com.newaim.purchase.archives.product.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.flow.inspection.dao.ComplianceApplyDao;
import com.newaim.purchase.archives.flow.inspection.dao.SampleQcDao;
import com.newaim.purchase.archives.flow.inspection.entity.ComplianceApply;
import com.newaim.purchase.archives.flow.inspection.entity.SampleQc;
import com.newaim.purchase.archives.flow.purchase.dao.NewProductDao;
import com.newaim.purchase.archives.flow.purchase.entity.NewProduct;
import com.newaim.purchase.archives.product.dao.ProductVendorPropDao;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.vo.ProductVendorPropVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductVendorPropService extends ServiceBase{

    @Autowired
    private ProductVendorPropDao productVendorPropDao;

    @Autowired
    private SampleQcDao sampleQcDao;

    @Autowired
    private NewProductDao newProductDao;

    @Autowired
    private ComplianceApplyDao complianceApplyDao;

	public Page<ProductVendorPropVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ProductVendorProp> spec =buildSpecification(params);
        Page<ProductVendorProp> p = productVendorPropDao.findAll(spec, pageRequest);
        Page<ProductVendorPropVo> page = p.map(dv -> {
            ProductVendorPropVo vo = BeanMapper.map(dv, ProductVendorPropVo.class);
            if(StringUtils.isNotBlank(vo.getFlagDevId())){
				NewProduct newProduct = newProductDao.findOne(vo.getFlagDevId());
				if(newProduct != null){
					vo.setFlagDevFlowId(newProduct.getBusinessId());
				}
			}
			if(StringUtils.isNotBlank(vo.getFlagQcId())){
				SampleQc sampleQc = sampleQcDao.findOne(vo.getFlagQcId());
				if(sampleQc != null){
					vo.setFlagQcFlowId(sampleQc.getBusinessId());
				}
			}
			if(StringUtils.isNotBlank(vo.getFlagComplianceId())){
				ComplianceApply complianceApply = complianceApplyDao.findOne(vo.getFlagComplianceId());
				if(complianceApply != null){
					vo.setFlagComplianceFlowId(complianceApply.getBusinessId());
				}
			}
            return vo;
        });
        
		return page;
	}

	public List<ProductVendorPropVo> listByVendorId(String vendorId) {
		List<ProductVendorPropVo> list = Lists.newArrayList();
		
		List<ProductVendorProp> rows = productVendorPropDao.findProductVendorPropByVendorId(vendorId);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, ProductVendorProp.class, ProductVendorPropVo.class);
		}
		
		return list;
	}
	
	public ProductVendorPropVo get(String id){
		ProductVendorProp row = productVendorPropDao.findProductVendorPropById(id);
		ProductVendorPropVo o = BeanMapper.map(row, ProductVendorPropVo.class);
		return o;
	}
	
	public ProductVendorProp getProductVendorProp(String id){
		ProductVendorProp o = productVendorPropDao.findProductVendorPropById(id);
		return o;
	}

	/**
	 * 通过产品id获取对应属性值
	 * @param productId
	 * @return
	 */
	public ProductVendorProp getProductVendorPropByProductId(String productId){
		return  productVendorPropDao.findProductVendorPropByProductId(productId);
	}

			    
	private Specification<ProductVendorProp> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductVendorProp> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductVendorProp.class);
        return spec;
    }	
}
