package com.newaim.purchase.admin.system.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.DictionaryCategoryDao;
import com.newaim.purchase.admin.system.entity.DictionaryCategory;
import com.newaim.purchase.admin.system.vo.DictionaryCategoryVo;

@Transactional
@Service
public class DictionaryCategoryService extends ServiceBase {

	@Autowired
	private DictionaryCategoryDao dictionaryCategoryDao;
	

	public List<DictionaryCategoryVo> list(Sort sort){
		List<DictionaryCategoryVo> list = null;
		
		List<DictionaryCategory> rows = dictionaryCategoryDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryCategory.class, DictionaryCategoryVo.class);
		}
		
		return list;
		
	}
	
	public Page<DictionaryCategoryVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<DictionaryCategory> spec =buildSpecification(params);
        Page<DictionaryCategory> p = dictionaryCategoryDao.findAll(spec, pageRequest);
        Page<DictionaryCategoryVo> page = p.map(new Converter<DictionaryCategory, DictionaryCategoryVo>() {
		    @Override
		    public DictionaryCategoryVo convert(DictionaryCategory dv) {
		        return BeanMapper.map(dv, DictionaryCategoryVo.class);
		    }
		});
        
		return page;
	}

	public Page<DictionaryCategoryVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<DictionaryCategory> spec =buildSpecification(params);
		Page<DictionaryCategory> p = dictionaryCategoryDao.findAll(spec, pageable);
		Page<DictionaryCategoryVo> page = p.map(new Converter<DictionaryCategory, DictionaryCategoryVo>() {
			@Override
			public DictionaryCategoryVo convert(DictionaryCategory dv) {
				return BeanMapper.map(dv, DictionaryCategoryVo.class);
			}
		});

		return page;
	}
	
	public DictionaryCategoryVo get(String id){
		DictionaryCategoryVo o;
		if(id == null) {
			id = "";
		}
		DictionaryCategory row = dictionaryCategoryDao.findDictionaryCategoryById(id);
		o = BeanMapper.map(row, DictionaryCategoryVo.class);
		
		return o;
		
	}
	
	public void save(DictionaryCategoryVo o){
		DictionaryCategory r = BeanMapper.map(o, DictionaryCategory.class);
		dictionaryCategoryDao.save(r);
	}
	

	public void delete(String id){
		dictionaryCategoryDao.delete(id);
	}
	    
	private Specification<DictionaryCategory> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<DictionaryCategory> spec = DynamicSpecifications.bySearchFilter(filters.values(), DictionaryCategory.class);
        return spec;
    }
}
