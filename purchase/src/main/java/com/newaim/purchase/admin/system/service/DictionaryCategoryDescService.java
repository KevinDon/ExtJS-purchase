package com.newaim.purchase.admin.system.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.DictionaryCategoryDescDao;
import com.newaim.purchase.admin.system.entity.DictionaryCategoryDesc;
import com.newaim.purchase.admin.system.vo.DictionaryCategoryDescVo;

@Transactional
@Service
public class DictionaryCategoryDescService extends ServiceBase {

	@Autowired
	private DictionaryCategoryDescDao dictionaryCategoryDescDao;
	

	public List<DictionaryCategoryDescVo> list(){
		List<DictionaryCategoryDescVo> list = null;
				
		List<DictionaryCategoryDesc> rows = dictionaryCategoryDescDao.findAll();
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryCategoryDesc.class, DictionaryCategoryDescVo.class);
		}
		
		return list;
		
	}

	/**
	 *  通过语言查找对应字典分类的键值对
	 */
	public Map<String, String> listMapByLang(String lang){
		Map<String, String> result = Maps.newHashMap();
		List<DictionaryCategoryDesc> data = dictionaryCategoryDescDao.findAllByLang(lang);
		for (DictionaryCategoryDesc categoryDesc: data) {
			result.put(categoryDesc.getCategoryId(), categoryDesc.getTitle());
		}
		return result;
	}
	
	public void save(DictionaryCategoryDescVo o){
		DictionaryCategoryDesc r =(DictionaryCategoryDesc)BeanMapper.map(o, DictionaryCategoryDesc.class);
		dictionaryCategoryDescDao.save(r);
	}
	

	public void delete(String id){
		dictionaryCategoryDescDao.delete(id);
	}
	
	public void deleteByCategoryId(String cateogryId){
		dictionaryCategoryDescDao.deleteByCategoryId(cateogryId);
		dictionaryCategoryDescDao.flush();
	}
	    
}
