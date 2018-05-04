package com.newaim.purchase.desktop.message.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.message.entity.MessageSku;

@Repository
public interface MessageSkuDao extends BaseDao<MessageSku, String> {

	MessageSku findMessageSkuById(String id);

	List<MessageSku> findMessageSkuByCreatorId(String creatorId);
	
	List<MessageSku> findMessageSkuByToUserId(String toUserId);
	
}
