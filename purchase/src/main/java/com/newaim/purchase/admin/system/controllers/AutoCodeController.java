package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.entity.AutoCode;
import com.newaim.purchase.admin.system.service.AutoCodeService;
import com.newaim.purchase.admin.system.vo.AutoCodeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/admin/autocode")
public class AutoCodeController extends ControllerBase {

    @Autowired
    private AutoCodeService autoCodeeService;

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
                params.put("type-S-LK-OR", keywords);
                params.put("title-S-LK-OR", keywords);

            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
//            params = setParams(params, "MyDocument", ":4:3:2:1", false);
            Page<AutoCodeVo> rd = autoCodeeService.list(params, pageNumber, pageSize, getSort(sort));
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
            AutoCodeVo rd =  autoCodeeService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") AutoCode main) {
        RestResult result = new RestResult();
        try {
            if( StringUtils.isNotBlank(main.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    main.setId(null);
                    autoCodeeService.saveAs(main);
                }else{
                    autoCodeeService.update(main);
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else{
                autoCodeeService.add(main);
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            autoCodeeService.delete(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/generate")
    public RestResult generateValue(String code, String departmentId){
        RestResult result = new RestResult();
        try {
            result.setData(autoCodeeService.generateValue(code, departmentId));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }
}
