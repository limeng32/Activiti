/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.explorer.conf;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Joram Barrez
 */
public class DemoDataConfiguration {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DemoDataConfiguration.class);

	// @Autowired
	// protected IdentityService identityService;
	//
	// @Autowired
	// protected RepositoryService repositoryService;
	//
	// @Autowired
	// protected RuntimeService runtimeService;
	//
	// @Autowired
	// protected TaskService taskService;
	//
	// @Autowired
	// protected ManagementService managementService;

	@Autowired
	protected ProcessEngineConfigurationImpl processEngineConfiguration;

	@Autowired
	protected Environment environment;

	private boolean createDemoUsers;

	private boolean createDemoDefinitions;

	private boolean createDemoModels;

	private boolean createDemoReports;

	@PostConstruct
	public void init() {
		if (isCreateDemoUsers()) {
			LOGGER.info("Initializing demo groups");
			initDemoGroups();
			LOGGER.info("Initializing demo users");
			initDemoUsers();
		}

		if (isCreateDemoDefinitions()) {
			LOGGER.info("Initializing demo process definitions");
			initProcessDefinitions();
		}

		if (isCreateDemoModels()) {
			LOGGER.info("Initializing demo models");
			initModelData();
		}

		if (isCreateDemoReports()) {
			LOGGER.info("Initializing demo report data");
			generateReportData();
		}
	}

	protected void initDemoGroups() {
		String[] assignmentGroups = new String[] { "major", "deputymajor", "manager", "tmo", "finance", "dean",
				"co-dean", "btmo", "bfinance", "leader", "projectmanager" };
		for (String groupId : assignmentGroups) {
			createGroup(groupId, "assignment");
		}

		String[] securityGroups = new String[] { "user", "admin" };
		for (String groupId : securityGroups) {
			createGroup(groupId, "security-role");
		}
	}

	protected void createGroup(String groupId, String type) {
		if (processEngineConfiguration.getIdentityService().createGroupQuery().groupId(groupId).count() == 0) {
			Group newGroup = processEngineConfiguration.getIdentityService().newGroup(groupId);
			newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
			newGroup.setType(type);
			processEngineConfiguration.getIdentityService().saveGroup(newGroup);
		}
	}

	protected void initDemoUsers() {
		createUser("wfadmin", "工作流管理员",
				"wfadmin", "1", "wfadmin@activiti.org", "", Arrays.asList("major", "deputymajor", "manager", "tmo",
						"finance", "dean", "co-dean", "btmo", "bfinance", "leader", "projectmanager", "user", "admin"),
				null);
		createUser("major", "总院事业部经理", "major", "1", "major@activiti.org", "", Arrays.asList("major", "user"), null);
		createUser("deputymajor", "总院事业部副经理", "deputymajor", "1", "deputymajor@activiti.org", "",
				Arrays.asList("deputymajor", "user"), null);
		createUser("manager", "总院中心经理", "manager", "1", "manager@activiti.org", "", Arrays.asList("manager", "user"),
				null);
		createUser("tmo", "总院TMO", "tmo", "1", "tmo@activiti.org", "", Arrays.asList("tmo", "user"), null);
		createUser("finance", "总院财务部", "finance", "1", "finance@activiti.org", "", Arrays.asList("finance", "user"),
				null);
		createUser("dean", "分院院长", "dean", "1", "dean@activiti.org", "", Arrays.asList("dean", "user"), null);
		createUser("co-dean", "分院副院长", "co-dean", "1", "co-dean@activiti.org", "", Arrays.asList("co-dean", "user"),
				null);
		createUser("btmo", "分院TMO", "btmo", "1", "btmo@activiti.org", "", Arrays.asList("btmo", "user"), null);
		createUser("bfinance", "分院财务部", "bfinance", "1", "bfinance@activiti.org", "", Arrays.asList("bfinance", "user"),
				null);
		createUser("leader", "总院分管领导", "leader", "1", "leader@activiti.org", "", Arrays.asList("leader", "user"), null);
		createUser("projectmanager", "项目经理", "projectmanager", "1", "projectmanager@activiti.org", "",
				Arrays.asList("projectmanager", "user", "admin"), null);
	}

	protected void createUser(String userId, String firstName, String lastName, String password, String email,
			String imageResource, List<String> groups, List<String> userInfo) {

		if (processEngineConfiguration.getIdentityService().createUserQuery().userId(userId).count() == 0) {

			// Following data can already be set by demo setup script

			User user = processEngineConfiguration.getIdentityService().newUser(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(password);
			user.setEmail(email);
			processEngineConfiguration.getIdentityService().saveUser(user);

			if (groups != null) {
				for (String group : groups) {
					processEngineConfiguration.getIdentityService().createMembership(userId, group);
				}
			}
		}

		// Following data is not set by demo setup script

		// image
		if (imageResource != null) {
			byte[] pictureBytes = IoUtil
					.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
			Picture picture = new Picture(pictureBytes, "image/jpeg");
			processEngineConfiguration.getIdentityService().setUserPicture(userId, picture);
		}

		// user info
		if (userInfo != null) {
			for (int i = 0; i < userInfo.size(); i += 2) {
				processEngineConfiguration.getIdentityService().setUserInfo(userId, userInfo.get(i),
						userInfo.get(i + 1));
			}
		}

	}

	protected void initProcessDefinitions() {

		String deploymentName = "Demo processes";
		List<Deployment> deploymentList = processEngineConfiguration.getRepositoryService().createDeploymentQuery()
				.deploymentName(deploymentName).list();

		if (deploymentList == null || deploymentList.isEmpty()) {
			processEngineConfiguration.getRepositoryService().createDeployment().name(deploymentName)
					.addClasspathResource("org/activiti/explorer/demo/process/createTimersProcess.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.png")
					.addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.png")
					.addClasspathResource("org/activiti/explorer/demo/process/simple-approval.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.png")
					.addClasspathResource("org/activiti/explorer/demo/process/reviewSalesLead.bpmn20.xml").deploy();
		}

		String reportDeploymentName = "Demo reports";
		deploymentList = processEngineConfiguration.getRepositoryService().createDeploymentQuery()
				.deploymentName(reportDeploymentName).list();
		if (deploymentList == null || deploymentList.isEmpty()) {
			processEngineConfiguration.getRepositoryService().createDeployment().name(reportDeploymentName)
					.addClasspathResource(
							"org/activiti/explorer/demo/process/reports/taskDurationForProcessDefinition.bpmn20.xml")
					.addClasspathResource(
							"org/activiti/explorer/demo/process/reports/processInstanceOverview.bpmn20.xml")
					.addClasspathResource(
							"org/activiti/explorer/demo/process/reports/helpdeskFirstLineVsEscalated.bpmn20.xml")
					.addClasspathResource("org/activiti/explorer/demo/process/reports/employeeProductivity.bpmn20.xml")
					.deploy();
		}

	}

	protected void generateReportData() {
		// Report data is generated in background thread

		Thread thread = new Thread(new Runnable() {

			public void run() {

				// We need to temporarily disable the job executor or it would
				// interfere with the process execution
				if (processEngineConfiguration.isAsyncExecutorEnabled()
						&& processEngineConfiguration.getAsyncExecutor() != null) {
					processEngineConfiguration.getAsyncExecutor().shutdown();
				} else if (processEngineConfiguration.isAsyncExecutorEnabled() == false
						&& processEngineConfiguration.getJobExecutor() != null) {
					processEngineConfiguration.getJobExecutor().shutdown();
				}

				Random random = new Random();

				Date now = new Date(new Date().getTime() - (24 * 60 * 60 * 1000));
				processEngineConfiguration.getClock().setCurrentTime(now);

				for (int i = 0; i < 50; i++) {

					if (random.nextBoolean()) {
						processEngineConfiguration.getRuntimeService().startProcessInstanceByKey("fixSystemFailure");
					}

					if (random.nextBoolean()) {
						processEngineConfiguration.getIdentityService().setAuthenticatedUserId("kermit");
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("customerName", "testCustomer");
						variables.put("details", "Looks very interesting!");
						variables.put("notEnoughInformation", false);
						processEngineConfiguration.getRuntimeService().startProcessInstanceByKey("reviewSaledLead",
								variables);
					}

					if (random.nextBoolean()) {
						processEngineConfiguration.getRuntimeService().startProcessInstanceByKey("escalationExample");
					}

					if (random.nextInt(100) < 20) {
						now = new Date(now.getTime() - ((24 * 60 * 60 * 1000) - (60 * 60 * 1000)));
						processEngineConfiguration.getClock().setCurrentTime(now);
					}
				}

				List<Job> jobs = processEngineConfiguration.getManagementService().createJobQuery().list();
				for (int i = 0; i < jobs.size() / 2; i++) {
					processEngineConfiguration.getClock().setCurrentTime(jobs.get(i).getDuedate());
					processEngineConfiguration.getManagementService().executeJob(jobs.get(i).getId());
				}

				List<Task> tasks = processEngineConfiguration.getTaskService().createTaskQuery().list();
				while (!tasks.isEmpty()) {
					for (Task task : tasks) {

						if (task.getAssignee() == null) {
							String assignee = random.nextBoolean() ? "kermit" : "fozzie";
							processEngineConfiguration.getTaskService().claim(task.getId(), assignee);
						}

						processEngineConfiguration.getClock().setCurrentTime(
								new Date(task.getCreateTime().getTime() + random.nextInt(60 * 60 * 1000)));

						processEngineConfiguration.getTaskService().complete(task.getId());
					}

					tasks = processEngineConfiguration.getTaskService().createTaskQuery().list();
				}

				processEngineConfiguration.getClock().reset();

				if (processEngineConfiguration.isAsyncExecutorEnabled()
						&& processEngineConfiguration.getAsyncExecutor() != null) {
					processEngineConfiguration.getAsyncExecutor().start();
				} else if (processEngineConfiguration.isAsyncExecutorEnabled() == false
						&& processEngineConfiguration.getJobExecutor() != null) {
					processEngineConfiguration.getJobExecutor().start();
				}
				LOGGER.info("Demo report data generated");
			}

		});
		thread.start();
	}

	protected void initModelData() {
		createModelData("Demo model", "This is a demo model", "org/activiti/explorer/demo/model/test.model.json");
	}

	protected void createModelData(String name, String description, String jsonFile) {
		List<Model> modelList = processEngineConfiguration.getRepositoryService().createModelQuery()
				.modelName("Demo model").list();

		if (modelList == null || modelList.isEmpty()) {

			Model model = processEngineConfiguration.getRepositoryService().newModel();
			model.setName(name);

			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("description", description);
			model.setMetaInfo(modelObjectNode.toString());

			processEngineConfiguration.getRepositoryService().saveModel(model);

			try {
				InputStream svgStream = this.getClass().getClassLoader()
						.getResourceAsStream("org/activiti/explorer/demo/model/test.svg");
				processEngineConfiguration.getRepositoryService().addModelEditorSourceExtra(model.getId(),
						IOUtils.toByteArray(svgStream));
			} catch (Exception e) {
				LOGGER.warn("Failed to read SVG", e);
			}

			try {
				InputStream editorJsonStream = this.getClass().getClassLoader().getResourceAsStream(jsonFile);
				processEngineConfiguration.getRepositoryService().addModelEditorSource(model.getId(),
						IOUtils.toByteArray(editorJsonStream));
			} catch (Exception e) {
				LOGGER.warn("Failed to read editor JSON", e);
			}
		}
	}

	public boolean isCreateDemoUsers() {
		return createDemoUsers;
	}

	public void setCreateDemoUsers(boolean createDemoUsers) {
		this.createDemoUsers = createDemoUsers;
	}

	public boolean isCreateDemoDefinitions() {
		return createDemoDefinitions;
	}

	public void setCreateDemoDefinitions(boolean createDemoDefinitions) {
		this.createDemoDefinitions = createDemoDefinitions;
	}

	public boolean isCreateDemoModels() {
		return createDemoModels;
	}

	public void setCreateDemoModels(boolean createDemoModels) {
		this.createDemoModels = createDemoModels;
	}

	public boolean isCreateDemoReports() {
		return createDemoReports;
	}

	public void setCreateDemoReports(boolean createDemoReports) {
		this.createDemoReports = createDemoReports;
	}

}
