package org.activiti.myExplorer.demo.listener;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.myExplorer.demo.model.SIGroup;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class GroupListener implements TaskListener, JavaDelegate, Serializable {

	private static final long serialVersionUID = 1L;

	private SIGroup siGroup = new SIGroup();

	public GroupListener() {
		// Make sonarqube happy
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("siGroup", siGroup);
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		delegateTask.addCandidateGroup(siGroup.getProjectmanager());
	}

}
