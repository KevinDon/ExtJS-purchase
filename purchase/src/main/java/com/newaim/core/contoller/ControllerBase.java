package com.newaim.core.contoller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.EncodeUtils;
import com.newaim.core.utils.JsonObj;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.StringEscapeEditor;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.service.DepartmentService;
import com.newaim.purchase.admin.account.vo.UserVo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.PropertyEditorSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ControllerBase {

	@Autowired
	protected LocaleMessageSource localeMessageSource;
	
	protected static Log logger = LogFactory.getLog("controller");

	protected static final String PAGE_SIZE = "25";
	
	@Autowired
    private DepartmentService departmentService;

	/**新增 - 通用添加操作符*/
	protected static final String ACT_ADD = "add";
	/**复制 - 通用复制操作符*/
	protected static final String ACT_COPY= "copy";
	/**编辑 - 通用复制操作符 */
	protected static final String ACT_EDIT = "edit";
	/**插入- 通用复制操作符 */
	protected static final String ACT_INS = "ins";
	/**下移- 通用复制操作符 */
	protected static final String ACT_UP = "up";
	/**上移- 通用复制操作符 */
	protected static final String ACT_DOWN = "down";
	/**升级- 通用复制操作符 */
	protected static final String ACT_LEFT = "left";
	/**降级- 通用复制操作符 */
	protected static final String ACT_RIGHT = "right";
	/**启动流程- 通用复制操作符 */
	protected static final String ACT_START = "start";
	/**通过*/
	protected static final String ACT_ALLOW= "allow";
	/**发邮件 - 通用复制操作符*/
	protected static final String ACT_SEND= "send";
	/**档案变更确认*/
	protected static final String ACT_COMFIRM = "confirm";
	/**冻结*/
	protected static final String ACT_HOLD = "hold";
	/**解冻*/
	protected static final String ACT_UN_HOLD = "unhold";

	/**作废*/
	protected static final String ACT_CANCEL = "cancel";


	protected static final String ACT_PDF = "pdf";
	protected static final String ACT_EXCEL = "xls";
	protected static final String ACT_WORD= "doc";
	protected static final String ACT_CSV = "csv";
	protected static final String ACT_EMAIL_PDF = "epdf";
	protected static final String ACT_EMAIL_EXCEL = "exls";
	protected static final String ACT_EMAIL_WORD = "edoc";
	protected static final String ACT_EMAIL_CSV = "ecsv";


	/**
	 * 异常处理
	 */
	@ResponseBody
	@ExceptionHandler
	public RestResult exp(Exception ex){
		RestResult result = new RestResult();
		if(ex instanceof UnauthorizedException){
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgUnauthorized());
		} else {
			result.setSuccess(false).setData(null).setMsg(ex.getLocalizedMessage());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected Sort getSort(String sort){
		Map<String, String> map = Maps.newHashMap();

        return getSort(sort, map);
	}

    protected Sort getSort(String sort, Map<String, String> fieldMap){
        Sort st = null;
        if(sort != null && !sort.isEmpty()){
            JsonObj jo = new JsonObj();
            List<?> param = jo.toList(EncodeUtils.unescapeHtml(sort));
            int i= param.size();
            while (i>0) {
                LinkedHashMap<String, String> strParam = (LinkedHashMap<String, String>) param.get(param.size()-i);
                String property =fieldMap.get(strParam.get("property")) !=null ? fieldMap.get(strParam.get("property")) : strParam.get("property") ;
                if(i == param.size()) {
                    if (strParam.get("direction").equals("ASC")) {
                        st = new Sort(Direction.ASC, property);
                    } else {
                        st = new Sort(Direction.DESC, property);
                    }
                }else{
                    if (strParam.get("direction").equals("ASC")) {
                        st.and(new Sort(Direction.ASC, property));
                    } else {
                        st.and(new Sort(Direction.DESC, property));
                    }
                }
                i--;
            }
        }else{
            st = new Sort(Direction.DESC, "id");
        }

        return st;
    }

	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder) {

		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));

		//统一将前台传入空字符串(empty)转为null
		binder.registerCustomEditor(String.class, new PropertyEditorSupport(){
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if(StringUtils.isBlank(text) || StringUtils.equals(text, "请选择...") || StringUtils.equals(text, "Please select...") ){
					text = null;
				}
				setValue(text);
			}
		});

	}
	
	/**
	 * 判断模块数据类型，
	 * @param dataType
	 * @return
	 */
	protected Boolean hasDataType(String dataType){
		Boolean result = false;

		UserVo user = SessionUtils.currentUserVo();
		
		if(user.getDatas().toString().indexOf(dataType) >0){
			result = true;
		}
		return result;
	}
	
	/**
	 * 数据查询过滤
	 * @param params
	 * @param moduleName
	 * @param datas
	 * @return
	 */
	protected LinkedHashMap<String, Object> setParams(LinkedHashMap<String, Object> params, String moduleName, String datas){
		return setParams(params, moduleName, datas, true);
	}
	
	/**
	 * 数据查询过滤
	 * @param params
	 * @param moduleName
	 * @param datas
	 * @param addDefault
	 * @return
	 */
	protected LinkedHashMap<String, Object> setParams(LinkedHashMap<String, Object> params, String moduleName, String datas, boolean addDefault){
		
		boolean hasData = false;
		
		if(datas.indexOf("4")>0){
			if(hasDataType(moduleName + ":4")){
				//带删除的数据
				params.put("status-N-LT-ADD", "5");
			}else{
				//非删除的数据
				params.put("status-N-NEQ-ADD", "3");
			}
			hasData = true;
		}
		
		if(datas.indexOf("3")>0 && hasDataType(moduleName + ":3")){
			//不分部门
			hasData = true;
		}else if(datas.indexOf("2")>0 && hasDataType(moduleName + ":2")){
			//部门内
			String depIds = getMyDepartments();
			params.remove("departmentId-S-EQ");
			params.put("departmentId-S-IN-ADD", depIds);
			
			hasData = true;
		}else if(datas.indexOf("1")>0 && hasDataType(moduleName + ":1")){
			//自身
			UserVo user = SessionUtils.currentUserVo();
			params.put("creatorId-S-EQ-ADD", user.getId());
			hasData = true;
		}
		
		if (! hasData && addDefault){
			params.put("status-N-EQ-ADD", "10");
		}
		
		return params;
	}
	
	/**
	 * 获取用户当前部门及下属部门的部门ID
	 * @return
	 */
	protected String getMyDepartments(){
		String result = null;
		List<String> deps= Lists.newArrayList();

		UserVo user = SessionUtils.currentUserVo();

		departmentService.getDepartmentIdsById(user.getDepartmentId(), deps);
		result = StringUtils.join(deps.toArray(), ",");
		return result;
	}
	/**
	 * 获取指定部门及下属部门的部门ID
	 * @param depId
	 * @return
	 */
	protected String getDepartmentsByDepId(String depId){
		String result = null;
		List<String> deps= Lists.newArrayList();
		departmentService.getDepartmentIdsById(depId, deps);
		result = StringUtils.join(deps.toArray(), ",");
		
		return result;
	}

	@InitBinder("main")
	protected void initBinder(WebDataBinder binder){
		binder.setFieldDefaultPrefix("main.");
	}

	/**
	 * 转换act值， 流程处理时使用
	 * @param flowAct
	 * @return
	 */
	protected String getApprovedValue(String flowAct){
		String approved = null;
		if(Constants.FlowAct.ALLOW.name.equals(flowAct)){
			//同意
			approved = Constants.FlowAct.ALLOW.code;
		}else if(Constants.FlowAct.BACK.name.equals(flowAct)){
			//退回
			approved = Constants.FlowAct.BACK.code;
		}else if(Constants.FlowAct.REDO.name.equals(flowAct)){
			//返审
			approved = Constants.FlowAct.REDO.code;
		}else if(Constants.FlowAct.REFUSE.name.equals(flowAct)){
			//拒绝
			approved = Constants.FlowAct.REFUSE.code;
		}
		return approved;
	}

	/**
	 * 用户跳转JSP页面
	 * 此方法不考虑权限控制
	 * @param folder  路径
	 * @param jspName JSP名称(不加后缀)
	 * @return 指定JSP页面
	 */
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder, @PathVariable String jspName) {
		return "/" + folder + "/" + jspName;
	}
}
