package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.pojoHelper.MapperFace;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public interface ActReProcdefMapper extends MapperFace<ActReProcdef> {
	
	public ActReProcdef selectForAssociation(Object id);

	@Override
	public ActReProcdef select(Object id);

	@Override
	public Collection<ActReProcdef> selectAll(ActReProcdef t);

	@Override
	public ActReProcdef selectOne(ActReProcdef t);

	@Override
	public void insert(ActReProcdef t);

	@Override
	public int update(ActReProcdef t);

	@Override
	public int updatePersistent(ActReProcdef t);

	@Override
	public int delete(ActReProcdef t);

	@Override
	public int count(ActReProcdef t);
}
