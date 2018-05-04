package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.MyTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTemplateDao extends BaseDao<MyTemplate, String> {

	MyTemplate findMyTemplateById(String id);

}
