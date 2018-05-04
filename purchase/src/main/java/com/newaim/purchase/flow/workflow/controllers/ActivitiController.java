package com.newaim.purchase.flow.workflow.controllers;

import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Mark on 2017/9/28.
 */
@RestController
@RequestMapping("/flow/workflow")
public class ActivitiController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequestMapping("/process/delete/{deploymentId}")
    public String delete(@PathVariable("deploymentId") String deploymentId){
        //级联删除相关实例
        repositoryService.deleteDeployment(deploymentId, true);
        return "success";
    }

    /**
     * 指定代理人
     */
    @PostMapping("/setagent")
    public RestResult setAgent(String taskId, String agentAccount){
        RestResult result = new RestResult();
        try{
            activitiService.setAgent(taskId, agentAccount);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }

    @GetMapping("/viewimage")
    public void viewImage(String processInstanceId,String processDefinitionKey, HttpServletResponse response) throws IOException {
        if(StringUtils.isNotBlank(processInstanceId) && "null".equals(processInstanceId)){
            processInstanceId = null;
        }
        activitiService.viewImage(processInstanceId, processDefinitionKey, response);
    }

}
