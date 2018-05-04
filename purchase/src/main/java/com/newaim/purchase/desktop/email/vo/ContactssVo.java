package com.newaim.purchase.desktop.email.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.newaim.purchase.desktop.email.entity.Contacts;

public class ContactssVo implements Serializable{

	private List<Contacts> contactss = Lists.newArrayList();

	public List<Contacts> getContactss() {
		return contactss;
	}

	public void setContactss(List<Contacts> contactss) {
		this.contactss = contactss;
	}
	
}
