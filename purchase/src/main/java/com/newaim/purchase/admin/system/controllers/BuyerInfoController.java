package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.entity.BuyerInfo;
import com.newaim.purchase.admin.system.service.BuyerInfoService;
import com.newaim.purchase.admin.system.vo.BuyerInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;


@RestController
@RequestMapping("/system/buyerInfo")
public class BuyerInfoController extends ControllerBase {

    @Autowired
    private BuyerInfoService buyerInfoService;

    @PostMapping("/list")
    public RestResult listBuyerInfo(ServletRequest request, String sort, String keywords,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("cnName-S-LK-OR", keywords);
                params.put("enName-S-LK-OR", keywords);
                params.put("cnAddress-S-LK-OR", keywords);
                params.put("enAddress-S-LK-OR", keywords);
                params.put("contactCnName-S-LK-OR", keywords);
                params.put("contactEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
                params.put("email-S-LK-OR", keywords);
                params.put("fax-S-LK-OR", keywords);
                params.put("phone-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
            Page<BuyerInfoVo> page = buyerInfoService.listBuyerInfo(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    public RestResult get(String id, String product) {
        RestResult result = new RestResult();
        try {
            BuyerInfoVo rd = new BuyerInfoVo();

            if(StringUtils.isBlank(product)){
                rd =  buyerInfoService.get(id);
            }
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    public RestResult save(String act, @ModelAttribute("main") BuyerInfo main){
        RestResult result = new RestResult();
        try {
            if("add".equals(act)){
                buyerInfoService.add(main);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else if( StringUtils.isNotBlank(main.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    main.setId(null);
                    buyerInfoService.saveAs(main);
                }else{
                    buyerInfoService.save(main);
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @ModelAttribute("main")
    public BuyerInfo main(String id){
        if(StringUtils.isNotBlank(id)){
            return buyerInfoService.getBuyerInfo(id);
        }
        return new BuyerInfo();
    }

    @PostMapping("/delete")
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            buyerInfoService.deleteBuyerInfo(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

}
