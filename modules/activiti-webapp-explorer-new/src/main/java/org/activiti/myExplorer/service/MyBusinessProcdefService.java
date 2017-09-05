package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.MyBusinessProcdefMapper;
import org.activiti.myExplorer.persist.MyBusinessProcdef;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyBusinessProcdefService extends ServiceSupport<MyBusinessProcdef> implements MyBusinessProcdefMapper {

	@Autowired
	private MyBusinessProcdefMapper mapper;

	@Override
	public MyBusinessProcdef select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<MyBusinessProcdef> selectAll(MyBusinessProcdef t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public MyBusinessProcdef selectOne(MyBusinessProcdef t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(MyBusinessProcdef t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(MyBusinessProcdef t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(MyBusinessProcdef t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(MyBusinessProcdef t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(MyBusinessProcdef t) {
		return supportCount(mapper, t);
	}

}
