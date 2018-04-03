package org.activiti.account.mapper;

import java.util.Collection;

import org.activiti.account.persist.Loginlog;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface LoginlogMapper extends MapperFace<Loginlog> {
	@Override
	public Loginlog select(Object id);

	@Override
	public Collection<Loginlog> selectAll(Loginlog t);

	@Override
	public Loginlog selectOne(Loginlog t);

	@Override
	public void insert(Loginlog t);

	@Override
	public int update(Loginlog t);

	@Override
	public int updatePersistent(Loginlog t);

	@Override
	public int delete(Loginlog t);

	@Override
	public int count(Loginlog t);
}
