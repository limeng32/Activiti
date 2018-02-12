package org.activiti.myExplorer.pojoHelper;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public interface PojoFace {

	Object getId();

	/* 获取供缓存使用的key值 */
	String getCacheKey();

}