package com.newaim.purchase.desktop.message.helper;

import com.google.common.collect.Lists;
import com.newaim.purchase.admin.account.service.UserService;
import org.apache.commons.lang.StringUtils;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.desktop.message.entity.Message;
import com.newaim.purchase.desktop.message.service.MessageService;

import java.util.List;

public class Msg {
	
	/**
	 * send messages
	 * @param userIds String 多个用户ID间用 "," 分隔
	 * @param o Message
	 * @return boolean
	 */
	public static boolean  send(String userIds, Message o){
		boolean result = false;
		
		if(StringUtils.isBlank(userIds)){
			return result;
		}
		
		String[] arrUserIds = userIds.split(",");
		
		for(String item: arrUserIds){
			Message r = new Message();
			BeanMapper.copyProperties(o, r, true);
			r.setToUserId(item);
			SpringUtil.getBean(MessageService.class).add(r);
			result = true;
		}
		
		return result;
	}
	
	/**
	 * send messages
	 * @param userIds String 多个用户ID间用 "," 分隔
	 * @param title String
	 * @param content String
	 * @return
	 */
	public static boolean send(String userIds, String title, String content){
		boolean result = false;
		
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(title) || StringUtils.isBlank(content)){
			return result;
		}
		
		String[] arrUserIds = userIds.split(",");
		
		for(String item: arrUserIds){
			Message r = new Message();
			r.setToUserId(item);
			r.setTitle(title);
			r.setContent(content);
			
			SpringUtil.getBean(MessageService.class).add(r);
			result = true;
		}
		
		return result;
	}

    /**
     * 根据用户登录名群发信息
     * @param accounts List<String> 用户登录号
     * @param title
     * @param content
     * @return
     */
    public static boolean sendByAccount(List<String> accounts, String title, String content){
        String userIds = SpringUtil.getBean(UserService.class).getUserIdsByAccounts(accounts);
        return send(userIds, title, content);
    }

	/**
	 * send messages
	 * @param userIds String 多个用户ID间用 "," 分隔
	 * @param title String
	 * @param content String
	 * @param fromUserId String
	 * @return
	 */
	public static boolean send(String userIds, String title, String content, String fromUserId){
		boolean result = false;
		
		if(StringUtils.isBlank(userIds) || StringUtils.isBlank(title) || StringUtils.isBlank(content)){
			return result;
		}
		
		String[] arrUserIds = userIds.split(",");
		
		for(String item: arrUserIds){
			Message r = new Message();
			r.setToUserId(item);
			r.setFromUserId(fromUserId);
			r.setTitle(title);
			r.setContent(content);
			
			SpringUtil.getBean(MessageService.class).addFromUserId(r);
			result = true;
		}
		
		return result;
	}

    public static void main(String[] args) {
        List<String> accounts = Lists.newArrayList();

        accounts.add("admin");
        sendByAccount(accounts, "titles", "contents");
    }
}
