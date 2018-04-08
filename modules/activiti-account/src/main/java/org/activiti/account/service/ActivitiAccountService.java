package org.activiti.account.service;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivitiAccountService {

	public static final FastDateFormat DATE_HHmmss_FORMAT = FastDateFormat.getInstance("HH:mm:ss");

	@Autowired
	private AccountBucketService accountBucketService;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private LoginlogService loginlogService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResetPasswordLogService resetPasswordLogService;

}
