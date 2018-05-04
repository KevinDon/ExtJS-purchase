package com.newaim.purchase.admin.system.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.BaseDaoCustomImpl;
import com.newaim.purchase.admin.system.dao.DictionaryCallDaoCustom;
import com.newaim.purchase.admin.system.entity.DictionaryCall;


public class DictionaryCallDaoImpl extends BaseDaoCustomImpl implements DictionaryCallDaoCustom{
	
	@Override
	public List<DictionaryCall> findDictionaryByCategoryId(String code) {
		
		List<DictionaryCall> list = Lists.newArrayList();
		
		if(StringUtils.isNotBlank(code)){
			Map<String, Object> params = Maps.newHashMap();
			StringBuilder hql = new StringBuilder("SELECT main FROM DictionaryCall AS main where 1 = 1 ");
		
			hql.append("AND (main.categoryId = :categoryId) ");
			params.put("categoryId", code);
		
			hql.append("ORDER BY main.codeMain ASC, main.codeSub ASC, main.sort ASC");
			list = list(hql.toString(), params);
		}
		return list;
	}
}
