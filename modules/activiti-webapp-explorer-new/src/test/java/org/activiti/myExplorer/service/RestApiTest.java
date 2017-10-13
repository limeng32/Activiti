package org.activiti.myExplorer.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
public class RestApiTest {

	@Autowired
	private CommonService commonService;

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
				.perform(MockMvcRequestBuilders.get("/justStart").param("businessId", "business_real_1")
						.contentType(MediaType.APPLICATION_JSON).content("{}").accept(MediaType.APPLICATION_JSON))
				.andReturn().getModelAndView();
		ProcessInstReturn processInstReturn = (ProcessInstReturn) modelAndView.getModelMap().get("_content");
		Assert.assertEquals(RetCode.success, processInstReturn.getRetCode());
		Assert.assertEquals("1", processInstReturn.getRetVal());
		Assert.assertEquals(EndCode.no, processInstReturn.getIsEnd());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testFlow.xml")
	public void testFlow() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.justStart("business_real_1", null, null, null);
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.flowOneStep(ExecutionReturnA[0].getExeId(), null, "dean", "{form_data:{isMain:\"yes\"}}");
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testStart.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testStart.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testStart.xml")
	public void testStart() {
		prepare();
		commonService.start("business_real_1", null, "dean", "{form_data:{isMain:\"yes\"}}");
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testSuspend.xml")
	public void testSuspend() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.start("business_real_1", null, "dean",
				"{form_data:{isMain:\"yes\"}}");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.suspend(ExecutionReturnA[0].getExeId());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testActivate.xml")
	public void testActivate() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.start("business_real_1", null, "dean",
				"{form_data:{isMain:\"yes\"}}");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.suspend(ExecutionReturnA[0].getExeId());
		commonService.activate(ExecutionReturnA[0].getExeId());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testTerminate.xml")
	public void testTerminate() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.start("business_real_1", null, "dean",
				"{form_data:{isMain:\"yes\"}}");
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.terminate(ExecutionReturnA[0].getExeId());
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testUnreceipt.xml")
	public void testUnreceipt() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.justStart("business_real_1", null, null, null);
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.unreceipt(ExecutionReturnA[0].getExeId(), "dean");
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testReceipt.xml")
	public void testReceipt() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.justStart("business_real_1", null, null, null);
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.unreceipt(ExecutionReturnA[0].getExeId(), "dean");
		commonService.receipt(ExecutionReturnA[0].getExeId(), "dean");
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testJump.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testJump.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testJump.xml")
	public void testJump() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.justStart("business_real_1", null, null, null);
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.jump(ExecutionReturnA[0].getExeId(), "zong_yuan_xu_1");
	}

	@Test
	@DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.xml")
	@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.result.xml")
	@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/restApiTest/testWithdraw.xml")
	public void testWithdraw() {
		prepare();
		ProcessInstReturn processInstReturn = commonService.justStart("business_real_1", null, null, null);
		ExecutionReturn[] ExecutionReturnA = processInstReturn.getExecutionReturn()
				.toArray(new ExecutionReturn[processInstReturn.getExecutionReturn().size()]);
		commonService.jump(ExecutionReturnA[0].getExeId(), "zong_yuan_xu_1");
		commonService.unreceipt(ExecutionReturnA[0].getExeId(), "dean");
		commonService.withdraw(ExecutionReturnA[0].getExeId(), ExecutionReturnA[0].getTaskId());
	}

	@Test
	// @Ignore
	public void getPurposeTest() throws ServletException, IOException {

		String requestURI = "/autodeploy/getPurpose";
		String localAddr = "10.0.209.59";
		Integer localPort = 8080;
		String httpMethod = "GET";

		Map<String, String> mapWebArgs = new HashMap<>();
		// mapWebArgs.put("argip", "10.124.131.78:30808");
		// mapWebArgs.put("arglayerurl", "/webdataopts");

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		Mockito.doNothing().when(request).setCharacterEncoding(Mockito.anyString());
		HttpServletResponse response = mock(HttpServletResponse.class);
		Mockito.doNothing().when(response).setContentType(Mockito.anyString());
		Mockito.doNothing().when(response).setCharacterEncoding(Mockito.anyString());
		Mockito.doNothing().when(response).setHeader(Mockito.anyString(), Mockito.anyString());

		Map<String, String[]> hash = new HashMap<String, String[]>();
		Vector<String> v = new Vector<>();
		for (String tmpKey : mapWebArgs.keySet()) {
			String tmpValue = mapWebArgs.get(tmpKey);
			String[] tmpValueArr = new String[] { tmpValue };
			hash.put(tmpKey, tmpValueArr);
			v.addElement(tmpKey);
			when(request.getParameter(tmpKey)).thenReturn(tmpValue);
			String[] tmpStrArr = new String[] { tmpValue };
			when(request.getParameterValues(tmpKey)).thenReturn(tmpStrArr);
		}

		when(request.getParameterNames()).thenReturn(v.elements());
		when(request.getParameterMap()).thenReturn(hash);

		when(request.getRequestURI()).thenReturn(requestURI);
		when(request.getLocalAddr()).thenReturn(localAddr);
		when(request.getLocalPort()).thenReturn(localPort);
		when(request.getMethod()).thenReturn(httpMethod);

		StringWriter writer = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(writer));

	}

	// @Test
	// public void createIncotermSuccess() throws Exception {
	// IncotermTo createdIncoterm = new IncotermTo();
	// createdIncoterm.setId(new
	// IncotermId(UUID.fromString("6305ff33-295e-11e5-ae37-54ee7534021a")));
	// createdIncoterm.setCode("EXW");
	// createdIncoterm.setDescription("code exw");
	// createdIncoterm.setLocationQualifier(LocationQualifier.DEPARTURE);
	//
	// when(inventoryService.create(any(IncotermTo.class))).thenReturn(createdIncoterm);
	//
	// mockMvc.perform(post("/secured/resources/incoterms/create").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
	// .content("{\"code\" : \"EXW\", \"description\" : \"code exw\",
	// \"locationQualifier\" : \"DEPARTURE\"}".getBytes()))
	// //.andDo(print())
	// .andExpect(status().isOk())
	// .andExpect(jsonPath("id.value").exists())
	// .andExpect(jsonPath("id.value").value("6305ff33-295e-11e5-ae37-54ee7534021a"))
	// .andExpect(jsonPath("code").value("EXW"));
	// }
}
