package com.newaim.purchase.desktop.message.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.message.entity.MessageSku;
import com.newaim.purchase.desktop.message.service.MessageSkuService;
import com.newaim.purchase.desktop.message.vo.MessageSkuVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/messagesku")
public class MessageSkuController extends ControllerBase{

    @Autowired
    private MessageSkuService messageSkuService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("MessageSku:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("title-S-LK-OR", keywords);
				params.put("toUserCnName-S-LK-OR", keywords);
				params.put("toUserEnName-S-LK-OR", keywords);
				params.put("toDepartmentCnName-S-LK-OR", keywords);
				params.put("toDepartmentEnName-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
				params.put("content-S-LK-OR", keywords);
				params.put("sku-S-LK-OR", keywords);
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
			
			params = setParams(params, "MessageSku", ":4:3:2", false);
			if(!hasDataType("MessageSku:3") && !hasDataType("MessageSku:2") && hasDataType("MessageSku:1")){
				//自身
				UserVo user = SessionUtils.currentUserVo();
				params.put("toUserId-S-EQ-ADD", user.getId());
			}
			
    		Page<MessageSkuVo> rd = messageSkuService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("MessageSku:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			MessageSkuVo rd =  messageSkuService.get(id);
			
			UserVo user = SessionUtils.currentUserVo();
			if(rd.getToUserId().equals(user.getId())){ rd = messageSkuService.setReaded(rd); }
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"MessageSku:normal:add","MessageSku:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") MessageSku main) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if(StringUtils.isNotBlank(main.getToDepartmentId())){

                LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
				String depIds = getDepartmentsByDepId(main.getToDepartmentId());
				params.put("departmentId-S-IN", depIds);
				Page<UserVo> page = userService.list(params, 1, 1000, getSort(null));
				
				for(UserVo uv: page.getContent()){
					MessageSku r = new MessageSku();
					BeanMapper.copyProperties(main, r, true);
					r.setToUserId(uv.getId());
					
					SpringUtil.getBean(MessageSkuService.class).saveAs(r);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveFailure(act));
			}
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("MessageSku:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String[] ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();

            for (String id: ids) {
                if (!messageSkuService.get(id).getToUserId().equals(user.getId())) {
                    throw new Exception(localeMessageSource.getMsgUnauthorized());
                }

                if (hasDataType("MessageSku:4")) {
                    //物理删除
                    messageSkuService.delete(id);
                } else {
                    //删除标记
                    messageSkuService.setDelete(id);
                }
            }
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public MessageSku main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return messageSkuService.getMessageSku(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new MessageSku();
        }
        return null;
    }
}
