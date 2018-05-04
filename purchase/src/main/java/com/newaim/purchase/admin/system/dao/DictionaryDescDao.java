package com.newaim.purchase.admin.system.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newaim.purchase.admin.system.entity.DictionaryDesc;

@Repository
@Transactional
public interface DictionaryDescDao extends JpaRepository<DictionaryDesc, String> {
	
	void deleteByDictId(String dictId);

}
