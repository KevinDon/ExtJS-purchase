package com.newaim.purchase.desktop.email.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.core.utils.mail.EmailUtils;
import com.newaim.core.utils.mail.model.MailSeting;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.email.dao.EmailSettingDao;
import com.newaim.purchase.desktop.email.entity.EmailBox;
import com.newaim.purchase.desktop.email.entity.EmailSetting;
import com.newaim.purchase.desktop.email.vo.EmailSettingVo;
import org.apache.commons.lang3.StringUtils;
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

public class EmailSettingService extends ServiceBase {

	@Autowired
	private EmailSettingDao emailSettingDao;

	@Autowired
	private EmailBoxService emailBoxService;

	public Page<EmailSettingVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<EmailSetting> spec =buildSpecification(params);
        Page<EmailSetting> p = emailSettingDao.findAll(spec, pageRequest);
        Page<EmailSettingVo> page = p.map(new Converter<EmailSetting, EmailSettingVo>() {
		    @Override
		    public EmailSettingVo convert(EmailSetting dv) {
		        return BeanMapper.map(dv, EmailSettingVo.class);
		    }
		});
        
		return page;
	}

	public EmailSettingVo get(String id){
		EmailSetting row = emailSettingDao.findEmailSettingById(id);
		EmailSettingVo o = BeanMapper.map(row, EmailSettingVo.class);
		
		return o;
	}

	public EmailSetting getEmailSetting(String id){
		EmailSetting o = emailSettingDao.findEmailSettingById(id);
		return o;
	}
		
	@Transactional
	public EmailSetting add(EmailSetting o){
		emailSettingDao.clear();
		
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

		o = emailSettingDao.save(o);
		//修改默认邮箱地址
        if(o.getIsDefault()==1){
            emailSettingDao.cancelDefaultByCreatorId(user.getId(), o.getId());
        }else{
            o.setIsDefault(2);
        }

		//创建邮箱
		for(int index=0; index<4; index++) {
			EmailBox eb = new EmailBox();
			eb.setEmailSettingId(o.getId());
			eb.setBoxType(index);
			emailBoxService.add(eb);
		}



    	return o;
	}

    @Transactional
    public EmailSetting saveAs(EmailSetting o){
        emailSettingDao.clear();

        return add(o);
    }
	
	@Transactional
	public EmailSetting save(EmailSetting o){
		o.setUpdatedAt(new Date());
		//修改默认项
        if(o.getIsDefault()==1){
            emailSettingDao.cancelDefaultByCreatorId(o.getCreatorId(), o.getId());
        }else{
            o.setIsDefault(2);
        }
		//创建邮箱
		for(int index=0; index<4; index++) {
			EmailBox eb = emailBoxService.getEmailBoxByEmailSettingIdAndBoxType(o.getId(), index);
			if(eb == null || eb.getId() == null || StringUtils.isBlank(eb.getId())){
				eb = new EmailBox();
				eb.setEmailSettingId(o.getId());
				eb.setBoxType(index);
				emailBoxService.add(eb);
			}
		}

		return emailSettingDao.save(o);
	}

	@Transactional
	public void delete(String id){
		for(int index=0; index<4; index++) {
			emailBoxService.deleteByEmailSettingId(id);
		}
		emailSettingDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		EmailSetting o = getEmailSetting(id);
		o.setStatus(3);
		emailSettingDao.save(o);
	}

	public List<EmailSetting> listEmailSettingsByCreatorId(UserVo user){
	    if(user == null) {
            user = SessionUtils.currentUserVo();
        }

	    return emailSettingDao.findEmailSettingByCreatorIdAndStatus(user.getId(), 1);
    }

	public Boolean checkConn(EmailSetting o, String cat){
		Boolean result = false;

		try {
            MailSeting ms = new MailSeting();
            if("pop".equals(cat)) {
				ms.setProtocal(MailSeting.POP3_PROTOCAL);
				ms.setReceiveHost(o.getServicePop());
				ms.setReceivePort(o.getServicePopPort());
				ms.setMailAddress(o.getPopAccount());
				ms.setPassword(o.getPopPassword());
				ms.setSSL(o.getServicePopSsl()== 1);
				ms.setValidate(true);

				EmailUtils mu = new EmailUtils(ms);
				mu.connectReciever();
			}else if("smtp".equals(cat)) {
				ms.setProtocal(MailSeting.SMTP_PROTOCAL);
				ms.setSendHost(o.getServiceSmtp());
				ms.setSendPort(o.getServiceSmtpPort());
				ms.setMailAddress(o.getSmtpAccount());
				ms.setPassword(o.getSmtpPassword());
				ms.setSSL(o.getServiceSmtpSsl() == 1);
				ms.setValidate(true);

                EmailUtils mu = new EmailUtils(ms);
				mu.connectSmtp();
			}
			result = true;
		}catch (Exception e){
            e.printStackTrace();
		}

		return result;
	}

	private Specification<EmailSetting> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<EmailSetting> spec = DynamicSpecifications.bySearchFilter(filters.values(), EmailSetting.class);
        return spec;
    }

}
