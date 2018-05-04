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
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.RoleDao;
import com.newaim.purchase.admin.account.entity.Role;
import com.newaim.purchase.admin.account.vo.RoleVo;

import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/8/10.
 * @author lance.hu
 */
@Service
@Transactional(readOnly=true)
public class RoleService extends ServiceBase {

    @Autowired
    private RoleDao roleDao;

    public Page<RoleVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Role> spec = buildSpecification(params);
        Page<Role> p = roleDao.findAll(spec, pageRequest);
        Page<RoleVo> page = p.map(new Converter<Role, RoleVo>() {
		    @Override
		    public RoleVo convert(Role role) {
		        return BeanMapper.map(role, RoleVo.class);
		    }
		});
        return page;
    }
    
	public RoleVo get(String id) {
		return BeanMapper.map(roleDao.findOne(id), RoleVo.class);
	}
	
	public Role getRole(String id){
		return roleDao.findOne(id);
	}
	
	@Transactional
	public void add(Role o){
		roleDao.clear();
		roleDao.save(o);
	}
	
	@Transactional
	public void save(Role o){
		roleDao.save(o);
	}
	
	@Transactional
	public void delete(String id){
		roleDao.delete(id);
	}
	
	/**
     * 创建动态查询条件组合.
     */
    private Specification<Role> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Role> spec = DynamicSpecifications.bySearchFilter(filters.values(), Role.class);
        return spec;
    }

}
