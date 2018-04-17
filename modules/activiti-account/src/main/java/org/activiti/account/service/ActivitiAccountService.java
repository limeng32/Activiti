package org.activiti.account.service;

import org.activiti.account.exception.ActivitiAccountException;
import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.AccountRole;
import org.activiti.account.persist.Loginlog;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(value = "txManagerAccount", rollbackFor = {
			ActivitiAccountException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	/** 新增account时确保email是唯一的 */
	public void insertAccountTransactive(Account account, AccountRole accountRole) throws ActivitiAccountException {
		accountService.insert(account);
		Account ac = new Account();
		ac.setEmail(account.getEmail());
		int c = accountService.count(ac);
		if (c > 1) {
			throw new ActivitiAccountException(ActivitiAccountExceptionEnum.RepetitionEmail.toString());
		}
		accountRoleService.insert(accountRole);
	}

	@Transactional(value = "txManagerAccount", rollbackFor = {
			ActivitiAccountException.class }, readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insertLoginlogTransactive(Loginlog loginlog) throws ActivitiAccountException {
		loginlogService.insert(loginlog);
		Account ac = new Account();
		ac.setId((String) loginlog.getAccount().getId());
		Loginlog lc = new Loginlog();
		lc.setAccount(ac);
		lc.setLoginDate(loginlog.getLoginDate());
		int count = loginlogService.count(lc);
		switch (count) {
		case 1:
			return;
		default:
			throw new ActivitiAccountException(ActivitiAccountExceptionEnum.FailedToInsertLoginLog.toString());
		}
	}
}
