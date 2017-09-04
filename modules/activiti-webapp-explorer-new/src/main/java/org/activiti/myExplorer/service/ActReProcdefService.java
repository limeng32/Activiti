package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.ActReProcdefMapper;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActReProcdefService extends ServiceSupport<ActReProcdef> implements ActReProcdefMapper {

	@Autowired
	private ActReProcdefMapper mapper;

	@Override
	public ActReProcdef select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<ActReProcdef> selectAll(ActReProcdef t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public ActReProcdef selectOne(ActReProcdef t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(ActReProcdef t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(ActReProcdef t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(ActReProcdef t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(ActReProcdef t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(ActReProcdef t) {
		return supportCount(mapper, t);
	}

}
