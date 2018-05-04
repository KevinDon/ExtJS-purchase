package com.newaim.purchase.admin.system.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.DictionaryValue;

@Repository
public interface DictionaryValueDao extends JpaRepository<DictionaryValue, String>, JpaSpecificationExecutor<DictionaryValue> {
	
	DictionaryValue findDictionaryValueById(String id);
	
	void deleteByDictId(String dictId);

}
