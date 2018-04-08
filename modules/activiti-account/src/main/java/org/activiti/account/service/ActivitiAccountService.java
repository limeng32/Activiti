package org.activiti.account.service;

import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.account.persist.Account;
import org.activiti.model.Callback;
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

	public Callback checkAccountNotExist(String email) {
		Callback ret = new Callback();
		Account ac = new Account();
		ac.setEmail(email);
		int count = accountService.count(ac);
		switch (count) {
		case 0:
			ret.setFlag(true);
			break;
		default:
			ret.setFlag(false);
			ret.setMessage(ActivitiAccountExceptionEnum.RepetitionEmail.description());
			break;
		}
		return ret;
	}
}
