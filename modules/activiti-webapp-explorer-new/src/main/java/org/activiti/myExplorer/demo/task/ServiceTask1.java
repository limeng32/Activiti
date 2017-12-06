package org.activiti.myExplorer.demo.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class ServiceTask1 implements JavaDelegate {

	private Expression text1;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String value1 = (String) text1.getValue(execution);
		execution.setVariable("var1", new StringBuffer(value1).reverse().toString());
	}

}
