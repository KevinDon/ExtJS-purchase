package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCombinedDao extends BaseDao<ProductCombined, String> {

    /**
     * 获取所有Combined非PARENT级的产品
     * @return
     */
    @Query("select pc from ProductCombined pc where (pc.flagSyncStatus = 1 or pc.flagSyncStatus = 2 or pc.flagSyncStatus is null ) and  pc.status = 1 AND pc.comboType <> 3")
    List<ProductCombined> findAllSycnProductCombined();

    /**
     * 获取所有Combined非PARENT级的产品
     * @return
     */
    @Query("select pc from ProductCombined pc where (pc.flagSyncStatus = 1 or pc.flagSyncStatus = 2 or pc.flagSyncStatus is null ) and  pc.status = 1 AND pc.comboType= 3")
    List<ProductCombined> findAllSycnProductCombinedForParent();
}
