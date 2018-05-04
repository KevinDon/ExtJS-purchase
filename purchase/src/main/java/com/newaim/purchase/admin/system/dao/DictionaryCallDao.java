package com.newaim.purchase.admin.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.DictionaryCall;

@Repository
public interface DictionaryCallDao extends BaseDao<DictionaryCall, String>, DictionaryCallDaoCustom {
	
	DictionaryCall findDictionaryById(String id);
	
	List<DictionaryCall> findDictionaryByCodeMainOrderBySort(String code);
	
	List<DictionaryCall> findDictionaryByCodeMainAndCodeSubOrderBySort(String code, String codeSub);

    List<DictionaryCall> findDictionaryByCodeMainAndCodeSubAndStatusOrderBySort(String code, String codeSub, int status);

}
