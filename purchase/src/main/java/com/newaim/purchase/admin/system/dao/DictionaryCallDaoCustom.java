package com.newaim.purchase.admin.system.dao;

import com.newaim.purchase.admin.system.entity.DictionaryCall;

import java.util.List;

public interface DictionaryCallDaoCustom {
	
	List<DictionaryCall> findDictionaryByCategoryId(String categoryId);
}
