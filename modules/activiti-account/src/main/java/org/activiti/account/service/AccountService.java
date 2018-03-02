package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.AccountMapper;
import org.activiti.account.persist.Account;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends ServiceSupport<Account> implements AccountMapper {

	@Autowired
	private AccountMapper mapper;

	@Override
	public Account select(Object id) {
		return mapper.select(id);
	}

	@Override
	public Collection<Account> selectAll(Account t) {
		return mapper.selectAll(t);
	}

	@Override
	public Account selectOne(Account t) {
		return mapper.selectOne(t);
	}

	@Override
	public void insert(Account t) {
		mapper.insert(t);
	}

	@Override
	public int update(Account t) {
		return mapper.update(t);
	}

	@Override
	public int updatePersistent(Account t) {
		return mapper.updatePersistent(t);
	}

	@Override
	public int delete(Account t) {
		return mapper.delete(t);
	}

	@Override
	public int count(Account t) {
		return mapper.count(t);
	}

}
