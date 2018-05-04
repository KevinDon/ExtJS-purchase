package com.newaim.purchase.desktop.email.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.mail.HtmlUtils;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.ServletUtils;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.mail.api.HtmlHandler;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.admin.system.vo.AttachmentsVo;
import com.newaim.purchase.desktop.email.entity.Email;
import com.newaim.purchase.desktop.email.service.EmailService;
import com.newaim.purchase.desktop.email.service.EmailSettingService;
import com.newaim.purchase.desktop.email.vo.EmailSettingMiniVo;
import com.newaim.purchase.desktop.email.vo.EmailSettingVo;
import com.newaim.purchase.desktop.email.vo.EmailVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/desktop/email")
public class EmailController extends ControllerBase{

    @Autowired
    private EmailService emailService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EmailSettingService emailSettingService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @RequiresPermissions("Email:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String box, String sort, String keywords,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
    		//搜索
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			if(StringUtils.isNotBlank(keywords)){
				//模糊搜索
				params.put("title-S-LK-OR", keywords);
				params.put("sendName-S-LK-OR", keywords);
				params.put("sendEmail-S-LK-OR", keywords);
		/*		params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);*/
				
			}else{
				//按字段搜索
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

			//邮箱过滤
			if(box.equals("in")){
				params.put("boxType-N-EQ-ADD", "1");
			}else if(box.equals("drafts")){
				params.put("boxType-N-EQ-ADD", "0");
			}else if(box.equals("dust")){
				params.put("boxType-N-EQ-ADD", "3");
			}else if(box.equals("out")){
				params.put("boxType-N-EQ-ADD", "2");
			}else{
				params.put("boxType-N-EQ-ADD", "100");
			}

			//数据过滤
			params = setParams(params, "Email", ":4:3:2:1", false);

    		Page<EmailVo> rd = emailService.list(params, pageNumber, pageSize, getSort(sort));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

    @RequiresPermissions("Email:normal:list")
	@PostMapping("/get")
	public RestResult get(ServletRequest request, String id) {
		RestResult result = new RestResult();
		try {

			String[] params = new String[]{"attachment"};
			emailService.setRead(id);

			EmailVo rd =  emailService.get(id, params);
            EmailSettingVo esv= emailSettingService.get(rd.getEmailTemplateId());
			rd.setEmailSetting(BeanMapper.map(esv, EmailSettingMiniVo.class));

			//正文中的图片替换
			if(null != rd.getContent() && StringUtils.isNotEmpty(rd.getContent())){
                rd.setContent(HtmlUtils.replaceHtmlTag(new HtmlHandler() {
                    @Override
                    public String getPartContent(String part) {
                        List<AttachmentVo> attach = rd.getAttachments();
                        if(null != attach && attach.size()>0){
                            for (AttachmentVo av: attach){
                                if(av.getDocument().getEmailCid() != null && !av.getDocument().getEmailCid().equals("")){
                                    String curProjectPath = request.getServletContext().getContextPath();
                                    return curProjectPath + "/" + av.getDocument().getPath();
                                }
                            }
                        }
                        return part;
                    }
                },rd.getContent(), "img", "src", "src=\"", "\""));
            }
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}
    
    @RequiresPermissions(value = {"Email:normal:add","Email:normal:edit"}, logical = Logical.OR)
    @PostMapping("/save")
	public RestResult save(ServletRequest request, String act, @ModelAttribute("main") Email main, @ModelAttribute("attachments") AttachmentsVo attachments) {
		RestResult result = new RestResult();
		try {
			UserVo user = SessionUtils.currentUserVo();
			if(main.getCreatorId() != null && !main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}

			if(StringUtils.isNotBlank(act) && ACT_SEND.equals(act)) {
                emailService.setRequest(request);
				emailService.send(main, attachments.getAttachments());
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgEmailSendSuccess());

			}else if( main.getId() != null && StringUtils.isNotBlank(main.getId())){
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					//复制另存
					main.setId(null);
					emailService.saveAs(main, attachments.getAttachments());
					result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("copy"));
				}else{
					//保存
					emailService.save(main, attachments.getAttachments());
					result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("edit"));
				}

			}else{
					
				emailService.add(main, attachments.getAttachments());
				
				result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess("add"));
				
			}

		} catch (Exception e) {
			result.setData(null).setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result; 
	}

	@RequiresPermissions("Email:normal:list")
	@PostMapping("/receive")
	public RestResult receiveEmail(ServletRequest request, String sid){
		RestResult result = new RestResult();
		try {
            emailService.setRequest(request);
            Integer count = emailService.receive(sid);

            result.setSuccess(true).setMsg(localeMessageSource.getMsgEmailReceiveSuccess(count));
        }catch (Exception e){
		    result.setSuccess(false).setMsg(localeMessageSource.getMsgEmailConnTestFailure());
        }
		return result;
	}
    
    @RequiresPermissions("Email:normal:del")
	@PostMapping("/delete")
	public RestResult delete(String box, String[] ids) {
		RestResult result = new RestResult();
		try {
            for (String id: ids) {
                UserVo user = SessionUtils.currentUserVo();
                if (!emailService.get(id).getCreatorId().equals(user.getId())) {
                    throw new Exception(localeMessageSource.getMsgUnauthorized());
                }
                if (!"dust".equals(box)) {
                    //删除标记
                    emailService.moveToDustbuin(id);
                } else {
                    if (hasDataType("Email" + ":4")) {
                        //物理删除
                        emailService.delete(id);
                    } else {
                        //删除标记
                        emailService.setDelete(id);
                    }
                }
            }
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
			e.printStackTrace();
		}
		
		return result;
	}
    
    @ModelAttribute("main")
    public Email main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return emailService.getEmail(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new Email();
        }
        return null;
    }
}
