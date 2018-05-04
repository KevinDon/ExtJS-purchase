package com.newaim.purchase.desktop.email.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.vendor.dao.VendorDao;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;
import com.newaim.purchase.desktop.email.dao.ContactsDao;
import com.newaim.purchase.desktop.email.entity.Contacts;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import org.thymeleaf.util.ListUtils;

@Service
@Transactional(readOnly=true)

public class ContactsService extends ServiceBase {

	@Autowired
	private ContactsDao contactsDao;

	@Autowired
	private VendorService vendorService;

	public Page<ContactsVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Contacts> spec =buildSpecification(params);
        Page<Contacts> p = contactsDao.findAll(spec, pageRequest);
        Page<ContactsVo> page = p.map(new Converter<Contacts, ContactsVo>() {
		    @Override
		    public ContactsVo convert(Contacts dv) {
		        return BeanMapper.map(dv, ContactsVo.class);
		    }
		});
        
		return page;
	}

    /**
     * 根据Vendor Id 获取所有联系人
     * @param vendorId
     * @return
     */
	public List<ContactsVo> listByVendorId(String vendorId) {
		List<ContactsVo> list = Lists.newArrayList();
		
		List<Contacts> rows = contactsDao.findContactsByVendorId(vendorId);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Contacts.class, ContactsVo.class);
		}
		
		return list;
	}
	
	
	public ContactsVo get(String id){
		Contacts row = contactsDao.findContactsById(id);
		ContactsVo o = BeanMapper.map(row, ContactsVo.class);
		
		return o;
	}

	public Contacts getContacts(String id){
		Contacts o = contactsDao.findContactsById(id);
		return o;
	}

	@Transactional(rollbackFor = Exception.class)
	public Contacts add(Contacts o){

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

		//设置供应商或者服务商名
		setVendorInfor(o);

    	return contactsDao.save(o);
	}

	@Transactional(rollbackFor = Exception.class)
    public Contacts saveAs(Contacts o){
        contactsDao.clear();
		o.setId(null);

	    return add(o);
    }


	@Transactional(rollbackFor = Exception.class)
	public Contacts save(Contacts o){

		o.setUpdatedAt(new Date());
		//设置供应商或者服务商名
		setVendorInfor(o);

		return contactsDao.save(o);
	}
	
	/**
	 * TODO 未完成，未启用
	 * 供应商同步保存
	 * @param list
	 * @param vendorId
	 * @param vendorCnName
	 * @param vendorEnName
	 */
	@Transactional
	public void save(List<Contacts> list, String vendorId, String vendorCnName, String vendorEnName) {
		List<Contacts> o = Lists.newArrayList();
		
		if(StringUtils.isNotBlank(vendorId) && !StringUtils.isEmpty(vendorId)){
			for(int i=0; i<list.size(); i++){
				Contacts e = new Contacts();
				Contacts en = list.get(i);
				
				if(list.get(i).getId() != null){ 
					e= contactsDao.getOne(list.get(i).getId());
					BeanMapper.copyProperties(e, en, true);
				}
				
				en.setVendorId(vendorId);
				en.setVendorCnName(vendorCnName);
				en.setVendorEnName(vendorEnName);
				
				o.add(en);
			}
		}
		contactsDao.save(o);
	}	
	
	@Transactional
	public void delete(String id){
		contactsDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		Contacts o = getContacts(id);
		o.setStatus(3);
		contactsDao.save(o);
	}

	/**
	 * 根据VendorId设置供应商或者服务商的名字
	 * @param o
	 */
	public void setVendorInfor(Contacts o ){

		if(null == o.getVendorId() || StringUtils.isBlank(o.getVendorId())) return;

		Vendor v = vendorService.getVendor(o.getVendorId());
		o.setVendorCnName(v.getCnName());
		o.setVendorEnName(v.getEnName());
	}

	private Specification<Contacts> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Contacts> spec = DynamicSpecifications.bySearchFilter(filters.values(), Contacts.class);
        return spec;
    }


    /**
     * @TODO 需要优化对象实例化
     * 根据联系人ID获取联系人对象List
     * @param contacts
     */
    public List<ContactsVo> listByContactId(List<ContactsVo> contacts) {
        List<ContactsVo> rd = Lists.newArrayList();

        if(!ListUtils.isEmpty(contacts)){
            for(int i=0; i<contacts.size(); i++) {
                ContactsVo cv = new ContactsVo();
                BeanMapper.copyProperties(contacts.get(i), cv, true);

                cv = get(cv.getId());
                rd.add(cv);
            }
        }

        return rd;
    }

}
