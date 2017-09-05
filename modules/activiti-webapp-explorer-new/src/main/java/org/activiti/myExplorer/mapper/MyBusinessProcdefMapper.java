package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.MyBusinessProcdef;
import org.activiti.myExplorer.pojoHelper.MapperFace;

public interface MyBusinessProcdefMapper extends MapperFace<MyBusinessProcdef> {

	@Override
	public MyBusinessProcdef select(Object id);

	@Override
	public Collection<MyBusinessProcdef> selectAll(MyBusinessProcdef t);

	@Override
	public MyBusinessProcdef selectOne(MyBusinessProcdef t);

	@Override
	public void insert(MyBusinessProcdef t);

	@Override
	public int update(MyBusinessProcdef t);

	@Override
	public int updatePersistent(MyBusinessProcdef t);

	@Override
	public int delete(MyBusinessProcdef t);

	@Override
	public int count(MyBusinessProcdef t);
}
