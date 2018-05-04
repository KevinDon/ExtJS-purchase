package com.newaim.purchase;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.controllers.UserController;
import com.newaim.purchase.admin.account.dao.RoleResourceUnionDao;
import com.newaim.purchase.admin.account.dao.UserRoleUnionDao;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.entity.UserRoleUnion;
import com.newaim.purchase.admin.account.service.ResourceService;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.ResourceMenuVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.PortalDao;
import com.newaim.purchase.admin.system.entity.Portal;
import com.newaim.purchase.desktop.email.service.EmailService;
import com.newaim.purchase.desktop.message.service.MessageService;
import com.newaim.purchase.desktop.systemtools.service.RateControlService;
import com.newaim.purchase.desktop.systemtools.vo.RateControlVo;
import com.newaim.purchase.flow.workflow.entity.MyAgentResource;
import com.newaim.purchase.flow.workflow.service.ActivitiService;
import com.newaim.purchase.flow.workflow.service.MyAgentResourceService;

/**
 * Created by Mark on 2017/8/10.
 * @author lance.hu
 */
@Controller
public class HomeController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private LocaleMessageSource localeMessageSource;

	@Autowired
	private UserService userService;
	
	@Autowired
    private ResourceService resourceService;

	@Autowired
    protected UserRoleUnionDao userRoleUnionDao;

    @Autowired
    protected RoleResourceUnionDao roleResourceUnionDao;

    @Autowired
    protected PortalDao portalDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private RateControlService rateControlService;
    
    @Autowired
    private MyAgentResourceService myAgentResourceService;

    @RequestMapping("/")
    public String index(Model model){
		UserVo user = SessionUtils.currentUserVo();
    	
    	if(user == null || user.getId().isEmpty()){
    		return "login";
    	}else{
	    	List<ResourceMenuVo> rd =  resourceService.list(null, user.getRoleIds());
	    	
	    	user.setResource(rd);

            List<Portal> portals =portalDao.findPortalByCreatorId(user.getId());
            user.setPortals(portals);

            //最新汇率
            RateControlVo rcv = rateControlService.listNewRate();
            if(rcv != null){
                user.setAudToRmb(rcv.getRateAudToRmb());
                user.setAudToUsd(rcv.getRateAudToUsd());
            }
            //新消息数量
            user.setCountMessageNew(messageService.countMessagesNew());
            user.setCountEmailNew(emailService.countEmailNew());
            user.setCountTaskNew(activitiService.countTaskNew());


	    	model.addAttribute("user", user);
	    	model.addAttribute("lang", user.getLang());
	    	model.addAttribute("libsrc", "/js/lib/ext/locale/ext-lang-"+user.getLang()+ ".js");
	    	model.addAttribute("langsrc", "/js/view/locale/"+user.getLang()+ ".js");
	    	model.addAttribute("userJson", new JsonMapper().toJson(user));
	    	
	        return "index";
    	}
    }

    @GetMapping("/login")
    public RestResult loginForm(){
    	RestResult result = new RestResult();
    	
    	result.setSuccess(true).setData(null).setMsg("登录");
    	return result;
    }

	@PostMapping("/login")
    public RestResult login(String account, String password, RedirectAttributes redirectAttributes){
    	RestResult result = new RestResult();

    	User user = userService.findUserByAccount(account);
        if(Constants.Status.DISABLED.code.equals(user.getStatus()) || Constants.Status.DELETED.code.equals(user.getStatus())){
            result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgLoginUserDisabled());
            return result;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        //获取当前的Subject
        Subject subject = SecurityUtils.getSubject();
        try {

    		try{
    			subject.login(token);
    		}catch(UnknownAccountException e){
    			result.setSuccess(false).setData(null).setMsg("您的账号或密码输入错误!");
    			return result;
    		}
    		
            //设置语言
            UserVo userVo = (UserVo) subject.getPrincipal();
            String lang = userVo.getLang();
            if(StringUtils.isNotBlank(lang)){
                String[] langArr = StringUtils.split(lang, "_");
                Locale locale = new Locale(langArr[0], langArr[1]);
                subject.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
            }

            //用户权限
            List<UserRoleUnion> role = userRoleUnionDao.findUserRoleUnionByUserId(userVo.getId());
            if(role != null && role.size()>0){
            	List<String> roleIds = Lists.newArrayList();
            	List<String> roles = Lists.newArrayList();
            	List<String> datas = Lists.newArrayList();
            	for(UserRoleUnion ur: role){
            		roleIds.add(ur.getRoleId());
            		List<RoleResourceUnion> rru = roleResourceUnionDao.findRoleResourceUnionByRoleId(ur.getRoleId());
            		
            		for(int j=0; j< rru.size(); j++){
            			if(StringUtils.isNotBlank(rru.get(j).getAction())){
            				roles.add(rru.get(j).getModel() + ":" + rru.get(j).getAction());
            			}else if(StringUtils.isNotBlank(rru.get(j).getData())){
            				datas.add(rru.get(j).getModel() + ":" + rru.get(j).getData());
            			}
            		}
            	}
            	userVo.setRoleIds(roleIds);
            	userVo.setRoles(roles);
            	userVo.setDatas(datas);
            }
            
            //流程代理授权grady01
        	List<String> roleIds = userVo.getRoleIds()!=null ?userVo.getRoleIds():Lists.newArrayList();
        	List<String> roles = userVo.getRoles()!=null ?userVo.getRoles():Lists.newArrayList();
        	List<String> datas = userVo.getDatas()!=null ?userVo.getDatas():Lists.newArrayList();
    		List<MyAgentResource> list = myAgentResourceService.findMyAgentResourcesByUserId(user.getId());
    		for(int j=0; j< list.size(); j++){
    			if(!roleIds.contains(list.get(j).getRoleId())){
    				roleIds.add(list.get(j).getRoleId());
    			}
    			if(StringUtils.isNotBlank(list.get(j).getAction())){
    				roles.add(list.get(j).getModel() + ":" + list.get(j).getAction());
    			}else if(StringUtils.isNotBlank(list.get(j).getData())){
    				datas.add(list.get(j).getModel() + ":" + list.get(j).getData());
    			}
    		}
        	userVo.setRoleIds(roleIds);
        	userVo.setRoles(roles);
        	userVo.setDatas(datas);
            
            List<Portal> portals =portalDao.findPortalByCreatorId(userVo.getId());
            userVo.setPortals(portals);

            //最新汇率
            RateControlVo rcv = rateControlService.listNewRate();
            if(rcv != null){
                userVo.setAudToRmb(rcv.getRateAudToRmb());
                userVo.setAudToUsd(rcv.getRateAudToUsd());
            }
            //新消息数量
            userVo.setCountMessageNew(messageService.countMessagesNew());
            userVo.setCountEmailNew(emailService.countEmailNew());
            userVo.setCountTaskNew(activitiService.countTaskNew());

            result.setSuccess(true).setData(null).setMsg("登录成功");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.setSuccess(false).setData(null).setMsg("您的账号或密码输入错误!");
        }
        
        return result;
    }


	@GetMapping("/logout")
    public String logout(){
		SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

}
