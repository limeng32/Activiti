package org.activiti.explorer.conf;

import javax.sql.DataSource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Configuration
public class ActivitiEngineConfiguration {

	@Autowired
	protected Environment environment;

	@Autowired
	protected DataSource dataSource;

	@Autowired
	protected ProcessEngineConfigurationImpl processEngineConfiguration;

	@Autowired
	protected ProcessEngine processEngine;

	@Bean(name = "transactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

	@Bean(name = "processEngineFactoryBean")
	public ProcessEngineFactoryBean processEngineFactoryBean() {
		ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
		factoryBean.setProcessEngineConfiguration(processEngineConfiguration);
		return factoryBean;
	}

	// @Bean(name="processEngine")
	public ProcessEngine processEngine1() {
		// Safe to call the getObject() on the @Bean annotated
		// processEngineFactoryBean(), will be
		// the fully initialized object instanced from the factory and will NOT
		// be created more than once
		try {
			return processEngineFactoryBean().getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public RepositoryService repositoryService() {
		return processEngine.getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService() {
		return processEngine.getRuntimeService();
	}

	@Bean
	public TaskService taskService() {
		return processEngine.getTaskService();
	}

	@Bean
	public HistoryService historyService() {
		return processEngine.getHistoryService();
	}

	@Bean
	public FormService formService() {
		return processEngine.getFormService();
	}

	@Bean
	public IdentityService identityService() {
		return processEngine.getIdentityService();
	}

	@Bean
	public ManagementService managementService() {
		return processEngine.getManagementService();
	}
}
