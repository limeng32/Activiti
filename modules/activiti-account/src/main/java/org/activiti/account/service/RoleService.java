package org.activiti.account.service;

import java.util.Collection;

import org.activiti.account.mapper.RoleMapper;
import org.activiti.account.persist.Role;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceSupport<Role> implements RoleMapper {

	@Autowired
	private RoleMapper mapper;

	@Override
	public Role select(Object id) {
		return mapper.select(id);
	}

	@Override
	public Collection<Role> selectAll(Role t) {
		return mapper.selectAll(t);
	}

	@Override
	public Role selectOne(Role t) {
		return mapper.selectOne(t);
	}

	@Override
	public void insert(Role t) {
		mapper.insert(t);
	}

	@Override
	public int update(Role t) {
		return mapper.update(t);
	}

	@Override
	public int updatePersistent(Role t) {
		return mapper.updatePersistent(t);
	}

	@Override
	public int delete(Role t) {
		return mapper.delete(t);
	}

	@Override
	public int count(Role t) {
		return mapper.count(t);
	}

}
