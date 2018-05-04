package com.newaim.purchase.admin.system.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.DictionaryValueDao;
import com.newaim.purchase.admin.system.entity.DictionaryValue;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;

@Transactional
@Service
public class DictionaryValueService extends ServiceBase {

	@Autowired
	private DictionaryValueDao dictionaryValueDao;

	public List<DictionaryValueVo> list(Sort sort){
		List<DictionaryValueVo> list = null;
		
		List<DictionaryValue> rows = dictionaryValueDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryValue.class, DictionaryValueVo.class);
		}
		
		return list;
		
	}
	
	public Page<DictionaryValueVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<DictionaryValue> spec =buildSpecification(params);
        
        Page<DictionaryValue> p = dictionaryValueDao.findAll(spec, pageRequest);
        Page<DictionaryValueVo> page = p.map(new Converter<DictionaryValue, DictionaryValueVo>() {
		    @Override
		    public DictionaryValueVo convert(DictionaryValue dv) {
		        return BeanMapper.map(dv, DictionaryValueVo.class);
		    }
		});
        
		return page;
	}
	
	public DictionaryValueVo get(String id){
		DictionaryValueVo o;
		if(id == null) {
			id = "";
		}
		DictionaryValue row = dictionaryValueDao.findDictionaryValueById(id);
		o = BeanMapper.map(row, DictionaryValueVo.class);
		
		return o;
		
	}
	
	public void save(DictionaryValueVo o){
		DictionaryValue r = BeanMapper.map(o, DictionaryValue.class);
		dictionaryValueDao.saveAndFlush(r);
		o.setId(r.getId());
	}
	

	public void delete(String id){
		dictionaryValueDao.delete(id);
	}
	
	public void deleteByDictId(String id){
		dictionaryValueDao.deleteByDictId(id);
	}
	
	private Specification<DictionaryValue> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<DictionaryValue> spec = DynamicSpecifications.bySearchFilter(filters.values(), DictionaryValue.class);
        return spec;
    }
}
