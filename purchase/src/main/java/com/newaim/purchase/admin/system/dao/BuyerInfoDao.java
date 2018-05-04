package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.BuyerInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerInfoDao  extends BaseDao<BuyerInfo, String>,BuyerInfoDaoCustom {

}
