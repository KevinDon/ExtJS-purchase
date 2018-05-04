package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.Tariff;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffDao extends BaseDao<Tariff, String> {

    /**
     * 通过海关编码获取海关信息
     * @param itemCode
     * @return
     */
    Tariff findTopByItemCode(String itemCode);


}
