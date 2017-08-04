package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.ActReModelMapper;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActReModelService extends ServiceSupport<ActReModel> implements ActReModelMapper {

	@Autowired
	private ActReModelMapper mapper;

	@Override
	public ActReModel select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<ActReModel> selectAll(ActReModel t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public ActReModel selectOne(ActReModel t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(ActReModel t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(ActReModel t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(ActReModel t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(ActReModel t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(ActReModel t) {
		return supportCount(mapper, t);
	}

}
