package com.newaim.purchase.admin.account.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.EncodeUtils;
import com.newaim.purchase.admin.account.dao.RoleResourceUnionDao;
import com.newaim.purchase.admin.account.dao.UserDao;
import com.newaim.purchase.admin.account.dao.UserRoleUnionDao;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;
import com.newaim.purchase.admin.account.vo.UserVo;

import javax.annotation.PostConstruct;

import java.util.List;

/**
 * Created by Mark on 2017/7/24.
 */
@Component("shiroDbRealm")
public class ShiroDbRealm extends AuthorizingRealm{

    @Autowired
    protected UserDao userDao;
    @Autowired
    protected UserRoleUnionDao userRoleUnionDao;
    @Autowired
    protected RoleResourceUnionDao roleResourceUnionDao;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        User user = userDao.findUserByAccount(token.getUsername());
        
        UserVo userDto = BeanMapper.map(user, UserVo.class);
        if(user != null){
            byte[] salt = EncodeUtils.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(userDto, user.getPassword(), ByteSource.Util.bytes(salt), getName());
        }else{
            return null;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	UserVo userDto = (UserVo) principals.getPrimaryPrincipal();
        User user = userDao.findUserByAccount(userDto.getAccount());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> roles = userDto.getRoles();
        for (String role : roles) {
//            info.addRole(role);
            info.addStringPermission(role);
        }
        
        List<UserRoleUnion> role = userRoleUnionDao.findUserRoleUnionByUserId(user.getId());
        if(role != null && role.size()>0){
        	for(int i=0; i<role.size(); i++){
        		List<RoleResourceUnion> rru = roleResourceUnionDao.findRoleResourceUnionByRoleId(role.get(i).getRoleId());
        		for(int j=0; j< rru.size(); j++){
        			if(StringUtils.isNotBlank(rru.get(j).getAction())){
        				info.addStringPermission(rru.get(j).getModel() + ":" + rru.get(j).getAction());
        			}
        		}
        	}
        }
        
        return info;
    }

    @PostConstruct
    public void initCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA-1");
        matcher.setHashIterations(1024);
        setCredentialsMatcher(matcher);
    }

    
}
