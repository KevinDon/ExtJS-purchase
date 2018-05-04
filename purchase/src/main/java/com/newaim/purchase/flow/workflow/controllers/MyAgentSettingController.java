package com.newaim.purchase.flow.workflow.controllers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.dao.RoleResourceUnionDao;
import com.newaim.purchase.admin.account.dao.UserRoleUnionDao;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.flow.workflow.entity.FlowProcesses;
import com.newaim.purchase.flow.workflow.entity.MyAgentResource;
import com.newaim.purchase.flow.workflow.entity.MyAgentSetting;
import com.newaim.purchase.flow.workflow.service.FlowProcessesService;
import com.newaim.purchase.flow.workflow.service.MyAgentResourceService;
import com.newaim.purchase.flow.workflow.service.MyAgentSettingService;
import com.newaim.purchase.flow.workflow.vo.FlowProcessesVo;
import com.newaim.purchase.flow.workflow.vo.MyAgentResourceVo;
import com.newaim.purchase.flow.workflow.vo.MyAgentSettingCodeListVo;
import com.newaim.purchase.flow.workflow.vo.MyAgentSettingVo;

@RestController
@RequestMapping("/flow/myagentsetting")
public class MyAgentSettingController extends ControllerBase{

    @Autowired
    private MyAgentSettingService myAgentSettingService;
    
    @Autowired
    private MyAgentResourceService myAgentResourceService;

    @Autowired
    private LocaleMessageSource localeMessageSource;
    
    @Autowired
    private FlowProcessesService flowProcessesService;
    
    @Autowired
    private UserRoleUnionDao userRoleUnionDao;
    
    @Autowired
    private RoleResourceUnionDao roleResourceUnionDao;

