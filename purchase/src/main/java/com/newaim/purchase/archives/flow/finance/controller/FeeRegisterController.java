package com.newaim.purchase.archives.flow.finance.controller;


import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.service.FeeRegisterService;
import com.newaim.purchase.archives.flow.finance.vo.FeeRegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/finance/feeRegister")
public class FeeRegisterController extends ControllerBase {

    @Autowired
    private FeeRegisterService feeRegisterService;
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
             	params.put("orderNumber-S-LK-OR", keywords);
				params.put("orderTitle-S-LK-OR", keywords);
            	params.put("beneficiaryBank-S-LK-OR", keywords);
				params.put("bankAccount-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
			/*	params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
				params.put("reviewerCnName-S-LK-OR", keywords);
				params.put("reviewerEnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentCnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentEnName-S-LK-OR", keywords);*/
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
				params.put("companyCnName-S-LK-OR", keywords);
				params.put("companyEnName-S-LK-OR", keywords);
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
            Page<FeeRegisterVo> page = feeRegisterService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listForDialog")
    public RestResult listForDialog(ServletRequest request, String sort, String keywords,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
    ) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
            	params.put("orderNumber-S-LK-OR", keywords);
             	params.put("beneficiaryBank-S-LK-OR", keywords);
			 	params.put("bankAccount-S-LK-OR", keywords);
				params.put("companyCnName-S-LK-OR", keywords);
				params.put("companyEnName-S-LK-OR", keywords);
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
            if(hasDataType("FeeRegister:4")){
                //带禁用的数据

            }else{
                //非禁用的数据
                //params.put("status-N-EQ-ADD", "1");
            }

            if(hasDataType("FeeRegister:3")){
                //不分部门
//				params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("User:2")){
                //部门内
                //params.put("status-N-EQ-ADD", "1");
            }else if(hasDataType("FeeRegister:1")){
                //自身
                //params.put("status-N-EQ-ADD", "1");
            }
            params.put("status-N-EQ-ADD", Constants.Status.NORMAL.code);
            params.put("hold-N-EQ-ADD", Constants.HoldStatus.UN_HOLD.code);
            params.put("paymentStatus-N-EQ", Constants.FeePaymentStatus.UNPAID.code.toString());
            Page<FeeRegisterVo> page = feeRegisterService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setPage(page).setMsg(localeMessageSource.getMsgListSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

}
