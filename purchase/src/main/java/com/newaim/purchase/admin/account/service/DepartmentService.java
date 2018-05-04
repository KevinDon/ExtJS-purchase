package com.newaim.purchase.admin.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.account.dao.DepartmentDao;
import com.newaim.purchase.admin.account.entity.Department;
import com.newaim.purchase.admin.account.vo.DepartmentVo;

@Service
@Transactional(readOnly=true)
public class DepartmentService extends ServiceBase  {

	@Autowired
	private DepartmentDao departmentDao;
	
	/**
	 * get list from menu
	 * @param parentId
	 * @return
	 */
	public List<DepartmentVo> list(String parentId){
		List<DepartmentVo> list = null;
		
		List<Department> rows = departmentDao.findDepartmentByParentIdOrderBySort(parentId);

		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Department.class, DepartmentVo.class);
		}
		
		
		return list;
		
	}
	
	/**
	 * get list from admin
	 * @param parentId
	 * @return
	 */
	public List<DepartmentVo> listForAdmin(String parentId){
		List<DepartmentVo> list = null;
		
		List<Department> rows = departmentDao.findDepartmentByParentIdOrderBySort(parentId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Department.class, DepartmentVo.class);
		}
		
		
		return list;
		
	}
		
	public DepartmentVo get(String id){
		DepartmentVo o = null;
		Department row = departmentDao.findDepartmentById(id);
		o = BeanMapper.map(row, DepartmentVo.class);
		
		return o;
		
	}
	
	public Department getDepartment(String id){
		Department o = departmentDao.findDepartmentById(id);
		return o;
	}
	
	public List<String> getDepartmentIdsById(String departmentId, List<String> list){
		
		List<Department> rows = departmentDao.listChildrenRows(departmentId);
		
		if(rows.size() >0 ){
			getListIds(rows, list);
		}
		list.add(departmentId);
		
		return list;
	}
	
	public Department getUp(String id){
		Department o = null;
		List<Department> rows = departmentDao.getUp(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
	}
	
	
	public Department getDown(String id){
		Department o = null;
		List<Department> rows = departmentDao.getDown(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
	}
	

	public Department getChildrenRow(String id){
		Department o = null;
		List<Department> rows = departmentDao.listChildrenRows(id);
		if(rows.size() >0 ){
			o = rows.get(0);
		}
		
		return o;
		
	}
	
	@Transactional
	public void add(Department o){
		departmentDao.clear();
		departmentDao.save(o);
	}

	@Transactional
	public void save(Department o){
		departmentDao.save(o);
	}
	
	@Transactional
	public void delete(String id){
		departmentDao.delete(id);
	}
	
	/**
     * 创建动态查询条件组合.
     */
//    private Specification<Department> buildSpecification(Map<String, Object> searchParams) {
//        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//        Specification<Department> spec = DynamicSpecifications.bySearchFilter(filters.values(), Department.class);
//        return spec;
//    }
	
	private void getListIds(List<Department> departments, List<String> list){
		for(int i=0; i<departments.size(); i++){
			list.add(departments.get(i).getId());
			if(departments.get(i).getChildren().size() >0){
				getListIds(departments.get(i).getChildren(), list);
			}
		}
	}
	
}