    @RequiresPermissions("MyAgentSetting:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
    		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("toUserCnName-S-LK-OR", keywords);
				params.put("toUserEnName-S-LK-OR", keywords);
				params.put("toDepartmentCnName-S-LK-OR", keywords);
				params.put("toDepartmentEnName-S-LK-OR", keywords);
				params.put("fromUserCnName-S-LK-OR", keywords);
				params.put("fromUserEnName-S-LK-OR", keywords);
				params.put("fromDepartmentCnName-S-LK-OR", keywords);
				params.put("fromDepartmentEnName-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
				params.put("flowCode-S-LK-OR", keywords);
				params.put("cnName-S-LK-OR", keywords);
				params.put("enName-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
				//搜索出指定部门以下的所有记录
				if(params.size()>0){
					if(params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())){
						String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
						params.remove("departmentId-S-EQ");
						params.put("departmentId-S-IN", depIds);
					}
				}
			}
			
			params = setParams(params, "MyAgentSetting", ":4:3:2:1", false);
    		Page<MyAgentSettingVo> rd = myAgentSettingService.list(params, pageNumber, pageSize, getSort(sort));
    		for(int i=0;i<rd.getContent().size();i++){
    			Date toTime = rd.getContent().get(i).getToTime();
    			Date fromTime = rd.getContent().get(i).getFromTime();
    			double times=(double)(toTime.getTime()-fromTime.getTime());
    			double hour = (double)(60*60*1000*24);
    	        DecimalFormat df = new DecimalFormat("#.0");  
    			String resultDays = df.format(times/hour); 
    			if(resultDays.length()==2){
    				if(resultDays.equals(".0")){
    					resultDays = "0.0";
    				}else{
    					resultDays = "0" + resultDays;
    				}
    			}
    			rd.getContent().get(i).setDays(resultDays);
    			
    		}
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    
    @RequiresPermissions("MyAgentSetting:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			MyAgentSettingVo rd =  myAgentSettingService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("MyAgentSetting:normal:list")
    @PostMapping("/processeslist")
    public List<FlowProcessesVo> processesList(String id){
    	List<FlowProcesses> list = flowProcessesService.lastestList();
		list = getProcessesListForLoginUser(list,id);
    	List<FlowProcessesVo> datas = Lists.newArrayList();
    	//默认勾选的授权流程
    	List<MyAgentSetting> sameList = myAgentSettingService.findSameMyAgentSettingById(id);
    	List<String> sameProcesseflowCodeList = Lists.newArrayList();
    	for(MyAgentSetting obj:sameList){
    		if(!sameProcesseflowCodeList.contains(obj.getFlowCode())){
    			sameProcesseflowCodeList.add(obj.getFlowCode());
    		}
    	}
    	for(FlowProcesses obj:list){
    		FlowProcessesVo vo = BeanMapper.map(obj, FlowProcessesVo.class);
        	if(sameProcesseflowCodeList.contains(obj.getCode())){
        		vo.setActive(true);
        	}else{
        		vo.setActive(false);
        	}
    		
    		datas.add(vo);
    	}
    	return datas;
    }
    
    
    private List<FlowProcesses> getProcessesListForLoginUser(List<FlowProcesses> flowProcesseslist,String id){
    	UserVo userVo = SessionUtils.currentUserVo();
    	List<FlowProcesses> resultList = Lists.newArrayList();
    	List<String> loginModelList = Lists.newArrayList();
    	List<String> grantedFlowCodeList = Lists.newArrayList();
    	List<String> toUserGrantedFlowCodeList = Lists.newArrayList();
    	//查询当前登录用户已授权的流程授权记录
    	List<MyAgentSetting>  grantedAgentSettingList = myAgentSettingService.findAllowedMyAgentSettingByFromUserId(userVo.getId(),new Date());
    	for(MyAgentSetting obj:grantedAgentSettingList){
    			grantedFlowCodeList.add(obj.getFlowCode());
    	}
    	if(id!=null){
			MyAgentSettingVo rd =  myAgentSettingService.get(id);
	    	List<MyAgentSetting>  toAgentSettingList = myAgentSettingService.findAllowedMyAgentSettingByToUserId(rd.getToUserId(),new Date());
	    	for(MyAgentSetting obj:toAgentSettingList){
	    		toUserGrantedFlowCodeList.add(obj.getFlowCode());
	    	}
    	}
    	//查询当前登录用户的权限，确认授权的流程
        List<UserRoleUnion> role = userRoleUnionDao.findUserRoleUnionByUserId(userVo.getId());
        if(role != null && role.size()>0){
        	for(UserRoleUnion ur: role){
        		List<RoleResourceUnion> rru = roleResourceUnionDao.findRoleResourceUnionByRoleId(ur.getRoleId());
        		for(int j=0; j< rru.size(); j++){
        			if(StringUtils.isNotBlank(rru.get(j).getModel())&&"flow:allow".equals(rru.get(j).getAction())){
        				loginModelList.add(rru.get(j).getModel());
        			}
        		}
        	}
		}
        for(FlowProcesses obj : flowProcesseslist){
        	if(loginModelList.contains(obj.getModel())&&!grantedFlowCodeList.contains(obj.getCode())){
        		resultList.add(obj);
        	}
        	if(toUserGrantedFlowCodeList.contains(obj.getCode())){
        		resultList.add(obj);
        	}
        }
        return resultList;
    }
    
    
    @RequiresPermissions(value = {"MyAgentSetting:normal:add","MyAgentSetting:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") MyAgentSetting main,@ModelAttribute("details")MyAgentSettingCodeListVo details) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			List<FlowProcesses> processesList = flowProcessesService.lastestList();
			if(StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					for(String code : details.getDetails()){
						main.setId(null);
						main.setFlowCode(code);
						for(FlowProcesses obj : processesList){
							if(obj.getCode().equals(code)){
								main.setCnName(obj.getContext());
								main.setEnName(obj.getName());
							}
						}
						main.setCreatedAt(new Date());
						myAgentSettingService.add(main);
						saveAgentReSource(main);
					}
				}else{
			    	List<MyAgentSetting> sameList = myAgentSettingService.findSameMyAgentSettingById(main.getId());
			    	for(MyAgentSetting delObj : sameList){
						myAgentSettingService.delete(delObj.getId());
						myAgentResourceService.deleteMyAgentResourceByBusinessId(delObj.getId());
			    	}
					for(String code : details.getDetails()){
						main.setFlowCode(code);
						for(FlowProcesses obj : processesList){
							if(obj.getCode().equals(code)){
								main.setCnName(obj.getContext());
								main.setEnName(obj.getName());
							}
						}
						main.setCreatedAt(new Date());
						myAgentSettingService.add(main,main.getStatus());
						saveAgentReSource(main);
					}
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				for(String code : details.getDetails()){
					main.setId(null);
					main.setFlowCode(code);
					for(FlowProcesses obj : processesList){
						if(obj.getCode().equals(code)){
							main.setCnName(obj.getContext());
							main.setEnName(obj.getName());
						}
					}
					main.setCreatedAt(new Date());
					myAgentSettingService.add(main);
					saveAgentReSource(main);
				}
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}

	private void saveAgentReSource(MyAgentSetting main) {
		List<String> flowModelList = Lists.newArrayList();
		List<String> removeActionList = Lists.newArrayList();
		removeActionList.add("flow:start");
		removeActionList.add("normal:add");
		removeActionList.add("normal:del");
		List<FlowProcesses> processesList = flowProcessesService.lastestList();
		if(main.getFlowCode().equals("ALL")||null==main.getFlowCode()){
		    //如果flowCode为all，则全部授权
			for(FlowProcesses flowObj : processesList){
				flowModelList.add(flowObj.getModel());
			}
		}else{
			for(FlowProcesses flowObj : processesList){
				if(main.getFlowCode().equals(flowObj.getCode())){
					flowModelList.add(flowObj.getModel());
				}
			}
		}
	    List<UserRoleUnion> role = userRoleUnionDao.findUserRoleUnionByUserId(main.getFromUserId());
	    if(role != null && role.size()>0){
	    	for(UserRoleUnion ur: role){
	    		List<RoleResourceUnion> rru = roleResourceUnionDao.findRoleResourceUnionByRoleId(ur.getRoleId());
	    		for(RoleResourceUnion roleResourceUnion : rru){
	    			//流程匹配的权限
	    			if(flowModelList.contains(roleResourceUnion.getModel())){
	    				//隔离流程发启、流程新建、流程删除权限
	    				if(!removeActionList.contains(roleResourceUnion.getAction())){
			    			if(StringUtils.isNotBlank(roleResourceUnion.getAction())||StringUtils.isNotBlank(roleResourceUnion.getData())){
			    				MyAgentResourceVo vo = new MyAgentResourceVo();
			    				vo.setBusinessId(main.getId());
			    				vo.setFromTime(main.getFromTime());
			    				vo.setToTime(main.getToTime());
			    				vo.setUserId(main.getToUserId());
			    				vo.setAction(roleResourceUnion.getAction());
			    				vo.setData(roleResourceUnion.getData());
			    				vo.setModel(roleResourceUnion.getModel());
			    				vo.setRoleId(roleResourceUnion.getRoleId());
			    				vo.setResourceId(roleResourceUnion.getResourceId());
			    				myAgentResourceService.save(vo);
			    			}
	    				}
	    			}
	    		}
	    	}
	    }
	}
    
    @RequiresPermissions("MyAgentSetting:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!myAgentSettingService.get(ids).getCreatorId().equals(user.getId()) ){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
				myAgentSettingService.delete(ids);
				myAgentResourceService.deleteMyAgentResourceByBusinessId(ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public MyAgentSetting main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return myAgentSettingService.getMyAgentSetting(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new MyAgentSetting();
        }
        return null;
    }
}
