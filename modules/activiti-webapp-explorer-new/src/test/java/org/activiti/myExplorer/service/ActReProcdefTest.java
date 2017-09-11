package org.activiti.myExplorer.service;

import org.activiti.engine.RepositoryService;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.MyBusinessProcdef;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-explorer-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class)
public class ActReProcdefTest {

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private MyBusinessProcdefService myBusinessProcdefService;

	@Autowired
	private ProcessEngineFactoryBean processEngine;

	@Test
	public void test() {
		RepositoryService repositoryService = processEngine.getProcessEngineConfiguration().getRepositoryService();
		Assert.assertNotNull(repositoryService);
		Assert.assertTrue(true);
	}

	// @Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.result.xml")
	public void testActReProcdef() {
		ActReProcdef actReProcdef = actReProcdefService.select("a");
		Assert.assertEquals("c", actReProcdef.getCategory());
	}

	// @Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessProcdef.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessProcdef.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessProcdef.result.xml")
	public void testMyBusinessProcdef() {
		MyBusinessProcdef myBusinessProcdef = myBusinessProcdefService.select("b");
		Assert.assertEquals("0001", myBusinessProcdef.getBusinessId());
		Assert.assertEquals("c", myBusinessProcdef.getActReProcdef().getCategory());

		myBusinessProcdef.setBusinessId("0002");
		myBusinessProcdefService.update(myBusinessProcdef);

		MyBusinessProcdef m = new MyBusinessProcdef();
		m.setBusinessId("0003");
		myBusinessProcdefService.insert(m);
		Assert.assertNotNull(m.getId());
		myBusinessProcdefService.delete(m);
	}
}
