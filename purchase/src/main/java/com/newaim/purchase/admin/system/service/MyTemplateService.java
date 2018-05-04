package com.newaim.purchase.admin.system.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.MyTemplateDao;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.entity.MyTemplate;
import com.newaim.purchase.admin.system.vo.MyTemplateVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.reports.entity.Reports;
import com.newaim.purchase.desktop.reports.vo.ReportsVo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)

public class MyTemplateService extends ServiceBase {

	private static final String FILE_MODULE_NAME =  ConstantsAttachment.Status.MyTemplate.code;

	@Autowired
	private MyTemplateDao myTemplateDao;

	@Autowired
	private AttachmentService attachmentService;

	public Page<MyTemplateVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MyTemplate> spec =buildSpecification(params);
        Page<MyTemplate> p = myTemplateDao.findAll(spec, pageRequest);
        Page<MyTemplateVo> page = p.map(new Converter<MyTemplate, MyTemplateVo>() {
		    @Override
		    public MyTemplateVo convert(MyTemplate dv) {
		        return BeanMapper.map(dv, MyTemplateVo.class);
		    }
		});
        
		return page;
	}

	public MyTemplateVo get(String id){
		MyTemplate row = myTemplateDao.findMyTemplateById(id);
		MyTemplateVo o = BeanMapper.map(row, MyTemplateVo.class);

		return o;
	}

	public MyTemplateVo get(String id, String[] params){
		MyTemplateVo o = get(id);

		//附件文件
		if(ArrayUtils.contains(params, "attachment")){
			o.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME));
		}

		return o;
	}

	public MyTemplate getMyTemplate(String id){
		MyTemplate o = myTemplateDao.findMyTemplateById(id);
		return o;
	}
		
	@Transactional
	public MyTemplate add(MyTemplate o, List<Attachment> attachments){
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());

		o.setId(null);
		o.setStatus(1);
		o.setCreatedAt(new Date());

		o = myTemplateDao.save(o);

		//保存附件
		attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());

    	return o;
	}

    @Transactional
    public MyTemplate saveAs(MyTemplate o, List<Attachment> attachments){
        myTemplateDao.clear();

        o =add(o, attachments);
        return o;
    }

	@Transactional
	public MyTemplate save(MyTemplate o, List<Attachment> attachments){

		o.setUpdatedAt(new Date());
		o = myTemplateDao.save(o);
		//保存附件
		attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());

		return o;
	}

	@Transactional
	public void delete(String id){
		//删除全部关联附件
		attachmentService.deleteByBusinessId(id);
		myTemplateDao.delete(id);
	}

	@Transactional
	public void setDelete(String id){
		MyTemplate o = getMyTemplate(id);
		o.setStatus(3);
		myTemplateDao.save(o);
	}

	private Specification<MyTemplate> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MyTemplate> spec = DynamicSpecifications.bySearchFilter(filters.values(), MyTemplate.class);
        return spec;
    }

}
