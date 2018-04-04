package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.MyBusinessModelMapper;
import org.activiti.myExplorer.persist.MyBusinessModel;
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
public class MyBusinessModelService extends ServiceSupport<MyBusinessModel> implements MyBusinessModelMapper {

	@Autowired
	private MyBusinessModelMapper mapper;

	@Override
	public MyBusinessModel select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<MyBusinessModel> selectAll(MyBusinessModel t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public MyBusinessModel selectOne(MyBusinessModel t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(MyBusinessModel t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(MyBusinessModel t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(MyBusinessModel t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(MyBusinessModel t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(MyBusinessModel t) {
		return supportCount(mapper, t);
	}

	@Override
	public MyBusinessModel selectForAssociation(Object id) {
		return mapper.selectForAssociation(id);
	}

}
