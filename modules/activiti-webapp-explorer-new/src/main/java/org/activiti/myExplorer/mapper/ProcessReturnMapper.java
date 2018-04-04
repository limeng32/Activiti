package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.ProcessReturn;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface ProcessReturnMapper extends MapperFace<ProcessReturn> {

	public ProcessReturn selectForAssociation(Object id);
	
	@Override
	public ProcessReturn select(Object id);

	@Override
	public Collection<ProcessReturn> selectAll(ProcessReturn t);

	@Override
	public ProcessReturn selectOne(ProcessReturn t);

	@Override
	public void insert(ProcessReturn t);

	@Override
	public int update(ProcessReturn t);

	@Override
	public int updatePersistent(ProcessReturn t);

	@Override
	public int delete(ProcessReturn t);

	@Override
	public int count(ProcessReturn t);
}
