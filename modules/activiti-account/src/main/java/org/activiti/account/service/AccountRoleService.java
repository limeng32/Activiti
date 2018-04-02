package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.AccountRoleMapper;
import org.activiti.account.persist.AccountRole;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountRoleService extends ServiceSupport<AccountRole> implements AccountRoleMapper {

	@Autowired
	private AccountRoleMapper mapper;

	@Override
	public AccountRole select(Object id) {
		return mapper.select(id);
	}

	@Override
	public Collection<AccountRole> selectAll(AccountRole t) {
		return mapper.selectAll(t);
	}

	@Override
	public AccountRole selectOne(AccountRole t) {
		return mapper.selectOne(t);
	}

	@Override
	public void insert(AccountRole t) {
		mapper.insert(t);
	}

	@Override
	public int update(AccountRole t) {
		return mapper.update(t);
	}

	@Override
	public int updatePersistent(AccountRole t) {
		return mapper.updatePersistent(t);
	}

	@Override
	public int delete(AccountRole t) {
		return mapper.delete(t);
	}

	@Override
	public int count(AccountRole t) {
		return mapper.count(t);
	}

}
