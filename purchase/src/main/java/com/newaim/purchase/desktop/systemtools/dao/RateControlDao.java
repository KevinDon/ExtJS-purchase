package com.newaim.purchase.desktop.systemtools.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.systemtools.entity.RateControl;
import org.springframework.stereotype.Repository;

@Repository
public interface RateControlDao extends BaseDao<RateControl, String>,RateControlDaoCustom {


}
