package com.newaim.purchase.flow.workflow.controllers;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.flow.workflow.entity.FlowProcesses;
import com.newaim.purchase.flow.workflow.service.FlowProcessesService;
import com.newaim.purchase.flow.workflow.vo.FlowProcessesVo;

@RestController
@RequestMapping("/workfolw/processes")
public class FlowProcessesController extends ControllerBase{

    @Autowired
    private FlowProcessesService flowProcessesService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("FlowProcesses:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("code-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
			}

    		Page<FlowProcessesVo> rd = flowProcessesService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("FlowProcesses:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			FlowProcessesVo rd =  flowProcessesService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"FlowProcesses:normal:add","FlowProcesses:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") FlowProcesses main) {
		RestResult result = new RestResult();
		try {

			if(StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					main.setCreatedAt(new Date());
					main.setIsPublish(2);
					main.setId(null);
					flowProcessesService.add(main);
				}else{
					flowProcessesService.save(main);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				main.setIsPublish(2);
				main.setCreatedAt(new Date());
				flowProcessesService.add(main);
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("FlowProcesses:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {

			if(hasDataType("FlowProcesses" + ":4")){
				//物理删除
				flowProcessesService.delete(ids);
			}else{
				//删除标记
				flowProcessesService.setDelete(ids);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @RequiresPermissions("FlowProcesses:normal:edit") 
    @PostMapping("/deploy")
    public RestResult deploy(String id) {
    	RestResult result = new RestResult();
    	try {
    		
    		if(StringUtils.isNotBlank(id)) {
				result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDeployFailure());
    		}
				FlowProcesses rd =  flowProcessesService.getFlowProcesses(id);
			if(StringUtils.isNotBlank(rd.getCode())||StringUtils.isNotBlank(rd.getContent())||rd.getIsPublish()==1) {
				result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgDeployFailure());
			}
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RepositoryService repositoryService = processEngine.getRepositoryService();
			
			String bpmnText = rd.getContent();
			String code = rd.getCode();
			//字符串方式发布
		    Deployment deploy = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
	                .createDeployment()//创建部署对象  
	                .addString(code+".bpmn",bpmnText)
	                .deploy();//完成部署  
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
	                  .deploymentId(deploy.getId())
	                  .singleResult();
			rd.setDeploymentId(processDefinition.getDeploymentId());
			rd.setName(processDefinition.getName());
			rd.setCode(processDefinition.getKey());
			rd.setVer(processDefinition.getVersion());
			rd.setImage(processDefinition.getDiagramResourceName());
			rd.setDpmn(processDefinition.getResourceName());
			rd.setProcessDefinitionId(processDefinition.getId());
			rd.setPublishAt(new Date());
			rd.setIsPublish(1);
			
 			flowProcessesService.save(rd);
    		
    		result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeploySuccess());
    	} catch (Exception e) {
    		result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
    	}
    	
    	return result;
    }
    
    
    @ModelAttribute("main")
    public FlowProcesses main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return flowProcessesService.getFlowProcesses(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new FlowProcesses();
        }
        return null;
    }
}
