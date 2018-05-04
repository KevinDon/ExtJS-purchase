package com.newaim.purchase.archives.product.controllers;

import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.archives.product.entity.TroubleTicketRemark;
import com.newaim.purchase.archives.product.service.TroubleTicketRemarkService;
import com.newaim.purchase.archives.product.vo.TroubleTicketRemarkVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/archives/troubleTicket/remark")
public class TroubleTicketRemarkController extends ControllerBase {

    @Autowired
    private TroubleTicketRemarkService troubleTicketRemarkService;

    @RequiresPermissions("ProductProblem:normal:list")
    @PostMapping("/list")
    public RestResult listByTroubleTicketId(String troubleTicketId){
        RestResult result = new RestResult();
        try{
            List<TroubleTicketRemarkVo> remarks = troubleTicketRemarkService.findRemarksVoByTroubleTicketId(troubleTicketId);
            result.setSuccess(true).setData(remarks).setMsg(localeMessageSource.getMsgListSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"ProductProblem:normal:list"}, logical = Logical.OR)
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            TroubleTicketRemarkVo rd = troubleTicketRemarkService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/update")
    @RequiresPermissions(value = {"ProductProblem:normal:add", "ProductProblem:normal:edit"}, logical = Logical.OR)
    public RestResult save(@ModelAttribute("main") TroubleTicketRemark main){
        RestResult result = new RestResult();
        try {
            main.setUpdatedAt(new Date());
            troubleTicketRemarkService.save(main);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"ProductProblem:normal:del"}, logical = Logical.OR)
    public RestResult delete(String id){
        RestResult result = new RestResult();
        try {
            troubleTicketRemarkService.delete(id);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    @ModelAttribute("main")
    public TroubleTicketRemark troubleTicket(String id){
        if(StringUtils.isNotBlank(id)){
            return troubleTicketRemarkService.getTroubleTicketRemark(id);
        }
        return new TroubleTicketRemark();
    }
}
