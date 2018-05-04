package com.newaim.purchase.admin.system.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.MyDocumentCategoryDescDao;
import com.newaim.purchase.admin.system.entity.MyDocumentCategoryDesc;
import com.newaim.purchase.admin.system.vo.MyDocumentCategoryDescVo;

@Transactional
@Service
public class MyDocumentCategoryDescService extends ServiceBase {

	@Autowired
	private MyDocumentCategoryDescDao myDocumentCategoryDescDao;
	

	public List<MyDocumentCategoryDescVo> list(){
		List<MyDocumentCategoryDescVo> list = null;
				
		List<MyDocumentCategoryDesc> rows = myDocumentCategoryDescDao.findAll();
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MyDocumentCategoryDesc.class, MyDocumentCategoryDescVo.class);
		}
		
		return list;
		
	}

	/**
	 *  通过语言查找对应字典分类的键值对
	 */
	public Map<String, String> listMapByLang(String lang){
		Map<String, String> result = Maps.newHashMap();
		List<MyDocumentCategoryDesc> data = myDocumentCategoryDescDao.findAllByLang(lang);
		for (MyDocumentCategoryDesc categoryDesc: data) {
			result.put(categoryDesc.getCategoryId(), categoryDesc.getTitle());
		}
		return result;
	}
	
	public void save(MyDocumentCategoryDescVo o){
		MyDocumentCategoryDesc r =(MyDocumentCategoryDesc)BeanMapper.map(o, MyDocumentCategoryDesc.class);
		myDocumentCategoryDescDao.save(r);
	}
	

	public void delete(String id){
		myDocumentCategoryDescDao.delete(id);
	}
	
	public void deleteByCategoryId(String cateogryId){
		myDocumentCategoryDescDao.deleteByCategoryId(cateogryId);
		myDocumentCategoryDescDao.flush();
	}
	    
}
