package org.activiti.myExplorer;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.support.ApplicationObjectSupport;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class HelloGroovy extends ApplicationObjectSupport implements JavaDelegate {

	private static void fun2(String s, DelegateExecution execution) {
		Binding binding = new Binding();
		binding.setVariable("x", 10);
		binding.setVariable("language", "Groovy");
		binding.setVariable("execution", execution);
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(s);
		assert value.equals(10);
		assert binding.getVariable("y").equals(20);
		assert binding.getVariable("z").equals(30);

	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String id = execution.getEngineServices().getRepositoryService().getModel("100014").getTenantId();
		System.out.println(id);
		fun2(id, execution);
		System.out.println(" ms");
	}
}
