package com.newaim.purchase.desktop.email.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.email.entity.EmailSetting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailSettingDao extends BaseDao<EmailSetting, String>, EmailSettingDaoCustom {

	EmailSetting findEmailSettingById(String id);

	List<EmailSetting> findEmailSettingByCreatorIdAndStatus(String creatorId, Integer status);

	/**
	 * 查询所有可用的设置
	 * @return
	 */
	@Query("select t from EmailSetting t where t.status = 1")
	List<EmailSetting> findAllUsefulEmailSettings();

}
