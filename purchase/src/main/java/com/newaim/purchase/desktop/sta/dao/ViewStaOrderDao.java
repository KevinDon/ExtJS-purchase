package com.newaim.purchase.desktop.sta.dao;


import com.newaim.purchase.desktop.sta.entity.StaOrder;
import com.newaim.purchase.desktop.sta.entity.ViewStaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewStaOrderDao extends JpaRepository<ViewStaOrder, String>, JpaSpecificationExecutor<ViewStaOrder> {

//    @Query("insert int  from select t from ViewStaOrder t where 1=1 order by t.createdAt")
//    List<StaOrder> copyFromView();
}
