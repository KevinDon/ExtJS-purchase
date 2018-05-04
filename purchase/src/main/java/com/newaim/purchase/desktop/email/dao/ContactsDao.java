package com.newaim.purchase.desktop.email.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.desktop.email.entity.Contacts;

@Repository
public interface ContactsDao extends BaseDao<Contacts, String> {

	Contacts findContactsById(String id);

	List<Contacts> findContactsByVendorId(String vendorId);
	
}
