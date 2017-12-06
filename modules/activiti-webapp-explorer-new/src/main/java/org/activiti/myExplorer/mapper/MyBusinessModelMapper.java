package org.activiti.myExplorer.mapper;

import java.util.Collection;

import org.activiti.myExplorer.persist.MyBusinessModel;
import org.activiti.myExplorer.pojoHelper.MapperFace;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public interface MyBusinessModelMapper extends MapperFace<MyBusinessModel> {

	@Override
	public MyBusinessModel select(Object id);

	@Override
	public Collection<MyBusinessModel> selectAll(MyBusinessModel t);

	@Override
	public MyBusinessModel selectOne(MyBusinessModel t);

	@Override
	public void insert(MyBusinessModel t);

	@Override
	public int update(MyBusinessModel t);

	@Override
	public int updatePersistent(MyBusinessModel t);

	@Override
	public int delete(MyBusinessModel t);

	@Override
	public int count(MyBusinessModel t);
}
