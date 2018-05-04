package com.newaim.purchase.admin.account.service;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.ResourceDao;
import com.newaim.purchase.admin.account.entity.Resource;
import com.newaim.purchase.admin.account.vo.ResourceMenuVo;
import com.newaim.purchase.admin.account.vo.ResourceVo;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class ResourceService extends ServiceBase {

	@Autowired
	private ResourceDao resourceDao;
	
	/**
	 * get list from menu
	 * @param parentId
	 * @return
	 */
	public List<ResourceMenuVo> list(String parentId, List<String> roleIds){
		List<ResourceMenuVo> list = null;
		
		List<Resource> rows = resourceDao.findResourceByParentIdOrderBySortAsc(parentId, roleIds);
//		List<Resource> rows = resourceDao.findResourceByParentIdOrderBySortAsc(parentId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Resource.class, ResourceMenuVo.class);
		}
		
		return list;
		
	}
	
	/**
	 * get list from admin
	 * @param parentId
	 * @return
	 */
	public List<ResourceVo> listForAdmin(String parentId){
		List<ResourceVo> list = null;
		
		List<Resource> rows = resourceDao.findResourceByParentIdOrderBySortAsc(parentId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Resource.class, ResourceVo.class);
		}
		
		
		return list;
		
	}
		
	public ResourceVo get(String id){
		ResourceVo o = null;
		Resource row = resourceDao.findResourceById(id);
		o = BeanMapper.map(row, ResourceVo.class);
		
		return o;
		
	}
	
	public Resource getResource(String id){
		Resource o = resourceDao.findResourceById(id);
		return o;
	}
	
	public Resource getUp(String id){
		Resource o = null;
		List<Resource> rows = resourceDao.getUp(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
	}
	
	
	public Resource getDown(String id){
		Resource o = null;
		List<Resource> rows = resourceDao.getDown(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
	}
	

	public Resource getChildrenRow(String id){
		Resource o = null;
		List<Resource> rows = resourceDao.listChildrenRows(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
		
	}

	public String getParentPath(String id) {
		List<String> path = Lists.newArrayList();
		List<Resource> r = resourceDao.getParentPath(id);
		if(r.size()>0){
			path.add(r.get(0).getParentId());
			getParentPath(r.get(0), path);
		}
		path.add(id);
		
		StringUtils.join(path, "_");
		
		return StringUtils.join(path, "_");
	}
	
	private List<Resource> getParentPath(Resource resource, List<String> path ){
		if(resource != null && StringUtils.isNotBlank(resource.getParentId())){
			List<Resource> r = resourceDao.getParentPath(resource.getParentId());
			if(r.size()>0 && StringUtils.isNotBlank(r.get(0).getParentId())){
				path.add(r.get(0).getParentId());
				return getParentPath(r.get(0), path);
			}else{
				return Lists.newArrayList();
			}
		}else{
			return Lists.newArrayList();
		}
	}
	
	
	@Transactional
	public void add(Resource o){
		resourceDao.clear();
		resourceDao.save(o);
	}
	
	@Transactional
	public void save(Resource o){
		resourceDao.save(o);
	}
	
	@Transactional
	public void delete(String id){
		resourceDao.delete(id);
	}
    
}
