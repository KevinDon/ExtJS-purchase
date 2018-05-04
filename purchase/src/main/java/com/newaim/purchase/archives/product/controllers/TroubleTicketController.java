package com.newaim.purchase.archives.product.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.TroubleTicket;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.service.TroubleTicketService;
import com.newaim.purchase.archives.product.vo.TroubleTicketProductsVo;
import com.newaim.purchase.archives.product.vo.TroubleTicketVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/archives/troubleTicket")
public class TroubleTicketController extends ControllerBase {

    @Autowired
    private TroubleTicketService troubleTicketService;

    @Autowired
    private ProductService productService;

    @RequiresPermissions("ProductProblem:normal:list")
    @PostMapping("/list")
    public RestResult listTroubleTicket(ServletRequest request, String sort, String keywords,
                                          @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                          @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("omsOrderId-S-LK-OR", keywords);
                params.put("orderNumber-S-LK-OR", keywords);
                params.put("memberNickname-S-LK-OR", keywords);
                params.put("transactionNumber-S-LK-OR", keywords);
                params.put("email-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);

            }else{
                params = ServletUtils.getParametersStartingWith(request);
            }
            params = setParams(params, "ProductProblem", ":4:3:2:1", false);
            Page<TroubleTicketVo> page = troubleTicketService.list(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
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
            TroubleTicketVo rd = troubleTicketService.get(id);

            if(rd.getDetails().size()>0){
                for (int i=0; i<rd.getDetails().size(); i++){
                    rd.getDetails().get(i).setProduct(productService.get(rd.getDetails().get(i).getProductId()));
                }
            }

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    @RequiresPermissions(value = {"ProductProblem:normal:add", "ProductProblem:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") TroubleTicket main, TroubleTicketProductsVo troubleTicketProducts,
                           @ModelAttribute("attachments") AttachmentsVo attachments){
        RestResult result = new RestResult();
        try {
            if(ACT_ADD.equals(act)){
                troubleTicketService.add(main, attachments.getAttachments(), troubleTicketProducts.getProducts());
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                
            }else if(StringUtils.isNotBlank(main.getId())){
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                    main.setId(null);
                    troubleTicketService.saveAs(main, attachments.getAttachments(), troubleTicketProducts.getProducts());
                }else{
                    troubleTicketService.update(main, attachments.getAttachments(), troubleTicketProducts.getProducts());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"ProductProblem:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            troubleTicketService.deleteTroubleTicket(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    @ModelAttribute("main")
    public TroubleTicket troubleTicket(String id){
        if(StringUtils.isNotBlank(id)){
            return troubleTicketService.getTroubleTicket(id);
        }
        return new TroubleTicket();
    }
}
