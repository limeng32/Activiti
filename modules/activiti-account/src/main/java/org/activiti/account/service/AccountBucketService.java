package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.AccountBucketMapper;
import org.activiti.account.persist.Account;
import org.activiti.account.persist.AccountBucket;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBucketService extends ServiceSupport<AccountBucket> implements AccountBucketMapper {

	@Autowired
	private AccountBucketMapper mapper;

	@Override
	public AccountBucket select(Object id) {
		return mapper.select(id);
	}

	@Override
	public Collection<AccountBucket> selectAll(AccountBucket t) {
		return mapper.selectAll(t);
	}

	@Override
	public AccountBucket selectOne(AccountBucket t) {
		return mapper.selectOne(t);
	}

	@Override
	public void insert(AccountBucket t) {
		mapper.insert(t);
	}

	@Override
	public int update(AccountBucket t) {
		return mapper.update(t);
	}

	@Override
	public int updatePersistent(AccountBucket t) {
		return mapper.updatePersistent(t);
	}

	@Override
	public int delete(AccountBucket t) {
		return mapper.delete(t);
	}

	@Override
	public int count(AccountBucket t) {
		return mapper.count(t);
	}

	public void loadAccount(Account account, AccountBucket accountBucket) {
		account.removeAllAccountBucket();
		accountBucket.setAccount(account);
		account.setAccountBucket(mapper.selectAll(accountBucket));
	}
}
