package com.newaim.purchase.admin.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.Dictionary;

@Repository
public interface DictionaryDao extends JpaRepository<Dictionary, String>,JpaSpecificationExecutor<Dictionary> {
	
	Dictionary findDictionaryById(String id);

}
