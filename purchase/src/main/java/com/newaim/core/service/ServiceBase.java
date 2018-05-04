package com.newaim.core.service;

import com.newaim.purchase.admin.account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceBase {
	
    @Autowired
    protected UserService userService;
    
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
}
