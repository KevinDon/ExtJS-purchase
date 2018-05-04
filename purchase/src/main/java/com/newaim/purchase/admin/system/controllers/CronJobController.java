package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.CronJob;
import com.newaim.purchase.admin.system.service.CronJobService;
import com.newaim.purchase.admin.system.vo.CronJobVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/cronjob")
public class CronJobController extends ControllerBase {

    @Autowired
    private CronJobService cronJobService;

//    @RequiresPermissions("MyDocument:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ){
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("id-S-LK-OR", keywords);
                params.put("code-S-LK-OR", keywords);
                params.put("name-S-LK-OR", keywords);
                
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
//            params = setParams(params, "MyDocument", ":4:3:2:1", false);
            Page<CronJobVo> rd = cronJobService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            CronJobVo rd =  cronJobService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") CronJob main) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){
                cronJobService.update(main);
                //重启动任务
                if(Constants.Status.NORMAL.code.equals(main.getStatus())){
                    cronJobService.startCronJobByCode(main.getCode());
                }else{
                    cronJobService.stopCronJobByCode(main.getCode());
                }
            }
            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgSaveFailure("save", e.getMessage()));
        }
        return result;
    }

    @PostMapping("/startJob")
    public RestResult startJob(String code) {
        RestResult result = new RestResult();
        try {
            cronJobService.startCronJobByCode(code);
            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgOperateSuccess());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @PostMapping("/runJob")
    public RestResult runJob(String code) {
        RestResult result = new RestResult();
        try {
            cronJobService.runJobByCode(code);
            if(!ListUtils.isEmpty(cronJobService.getRunMessages())){
                result.setSuccess(true).setData(null).setMsg(StringUtils.join(cronJobService.getRunMessages(), ";</br>"));
            }else {
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgGetSuccess());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @PostMapping("/stopJob")
    public RestResult stopJob(String code) {
        RestResult result = new RestResult();
        try {
            cronJobService.stopCronJobByCode(code);
            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgOperateSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
        }
        return result;
    }

    @ModelAttribute("main")
    public CronJob main(String id){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(id)){
            return cronJobService.getCronJob(id);
        }else{
            return new CronJob();
        }
    }

}
