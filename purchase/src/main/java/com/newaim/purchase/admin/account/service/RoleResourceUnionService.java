package com.newaim.purchase.admin.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.RoleResourceUnionDao;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.vo.RoleResourceUnionVo;

@Service
@Transactional(readOnly=true)
public class RoleResourceUnionService extends ServiceBase {

	@Autowired
	private RoleResourceUnionDao roleResourceUnionDao;
	
	public List<RoleResourceUnionVo> listByRoleId(String roleId){
		List<RoleResourceUnionVo> list = null;
		
		List<RoleResourceUnion> rows = roleResourceUnionDao.findRoleResourceUnionByRoleId(roleId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, RoleResourceUnion.class, RoleResourceUnionVo.class);
		}
		
		
		return list;
		
	}
	
	public List<RoleResourceUnionVo> listByResourceId(String resourceId){
		List<RoleResourceUnionVo> list = null;
		
		List<RoleResourceUnion> rows = roleResourceUnionDao.findRoleResourceUnionByResourceId(resourceId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, RoleResourceUnion.class, RoleResourceUnionVo.class);
		}
		
		
		return list;
		
	}
	
	
	public RoleResourceUnionVo get(String id){
		RoleResourceUnionVo o;
		if(id == null) {
			id = "";
		}
		RoleResourceUnion row = roleResourceUnionDao.findResourceById(id);
		o = BeanMapper.map(row, RoleResourceUnionVo.class);
		
		return o;
	}
	

	@Transactional
	public void save(RoleResourceUnion o){
        deleteRoleResourceByRoleId(o.getRoleId());
		roleResourceUnionDao.saveAndFlush(o);
	}

	@Transactional
	public void save(List<RoleResourceUnion> o){
        deleteRoleResourceByRoleId(o.get(0).getRoleId());
		roleResourceUnionDao.save(o);
	}

	@Transactional
	public void delete(String id){
		roleResourceUnionDao.delete(id);
	}
	
	@Transactional
	public void deleteRoleResourceByRoleId(String roleId){
		roleResourceUnionDao.deleteRoleResourceUnionByRoleId(roleId);
	}
	
	public List<RoleResourceUnion> findRoleResourceUnionByModelAndAction(String model,  String action){
		return roleResourceUnionDao.findRoleResourceUnionByModelAndAction(model, action);
	}
}
