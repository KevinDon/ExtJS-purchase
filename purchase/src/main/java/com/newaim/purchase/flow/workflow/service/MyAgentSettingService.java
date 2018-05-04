package com.newaim.purchase.flow.workflow.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.flow.workflow.dao.MyAgentSettingDao;
import com.newaim.purchase.flow.workflow.entity.MyAgentSetting;
import com.newaim.purchase.flow.workflow.vo.MyAgentSettingVo;

@Service
@Transactional(readOnly=true)
public class MyAgentSettingService extends ServiceBase {

	@Autowired
	private MyAgentSettingDao myAgentSettingDao;

	@Autowired
	private UserService userService;
	
	public Page<MyAgentSettingVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MyAgentSetting> spec =buildSpecification(params);
        Page<MyAgentSetting> p = myAgentSettingDao.findAll(spec, pageRequest);
        Page<MyAgentSettingVo> page = p.map(new Converter<MyAgentSetting, MyAgentSettingVo>() {
		    @Override
		    public MyAgentSettingVo convert(MyAgentSetting dv) {
		        return BeanMapper.map(dv, MyAgentSettingVo.class);
		    }
		});
        
		return page;
	}

	public Page<MyAgentSettingVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<MyAgentSetting> spec =buildSpecification(params);
		Page<MyAgentSetting> p = myAgentSettingDao.findAll(spec, pageable);
		Page<MyAgentSettingVo> page = p.map(new Converter<MyAgentSetting, MyAgentSettingVo>() {
			@Override
			public MyAgentSettingVo convert(MyAgentSetting dv) {
				return BeanMapper.map(dv, MyAgentSettingVo.class);
			}
		});

		return page;
	}
	
	public List<MyAgentSettingVo> listByToUserId(String id) {
		List<MyAgentSettingVo> list = Lists.newArrayList();
		
		List<MyAgentSetting> rows = myAgentSettingDao.findMyAgentSettingByToUserId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MyAgentSetting.class, MyAgentSettingVo.class);
		}
		
		return list;
	}
	
	public List<MyAgentSettingVo> listByFromUserId(String id) {
		List<MyAgentSettingVo> list = Lists.newArrayList();
		
		List<MyAgentSetting> rows = myAgentSettingDao.findMyAgentSettingByFromUserId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MyAgentSetting.class, MyAgentSettingVo.class);
		}
		
		return list;
	}
	
	public MyAgentSettingVo get(String id){
		MyAgentSetting row = myAgentSettingDao.findMyAgentSettingById(id);
		MyAgentSettingVo o = BeanMapper.map(row, MyAgentSettingVo.class);
		
		return o;
	}

	public MyAgentSetting getMyAgentSetting(String id){
		MyAgentSetting o = myAgentSettingDao.findMyAgentSettingById(id);
		return o;
	}
		
	@Transactional
	public MyAgentSetting add(MyAgentSetting o){
		myAgentSettingDao.clear();
		
		UserVo fromUser = userService.get(o.getFromUserId());
		o.setFromUserId(fromUser.getId());
		o.setFromUserCnName(fromUser.getCnName());
		o.setFromUserEnName(fromUser.getEnName());
		o.setFromDepartmentId(fromUser.getDepartmentId());
		o.setFromDepartmentCnName(fromUser.getDepartmentCnName());
		o.setFromDepartmentEnName(fromUser.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		o.setId(null);
		o.setStatus(1);
		o.setCreatedAt(new Date());
		
    	return myAgentSettingDao.save(o);
	}
		
	@Transactional
	public MyAgentSetting add(MyAgentSetting o,Integer status){
		myAgentSettingDao.clear();
		
		UserVo fromUser = userService.get(o.getFromUserId());
		o.setFromUserId(fromUser.getId());
		o.setFromUserCnName(fromUser.getCnName());
		o.setFromUserEnName(fromUser.getEnName());
		o.setFromDepartmentId(fromUser.getDepartmentId());
		o.setFromDepartmentCnName(fromUser.getDepartmentCnName());
		o.setFromDepartmentEnName(fromUser.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		o.setId(null);
		o.setStatus(status);
		o.setCreatedAt(new Date());
		
		return myAgentSettingDao.save(o);
	}
	
	@Transactional
	public MyAgentSetting save(MyAgentSetting o){
		
		UserVo fromUser = userService.get(o.getFromUserId());
		o.setFromUserId(fromUser.getId());
		o.setFromUserCnName(fromUser.getCnName());
		o.setFromUserEnName(fromUser.getEnName());
		o.setFromDepartmentId(fromUser.getDepartmentId());
		o.setFromDepartmentCnName(fromUser.getDepartmentCnName());
		o.setFromDepartmentEnName(fromUser.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		o.setUpdatedAt(new Date());
		
		return myAgentSettingDao.save(o);
	}
	
	
	@Transactional
	public void delete(String id){
		myAgentSettingDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		MyAgentSetting o = getMyAgentSetting(id);
		o.setStatus(3);
		myAgentSettingDao.save(o);
	}
	
	/**
	 * 通过Id，查找同批次的授权
	 * @param MyAgentSettingId
	 * @return List<MyAgentSetting>
	 */
	public 	List<MyAgentSetting> findSameMyAgentSettingById(String id){
		return myAgentSettingDao.findSameMyAgentSettingById(id);
	}

	/**
	 * 通过用户列表，查找代理人，若列表中无代理人的，返回自身
	 * @param users 用户列表
	 * @return
	 */
	public List<User> listAgentUsersByFromUsers(List<User> users,String flowCode){
		Date now = new Date();
		List<User> reslut = Lists.newArrayList();
		if(users != null){
			for(int i=0;i<users.size();i++){
				User userObj=users.get(i);
				boolean sign = true;
				// 1. 获取用户对应的有效的代理设置
				List<MyAgentSetting> list = myAgentSettingDao.findAllowedMyAgentSettingByFromUserId(userObj.getId(),flowCode, now);
				if(list != null){
					for(MyAgentSetting obj : list){
					if(obj.getFlowCode().equals("ALL")||obj.getFlowCode().equals(flowCode)){
						User u = userService.getUser(obj.getToUserId());
						sign = false;
						if(!reslut.contains(u)){
							reslut.add(u);
						}
					}
					}
				}
				if(sign){
					reslut.add(userObj);
				}
			}
		}
		return reslut;
	}
	
	
	public List<MyAgentSetting> findAllowedMyAgentSettingByFromUserId(String userId,String flowCode,Date now){
		return myAgentSettingDao.findAllowedMyAgentSettingByFromUserId(userId,flowCode, now);
	}
	
	public List<MyAgentSetting> findAllowedMyAgentSettingByFromUserId(String userId,Date now){
		return myAgentSettingDao.findAllowedMyAgentSettingByFromUserId(userId, now);
	}
	
	public List<MyAgentSetting> findAllowedMyAgentSettingByToUserId(String userId,Date now){
		return myAgentSettingDao.findAllowedMyAgentSettingByToUserId(userId, now);
	}
	
	/**
	 * 通过用户列表，查找用户列表中所有上级授权人
	 * @param users 用户列表
	 * @return
	 */
	public List<User> listAgentUsersByToUsers(List<User> users,String flowCode){
		Date now = new Date();
		List<User> reslut = Lists.newArrayList();
		if(users != null){
			for(int i=0;i<users.size();i++){
				User userObj=users.get(i);
				// 1. 获取用户对应的有效的代理设置
				List<MyAgentSetting> list = myAgentSettingDao.findAllowedMyAgentSettingByToUserId(userObj.getId(),flowCode, now);
				if(list != null){
					for(MyAgentSetting obj : list){
						if(obj.getFlowCode().equals("ALL")||obj.getFlowCode().equals(flowCode)){
							User u = userService.getUser(obj.getFromUserId());
							if(!reslut.contains(u)){
								reslut.add(u);
							}
						}
					}
				}
			}
		}
		return reslut;
	}
	/**
	 * 通过用户userIds，查找用户列表中所有上级授权人
	 * @param userIds 用户id,id...
	 * @return
	 */
	public List<User> listAgentUsersByToUserIds(String userIds,String flowCode){
		Date now = new Date();
		List<User> reslut = Lists.newArrayList();
		String[] userIdArr = userIds.split(",");
		if(userIdArr != null){
			for(int i=0;i<userIdArr.length;i++){
				// 1. 获取用户对应的有效的代理设置
				List<MyAgentSetting> list = myAgentSettingDao.findAllowedMyAgentSettingByToUserId(userIdArr[i],flowCode, now);
				if(list != null){
					for(MyAgentSetting obj : list){
						if(obj.getFlowCode().equals("ALL")||obj.getFlowCode().equals(flowCode)){
							User u = userService.getUser(obj.getFromUserId());
							if(!reslut.contains(u)){
								reslut.add(u);
							}
						}
					}
				}
			}
		}
		return reslut;
	}
	/**
	 * 通过用户，查找用户列表中所有上级授权人
	 * @param user
	 * @return
	 */
	public List<User> listAgentUsersByToUsers(User user,String flowCode){
		Date now = new Date();
		List<User> reslut = Lists.newArrayList();
				// 1. 获取用户对应的有效的代理设置
				List<MyAgentSetting> list = myAgentSettingDao.findAllowedMyAgentSettingByToUserId(user.getId(),flowCode, now);
				if(list != null){
					for(MyAgentSetting obj : list){
						if(obj.getFlowCode().equals("ALL")||obj.getFlowCode().equals(flowCode)){
							User u = userService.getUser(obj.getFromUserId());
							if(!reslut.contains(u)){
								reslut.add(u);
							}
						}
					}
				}
		return reslut;
	}
	
	
	/**
	 * 获取去重后的所有记录的id
	 * @param 
	 * @return ids
	 */
	public String finDistinctMyAgentSettingIds(){
		List<String> myAgentSettingIdList= myAgentSettingDao.finDistinctMyAgentSettingIds();
		String result = StringUtils.join(myAgentSettingIdList.toArray(), ",");
		return result;
	}
	
	
	
	private Specification<MyAgentSetting> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MyAgentSetting> spec = DynamicSpecifications.bySearchFilter(filters.values(), MyAgentSetting.class);
        return spec;
    }	
}
