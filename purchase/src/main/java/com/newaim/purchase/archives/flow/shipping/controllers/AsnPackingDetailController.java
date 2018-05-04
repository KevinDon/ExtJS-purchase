package com.newaim.purchase.archives.flow.shipping.controllers;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.archives.flow.shipping.service.AsnPackingDetailService;
import com.newaim.purchase.archives.flow.shipping.vo.AsnPackingDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shipping/asnpackingdetail")
public class AsnPackingDetailController extends ControllerBase {

    @Autowired
    private AsnPackingDetailService asnPackingDetailService;

    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("cnName-S-LK-OR", keywords);
                params.put("enName-S-LK-OR", keywords);
                params.put("account-S-LK-OR", keywords);
                params.put("email-S-LK-OR", keywords);
                params.put("qq-S-LK-OR", keywords);
                params.put("skype-S-LK-OR", keywords);
                params.put("wechat-S-LK-OR", keywords);
                params.put("phone-S-LK-OR", keywords);
                params.put("extension-S-LK-OR", keywords);
                params.put("department.cnName-S-LK-OR", keywords);
                params.put("department.enName-S-LK-OR", keywords);
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

            Page<AsnPackingDetailVo> page = asnPackingDetailService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listByOrderId")
    public RestResult listByOrderId(String orderId) {
        RestResult result = new RestResult();
        try {
            List<AsnPackingDetailVo> data = asnPackingDetailService.findMergeDetailVosByOrderId(orderId);
            result.setSuccess(true).setData(data).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }
}
