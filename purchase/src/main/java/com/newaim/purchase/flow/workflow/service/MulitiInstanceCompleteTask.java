package com.newaim.purchase.flow.workflow.service;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;

/**
 * 会签功能所需方法
 * @author grady
 */
@Service("mulitiInstanceCompleteTask")
public class MulitiInstanceCompleteTask implements Serializable {
	

    @Autowired
    private UserService userService;
	
	private static final long serialVersionUID = 1L;
	public boolean completeTask(DelegateExecution execution) {
//		System.out.println("总的会签任务数量：" + execution.getVariable("nrOfInstances") + "当前获取的会签任务数量：" + execution.getVariable("nrOfActiveInstances") + " - " + "已经完成的会签任务数量：" + execution.getVariable("nrOfCompletedInstances"));
		if((int)execution.getVariable("nrOfCompletedInstances")==1) {
			return true;
		}else {
			return false;
		}
	}
	
	 public List<String> findUsers(String roleCodes, String findType){
		 	List<User> users = Lists.newArrayList();
		 	List<String> accountList = Lists.newArrayList();
            UserVo user = SessionUtils.currentUserVo();
            String[] roleCodesArr =  roleCodes.split(",");
            for(int i=0;i<roleCodesArr.length;i++) {
	            if(Constants.FlowFindType.UP_ROLE.code.equals(findType)){
	                //向上查找
	                users = userService.findUpUserByRoleCodeAndDepartmentId(roleCodesArr[i], user.getDepartmentId());
	            }else if(Constants.FlowFindType.DOWN_ROLE.code.equals(findType)){
	                //向下查找
	                users = userService.findDownUserByRoleCodeAndDepartmentId(roleCodesArr[i], user.getDepartmentId());
	            }else if(Constants.FlowFindType.ROLE.code.equals(findType)){
	                //通过角色查找
	                users = userService.findUserByRoleCode(roleCodesArr[i]);
	            }else{
	                //默认向上查找
	                users = userService.findUpUserByRoleCodeAndDepartmentId(roleCodesArr[i], user.getDepartmentId());
	            }
	            for(User u : users) {
	            	if(!accountList.contains(u.getAccount())){
	            		accountList.add(u.getAccount());
	            	}
	            }
            }
            return accountList;
	    }
	
}