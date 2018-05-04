package com.newaim.purchase.desktop.sta.dao;


import com.newaim.purchase.desktop.sta.entity.ViewStaProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewStaProblemDao extends JpaRepository<ViewStaProblem, String>, JpaSpecificationExecutor<ViewStaProblem> {


}
