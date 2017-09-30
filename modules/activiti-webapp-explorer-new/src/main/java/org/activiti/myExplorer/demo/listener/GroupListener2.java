package org.activiti.myExplorer.demo.listener;

import org.activiti.ApplicationContextProvider;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.myExplorer.demo.model.SIGroup;

public class GroupListener2 implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("siGroup", getSIGroup());
	}

	private SIGroup getSIGroup() {
		return (SIGroup) ApplicationContextProvider.getApplicationContext().getBean(SIGroup.class);
	}
}
