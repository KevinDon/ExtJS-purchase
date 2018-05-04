package com.newaim.purchase.admin.system.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.MyDocumentCategoryDesc;

import java.util.List;

@Repository
@Transactional
public interface MyDocumentCategoryDescDao extends JpaRepository<MyDocumentCategoryDesc, String> {
	
	void deleteByCategoryId(String categoryId);

	/**
	 * 通过语言查询所有对应的字典分类
	 */
	List<MyDocumentCategoryDesc> findAllByLang(String lang);

}
