package org.activiti.myExplorer.pojoHelper;

import java.util.Collection;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public interface MapperFace<T> {

	public T select(Object id);

	public Collection<T> selectAll(T t);

	public T selectOne(T t);

	public void insert(T t);

	public int update(T t);

	public int updatePersistent(T t);

	public int delete(T t);

	public int count(T t);
}
