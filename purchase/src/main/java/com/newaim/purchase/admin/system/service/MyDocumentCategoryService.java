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
import com.newaim.purchase.admin.system.dao.MyDocumentCategoryDao;
import com.newaim.purchase.admin.system.entity.MyDocumentCategory;
import com.newaim.purchase.admin.system.vo.MyDocumentCategoryVo;

@Transactional
@Service
public class MyDocumentCategoryService extends ServiceBase {

	@Autowired
	private MyDocumentCategoryDao myDocumentCategoryDao;
	

	public List<MyDocumentCategoryVo> list(Sort sort){
		List<MyDocumentCategoryVo> list = null;
		
		List<MyDocumentCategory> rows = myDocumentCategoryDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MyDocumentCategory.class, MyDocumentCategoryVo.class);
		}
		
		return list;
		
	}
	
	public Page<MyDocumentCategoryVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MyDocumentCategory> spec =buildSpecification(params);
        Page<MyDocumentCategory> p = myDocumentCategoryDao.findAll(spec, pageRequest);
        Page<MyDocumentCategoryVo> page = p.map(new Converter<MyDocumentCategory, MyDocumentCategoryVo>() {
		    @Override
		    public MyDocumentCategoryVo convert(MyDocumentCategory dv) {
		        return BeanMapper.map(dv, MyDocumentCategoryVo.class);
		    }
		});
        
		return page;
	}

	public Page<MyDocumentCategoryVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<MyDocumentCategory> spec =buildSpecification(params);
		Page<MyDocumentCategory> p = myDocumentCategoryDao.findAll(spec, pageable);
		Page<MyDocumentCategoryVo> page = p.map(new Converter<MyDocumentCategory, MyDocumentCategoryVo>() {
			@Override
			public MyDocumentCategoryVo convert(MyDocumentCategory dv) {
				return BeanMapper.map(dv, MyDocumentCategoryVo.class);
			}
		});

		return page;
	}
	
	public MyDocumentCategoryVo get(String id){
		MyDocumentCategoryVo o;
		if(id == null) {
			id = "";
		}
		MyDocumentCategory row = myDocumentCategoryDao.findMyDocumentCategoryById(id);
		o = BeanMapper.map(row, MyDocumentCategoryVo.class);
		
		return o;
		
	}
    public MyDocumentCategoryVo getByPath(String path){
        MyDocumentCategory row = myDocumentCategoryDao.findFirstByPath(path);
        return BeanMapper.map(row, MyDocumentCategoryVo.class);
    }
	public void save(MyDocumentCategoryVo o){
		MyDocumentCategory r = BeanMapper.map(o, MyDocumentCategory.class);
		myDocumentCategoryDao.save(r);
	}
	

	public void delete(String id){
		myDocumentCategoryDao.delete(id);
	}
	    
	private Specification<MyDocumentCategory> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MyDocumentCategory> spec = DynamicSpecifications.bySearchFilter(filters.values(), MyDocumentCategory.class);
        return spec;
    }
}
