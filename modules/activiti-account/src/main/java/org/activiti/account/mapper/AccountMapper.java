package org.activiti.account.mapper;

import java.util.Collection;

import org.activiti.account.persist.Account;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface AccountMapper extends MapperFace<Account> {
	@Override
	public Account select(Object id);

	@Override
	public Collection<Account> selectAll(Account t);

	@Override
	public Account selectOne(Account t);

	@Override
	public void insert(Account t);

	@Override
	public int update(Account t);

	@Override
	public int updatePersistent(Account t);

	@Override
	public int delete(Account t);

	@Override
	public int count(Account t);
}
