package com.newaim.purchase.desktop.sta.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.sta.entity.StaOrdersCycle;
import com.newaim.purchase.desktop.sta.entity.ViewStaOrder;
import com.newaim.purchase.desktop.sta.entity.ViewStaOrdersCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewStaOrdersCycleDao extends JpaRepository<ViewStaOrdersCycle, String>, JpaSpecificationExecutor<ViewStaOrdersCycle> {


}
