package com.newaim.purchase.desktop.sta.dao;

import com.newaim.core.jpa.BaseDao;

import com.newaim.purchase.desktop.sta.entity.ViewStaCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewStaCostDao extends JpaRepository<ViewStaCost, String>, JpaSpecificationExecutor<ViewStaCost> {


}
