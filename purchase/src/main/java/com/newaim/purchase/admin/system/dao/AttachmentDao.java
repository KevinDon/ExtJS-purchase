package com.newaim.purchase.admin.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.Attachment;

@Repository
public interface AttachmentDao extends BaseDao<Attachment, String> {

	Attachment findAttachmentById(String id);
	
	Attachment findAttachmentByDocumentIdAndBusinessId(String documentId, String businessId);

	List<Attachment> findAttachmentByBusinessId(String businessId);
	
	List<Attachment> findAttachmentByBusinessIdAndModuleName(String businessId, String moduleName);
	
	void deleteByBusinessIdAndModuleName(String businessId, String moduleName);

	void deleteByBusinessId(String businessId);

	void deleteByDocumentId(String documentId);
}
