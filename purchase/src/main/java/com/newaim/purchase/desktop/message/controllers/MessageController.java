package com.newaim.purchase.desktop.message.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.message.entity.Message;
import com.newaim.purchase.desktop.message.helper.Msg;
import com.newaim.purchase.desktop.message.service.MessageService;
import com.newaim.purchase.desktop.message.vo.MessageVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase{

    @Autowired
    private MessageService messageService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("Message:normal:list")
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
				params.put("fromUserCnName-S-LK-OR", keywords);
				params.put("fromUserEnName-S-LK-OR", keywords);
				params.put("fromDepartmentCnName-S-LK-OR", keywords);
				params.put("fromDepartmentEnName-S-LK-OR", keywords);
				params.put("content-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
				//搜索出指定部门以下的所有记录
				if(params.size()>0){
					if(params.get("toDepartmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("toDepartmentId-S-EQ").toString())){
						String depIds = getDepartmentsByDepId(params.get("toDepartmentId-S-EQ").toString());
						params.remove("toDepartmentId-S-EQ");
						params.put("toDepartmentId-S-IN", depIds);
					}
				}
			}
			
			params = setParams(params, "Message", ":4:3", false);
			if(!hasDataType("Message" + ":3") && hasDataType("Message" + ":2")){

				String depIds = getMyDepartments();
				params.remove("departmentId-S-EQ");
				params.put("toDepartmentId-S-IN-ADD", depIds);

			}else if(!hasDataType("Message" + ":3") && !hasDataType("Message" + ":2") && hasDataType("Message" + ":1")){
				//自身
				UserVo user = SessionUtils.currentUserVo();
				params.put("toUserId-S-EQ-ADD", user.getId());
			}
			
			
    		Page<MessageVo> rd = messageService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("Message:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			MessageVo rd =  messageService.get(id);
			
			UserVo user = SessionUtils.currentUserVo();
			if(rd.getToUserId().equals(user.getId())){ rd = messageService.setReaded(rd); }
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"Message:normal:add","Message:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(HttpServletRequest request, String act, @ModelAttribute("main") Message main) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(main.getFromUserId() != null && !main.getFromUserId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if(StringUtils.isNotBlank(main.getToUserId())){
				
				Msg.send(main.getToUserId(), main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}else{
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
			}
			
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions("Message:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String[] ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();

            for (String id: ids) {
                if (!messageService.get(id).getToUserId().equals(user.getId())) {
                    throw new Exception(localeMessageSource.getMsgUnauthorized());
                }

                if (hasDataType("Message" + ":4")) {
                    //物理删除
                    messageService.delete(id);
                } else {
                    //删除标记
                    messageService.setDelete(id);
                }
            }
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public Message main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return messageService.getMessage(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Message();
        }
        return null;
    }
}
