package org.activiti.account.mapper;

import java.util.Collection;

import org.activiti.account.persist.ResetPasswordLog;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface ResetPasswordLogMapper extends MapperFace<ResetPasswordLog> {
	@Override
	public ResetPasswordLog select(Object id);

	@Override
	public Collection<ResetPasswordLog> selectAll(ResetPasswordLog t);

	@Override
	public ResetPasswordLog selectOne(ResetPasswordLog t);

	@Override
	public void insert(ResetPasswordLog t);

	@Override
	public int update(ResetPasswordLog t);

	@Override
	public int updatePersistent(ResetPasswordLog t);

	@Override
	public int delete(ResetPasswordLog t);

	@Override
	public int count(ResetPasswordLog t);
}
