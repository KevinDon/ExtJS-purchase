package com.newaim.purchase.desktop.message.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import com.newaim.purchase.flow.workflow.vo.TaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * Created by Mark on 2017/10/13.
 */
@RestController
@RequestMapping("/desktop/events")
public class EventsController extends ControllerBase{

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize){
        RestResult result = new RestResult();
        try {
            Page<TaskVo> page = activitiService.findAllTodoTask(new PageRequest(pageNumber - 1, pageSize), keywords);
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            e.printStackTrace();
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id){
        RestResult result = new RestResult();
        try{
            TaskVo task = activitiService.getTaskVo(id);
            result.setSuccess(true).setData(task).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }
}
