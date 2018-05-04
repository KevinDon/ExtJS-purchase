package com.newaim.purchase.admin.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.DictionaryCategory;

import java.util.List;

@Repository
public interface DictionaryCategoryDao extends JpaRepository<DictionaryCategory, String>, JpaSpecificationExecutor<DictionaryCategory> {

	DictionaryCategory findDictionaryCategoryById(String id);

}
