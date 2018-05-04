package com.newaim.purchase.desktop.message.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.newaim.purchase.desktop.message.dao.MessageSkuDao;
import com.newaim.purchase.desktop.message.entity.MessageSku;
import com.newaim.purchase.desktop.message.vo.MessageSkuVo;

@Service
@Transactional(readOnly=true)
public class MessageSkuService extends ServiceBase {

	@Autowired
	private MessageSkuDao messageSkuDao;
	
	public Page<MessageSkuVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MessageSku> spec =buildSpecification(params);
        Page<MessageSku> p = messageSkuDao.findAll(spec, pageRequest);
        Page<MessageSkuVo> page = p.map(new Converter<MessageSku, MessageSkuVo>() {
		    @Override
		    public MessageSkuVo convert(MessageSku dv) {
		        return BeanMapper.map(dv, MessageSkuVo.class);
		    }
		});
        
		return page;
	}

	public List<MessageSkuVo> listByToUserId(String id) {
		List<MessageSkuVo> list = Lists.newArrayList();
		
		List<MessageSku> rows = messageSkuDao.findMessageSkuByToUserId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MessageSku.class, MessageSkuVo.class);
		}
		
		return list;
	}
	
	public List<MessageSkuVo> listByCreatorId(String id) {
		List<MessageSkuVo> list = Lists.newArrayList();
		
		List<MessageSku> rows = messageSkuDao.findMessageSkuByCreatorId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MessageSku.class, MessageSkuVo.class);
		}
		
		return list;
	}
	
	public MessageSkuVo get(String id){
		MessageSku row = messageSkuDao.findMessageSkuById(id);
		MessageSkuVo o = BeanMapper.map(row, MessageSkuVo.class);
		
		return o;
	}

	public MessageSku getMessageSku(String id){
		MessageSku o = messageSkuDao.findMessageSkuById(id);
		return o;
	}
		
	@Transactional
	public MessageSku add(MessageSku o){
		messageSkuDao.clear();
		
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		o.setId(null);
		o.setStatus(1);
		o.setRead(1);
		o.setCreatedAt(new Date());
		
    	return messageSkuDao.save(o);
	}

    @Transactional
    public MessageSku saveAs(MessageSku o) {
        messageSkuDao.clear();

        return add(o);
    }

	@Transactional
	public MessageSku save(MessageSku o){
		
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		o.setUpdatedAt(new Date());
		
		return messageSkuDao.save(o);
	}
	
	
	@Transactional
	public void delete(String id){
		messageSkuDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		MessageSku o = getMessageSku(id);
		o.setStatus(3);
		messageSkuDao.save(o);
	}
	
	@Transactional
	public MessageSkuVo setReaded(MessageSkuVo o){
		o.setRead(2);
		o.setUpdatedAt(new Date());
		MessageSku r = BeanMapper.map(o, MessageSku.class);
		messageSkuDao.save(r);
		return o;
	}

	private Specification<MessageSku> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MessageSku> spec = DynamicSpecifications.bySearchFilter(filters.values(), MessageSku.class);
        return spec;
    }	
}
