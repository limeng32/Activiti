package org.activiti.account.mapper;

import java.util.Collection;

import org.activiti.account.persist.AccountRole;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface AccountRoleMapper extends MapperFace<AccountRole> {
	@Override
	public AccountRole select(Object id);

	@Override
	public Collection<AccountRole> selectAll(AccountRole t);

	@Override
	public AccountRole selectOne(AccountRole t);

	@Override
	public void insert(AccountRole t);

	@Override
	public int update(AccountRole t);

	@Override
	public int updatePersistent(AccountRole t);

	@Override
	public int delete(AccountRole t);

	@Override
	public int count(AccountRole t);
}
