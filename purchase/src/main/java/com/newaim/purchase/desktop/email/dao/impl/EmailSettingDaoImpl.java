package com.newaim.purchase.desktop.email.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.desktop.email.dao.EmailSettingDaoCustom;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.MARSHAL;

import java.util.Map;

public class EmailSettingDaoImpl extends BaseDaoCustomImpl implements EmailSettingDaoCustom {

    @Override
    public Boolean cancelDefaultByCreatorId(String creatorId, String emailSettingId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("creatorId", creatorId);

        StringBuilder hql = new StringBuilder("UPDATE EmailSetting SET ");

        hql.append("isDefault=2 WHERE creatorId=:creatorId ");

        if(null != emailSettingId && StringUtils.isNotBlank(emailSettingId)){
            params.put("emailSettingId", emailSettingId);
            hql.append("AND id <> :emailSettingId ");
        }
        logger.debug(String.format(hql.toString(), params));
        return execute(hql.toString(), params) > 0;
    }
}
