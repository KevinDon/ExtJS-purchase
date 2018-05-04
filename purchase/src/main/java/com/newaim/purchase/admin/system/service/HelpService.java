package com.newaim.purchase.admin.system.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.HelpDao;
import com.newaim.purchase.admin.system.entity.Help;
import com.newaim.purchase.admin.system.vo.HelpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly=true)
public class HelpService extends ServiceBase {

	@Autowired
	private HelpDao helpDao;
	

	public List<HelpVo> list(Sort sort){
		List<HelpVo> list = Lists.newArrayList();
		
		List<Help> rows = helpDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Help.class, HelpVo.class);
		}
		
		return list;
		
	}
	
	public Page<HelpVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Help> spec =buildSpecification(params);
        Page<Help> p = helpDao.findAll(spec, pageRequest);
        Page<HelpVo> page = p.map(new Converter<Help, HelpVo>() {
		    @Override
		    public HelpVo convert(Help dv) {
		        return BeanMapper.map(dv, HelpVo.class);
		    }
		});
        
		return page;
	}

	public Page<HelpVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<Help> spec =buildSpecification(params);
		Page<Help> p = helpDao.findAll(spec, pageable);
		Page<HelpVo> page = p.map(new Converter<Help, HelpVo>() {
			@Override
			public HelpVo convert(Help dv) {
				return BeanMapper.map(dv, HelpVo.class);
			}
		});

		return page;
	}
	
	public HelpVo get(String id){
		HelpVo o;
		if(id == null) {id = "";}
		Help row = helpDao.findHelpById(id);
		o = BeanMapper.map(row, HelpVo.class);
		
		return o;
	}

	public Help getHelp(String id){
		Help o = helpDao.findHelpById(id);
		return o;
	}
	
	@Transactional
	public Help add(Help o){
		o.setId(null);
    	o = helpDao.save(o);

    	return o;
	}

    @Transactional
    public Help saveAs(Help o){
        helpDao.clear();

        return add(o);
    }

	
	@Transactional
	public Help save(Help o){
		o = helpDao.save(o);
		return o;
	}


    @Transactional
    public void delete(String id){
        //删除全部关联附件
        helpDao.delete(id);
    }

    @Transactional
    public void setDelete(String id){
        Help o = getHelp(id);
        o.setStatus(3);
        helpDao.save(o);
    }
	    
	private Specification<Help> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Help> spec = DynamicSpecifications.bySearchFilter(filters.values(), Help.class);
        return spec;
    }
}
