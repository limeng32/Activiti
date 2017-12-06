package org.activiti.myExplorer.demo.listener;

import org.activiti.ApplicationContextProvider;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.myExplorer.demo.model.SIGroup;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class GroupListener2 implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("siGroup", getSIGroup());
	}

	private SIGroup getSIGroup() {
		return (SIGroup) ApplicationContextProvider.getApplicationContext().getBean(SIGroup.class);
	}
}
