package org.activiti;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class ApplicationContextProvider implements ApplicationContextAware {
	private static ApplicationContext context;

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		context = ctx;
	}
}
