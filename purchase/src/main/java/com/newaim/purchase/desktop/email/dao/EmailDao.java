package com.newaim.purchase.desktop.email.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.email.entity.Email;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDao extends BaseDao<Email, String>, EmailDaoCustom {

	Email findEmailById(String id);

	void deleteByEmailSettingId(String emailSettingId);

	void deleteByBoxType(String boxType);

    Integer countByEmailSettingIdEqualsAndBoxTypeAndUid(String emailSettingId, Integer boxType, String uid);

//	Integer countByEmailSettingIdEqualsAndBoxType(String emailSettingId, Integer boxType );

	Integer countByEmailSettingIdEqualsAndBoxTypeAndIsRead(String emailSettingId, Integer boxType, Integer isRead );
}
