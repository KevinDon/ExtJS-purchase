package com.newaim.purchase.admin.account.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.EncodeUtils;
import com.newaim.purchase.admin.account.dao.UserDao;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/8/10.
 */

@Service
@Transactional(readOnly=true, rollbackFor = Exception.class)
public class UserService extends ServiceBase {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

    @Autowired
    private UserDao userDao;
    
    public Page<UserVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<User> spec = buildSpecification(params);
        Page<User> p = userDao.findAll(spec, pageRequest);
        Page<UserVo> page = p.map(new Converter<User, UserVo>() {
		    @Override
		    public UserVo convert(User user) {
		        return BeanMapper.map(user, UserVo.class);
		    }
		});
        return page;
    }
    
	public UserVo get(String id) {
		return BeanMapper.map(userDao.findOne(id), UserVo.class);
	}
	
	
    public UserVo findUserVoByAccount(String account){
        return BeanMapper.map(userDao.findUserByAccount(account), UserVo.class);
    }

    public User findUserByAccount(String account){
        return userDao.findUserByAccount(account);
    }

    /**
     * 根据用户账号List获取 ID，并且以“，”间隔输出
     * @param userAccounts
     * @return
     */
    public String getUserIdsByAccounts(List<String> userAccounts){
        StringBuilder userIds = new StringBuilder();
        if(userAccounts!= null && userAccounts.size()>0) {

            List<User> userList = userDao.findUsersByAccountInAndStatusEquals(userAccounts, 1);
            if(userList != null && userList.size() > 0){
                for (int i = 0; i < userList.size(); i++) {
                    if(i > 0){
                        userIds.append(",");
                    }
                    userIds.append(userList.get(i).getId());
                }
            }

        }
        return userIds.toString();
    }

    public User getUser(String id){
        return userDao.findOne(id);
    }

    
    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
    	byte[] salt = EncodeUtils.generateSalt(SALT_SIZE);
		user.setSalt(EncodeUtils.encodeHex(salt));

		byte[] hashPassword = EncodeUtils.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(EncodeUtils.encodeHex(hashPassword));
    }
    
    @Transactional(rollbackFor = Exception.class)
	public void add(User o){
    	userDao.clear();
    	if(StringUtils.isNotBlank(o.getPlainPassword())) {
    		entryptPassword(o);
    	}
		userDao.save(o);
	}

    @Transactional(rollbackFor = Exception.class)
    public void save(User o){
    	if(StringUtils.isNotBlank(o.getPlainPassword())) {
    		entryptPassword(o);
    	}
		userDao.save(o);
	}
	
    @Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		userDao.delete(id);
	}

    /**
     * 通过当前部门id向上查找拥有指定角色的人
     *
     * @param roleCodes 角色code，默认以逗号分隔
     * @param departmentId 当前部门id
     * @return
     */
    public List<User> findUpUserByRoleCodeAndDepartmentId(String roleCodes, String departmentId){
        String[] codes = StringUtils.split(roleCodes, "|");
        return  userDao.findUpUserByRoleCodeAndDepartmentId(Lists.newArrayList(codes), departmentId);
    }

    /**
     * 通过当前部门id向下查找拥有指定角色的人
     *
     * @param roleCodes 角色code，默认以逗号分隔
     * @param departmentId 当前部门id
     * @return
     */
    public List<User> findDownUserByRoleCodeAndDepartmentId(String roleCodes, String departmentId){
        String[] codes = StringUtils.split(roleCodes, "|");
        return userDao.findDownUserByRoleCodeAndDepartmentId(Lists.newArrayList(codes), departmentId);
    }

    /**
     * 通过角色查找用户
     * @param roleCode 角色code
     * @return
     */
    public List<User> findUserByRoleCode(String roleCode){
        if(StringUtils.contains(roleCode, "|")){
            String[] codes = StringUtils.split(roleCode, "|");
            return userDao.findUserByRoleCodes(Lists.newArrayList(codes));
        }else{
            return userDao.findUserByRoleCode(roleCode);
        }
    }

    public List<User> findByDepartmentId(String departmentId){
        return userDao.findByDepartmentId(departmentId);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<User> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
        return spec;
    }
    
}
