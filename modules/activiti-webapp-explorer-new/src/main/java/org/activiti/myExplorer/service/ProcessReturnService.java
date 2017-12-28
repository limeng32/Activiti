package org.activiti.myExplorer.service;

import java.util.Collection;

import org.activiti.myExplorer.mapper.ProcessReturnMapper;
import org.activiti.myExplorer.persist.ProcessReturn;
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
public class ProcessReturnService extends ServiceSupport<ProcessReturn> implements ProcessReturnMapper {

	@Autowired
	private ProcessReturnMapper mapper;

	@Override
	public ProcessReturn select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Collection<ProcessReturn> selectAll(ProcessReturn t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public ProcessReturn selectOne(ProcessReturn t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(ProcessReturn t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(ProcessReturn t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public int updatePersistent(ProcessReturn t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(ProcessReturn t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(ProcessReturn t) {
		return supportCount(mapper, t);
	}

}
