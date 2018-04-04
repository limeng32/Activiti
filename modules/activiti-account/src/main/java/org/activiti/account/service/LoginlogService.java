package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.LoginlogMapper;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.Loginlog;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginlogService extends ServiceSupport<Loginlog> implements LoginlogMapper {

	@Autowired
	private LoginlogMapper mapper;

	@Override
	public Loginlog select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public void insert(Loginlog t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Loginlog t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Loginlog> selectAll(Loginlog t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Loginlog t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Loginlog t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Loginlog t) {
		return supportCount(mapper, t);
	}

	public void loadAccount(Account account, Loginlog loginlog) {
		account.removeAllLoginlog();
		loginlog.setAccount(account);
		account.setLoginlog(mapper.selectAll(loginlog));
	}

	@Override
	public Loginlog selectOne(Loginlog t) {
		return supportSelectOne(mapper, t);
	}

}