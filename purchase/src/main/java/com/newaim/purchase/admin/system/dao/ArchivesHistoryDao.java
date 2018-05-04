package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.ArchivesHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivesHistoryDao extends BaseDao<ArchivesHistory, String>, ArchivesHistoryDaoCustom {
    ArchivesHistory findArchivesHistoryById(String id);
}