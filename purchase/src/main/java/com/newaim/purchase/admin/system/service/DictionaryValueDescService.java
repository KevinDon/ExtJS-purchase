package com.newaim.purchase.admin.system.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.DictionaryValueDescDao;
import com.newaim.purchase.admin.system.entity.DictionaryValueDesc;
import com.newaim.purchase.admin.system.vo.DictionaryValueDescVo;

@Transactional
@Service
public class DictionaryValueDescService extends ServiceBase {

	@Autowired
	private DictionaryValueDescDao dictionaryValueDescDao;
	

	public List<DictionaryValueDescVo> list(Sort sort){
		List<DictionaryValueDescVo> list = null;
				
		List<DictionaryValueDesc> rows = dictionaryValueDescDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryValueDesc.class, DictionaryValueDescVo.class);
		}
		
		return list;
		
	}
	
	public void save(DictionaryValueDescVo o){
		DictionaryValueDesc r =(DictionaryValueDesc)BeanMapper.map(o, DictionaryValueDesc.class);
		dictionaryValueDescDao.saveAndFlush(r);
	}
	

	public void delete(String id){
		dictionaryValueDescDao.delete(id);
	}
	
	public void deleteByDictId(String dictValueId){
		dictionaryValueDescDao.deleteByDictValueId(dictValueId);
		dictionaryValueDescDao.flush();
	}
	    
}
