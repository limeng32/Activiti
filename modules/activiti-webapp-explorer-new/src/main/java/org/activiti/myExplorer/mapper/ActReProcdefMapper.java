package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface ActReProcdefMapper extends MapperFace<ActReProcdef> {

	@Override
	public ActReProcdef select(Object id);

	@Override
	public Collection<ActReProcdef> selectAll(ActReProcdef t);

	@Override
	public ActReProcdef selectOne(ActReProcdef t);

	@Override
	public void insert(ActReProcdef t);

	@Override
	public int update(ActReProcdef t);

	@Override
	public int updatePersistent(ActReProcdef t);

	@Override
	public int delete(ActReProcdef t);

	@Override
	public int count(ActReProcdef t);
}
