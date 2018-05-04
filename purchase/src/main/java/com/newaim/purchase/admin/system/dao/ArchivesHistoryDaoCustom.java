package com.newaim.purchase.admin.system.dao;

import com.newaim.purchase.admin.system.entity.ArchivesHistory;

public interface ArchivesHistoryDaoCustom {

    Integer getLastVerByBusinessId(String businessId);

    ArchivesHistory getLastByCreatorIdAndBusinessIdAndStatusAndIsApplied(String creatorId, String businessId);

}
