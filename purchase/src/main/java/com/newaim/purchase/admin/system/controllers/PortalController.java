package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Portal;
import com.newaim.purchase.admin.system.service.PortalService;
import com.newaim.purchase.admin.system.vo.PortalVo;
import com.newaim.purchase.admin.system.vo.PortalsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/portal")
public class PortalController extends ControllerBase {

	@Autowired
	private PortalService portalService;

	@PostMapping("/list")
	public RestResult list(ServletRequest request, String sort,
						   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
						   @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize){
		RestResult result = new RestResult();
		try {
            LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();
			Page<PortalVo> rd = portalService.list(params, pageNumber, pageSize, getSort(sort));

			result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
		return result;
	}

	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			PortalVo rd =  portalService.get(id);
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());

		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}

		return result;
	}

	@PostMapping("/save")
	public RestResult save(String act, @ModelAttribute("main") PortalsVo main) {
		RestResult result = new RestResult();
		try {
            UserVo user = SessionUtils.currentUserVo();
			if( main != null && main.getItems().size()>0 ){
                portalService.deletePortalsByCreatorId(user.getId());
				portalService.save(main);

				result.setSuccess(true).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			}
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateFailure());
		}

		return result;
	}

	@PostMapping("/delete")
	public RestResult delete(String ids) {
		RestResult result = new RestResult();
		try {
			portalService.delete(ids);

			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}

		return result;
	}
}
