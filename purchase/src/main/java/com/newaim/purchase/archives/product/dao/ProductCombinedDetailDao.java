package com.newaim.purchase.archives.product.dao;

import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.archives.product.entity.ProductCombinedDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCombinedDetailDao extends JpaRepository<ProductCombinedDetail, String>, JpaSpecificationExecutor<ProductCombinedDetail> {

    /**
     * 通过业务ID删除所有关联的明细
     * @param productCombinedId 业务ID
     */
    void deleteByProductCombinedId(String productCombinedId);

    /**
     * 根据业务ID查找明细结合
     * @param productCombinedId 业务id
     * @return 明细集合
     */
    List<ProductCombinedDetail> findDetailsByProductCombinedId(String productCombinedId);
    
    
    public ProductCombinedDetail findProductCombinedDetailById(String id); 
    


}
