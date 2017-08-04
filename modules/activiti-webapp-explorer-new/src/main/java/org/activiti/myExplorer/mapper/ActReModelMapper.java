package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface ActReModelMapper extends MapperFace<ActReModel> {

	@Override
	public ActReModel select(Object id);

	@Override
	public Collection<ActReModel> selectAll(ActReModel t);

	@Override
	public ActReModel selectOne(ActReModel t);

	@Override
	public void insert(ActReModel t);

	@Override
	public int update(ActReModel t);

	@Override
	public int updatePersistent(ActReModel t);

	@Override
	public int delete(ActReModel t);

	@Override
	public int count(ActReModel t);
}
