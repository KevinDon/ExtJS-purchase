package com.newaim.purchase.desktop.email.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.email.entity.EmailBox;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailBoxDao extends BaseDao<EmailBox, String> {

	EmailBox findEmailBoxById(String id);

	EmailBox findEmailBoxByEmailSettingIdAndBoxType(String esId, int boxType);

	List<EmailBox> findEmailBoxByEmailSettingId(String emailStringId);

	void deleteByEmailSettingId(String emailSettingId);

}
