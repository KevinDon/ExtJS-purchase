package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.Portal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortalDao extends BaseDao<Portal, String> {

	Portal findPortalById(String id);

	List<Portal> findPortalByCreatorId(String creatorId);

	void deletePortalsByCreatorId(String creatorId);
}
