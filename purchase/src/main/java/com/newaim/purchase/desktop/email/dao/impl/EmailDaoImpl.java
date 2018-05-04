package com.newaim.purchase.desktop.email.dao.impl;

import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.desktop.email.dao.EmailDaoCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class EmailDaoImpl extends BaseDaoCustomImpl implements EmailDaoCustom {

    @Override
    public Boolean reSetNumberAndRead(String emailSettingId, Integer boxType) {
        Boolean result = false;
        if(StringUtils.isNotBlank(emailSettingId)  && boxType > -1){

            Map<String, Object> params = Maps.newHashMap();
            params.put("emailSettingId", emailSettingId);
            params.put("boxType", boxType);
            params.put("updateAt", new Date());

            StringBuilder hql = new StringBuilder("UPDATE EmailBox main SET ");
            //重记总数量
            hql.append("main.number=(SELECT count(*) From Email WHERE 1=1 AND emailSettingId=:emailSettingId AND boxType=:boxType AND status=1),");
            //重记未读数量
            hql.append("main.noRead=(SELECT count(*) From Email WHERE isRead = 2 AND emailSettingId=:emailSettingId AND boxType=:boxType AND status=1),");
            hql.append("main.updatedAt=:updateAt ");
            hql.append("WHERE main.emailSettingId=:emailSettingId AND main.boxType=:boxType");

            result = execute(hql.toString(), params) > 0;
        }

        //重记未读数量


        return result;
    }
}
