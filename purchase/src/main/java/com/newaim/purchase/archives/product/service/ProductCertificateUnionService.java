package com.newaim.purchase.archives.product.service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.archives.product.dao.ProductCertificateUnionDao;
import com.newaim.purchase.archives.product.entity.ProductCertificateUnion;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionVo;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import com.newaim.purchase.flow.inspection.vo.FlowSampleQcDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductCertificateUnionService extends ServiceBase{

    @Autowired
    private ProductCertificateUnionDao productCertificateUnionDao;

    public List<ProductCertificateUnionVo> findDetailsVoByBusinessId(String certificateId){
        return BeanMapper.mapList(productCertificateUnionDao.findByCertificateId(certificateId), ProductCertificateUnion.class, ProductCertificateUnionVo.class);
    }

    public List<ProductCertificateUnionVo> findDetailsVoByProductId(String productIds){
        return BeanMapper.mapList(productCertificateUnionDao.findByProductId(productIds), ProductCertificateUnion.class, ProductCertificateUnionVo.class);
    }

    public List<ProductCertificateUnionVo> findProductCertificateUnionsByProductIdIn(String[] productIds){
        return BeanMapper.mapList(productCertificateUnionDao.findProductCertificateUnionsByProductIdIn(productIds), ProductCertificateUnion.class, ProductCertificateUnionVo.class);
    }

}
