package org.activiti.myExplorer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.support.ApplicationObjectSupport;

import groovy.lang.Binding;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class HelloGroovy extends ApplicationObjectSupport implements JavaDelegate {

	private static void fun2(String s, DelegateExecution execution) {
		Binding binding = new Binding();
		binding.setVariable("x", 10);
		binding.setVariable("language", "Groovy");
		binding.setVariable("execution", execution);

		/*
		 * GroovyShell shell = new GroovyShell(binding); bject value =
		 * shell.evaluate(s); System.out.println("value:" + value);
		 * System.out.println("y:" + binding.getVariable("y"));
		 * System.out.println("z:" + binding.getVariable("z"));
		 */
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String id = execution.getEngineServices().getRepositoryService().getModel("100014").getTenantId();
		/* System.out.println(id); */
		fun2(id, execution);
	}
}
