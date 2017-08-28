package org.activiti.myExplorer.demo.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTask implements JavaDelegate {

	private Expression param;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if (execution.getVariable("comment") != null) {
			String comment = execution.getVariable("comment").toString();
			comment = comment + "-" + param.getValue(execution);
			execution.setVariable("comment", comment);
		}
	}

}
