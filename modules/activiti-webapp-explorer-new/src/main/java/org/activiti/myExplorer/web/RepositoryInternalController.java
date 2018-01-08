package org.activiti.myExplorer.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.myExplorer.condition.ProcessReturnCondition;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.PageInfo;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.persist.ProcessReturn;
import org.activiti.myExplorer.persist.User;
import org.activiti.myExplorer.service.ProcessReturnService;
import org.activiti.myExplorer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import indi.mybatis.flying.pagination.Page;
import indi.mybatis.flying.pagination.PageParam;

@Controller
public class RepositoryInternalController {

	@Autowired
	private ProcessReturnService processReturnService;

	@Autowired
	private UserService userService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	public static final String UNIQUE_PATH = "__unique_path";

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/currentUser")
	public String currentUser(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		User user = userService.select(1);
		mm.addAttribute("_content", user);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/fake_list")
	public String fakeList(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		ProcessReturnCondition pc = new ProcessReturnCondition();
		pc.setLimiter(new PageParam(count < 1 ? 1 : count, 5));
		Collection<ProcessReturn> c = processReturnService.selectAll(pc);
		Page<ProcessReturn> p = new Page<>(c, pc.getLimiter());
		mm.addAttribute("_content", p);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listDesigning")
	public String listDesigning(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		count = count > 0 ? count - 1 : count;
		int first = count * 5;
		List<Model> l = repositoryService.createModelQuery().listPage(first, 5);
		int c = (int) (repositoryService.createModelQuery().count());
		int maxPageNum = c / 5;
		int pageNo = count + 1;
		PageInfo<Model> pi = new PageInfo<>(l, pageNo, maxPageNum, c);
		mm.addAttribute("_content", pi);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listDeployed")
	public String listDeployed(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		count = count > 0 ? count - 1 : count;
		int first = count * 5;
		List<ProcessDefinition> l = repositoryService.createProcessDefinitionQuery().latestVersion().listPage(first, 5);
		for (ProcessDefinition p : l) {
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(p.getDeploymentId())
					.singleResult();
			p.setDeployment(deployment);
		}
		int c = (int) (repositoryService.createProcessDefinitionQuery().latestVersion().count());
		int maxPageNum = c / 5;
		int pageNo = count + 1;
		PageInfo<ProcessDefinition> pi = new PageInfo<>(l, pageNo, maxPageNum, c);
		mm.addAttribute("_content", pi);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/doStart")
	public String doStart(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) {
		CommonReturn cr = null;
		try {
			runtimeService.startProcessInstanceById(id);
			cr = new CommonReturn(RetCode.SUCCESS, "流程 " + id + " 已启动了一个实例");
		} catch (Exception e) {
			cr = new CommonReturn(RetCode.EXCEPTION, "流程 " + id + " 启动时发生异常：" + e.getMessage());
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/doDeploy")
	public String doDeploy(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) {
		Model model = repositoryService.getModel(id);
		ObjectNode modelNode = null;
		CommonReturn cr = null;
		try {
			modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(id));
			DefaultProcessDiagramGenerator dpdg = new DefaultProcessDiagramGenerator();
			BpmnModel bpmnMode = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			dpdg.generatePngDiagram(bpmnMode);
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnMode);

			String processName = model.getName() + ".bpmn20.xml";
			repositoryService.createDeployment().name(model.getName()).addString(processName, new String(bpmnBytes))
					.deploy();
			cr = new CommonReturn(RetCode.SUCCESS, "流程 " + model.getName() + " 已经部署");
		} catch (Exception e) {
			cr = new CommonReturn(RetCode.EXCEPTION, "ID 为 " + id + " 的流程部署时发生异常：" + e.getMessage());
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listShowImg")
	public void listShowImg(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") String id) {
		response.setContentType("image/png");
		try {
			ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(id));
			DefaultProcessDiagramGenerator dpdg = new DefaultProcessDiagramGenerator();
			BpmnModel bpmnMode = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			try (OutputStream stream = response.getOutputStream();
					InputStream is = dpdg.generatePngDiagram(bpmnMode);) {

				byte[] b = new byte[2048];
				int length = 0;
				while ((length = is.read(b)) > 0) {
					stream.write(b, 0, length);
				}
				stream.flush();
			}
		} catch (Exception e) {
			// make sonar happy
		}
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listDevelopedImg")
	public void listDevelopedImg(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id") String id) {
		response.setContentType("image/png");
		try (InputStream is = repositoryService.getProcessDiagram(id);
				OutputStream stream = response.getOutputStream();) {
			byte[] b = new byte[2048];
			int length = 0;
			while ((length = is.read(b)) > 0) {
				stream.write(b, 0, length);
			}
			stream.flush();
		} catch (IOException e) {
			// make sonar happy
		}
	}
}
