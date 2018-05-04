package com.newaim.purchase.desktop.email.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.email.entity.EmailSetting;
import com.newaim.purchase.desktop.email.service.EmailSettingService;
import com.newaim.purchase.desktop.email.vo.EmailSettingMiniVo;
import com.newaim.purchase.desktop.email.vo.EmailSettingVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/desktop/emailsetting")
public class EmailSettingController extends ControllerBase{

    @Autowired
    private EmailSettingService emailSettingService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("EmailSetting:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, Integer mini, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				params.put("title-S-LK-OR", keywords);
				params.put("sendName-S-LK-OR", keywords);
				params.put("email-S-LK-OR", keywords);
				params.put("servicePop-S-LK-OR", keywords);
				params.put("popAccount-S-LK-OR", keywords);
				params.put("popPassword-S-LK-OR", keywords);
				params.put("serviceSmtp-S-LK-OR", keywords);
				params.put("smtpAccount-S-LK-OR", keywords);
				params.put("smtpPassword-S-LK-OR", keywords);
				params.put("serviceImap-S-LK-OR", keywords);
				params.put("imapAccount-S-LK-OR", keywords);
				params.put("imapPassword-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
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
			
			params = setParams(params, "EmailSetting", ":4:3:2:1", true);

    		Page<EmailSettingVo> rd = emailSettingService.list(params, pageNumber, pageSize, getSort(sort));

    		if(null != mini && mini==1 && rd.getContent().size()>0){
    		    //mimi list
                List<EmailSettingMiniVo> data = BeanMapper.mapList(rd.getContent(), EmailSettingVo.class, EmailSettingMiniVo.class);
                result.setSuccess(true).setData(data).setMsg(localeMessageSource.getMsgListSuccess());
            }else {
    		    //manage list
                result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
            }
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("EmailSetting:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			EmailSettingVo rd =  emailSettingService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"EmailSetting:normal:add","EmailSetting:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") EmailSetting main) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(main.getCreatorId() != null && !main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if( main.getId() != null && StringUtils.isNotBlank(main.getId())){
				//复制另存
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					main.setId(null);
					emailSettingService.saveAs(main);
				}else{
					emailSettingService.save(main);
				}
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				
			}else{
					
				emailSettingService.add(main);
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
				
			}
			
			
		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
			e.printStackTrace();
		}
		
		return result; 
	}
    
    @RequiresPermissions("EmailSetting:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(!emailSettingService.get(ids).getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			if(hasDataType("EmailSetting" + ":4")){
				//物理删除
				emailSettingService.delete(ids);
			}else{
				//删除标记
				emailSettingService.setDelete(ids);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}

	@RequiresPermissions("EmailSetting:normal:add")
	@PostMapping("/checkconn")
	public RestResult checkConn(String act, @ModelAttribute("main") EmailSetting main){
		RestResult result = new RestResult();
		try {

			if (emailSettingService.checkConn(main, act)) {
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgEmailConnTestSuccess());
			} else {
				result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgEmailConnTestFailure());
			}

		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}

    @ModelAttribute("main")
    public EmailSetting main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return emailSettingService.getEmailSetting(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new EmailSetting();
        }
        return null;
    }
}
