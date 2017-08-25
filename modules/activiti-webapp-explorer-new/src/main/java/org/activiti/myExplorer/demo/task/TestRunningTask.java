package org.activiti.myExplorer.demo.task;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.runtime.Execution;

public class TestRunningTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// receiveTaskprocess
		List<Execution> executions = execution.getEngineServices().getRuntimeService().createExecutionQuery()
				.processDefinitionKey("receive_1").activityId("receiveTask").list();
		for (Execution e : executions) {
			execution.getEngineServices().getRuntimeService().signal(e.getId());
		}
		System.out.println("TestRunningTask is running！");
	}

}
