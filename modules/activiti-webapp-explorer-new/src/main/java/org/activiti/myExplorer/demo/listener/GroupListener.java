package org.activiti.myExplorer.demo.listener;

import java.io.Serializable;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.myExplorer.demo.model.SIGroup;

public class GroupListener implements TaskListener, JavaDelegate, Serializable {

	private static final long serialVersionUID = 1L;

	private SIGroup siGroup = new SIGroup();

	public GroupListener() {

	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("::"+siGroup);
		execution.setVariable("siGroup", siGroup);
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		delegateTask.addCandidateGroup(siGroup.getProjectmanager());
	}

}
