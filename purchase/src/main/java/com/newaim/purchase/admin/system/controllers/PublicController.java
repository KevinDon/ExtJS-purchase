package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.FreeMarkerUtil;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.vo.TimerRunVo;
import com.newaim.purchase.desktop.email.service.EmailService;
import com.newaim.purchase.desktop.message.service.MessageService;
import com.newaim.purchase.desktop.systemtools.entity.RateControl;
import com.newaim.purchase.desktop.systemtools.service.RateControlService;
import com.newaim.purchase.desktop.systemtools.vo.RateControlVo;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/pub")
public class PublicController  extends ControllerBase {

    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private RateControlService rateControlService;

    @ResponseBody
	@GetMapping("/timerrun")
    public RestResult timerRun(){ 
		RestResult result = new RestResult();

        TimerRunVo trv = new TimerRunVo();
        trv.setCountMessageNew(messageService.countMessagesNew());
        trv.setCountEmailNew(emailService.countEmailNew());
        trv.setCountTaskNew(activitiService.countTaskNew());

        RateControlVo rcv = rateControlService.listNewRate();
        if(rcv != null){
            trv.setAudToRmb(rcv.getRateAudToRmb());
            trv.setAudToUsd(rcv.getRateAudToUsd());
        }

		result.setSuccess(true).setData(trv).setMsg("");
		return result;
    }

	@ResponseBody
	@GetMapping("/getnull")
	public  RestResult getNull(){
		RestResult result = new RestResult();

		result.setSuccess(true).setData(null).setMsg("");

		return result;
	}

	@GetMapping("/role")
	public String getMyRole(ServletRequest request, Model model){
		UserVo user = SessionUtils.currentUserVo();

		model.addAttribute("lang", user.getLang());
		model.addAttribute("role", user.getRoles());

		return "public/role";
	}

}
