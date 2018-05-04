package com.newaim.purchase.admin.account.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.UserRoleUnionDao;
import com.newaim.purchase.admin.account.entity.Resource;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;
import com.newaim.purchase.admin.account.vo.UserRoleUnionVo;

@Service
@Transactional(readOnly=true)
public class UserRoleUnionService extends ServiceBase {

	@Autowired
	private UserRoleUnionDao userRoleUnionDao;
	
	public List<UserRoleUnionVo> listByRoleId(String roleId){
		List<UserRoleUnionVo> list = null;
		
		List<UserRoleUnion> rows = userRoleUnionDao.findUserRoleUnionByRoleId(roleId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, UserRoleUnion.class, UserRoleUnionVo.class);
		}
		
		return list;
		
	}
	
	public List<UserRoleUnionVo> listByUserId(String userId){
		List<UserRoleUnionVo> list = null;
		
		List<UserRoleUnion> rows = userRoleUnionDao.findUserRoleUnionByUserId(userId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, UserRoleUnion.class, UserRoleUnionVo.class);
		}
		
		return list;
		
	}
	
	@Transactional
	public void save(UserRoleUnion o){
		userRoleUnionDao.save(o);
	}
	
	@Transactional
	public void deleteUserRoleByRoleId(String roleId){
		userRoleUnionDao.deleteUserRoleUnionByRoleId(roleId);
	}
	
	@Transactional
	public void deleteUserRoleByUserId(String userId){
		userRoleUnionDao.deleteUserRoleUnionByUserId(userId);
	}
	/**
     * 创建动态查询条件组合.
     */
    private Specification<UserRoleUnion> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<UserRoleUnion> spec = DynamicSpecifications.bySearchFilter(filters.values(), UserRoleUnion.class);
        return spec;
    }
    
}
