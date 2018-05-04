package com.newaim.purchase.desktop.sta.dao;

import com.newaim.purchase.desktop.sta.entity.StaProblemTeam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StaProblemTeamDao extends JpaRepository<StaProblemTeam, String>, JpaSpecificationExecutor<StaProblemTeam> {


}
