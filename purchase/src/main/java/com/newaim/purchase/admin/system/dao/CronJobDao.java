package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.CronJob;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobDao extends BaseDao<CronJob, String> {

    CronJob findByCode(String code);
}
