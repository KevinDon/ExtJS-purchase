package com.newaim.purchase.admin.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.DictionaryDescDao;
import com.newaim.purchase.admin.system.entity.DictionaryDesc;
import com.newaim.purchase.admin.system.vo.DictionaryDescVo;

@Transactional
@Service
public class DictionaryDescService extends ServiceBase {

	@Autowired
	private DictionaryDescDao dictionaryDescDao;
	

	public List<DictionaryDescVo> list(Sort sort){
		List<DictionaryDescVo> list = null;
				
		List<DictionaryDesc> rows = dictionaryDescDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryDesc.class, DictionaryDescVo.class);
		}
		
		return list;
		
	}
	
	public void save(DictionaryDescVo o){
		DictionaryDesc r =(DictionaryDesc)BeanMapper.map(o, DictionaryDesc.class);
		dictionaryDescDao.save(r);
	}
	

	public void delete(String id){
		dictionaryDescDao.delete(id);
	}
	
	public void deleteByDictId(String cateogryId){
		dictionaryDescDao.deleteByDictId(cateogryId);
		dictionaryDescDao.flush();
	}
	    
}
