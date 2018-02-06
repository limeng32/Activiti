package org.activiti.account.face;

import org.activiti.account.persist.Account;
import org.activiti.account.persist.Role;

public interface AccountRoleFace {
	void setAccount(Account account);

	void setRole(Role role);
}
