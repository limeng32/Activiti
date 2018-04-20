package org.activiti.myExplorer.model;

import java.util.Date;

import org.activiti.account.persist.Account;

public class AccountSession {

	public AccountSession(Account account, Date expirationTime) {
		this.account = account;
		this.expirationTime = expirationTime;
	}

	private Account account;

	private Date expirationTime;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

}
