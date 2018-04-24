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
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-explorer-test.xml", "classpath:activiti-account-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class, databaseConnection = { "dataSource" })
public class RestApiTest {

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
		demoDataConfiguration.initDeployDemoProcess();
	}

	@Test
	public void test() {
		Assert.assertNotNull(repositoryService);
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testJustStart.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testJustStart.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testJustStart.xml")
	public void testJustStart() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(RetCode.SUCCESS, processInstReturn.getRetCode());
		Assert.assertEquals("1", processInstReturn.getRetVal());
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.xml")
	public void testFlow() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");

		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/flow").param("exeId", ExecutionReturnA[0].getExeId())
				.param("formData", "{form_data:{isMain:\"yes\"}}").param("dealPerson", "dean")
				.param("dealRole", "projectmanager")).andReturn().getModelAndView();
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testStart.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testStart.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testStart.xml")
	public void testStart() throws Exception {
		prepare();
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1").param("dealPerson", "dean")
						.param("formData", "{form_data:{isMain:\"yes\"}}").param("dealRole", "leader"))
				.andReturn().getModelAndView();
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testStart2.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testStart2.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testStart2.xml")
	public void testStart2() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(
				MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1").param("dealPerson", "dean")
						.param("formData", "{form_data:{isMain:\"yes\"}}").param("dealRole", "wrongRole"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());
		Assert.assertEquals(RetCode.EXCEPTION, processInstReturn.getRetCode());
		Assert.assertEquals("角色 wrongRole 没有权限流转这个环节", processInstReturn.getRetVal());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testStart3.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testStart3.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testStart3.xml")
	public void testStart3() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1")
						.param("dealPerson", "dean").param("formData", "{form_data:{isMain:\"yes\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(EndCode.NO, processInstReturn.getIsEnd());
		Assert.assertEquals(RetCode.EXCEPTION, processInstReturn.getRetCode());
		Assert.assertEquals("角色 null 没有权限流转这个环节", processInstReturn.getRetVal());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.xml")
	public void testSuspend() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(
				MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1").param("dealPerson", "dean")
						.param("dealRole", "projectmanager").param("formData", "{form_data:{isMain:\"yes\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/suspend").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();

		/* 对已经挂起的工作项继续挂起 */
		ModelAndView modelAndView2 = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/suspend").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn2 = (ProcessInstReturn) modelAndView2.getModelMap().get("_content");
		Assert.assertEquals(RetCode.SUCCESS, processInstReturn2.getRetCode());

		/* 挂起一个不存在的工作项 */
		ModelAndView modelAndView3 = this.mockMvc.perform(MockMvcRequestBuilders.get("/suspend").param("exeId", "0"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn3 = (ProcessInstReturn) modelAndView3.getModelMap().get("_content");
		Assert.assertEquals(RetCode.EXCEPTION, processInstReturn3.getRetCode());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.xml")
	public void testActivate() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(
				MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1").param("dealPerson", "dean")
						.param("dealRole", "projectmanager").param("formData", "{form_data:{isMain:\"yes\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/suspend").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/activate").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();

		/* 对已经激活的工作项继续激活 */
		ModelAndView modelAndView2 = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/activate").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn2 = (ProcessInstReturn) modelAndView2.getModelMap().get("_content");
		Assert.assertEquals(RetCode.SUCCESS, processInstReturn2.getRetCode());

		/* 激活一个不存在的工作项 */
		ModelAndView modelAndView3 = this.mockMvc.perform(MockMvcRequestBuilders.get("/activate").param("exeId", "0"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn3 = (ProcessInstReturn) modelAndView3.getModelMap().get("_content");
		Assert.assertEquals(RetCode.EXCEPTION, processInstReturn3.getRetCode());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.xml")
	public void testTerminate() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc.perform(
				MockMvcRequestBuilders.get("/start").param("businessId", "business_real_1").param("dealPerson", "dean")
						.param("dealRole", "projectmanager").param("formData", "{form_data:{isMain:\"yes\"}}"))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/terminate").param("exeId", ExecutionReturnA[0].getExeId()))
				.andReturn().getModelAndView();
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.xml")
	public void testUnreceipt() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		ModelAndView modelAndView2 = this.mockMvc.perform(MockMvcRequestBuilders.get("/unreceipt")
				.param("dealPerson", "dean").param("exeId", ExecutionReturnA[0].getExeId())).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn2 = (ProcessInstReturn) modelAndView2.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA2 = processInstReturn2.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn2.getExecutionReturn().size()]);
		Assert.assertEquals(ExecutionReturnA[0].getExeId(), ExecutionReturnA2[0].getExeId());
		Assert.assertEquals(ExecutionReturnA[0].getTaskId(), ExecutionReturnA2[0].getTaskId());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.xml")
	public void testReceipt() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/unreceipt").param("dealPerson", "dean").param("exeId",
				ExecutionReturnA[0].getExeId())).andReturn().getModelAndView();
		ModelAndView modelAndView2 = this.mockMvc.perform(MockMvcRequestBuilders.get("/receipt")
				.param("dealPerson", "dean").param("exeId", ExecutionReturnA[0].getExeId())).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn2 = (ProcessInstReturn) modelAndView2.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA2 = processInstReturn2.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn2.getExecutionReturn().size()]);
		Assert.assertEquals(ExecutionReturnA[0].getExeId(), ExecutionReturnA2[0].getExeId());
		Assert.assertEquals(ExecutionReturnA[0].getTaskId(), ExecutionReturnA2[0].getTaskId());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testJump.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testJump.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testJump.xml")
	public void testJump() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jump").param("activityKey", "zong_yuan_xu_1").param("exeId",
				ExecutionReturnA[0].getExeId())).andReturn().getModelAndView();
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.xml")
	public void testWithdraw() throws Exception {
		prepare();
		ModelAndView modelAndView = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")).andReturn()
				.getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jump").param("activityKey", "zong_yuan_xu_1").param("exeId",
				ExecutionReturnA[0].getExeId())).andReturn().getModelAndView();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/unreceipt").param("dealPerson", "dean").param("exeId",
				ExecutionReturnA[0].getExeId())).andReturn().getModelAndView();
		this.mockMvc.perform(MockMvcRequestBuilders.get("/withdraw").param("taskId", ExecutionReturnA[0].getTaskId())
				.param("exeId", ExecutionReturnA[0].getExeId())).andReturn().getModelAndView();
	}

}
