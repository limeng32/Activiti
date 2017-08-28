package org.activiti.myExplorer.demo.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ReceiveTestRunningTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("ReceiveTestRunningTask is running！");
	}

}
