package com.newaim.purchase.archives.product.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.purchase.admin.system.service.ArchivesHistoryService;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import com.newaim.purchase.archives.product.entity.Tariff;
import com.newaim.purchase.archives.product.service.TariffService;
import com.newaim.purchase.archives.product.vo.ProductCombinedDetailsVo;
import com.newaim.purchase.archives.product.vo.ProductCombinedVo;
import com.newaim.purchase.archives.product.vo.TariffVo;

import org.apache.commons.lang3.SerializationUtils;
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
@RequestMapping("/archives/tariff")
public class TariffController extends ControllerBase {

    private final static String MSG_CONFIRM_TARIFF_SUCCESS = "msg_confirm_tariff_success";
    private final static String MSG_CONFIRM_TARIFF_FAILURE = "msg_confirm_tariff_failure";

    @Autowired
    private TariffService tariffService;
    
    @Autowired
	private ArchivesHistoryService archivesHistoryService;


    @RequiresPermissions("Tariff:normal:list")
    @PostMapping("/list")
    public RestResult listTariff(ServletRequest request, String sort, String keywords,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("departmentCnName-S-LK-OR", keywords);
                params.put("departmentEnName-S-LK-OR", keywords);
                params.put("creatorCnName-S-LK-OR", keywords);
                params.put("creatorEnName-S-LK-OR", keywords);
                params.put("itemCode-S-LK-OR", keywords);
                params.put("description-S-LK-OR", keywords);
                params.put("id-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);
            	
            }

            Page<TariffVo> page = tariffService.listTariff(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/listTariff")
    public RestResult list(ServletRequest request, String sort, String keywords,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize ){
        RestResult result = new RestResult();
        try{
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            if(StringUtils.isNotBlank(keywords)){
                params.put("sku-S-LK-OR", keywords);
                params.put("name-S-LK-OR", keywords);
                params.put("packageName-S-LK-OR", keywords);
                params.put("barcode-S-LK-OR", keywords);
                params.put("color-S-LK-OR", keywords);
                params.put("model-S-LK-OR", keywords);
                params.put("style-S-LK-OR", keywords);
            }else{
                params = ServletUtils.getParametersStartingWith(request);

            }

            params = setParams(params, "Tariff", ":4:3:2:1", false);
            Page<TariffVo> page = tariffService.listTariff(params, pageNumber, pageSize, getSort(sort));
            result.setSuccess(true).setMsg(localeMessageSource.getMsgListSuccess()).setPage(page);
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }


    @PostMapping("/get")
    @RequiresPermissions(value = {"Tariff:normal:list"}, logical = Logical.OR)
    public RestResult get(String id) {
        RestResult result = new RestResult();
        try {

            TariffVo rd =  tariffService.get(id);
            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/save")
    @RequiresPermissions(value = {"Tariff:normal:add", "Tariff:normal:edit"}, logical = Logical.OR)
    public RestResult save(String act, @ModelAttribute("main") Tariff main){
        RestResult result = new RestResult();
        try {
            if(ACT_ADD.equals(act)){
                tariffService.add(main);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
            }else {
                //复制另存
                if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
                	main.setId(null);
                    tariffService.saveAs(main);
                }else{
                	 if (main.getStatus() > 0) {
                         //储审核数据
                         try {
                        	 Tariff newMain = BeanMapper.map(main, Tariff.class);
                             archivesHistoryService.clean();
                             TariffVo newObj = tariffService.getApplyVoToSave(newMain);
                             archivesHistoryService.save("Tariff", newMain.getId(), newObj);

                             result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitSuccess());
                         }catch(Exception e){
                             result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgArchivesSubmitFailure());
                         }
                     } else {
                         //保存
                    	 tariffService.update(main);
                         result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
                     }
                }
                
            }
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgSaveFailure(act, e.getMessage()));
        }
        return result;
    }

    /**
     * 确认产品档案
     * @param id
     */
    @PostMapping("/confirm")
    public RestResult confirm(String id){
        RestResult result = new RestResult();
        try{
            tariffService.confirmTariff(id);
            result.setSuccess(true).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_TARIFF_SUCCESS));
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMessage(MSG_CONFIRM_TARIFF_FAILURE, e.getMessage()));
        }
        return result;
    }

    @PostMapping("/delete")
    @RequiresPermissions(value = {"Tariff:normal:del"}, logical = Logical.OR)
    public RestResult delete(String ids){
        RestResult result = new RestResult();
        try {
            tariffService.deleteTariff(ids);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgDeleteSuccess());
        }catch (RuntimeException e){
            result.setSuccess(false).setMsg(localeMessageSource.getMsgDeleteFailure(e.getMessage()));
        }
        return result;
    }
    //============审批历史==========================

    @PostMapping("/history")
    @RequiresPermissions(value = {"Tariff:normal:list"}, logical = Logical.OR)
    public RestResult historyList(ServletRequest request, String bid, String sort,
                                  @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize) {
        RestResult result = new RestResult();
        try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
            params = ServletUtils.getParametersStartingWith(request);
            params.put("moduleName-S-EQ-ADD", "Tariff");
            params.put("businessId-S-EQ-ADD", bid);
            params.put("status-N-LT-ADD", 3);

            Page<ArchivesHistoryVo> rd = archivesHistoryService.list(params, pageNumber, pageSize, getSort(sort));

            result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
        }
        return result;
    }

    @PostMapping("/historyget")
    @RequiresPermissions(value = {"Tariff:normal:list"}, logical = Logical.OR)
    public RestResult historyGet(String id, String hid) {
        RestResult result = new RestResult();
        try {

            if (StringUtils.isBlank(hid)) {
                throw new Exception("");
            }

            ArchivesHistoryVo ah = archivesHistoryService.get(hid);

            TariffVo rd = tariffService.getApplyVoToDisplay((TariffVo) BeanMapper.toBean(TariffVo.class, BeanMapper.toMap(SerializationUtils.deserialize(ah.getNewContent()))));

            result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
        }

        return result;
    }

    @PostMapping("/historyconfirm")
    @RequiresPermissions(value = {"Tariff:normal:confirm"}, logical = Logical.OR)
    public RestResult historyConfirm(String act, String ahid, @ModelAttribute("main") Tariff main) {
        RestResult result = new RestResult();
        try {
            if(StringUtils.isNoneBlank(ahid) && ACT_COMFIRM.equals(act)){
            	 
            	tariffService.update(main);
                archivesHistoryService.confirm(ahid);
                result.setSuccess(true).setMsg(localeMessageSource.getMsgArchivesConfirmSuccess());
            }else{
                result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure());
            }
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgArchivesConfirmFailure());
        }

        return result;
    }

    @PostMapping("/historydelete")
    @RequiresPermissions("Tariff:normal:confirm")
    public RestResult historyDelete(String ids) {
        RestResult result = new RestResult();
        try {
            if(hasDataType("Tariff" + ":4")){
                //物理删除
                archivesHistoryService.delete(ids);
            }else{
                //删除标记
                archivesHistoryService.setDelete(ids);
            }

            result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }

        return result;
    }

    @ModelAttribute("main")
    public Tariff main(String id){
        if(StringUtils.isNotBlank(id)){
            return tariffService.getTariff(id);
        }
        return new Tariff();
    }
}
