package com.newaim.purchase.archives.product.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.archives.product.dao.ProductCombinedDetailDao;
import com.newaim.purchase.archives.product.entity.ProductCombinedDetail;
import com.newaim.purchase.archives.product.vo.ProductCombinedDetailVo;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderChargeItem;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderChargeItemVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductCombinedDetailService extends ServiceBase {

    @Autowired
    private ProductCombinedDetailDao productCombinedDetailDao;

    /**
     * 根据productCombinedId返回所有明细
     * @param productCombinedId 业务ID
     * @return detail集合
     */
    public List<ProductCombinedDetail> findDetailsByProductCombinedId(String productCombinedId){
        return productCombinedDetailDao.findDetailsByProductCombinedId(productCombinedId);
    }

    /**
     * 根据productCombinedId返回所有明细
     * @param productCombinedId 业务ID
     * @return detailVo集合
     */
    public List<ProductCombinedDetailVo> findDetailsVoByProductCombinedId(String productCombinedId){
        return BeanMapper.mapList(findDetailsByProductCombinedId(productCombinedId), ProductCombinedDetail.class, ProductCombinedDetailVo.class);
    }

    public ProductCombinedDetailVo get(String id){
    	ProductCombinedDetailVo o;
		if(id == null) {id = "";}
		ProductCombinedDetail row = productCombinedDetailDao.findProductCombinedDetailById(id);
		o = BeanMapper.map(row, ProductCombinedDetailVo.class);
		
		return o;
	}
    

}
