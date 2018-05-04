package com.newaim.purchase.archives.flow.finance.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.flow.finance.service.BalanceRefundService;
import com.newaim.purchase.archives.flow.finance.vo.BalanceRefundVo;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseBalanceRefundUnionService;
import com.newaim.purchase.archives.flow.purchase.vo.PurchaseBalanceRefundUnionVo;
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
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/finance/balanceRefund")
public class BalanceRefundController extends ControllerBase {

    @Autowired
    private BalanceRefundService balanceRefundService;

    @Autowired
    private PurchaseBalanceRefundUnionService purchaseBalanceRefundUnionService;


    @PostMapping("/list")
    public RestResult list(ServletRequest request,String modal, String sort, String keywords, String vendorId,
                           String[] purchasePlanIds, String type,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
            	params.put("id-S-LK-OR", keywords);
            	params.put("chargebackReason-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("vendorCnName-S-LK-OR", keywords);
				params.put("vendorEnName-S-LK-OR", keywords);
/*				params.put("reviewerCnName-S-LK-OR", keywords);
				params.put("reviewerEnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentCnName-S-LK-OR", keywords);
				params.put("reviewerDepartmentEnName-S-LK-OR", keywords);*/
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
            //列表数据过滤
            if(null != modal && StringUtils.isNotBlank(modal)){
                UserVo user = SessionUtils.currentUserVo();

                if("mine".equals(modal)){
                    //我发启的
                    params.remove("creatorId-S-EQ-ADD");
                    params.put("creatorId-S-EQ-ADD", user.getId());
                }else if("involved".equals(modal)){
                    //我参与的
//					params.put("history.operatorId-S-EQ-ADD", user.getId());
//                  params.put("history.businessId-S-GBY-NON", "");
                }
            }

            //记录与采购计划有关联的差额退款单
            List<PurchaseBalanceRefundUnionVo> unions = Lists.newArrayList();

            if(StringUtils.equals(type, "1")){
                //采购计划申请单调用
                if(null != vendorId && StringUtils.isNotEmpty(vendorId)){
                    params.put("vendorId-S-EQ-ADD", vendorId);
                    params.put("status-N-EQ-ADD", "1");
                    params.put("hold-N-EQ-ADD", "2");
                    params.put("chargebackStatus-N-EQ-ADD", "2");
                }else{
                    //默认不出结果
                    params.put("status-N-EQ-ADD", "-1");
                }

            }else if(StringUtils.equals(type, "2")){
                //采购合同调用
                if(purchasePlanIds != null && purchasePlanIds.length > 0){
                    StringBuilder balanceRefundIds = new StringBuilder();
                    unions = purchaseBalanceRefundUnionService.findVoByPurchasePlanIdIn(Lists.newArrayList(purchasePlanIds));
                    if(unions != null && unions.size() > 0){
                        for (int i = 0; i < unions.size(); i++) {
                            if(null != unions.get(i).getBalanceRefundId()) {
                                if (i > 0 && balanceRefundIds.length()>0) {
                                    balanceRefundIds.append(",");
                                }
                                balanceRefundIds.append(unions.get(i).getBalanceRefundId());
                            }
                        }
                    }
                    if(balanceRefundIds.length() > 0){
                        params.put("status-N-EQ-ADD", "1");
                        params.put("hold-N-EQ-ADD", "2");
                        params.put("chargebackStatus-N-EQ-ADD", "2");
                        params.put("id-S-IN-ADD", balanceRefundIds);
                    }else{
                        params.put("status-N-EQ-ADD", "-1");
                    }
                }else{
                    params.put("status-N-EQ-ADD", "-1");
                }
            }
            
            Page<BalanceRefundVo> page = balanceRefundService.list(params, pageNumber, pageSize, getSort(sort));

            //加入采购计划ID
            if(page.getContent().size()>0 && unions.size()>0){
                for(int i=0; i< page.getContent().size(); i++){
                    for (PurchaseBalanceRefundUnionVo pbruv : unions){
                        if(null != pbruv.getBalanceRefundId() && pbruv.getBalanceRefundId().equals(page.getContent().get(i).getId())) {
                            page.getContent().get(i).setPurchasePlanId(pbruv.getPurchasePlanId());
                            page.getContent().get(i).setPurchasePlanBusinessId(pbruv.getPurchasePlanBusinessId());
                        }
                    }
                }
            }

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
            BalanceRefundVo rd =  balanceRefundService.get(id);
            //设置流程备选人
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

}
