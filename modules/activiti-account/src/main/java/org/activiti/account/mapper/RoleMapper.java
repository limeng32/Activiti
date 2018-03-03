package org.activiti.account.mapper;

import java.util.Collection;

import org.activiti.account.persist.Role;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface RoleMapper extends MapperFace<Role> {
	@Override
	public Role select(Object id);

	@Override
	public Collection<Role> selectAll(Role t);

	@Override
	public Role selectOne(Role t);

	@Override
	public void insert(Role t);

	@Override
	public int update(Role t);

	@Override
	public int updatePersistent(Role t);

	@Override
	public int delete(Role t);

	@Override
	public int count(Role t);
}
