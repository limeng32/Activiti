package org.activiti.myExplorer.service;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.MyBusinessModel;
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
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource" })
public class ActReProcdefTest {

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private ActReProcdefService actReProcdefService;

	@Autowired
	private MyBusinessModelService myBusinessModelService;

	@Autowired
	private StandaloneProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	private IdentityService identityService;

	@Test
	public void test() {
		RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();

		identityService.createGroupQuery().groupId("1");

		Assert.assertNotNull(repositoryService);
		Assert.assertTrue(true);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/actReProcdefTest/testActReProcdef.result.xml")
	public void testActReProcdef() {
		ActReProcdef actReProcdef = actReProcdefService.select("a");
		Assert.assertEquals("c", actReProcdef.getCategory());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessModel.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessModel.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/actReProcdefTest/testMyBusinessModel.result.xml")
	public void testMyBusinessModel() {
		MyBusinessModel myBusinessModel = myBusinessModelService.select("b");
		Assert.assertEquals("0001", myBusinessModel.getBusinessId());
		Assert.assertEquals("c", myBusinessModel.getActReModel().getCategory());

		myBusinessModel.setBusinessId("0002");
		myBusinessModelService.update(myBusinessModel);

		ActReModel actReModel = actReModelService.select("a2");

		MyBusinessModel m = new MyBusinessModel();
		m.setBusinessId("0003");
		m.setActReModel(actReModel);
		myBusinessModelService.insert(m);
	}
}
