package com.newaim.purchase.admin.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.admin.account.dao.RoleResourceDao;
import com.newaim.purchase.admin.account.entity.RoleResource;
import com.newaim.purchase.admin.account.vo.RoleResourceVo;

import java.util.LinkedHashMap;

@Service
@Transactional(readOnly=true)
public class RoleResourceService {

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Transactional(readOnly = true)
    public Page<RoleResourceVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<RoleResource> spec = buildSpecification(params);
        Page<RoleResource> p = roleResourceDao.findAll(spec, pageRequest);
        Page<RoleResourceVo> page = p.map(new Converter<RoleResource, RoleResourceVo>() {
		    @Override
		    public RoleResourceVo convert(RoleResource role) {
		        return BeanMapper.map(role, RoleResourceVo.class);
		    }
		});
        return page;
    }
    
	public RoleResourceVo get(String id) {
		return BeanMapper.map(roleResourceDao.findOne(id), RoleResourceVo.class);
	}
	
	@Transactional
	public void save(RoleResourceVo o){
		RoleResource r =(RoleResource)BeanMapper.map(o, RoleResource.class);
    	roleResourceDao.saveAndFlush(r);
		o.setId(r.getId());
	}
	
	@Transactional
	public void delete(String id){
		roleResourceDao.delete(id);
	}
	
	@Transactional
	public void deleteRoleResourceByRoleId(String roleId){
		roleResourceDao.deleteRoleResourceByRoleId(roleId);
	}
	/**
     * 创建动态查询条件组合.
     */
    private Specification<RoleResource> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<RoleResource> spec = DynamicSpecifications.bySearchFilter(filters.values(), RoleResource.class);
        return spec;
    }

}
