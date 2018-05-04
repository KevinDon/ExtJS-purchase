package com.newaim.purchase.admin.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.MyDocumentCategory;

@Repository
public interface MyDocumentCategoryDao extends JpaRepository<MyDocumentCategory, String>, JpaSpecificationExecutor<MyDocumentCategory> {

	MyDocumentCategory findMyDocumentCategoryById(String id);

	MyDocumentCategory findFirstByPath(String path);

}
