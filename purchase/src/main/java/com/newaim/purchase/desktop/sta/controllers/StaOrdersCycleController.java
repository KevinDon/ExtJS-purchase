package com.newaim.purchase.desktop.sta.controllers;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.ExportService;
import com.newaim.purchase.admin.system.vo.ExportColumnsVo;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import com.newaim.purchase.desktop.sta.entity.StaOrdersCycle;
import com.newaim.purchase.desktop.sta.service.StaOrdersCycleService;
import com.newaim.purchase.desktop.sta.vo.StaOrdersCycleVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/desktop/staorderscycle")
public class StaOrdersCycleController extends ControllerBase {

    @Autowired
    private StaOrdersCycleService staOrdersCycleService;
    @Autowired
    private ExportService exportService;

//    @RequiresPermissions("StaOrder:normal:list")
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
                params.put("orderNumber-S-LK-OR", keywords);
                params.put("orderTitle-S-LK-OR", keywords);
                params.put("vendorId-S-LK-OR", keywords);
                params.put("vendorCnName-S-LK-OR", keywords);
                params.put("vendorEnName-S-LK-OR", keywords);
//                params.put("destinationPortCnName-S-LK-OR", keywords);
//                params.put("destinationPortEnName-S-LK-OR", keywords);
//                params.put("creatorCnName-S-LK-OR", keywords);
//                params.put("creatorEnName-S-LK-OR", keywords);
//                params.put("reviewerCnName-S-LK-OR", keywords);
//                params.put("reviewerEnName-S-LK-OR", keywords);
//                params.put("reviewerDepartmentCnName-S-LK-OR", keywords);
//                params.put("reviewerDepartmentEnName-S-LK-OR", keywords);
//                params.put("departmentCnName-S-LK-OR", keywords);
//                params.put("departmentEnName-S-LK-OR", keywords);
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


            if(hasDataType("StaOrder:4")){
                //带禁用的数据

            }else{
                //非禁用的数据
                //params.put("status-N-EQ-ADD", "1");
            }

            if(hasDataType("StaOrder:3")){
                //不分部门
//				params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("User:2")){
                //部门内
                //params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("StaOrder:1")){
                //自身
                //params.put("status-N-EQ-ADD", "1");
            }
//            params = setParams(params, "StaOrder", ":4:3:2:1", false);
            Page<StaOrdersCycleVo> page = staOrdersCycleService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            StaOrdersCycleVo rd =  staOrdersCycleService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }
        return result;
    }

    /**
     *同步视图
     * @return
     */
    @PostMapping("/syncView")
    public RestResult syncView(){
        RestResult result = new RestResult();
        try {
            List<StaOrdersCycle> staOrdersCycles =  staOrdersCycleService.copyFromView();
            result.setSuccess(true).setData(staOrdersCycles).setMsg(localeMessageSource.getMsgUpdateSuccess());
        }catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgUpdateFailure(e.getMessage()));
        }
        return result;
    }


    @RequiresPermissions("StaOrder:normal:export")
    @PostMapping("/export")
    public RestResult export(String act, String fileTitle, String fileName, String id, @ModelAttribute("columns") ExportColumnsVo columns,
                             HttpServletRequest request, String modal, String sort, String keywords,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                             Integer type
    ) {
        RestResult result = new RestResult();

        if (ACT_PDF.equals(act) || ACT_EXCEL.equals(act) || ACT_CSV.equals(act)) {
            RestResult rl = list(request, sort, keywords, pageNumber, 500);
            if (rl.getTc() < 1) {
                result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateFailure());
                return result;
            }

            try {
                MyDocumentVo mydoc = exportService.exportFile(request, act, fileTitle, fileName, id, columns, (List<StaOrdersCycleVo>) rl.getData());

                if (null != mydoc) {
                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
                } else {
                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        } else if (ACT_WORD.equals(act) || ACT_EMAIL_WORD.equals(act)) {
            StaOrdersCycleVo vo = staOrdersCycleService.get(id);

            try {
                MyDocumentVo mydoc = exportService.exportFileForWordByXml(request, act, fileTitle, fileName, vo);

                if (null != mydoc) {
                    result.setSuccess(true).setData(mydoc).setMsg(localeMessageSource.getMsgOperateSuccess());
                } else {
                    result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
            }

        }

        return result;
    }
}
