package org.activiti.myExplorer.demo.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class receiveTestRunningTask1 implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// receiveTaskprocess
		System.out.println("receiveTestRunningTask is running！");
	}

}
