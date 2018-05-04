package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.Help;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpDao extends BaseDao<Help, String> {

	Help findHelpById(String id);

}
