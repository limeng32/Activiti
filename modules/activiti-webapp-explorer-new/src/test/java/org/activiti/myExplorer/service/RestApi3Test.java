package org.activiti.myExplorer.service;

import org.activiti.engine.RepositoryService;
import org.activiti.explorer.conf.DemoDataConfiguration;
import org.activiti.myExplorer.model.EndCode;
import org.activiti.myExplorer.model.ExecutionReturn;
import org.activiti.myExplorer.model.ProcessInstReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.web.CommonExternalController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-explorer-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource" })
public class RestApi3Test {

	@Autowired
	private DemoDataConfiguration demoDataConfiguration;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private CommonExternalController commonController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(commonController).build(); // 构造MockMvc
	}

	public void prepare() {
		demoDataConfiguration.initDeployDemoProcess("diagrams/testMessage.bpmn20.xml", "testMessage", "testMessage");
	}

	@Test
	public void test() {
		Assert.assertNotNull(repositoryService);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi3Test/testJustStart.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi3Test/testJustStart.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi3Test/testJustStart.xml")
	public void testJustStart() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "testMessage")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(RetCode.SUCCESS, processInstReturn.getRetCode());
		Assert.assertEquals("1", processInstReturn.getRetVal());
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi3Test/testFlow.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi3Test/testFlow.xml")
	public void testFlow() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/start").param("businessId", "testMessage").param("dealRole", "e")
						.param("dealPerson", "dean").param("formData", "{form_data:{ou:\"1\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());

		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnA[0].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.YES, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi3Test/testMessage.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi3Test/testMessage.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi3Test/testMessage.xml")
	public void testMessage() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/start").param("businessId", "testMessage").param("dealRole", "e")
						.param("dealPerson", "dean").param("formData", "{form_data:{ou:\"1\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());

		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnA[0].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.YES, processInstReturn.getIsEnd());

		ExecutionReturn[] ExecutionReturnB = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/message")
				.param("exeId", ExecutionReturnB[0].getExeId()).param("message", "finish")).andReturn()
				.getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.YES, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi3Test/testWithdraw.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi3Test/testWithdraw.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi3Test/testWithdraw.xml")
	public void testWithdraw() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/start").param("businessId", "testMessage").param("dealRole", "e")
						.param("dealPerson", "dean").param("formData", "{form_data:{ou:\"1\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());

		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnA[0].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.YES, processInstReturn.getIsEnd());

		modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/withdraw").param("taskId", ExecutionReturnA[0].getTaskId()))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());

		/* 增加一个错误的taskId的撤回操作 */
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/withdraw").param("taskId", "000000"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(RetCode.EXCEPTION, processInstReturn.getRetCode());
	}
}
