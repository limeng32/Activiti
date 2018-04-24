package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.AccountBucket;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface AccountBucketMapper extends MapperFace<AccountBucket> {
	@Override
	public AccountBucket select(Object id);

	@Override
	public Collection<AccountBucket> selectAll(AccountBucket t);

	@Override
	public AccountBucket selectOne(AccountBucket t);

	@Override
	public void insert(AccountBucket t);

	@Override
	public int update(AccountBucket t);

	@Override
	public int updatePersistent(AccountBucket t);

	@Override
	public int delete(AccountBucket t);

	@Override
	public int count(AccountBucket t);
}
