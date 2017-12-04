package org.activiti.myExplorer.service;

import org.activiti.engine.RepositoryService;
import org.activiti.explorer.conf.DemoDataConfiguration;
import org.activiti.myExplorer.model.EndCode;
import org.activiti.myExplorer.model.ExecutionReturn;
import org.activiti.myExplorer.model.ProcessInstReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.web.CommonController;
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
public class RestApi2Test {

	@Autowired
	private DemoDataConfiguration demoDataConfiguration;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private CommonController commonController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(commonController).build(); // 构造MockMvc
	}

	public void prepare() {
		demoDataConfiguration.initDeployDemoProcess2();
	}

	@Test
	public void test() {
		Assert.assertNotNull(repositoryService);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi2Test/testJustStart.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi2Test/testJustStart.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi2Test/testJustStart.xml")
	public void testJustStart() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "ttt")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(RetCode.success, processInstReturn.getRetCode());
		Assert.assertEquals("1", processInstReturn.getRetVal());
		Assert.assertEquals(EndCode.no, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow.xml")
	public void testFlow() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/start").param("businessId", "ttt")
				.param("dealRole", "e").param("dealPerson", "dean")).andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/flow").param("exeId", ExecutionReturnA[0].getExeId())
				.param("dealPerson", "dean").param("dealRole", "e")).andReturn().getModelAndView();
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApi2Test/testFlow2.xml")
	public void testFlow2() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/start").param("businessId", "ttt")
				.param("dealRole", "e").param("dealPerson", "dean")).andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.no, processInstReturn.getIsEnd());

		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnA[0].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();

		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.no, processInstReturn.getIsEnd());
		ExecutionReturn[] ExecutionReturnB = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);

		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnB[0].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(0, processInstReturn.getExecutionReturn().size());
		Assert.assertEquals(EndCode.no, processInstReturn.getIsEnd());

		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnB[1].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		modelAndView = this.mockMvc.perform(MockMvcRequestBuilders.get("/flow")
				.param("exeId", ExecutionReturnB[1].getExeId()).param("dealPerson", "dean").param("dealRole", "e"))
				.andReturn().getModelAndView();
		processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.yes, processInstReturn.getIsEnd());
	}
}
