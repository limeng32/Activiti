package org.activiti.myExplorer.demo.task;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.runtime.Execution;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class TestRunningTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// receiveTaskprocess
		List<Execution> executions = execution.getEngineServices().getRuntimeService().createExecutionQuery()
				.processDefinitionKey("receive_1").activityId("receiveTask").list();
		for (Execution e : executions) {
			execution.getEngineServices().getRuntimeService().signal(e.getId());
		}
	}

}
