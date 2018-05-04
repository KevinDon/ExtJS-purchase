package com.newaim.purchase.admin.system.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.DictionaryValueDesc;

@Repository
@Transactional
public interface DictionaryValueDescDao extends JpaRepository<DictionaryValueDesc, String> {
	
	void deleteByDictValueId(String dictValueId);

}
