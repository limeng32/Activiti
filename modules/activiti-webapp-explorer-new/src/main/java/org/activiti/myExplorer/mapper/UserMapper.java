package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.User;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface UserMapper extends MapperFace<User> {

	public User selectForAssociation(Object id);
	
	@Override
	public User select(Object id);

	@Override
	public Collection<User> selectAll(User t);

	@Override
	public User selectOne(User t);

	@Override
	public void insert(User t);

	@Override
	public int update(User t);

	@Override
	public int updatePersistent(User t);

	@Override
	public int delete(User t);

	@Override
	public int count(User t);
}
