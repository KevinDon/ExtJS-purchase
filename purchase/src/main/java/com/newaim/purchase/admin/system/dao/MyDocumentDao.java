package com.newaim.purchase.admin.system.dao;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.MyDocument;

@Repository
public interface MyDocumentDao extends BaseDao<MyDocument, String> {

	MyDocument findMyDocumentById(String id);

    MyDocument findMyDocumentByPath(String path);

	Integer countMyDocumentByEmailCid(String emailCid);

}
