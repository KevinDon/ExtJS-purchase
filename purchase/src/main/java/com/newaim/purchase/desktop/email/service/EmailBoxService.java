package com.newaim.purchase.desktop.email.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Dictionary;
import com.newaim.purchase.admin.system.entity.DictionaryValue;
import com.newaim.purchase.admin.system.entity.DictionaryValueDesc;
import com.newaim.purchase.admin.system.service.DictionaryService;
import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
import com.newaim.purchase.admin.system.vo.DictionaryValueDescVo;
import com.newaim.purchase.admin.system.vo.DictionaryValueVo;
import com.newaim.purchase.desktop.email.dao.EmailBoxDao;
import com.newaim.purchase.desktop.email.dao.EmailDao;
import com.newaim.purchase.desktop.email.entity.EmailBox;
import com.newaim.purchase.desktop.email.vo.EmailBoxVo;
import org.springframework.beans.BeanUtils;
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

public class EmailBoxService extends ServiceBase {

	@Autowired
	private EmailBoxDao emailBoxDao;

    @Autowired
    private EmailService emailService;

	@Autowired
	private DictionaryService dictionaryService;

	public Page<EmailBoxVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<EmailBox> spec =buildSpecification(params);
        Page<EmailBox> p = emailBoxDao.findAll(spec, pageRequest);
        Page<EmailBoxVo> page = p.map(new Converter<EmailBox, EmailBoxVo>() {
		    @Override
		    public EmailBoxVo convert(EmailBox dv) {
		        return BeanMapper.map(dv, EmailBoxVo.class);
		    }
		});
        
		return page;
	}

	public EmailBoxVo get(String id){
		EmailBox row = emailBoxDao.findEmailBoxById(id);
		EmailBoxVo o = BeanMapper.map(row, EmailBoxVo.class);
		
		return o;
	}

	public List<EmailBoxVo> getByEmailSettingId(String id){
		List<EmailBox> rows = emailBoxDao.findEmailBoxByEmailSettingId(id);
		List<EmailBoxVo> listvo = Lists.newArrayList();
		if(rows.size() >0 ){
			listvo =BeanMapper.mapList(rows, EmailBox.class, EmailBoxVo.class);
		}
		return listvo;
	}

	public EmailBox getEmailBox(String id){
		EmailBox o = emailBoxDao.findEmailBoxById(id);
		return o;
	}

	public EmailBox getEmailBoxByEmailSettingIdAndBoxType(String esId, int boxType){
		EmailBox o = emailBoxDao.findEmailBoxByEmailSettingIdAndBoxType(esId, boxType);
		return o;
	}

    /**
     * 创建邮箱
     * @param o
     * @return
     */
	@Transactional
	public EmailBox add(EmailBox o){

		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());

		o.setId(null);
		o.setNumber(0);
		o.setNoRead(0);
		o.setStatus(1);

		//自动载出数据字典
		if(o.getBoxType() > -1){
			DictionaryCallVo dict = dictionaryService.getByCodemainAndCodeSub("system", "email_box_type").get(0);
			for (DictionaryValueVo item : dict.getOptions()){
				if(item.getValue().equals(o.getBoxType().toString())){
					for (DictionaryValueDescVo od: item.getDesc()){
						if("zh_CN".equals(od.getLang())){
							o.setCnName(od.getText());
						}else{
							o.setEnName(od.getText());
						}
					}
					break;
				}
			}
		}

		o.setCreatedAt(new Date());

    	return emailBoxDao.save(o);
	}

    @Transactional
    public EmailBox saveAs(EmailBox o){
        emailBoxDao.clear();

	    return add(o);
    }

    /**
     * 保存邮箱,并且自动创建缺失
     * @param o
     * @return
     */
	@Transactional
	public EmailBox save(EmailBox o){
		o.setUpdatedAt(new Date());

		//自动载出数据字典
		if(o.getBoxType() > -1){
			DictionaryCallVo dict = dictionaryService.getByCodemainAndCodeSub("system", "email_box_type").get(0);
			for (DictionaryValueVo item : dict.getOptions()){
				if(item.getValue() == o.getBoxType().toString()){
					for (DictionaryValueDescVo od: item.getDesc()){
						if("zh_CN".equals(od.getLang())){
							o.setCnName(od.getText());
						}else{
							o.setEnName(od.getText());
						}
					}
					break;
				}
			}
		}

		return emailBoxDao.save(o);
	}

	@Transactional
	public void delete(String id){
		emailBoxDao.delete(id);
	}

	@Transactional
	public void deleteByEmailSettingId(String emailSettingId){
		emailBoxDao.deleteByEmailSettingId(emailSettingId);
	}

	@Transactional
	public void setDelete(String id){
		EmailBox o = getEmailBox(id);
		o.setStatus(3);
		emailBoxDao.save(o);
	}

	private Specification<EmailBox> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<EmailBox> spec = DynamicSpecifications.bySearchFilter(filters.values(), EmailBox.class);
        return spec;
    }

}
