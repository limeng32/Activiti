package org.activiti.myExplorer.pojoHelper;

public interface PojoFace<T> {

	Object getId();

	/* 获取供缓存使用的key值 */
	String getCacheKey();

}