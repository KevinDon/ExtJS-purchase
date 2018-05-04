package com.newaim.purchase.desktop.message.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.message.entity.Message;

@Repository
public interface MessageDao extends BaseDao<Message, String> {

	Message findMessageById(String id);

	List<Message> findMessageByFromUserId(String fromUserId);
	
	List<Message> findMessageByToUserId(String toUserId);

	Integer countMessageByToUserIdAndReadAndStatus(String toUserId, int read, int status);
}
