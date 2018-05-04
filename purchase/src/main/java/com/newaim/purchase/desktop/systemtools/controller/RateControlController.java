package com.newaim.purchase.desktop.systemtools.controller;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.desktop.systemtools.entity.RateControl;
import com.newaim.purchase.desktop.systemtools.service.RateControlService;
import com.newaim.purchase.desktop.systemtools.vo.RateControlVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/desktop/rateControl")
public class RateControlController extends ControllerBase {

    @Autowired
    private RateControlService rateControlService;
    @RequiresPermissions("ExchangeRate:normal:list")
    @PostMapping("/list")
    public RestResult listRateControl(ServletRequest request, String sort, String keywords,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
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
          System.out.println(params);
          params = setParams(params, "ExchangeRate", ":4:3:2:1", false);
            Page<RateControlVo> page = rateControlService.listRateControl(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }
    @RequiresPermissions("ExchangeRate:normal:list")
    @PostMapping("/get")
    public RestResult get(String id, String product) {
        RestResult result = new RestResult();
        try {
            RateControlVo rc = new RateControlVo();

            if(StringUtils.isBlank(product)){
                rc =  rateControlService.get(id);
            }
            result.setSuccess(true).setData(rc).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }
    
    @RequiresPermissions(value = {"ExchangeRate:normal:add", "ExchangeRate:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") RateControl main){
        RestResult result = new RestResult();
        try {
            if("add".equals(act)){
                rateControlService.add(main);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else if( StringUtils.isNotBlank(main.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    main.setId(null);
                    rateControlService.saveAs(main);
                }else{
                    rateControlService.save(main);
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @RequiresPermissions("ExchangeRate:normal:del")
    @PostMapping("/delete")
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            rateControlService.deleteRateControl(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }


    @ModelAttribute("main")
    public RateControl main(String id){
        if(StringUtils.isNotBlank(id)){
            return rateControlService.getRateControl(id);
        }
        return new RateControl();
    }

}
