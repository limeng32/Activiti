package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.UserMapper;
import org.activiti.myExplorer.persist.User;
import org.activiti.myExplorer.pojoHelper.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Service
public class UserService extends ServiceSupport<User> implements UserMapper {

	@Autowired
	private UserMapper mapper;

	@Override
	public User select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<User> selectAll(User t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public User selectOne(User t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(User t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(User t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(User t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(User t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(User t) {
		return supportCount(mapper, t);
	}

	@Override
	public User selectForAssociation(Object id) {
		return mapper.selectForAssociation(id);
	}

}
