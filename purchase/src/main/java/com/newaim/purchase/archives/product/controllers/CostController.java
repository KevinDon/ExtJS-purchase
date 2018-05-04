package com.newaim.purchase.archives.product.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.archives.product.entity.Cost;
import com.newaim.purchase.archives.product.entity.CostOrder;
import com.newaim.purchase.archives.product.service.CostService;
import com.newaim.purchase.archives.product.vo.*;
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
import java.util.List;

@RestController
@RequestMapping("/archives/cost")
public class CostController extends ControllerBase {

    @Autowired
    private CostService costService;

    @RequiresPermissions("ProductCost:normal:list")
    @PostMapping("/list")
    public RestResult listCost(ServletRequest request, String sort, String keywords,
                               @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                               @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if (StringUtils.isNotBlank(keywords)) {
                params.put("orderShippingPlanBusinessId-S-LK-OR", keywords);
                params.put("creatorId-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("departmentId-S-LK-OR", keywords);
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
            } else {
                params = ServletUtils.getParametersStartingWith(request);
            }
            params = setParams(params, "ProductCost", ":4:3:2:1", false);
            Page<CostVo> page = costService.listCost(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/get")
    @RequiresPermissions(value = {"ProductCost:normal:list"}, logical = Logical.OR)
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {
            CostVo rd = costService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    @RequiresPermissions(value = {"ProductCost:normal:add", "ProductCost:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") Cost cost,
                           @ModelAttribute("attachments") AttachmentsVo attachments,
                           @ModelAttribute("costProducts") CostProductsVo costProducts,
                           @ModelAttribute("costPorts") CostPortsVo costPorts,
                           @ModelAttribute("costChargeItems") CostChargeItemsVo costChargeItems,
                           @ModelAttribute("costTariffs") CostTariffsVo costTariffs,
                           @ModelAttribute("costProductCosts") CostProductCostsVo costProductCosts,
                           @ModelAttribute("costOrdersVo") CostOrdersVo costOrdersVo) {
        RestResult result = new RestResult();
        try {
            if (ACT_ADD.equals(act)) {
                costService.add(cost, attachments.getAttachments(), costProducts.getCostProducts(), costPorts.getCostPorts(),
                        costChargeItems.getCostChargeItems(), costTariffs.getCostTariffs(), costProductCosts.getCostProductCosts(), costOrdersVo.getCostOrders());
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));

            } else if (StringUtils.isNotBlank(cost.getId())) {
                //复制另存
                if (StringUtils.isNotBlank(act) && ACT_COPY.equals(act)) {
                    cost.setId(null);
                    costService.saveAs(cost, attachments.getAttachments(), costProducts.getCostProducts(), costPorts.getCostPorts(),
                            costChargeItems.getCostChargeItems(), costTariffs.getCostTariffs(), costProductCosts.getCostProductCosts(), costOrdersVo.getCostOrders());
                } else {
                    costService.update(cost, attachments.getAttachments(), costProducts.getCostProducts(), costPorts.getCostPorts(),
                            costChargeItems.getCostChargeItems(), costTariffs.getCostTariffs(), costProductCosts.getCostProductCosts());
                }
                result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"ProductCost:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids) {
        RestResult result = new RestResult();
        try {
            costService.deleteCost(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        } catch (RuntimeException e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }

    @ModelAttribute("main")
    public Cost cost(String id) {
        if (StringUtils.isNotBlank(id)) {
            return costService.getCost(id);
        }
        return new Cost();
    }

    @PostMapping("getFee")
    public RestResult getFee(String costId) {
        RestResult result = new RestResult();
        try {
            CostFeeDetailVo rd = costService.getFee(costId);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }
}
