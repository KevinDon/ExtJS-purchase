package com.newaim.purchase.admin.system.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.admin.system.dao.ArchivesHistoryDaoCustom;
import com.newaim.purchase.admin.system.entity.ArchivesHistory;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.LinkedHashMap;


public class ArchivesHistoryDaoImpl extends BaseDaoCustomImpl implements ArchivesHistoryDaoCustom {

    /**
     * 获取最后的版本号
     * @param businessId
     * @return
     */
    @Override
    public Integer getLastVerByBusinessId(String businessId) {
        int result = 0;
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();

        StringBuffer hql = new StringBuffer("select t from ArchivesHistory t where 1 = 1 ");
        if (StringUtils.isNotBlank(businessId)) {
            params.put("businessId", businessId);
            hql.append(" and t.businessId = :businessId ");
        }
        hql.append(" ORDER BY t.ver DESC, t.createdAt DESC");

        List<ArchivesHistory> ah = list(hql.toString(), params, 1);
        if (null != ah && ah.size()>0 && ah.get(0).getVer() != null) {
            result = ah.get(0).getVer();
        }

        return result;
    }

    /**
     * 根据创建人和业务ID查找最后一台未启用的记录
     * @param creatorId
     * @param businessId
     * @return
     */
    @Override
    public ArchivesHistory getLastByCreatorIdAndBusinessIdAndStatusAndIsApplied(String creatorId, String businessId) {
        ArchivesHistory result = null;
        LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();

        StringBuffer hql = new StringBuffer("select t from ArchivesHistory t where 1 = 1 ");
        if (StringUtils.isNotBlank(businessId)) {
            params.put("businessId", businessId);
            params.put("creatorId", creatorId);
            hql.append(" and t.businessId = :businessId AND t.creatorId = :creatorId");
        }
        params.put("status", 1);
        params.put("IsApplied", 2);
        hql.append(" AND t.status = :status AND t.isApplied = :IsApplied ");

        hql.append(" ORDER BY t.ver DESC, t.createdAt DESC");
        try{
            List<ArchivesHistory> ah = list(hql.toString(), params, 1);
            if (null != ah && ah.size()>0) {
                result = ah.get(0);
            }
        }catch (Exception e){

        }


        return result;
    }
}
