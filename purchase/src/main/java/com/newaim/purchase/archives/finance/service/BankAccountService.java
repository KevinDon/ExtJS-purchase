package com.newaim.purchase.archives.finance.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.finance.dao.BankAccountDao;
import com.newaim.purchase.archives.finance.entity.BankAccount;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.constants.ConstantsAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true, rollbackFor = Exception.class)
public class BankAccountService extends ServiceBase {

	@Autowired
	private BankAccountDao bankAccountDao;

	@Autowired
	private AttachmentService attachmentService;

	public List<BankAccountVo> list(Sort sort){
		List<BankAccountVo> list = Lists.newArrayList();
		
		List<BankAccount> rows = bankAccountDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, BankAccount.class, BankAccountVo.class);
		}
		
		return list;
		
	}
	
	public Page<BankAccountVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<BankAccount> spec =buildSpecification(params);
        Page<BankAccount> p = bankAccountDao.findAll(spec, pageRequest);
        Page<BankAccountVo> page = p.map(new Converter<BankAccount, BankAccountVo>() {
		    @Override
		    public BankAccountVo convert(BankAccount dv) {
		        return BeanMapper.map(dv, BankAccountVo.class);
		    }
		});
        
		return page;
	}

	public Page<BankAccountVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<BankAccount> spec =buildSpecification(params);
		Page<BankAccount> p = bankAccountDao.findAll(spec, pageable);
		Page<BankAccountVo> page = p.map(new Converter<BankAccount, BankAccountVo>() {
			@Override
			public BankAccountVo convert(BankAccount dv) {
				return BeanMapper.map(dv, BankAccountVo.class);
			}
		});

		return page;
	}
	
	
	public List<BankAccountVo> listByVendorId(String vendorId) {
		List<BankAccountVo> list = Lists.newArrayList();
		
		List<BankAccount> rows = bankAccountDao.findBankAccountByVendorId(vendorId);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, BankAccount.class, BankAccountVo.class);
		}
		
		return list;
	}
	
	public BankAccountVo get(String id){
		BankAccount row = bankAccountDao.findBankAccountById(id);
		BankAccountVo o = BeanMapper.map(row, BankAccountVo.class);
		return o;
	}
	
	public BankAccountVo getByVendorId(String vendorId){
		BankAccountVo o = null;
		BankAccount r = getBankAccountByVendorId(vendorId);
		if(r != null) {
			o = BeanMapper.map(r, BankAccountVo.class);
			//保函附件
			o.setGuaranteeLetterFile(attachmentService.getByBusinessIdAndModuleName(r.getBusinessId(), ConstantsAttachment.Status.BankAccount_File.code));
			//合同保函附件
			o.setContractGuaranteeLetterFile(attachmentService.getByBusinessIdAndModuleName(r.getBusinessId(), ConstantsAttachment.Status.BankAccount_ContractFile.code));
		}
		return o;
	}

	public BankAccount getBankAccount(String id){
		BankAccount o = bankAccountDao.findBankAccountById(id);
		return o;
	}
	
	public BankAccount getBankAccountByVendorId(String vendorId){
		List<BankAccount> list = bankAccountDao.findBankAccountByVendorIdAndStatus(vendorId, Constants.Status.NORMAL.code);
		if(list.size()>0){
			return list.get(0); 
		}else{
			return null;
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void add(BankAccount o){
		bankAccountDao.clear();
		
    	bankAccountDao.save(o);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void update(BankAccount o){
		bankAccountDao.save(o);
	}
		
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id){
		bankAccountDao.delete(id);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteByVendorId(String vendorId){
		bankAccountDao.deleteByVendorId(vendorId);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteByVendorIdAndStatus(String vendorId, Integer status){
		bankAccountDao.deleteByVendorIdAndStatus(vendorId, status);
	}
	    
	private Specification<BankAccount> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<BankAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(), BankAccount.class);
        return spec;
    }	
}
