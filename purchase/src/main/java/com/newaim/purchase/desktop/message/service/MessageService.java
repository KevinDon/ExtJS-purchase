package com.newaim.purchase.desktop.message.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.desktop.message.dao.MessageDao;
import com.newaim.purchase.desktop.message.entity.Message;
import com.newaim.purchase.desktop.message.vo.MessageVo;
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
public class MessageService extends ServiceBase {

	@Autowired
	private MessageDao messageDao;
	
	public Page<MessageVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Message> spec =buildSpecification(params);

        Page<Message> p = messageDao.findAll(spec, pageRequest);
        Page<MessageVo> page = p.map(new Converter<Message, MessageVo>() {
		    @Override
		    public MessageVo convert(Message dv) {
		        return BeanMapper.map(dv, MessageVo.class);
		    }
		});
        
		return page;
	}

	public List<MessageVo> listByToUserId(String id) {
		List<MessageVo> list = Lists.newArrayList();
		
		List<Message> rows = messageDao.findMessageByToUserId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Message.class, MessageVo.class);
		}
		
		return list;
	}
	
	public List<MessageVo> listByFromUserId(String id) {
		List<MessageVo> list = Lists.newArrayList();
		
		List<Message> rows = messageDao.findMessageByFromUserId(id);
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Message.class, MessageVo.class);
		}
		
		return list;
	}
	
	public MessageVo get(String id){
		Message row = messageDao.findMessageById(id);
		MessageVo o = BeanMapper.map(row, MessageVo.class);
		
		return o;
	}

	public Message getMessage(String id){
		Message o = messageDao.findMessageById(id);
		return o;
	}
		
	@Transactional
	public Message add(Message o){
		UserVo user = SessionUtils.currentUserVo();
		o.setFromUserId(user.getId());
		o.setFromUserCnName(user.getCnName());
		o.setFromUserEnName(user.getEnName());
		o.setFromDepartmentId(user.getDepartmentId());
		o.setFromDepartmentCnName(user.getDepartmentCnName());
		o.setFromDepartmentEnName(user.getDepartmentEnName());
		
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
		
    	return messageDao.saveAndFlush(o);
	}
	
	@Transactional
	public Message addFromUserId(Message o){
		messageDao.clear();
		
		UserVo user = userService.get(o.getFromUserId());
		o.setFromUserId(user.getId());
		o.setFromUserCnName(user.getCnName());
		o.setFromUserEnName(user.getEnName());
		o.setFromDepartmentId(user.getDepartmentId());
		o.setFromDepartmentCnName(user.getDepartmentCnName());
		o.setFromDepartmentEnName(user.getDepartmentEnName());
		
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
		
    	return messageDao.save(o);
	}

	@Transactional
    public Message saveAs(Message o){
        messageDao.clear();

        return add(o);
    }

	@Transactional
	public Message save(Message o){
		
		UserVo user = SessionUtils.currentUserVo();
		o.setFromUserId(user.getId());
		o.setFromUserCnName(user.getCnName());
		o.setFromUserEnName(user.getEnName());
		o.setFromDepartmentId(user.getDepartmentId());
		o.setFromDepartmentCnName(user.getDepartmentCnName());
		o.setFromDepartmentEnName(user.getDepartmentEnName());
		
		UserVo toUser = userService.get(o.getToUserId());
		o.setToUserId(toUser.getId());
		o.setToUserCnName(toUser.getCnName());
		o.setToUserEnName(toUser.getEnName());
		o.setToDepartmentId(toUser.getDepartmentId());
		o.setToDepartmentCnName(toUser.getDepartmentCnName());
		o.setToDepartmentEnName(toUser.getDepartmentEnName());
		
		o.setUpdatedAt(new Date());
		
		return messageDao.save(o);
	}

	public Integer countMessagesNew(){
        UserVo user = SessionUtils.currentUserVo();

	    return messageDao.countMessageByToUserIdAndReadAndStatus(user.getId(), 1, 1);
    }
	
	@Transactional
	public void delete(String id){
		messageDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		Message o = getMessage(id);
		o.setStatus(3);
		messageDao.save(o);
	}
	
	@Transactional
	public MessageVo setReaded(MessageVo o){
		o.setRead(2);
		o.setUpdatedAt(new Date());
		Message r = BeanMapper.map(o, Message.class);
		messageDao.save(r);
		return o;
	}
	    
	private Specification<Message> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Message> spec = DynamicSpecifications.bySearchFilter(filters.values(), Message.class);
        return spec;
    }	
}
