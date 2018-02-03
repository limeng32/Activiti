package org.activiti.myExplorer.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.process.simple.editor.SimpleTableEditorConstants;
import org.activiti.explorer.util.XmlUtil;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.myExplorer.condition.ProcessReturnCondition;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.PageInfo;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.persist.ProcessReturn;
import org.activiti.myExplorer.persist.User;
import org.activiti.myExplorer.service.ProcessReturnService;
import org.activiti.myExplorer.service.UserService;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
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

	private static String handleLikeValue(String parameter) {
		if (parameter.indexOf("%") > -1) {
			parameter = parameter.replaceAll("%", "\\\\%");
		}
		if (parameter.indexOf("_") > -1) {
			parameter = parameter.replaceAll("_", "\\\\_");
		}
		return new StringBuffer("%").append(parameter).append("%").toString();
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listDesigning")
	public String listDesigning(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "name", required = false) String name) {
		count = count > 0 ? count - 1 : count;
		pageSize = pageSize == 0 ? 5 : pageSize;
		int first = count * pageSize;
		Collection<Model> l = null;
		int c = 0;
		if (name == null || "".equals(name)) {
			l = repositoryService.createModelQuery().listPage(first, pageSize);
			c = (int) (repositoryService.createModelQuery().count());
		} else {
			name = handleLikeValue(name);
			l = repositoryService.createModelQuery().modelNameLike(name).listPage(first, pageSize);
			c = (int) (repositoryService.createModelQuery().modelNameLike(name).count());
		}
		int maxPageNum = c / pageSize;
		int pageNo = count + 1;
		PageInfo<Model> pi = new PageInfo<>(l, pageNo, maxPageNum, c, pageSize);
		mm.addAttribute("_content", pi);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/listDeployed")
	public String listDeployed(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count, @RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "name", required = false) String name) {
		count = count > 0 ? count - 1 : count;
		pageSize = pageSize == 0 ? 5 : pageSize;
		int first = count * pageSize;
		Collection<ProcessDefinition> l = null;
		int c = 0;
		if (name == null || "".equals(name)) {
			l = repositoryService.createProcessDefinitionQuery().latestVersion().listPage(first, pageSize);
			c = (int) (repositoryService.createProcessDefinitionQuery().latestVersion().count());
		} else {
			name = handleLikeValue(name);
			l = repositoryService.createProcessDefinitionQuery().processDefinitionKeyLike(name).latestVersion()
					.listPage(first, pageSize);
			c = (int) (repositoryService.createProcessDefinitionQuery().processDefinitionKeyLike(name).latestVersion()
					.count());
		}
		for (ProcessDefinition p : l) {
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(p.getDeploymentId())
					.singleResult();
			p.setDeployment(deployment);
		}
		int maxPageNum = c / pageSize;
		int pageNo = count + 1;
		PageInfo<ProcessDefinition> pi = new PageInfo<>(l, pageNo, maxPageNum, c, pageSize);
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
			cr = new CommonReturn(RetCode.SUCCESS, "流程模型 " + model.getName() + " 已经部署");
		} catch (Exception e) {
			cr = new CommonReturn(RetCode.EXCEPTION, "ID 为 " + id + " 的流程模型部署时发生异常：" + e.getMessage());
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

	@RequestMapping(method = { RequestMethod.POST }, value = "/uploadProcessFile")
	public String uploadProcessFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "Filedata", required = false) MultipartFile file, ModelMap mm) throws IOException {
		CommonReturn cr = null;
		if (!file.isEmpty()) {
			String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
			File newFile = new File(realPath, file.getOriginalFilename());
			FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);

			if (file.getOriginalFilename().endsWith(".bpmn20.xml") || file.getOriginalFilename().endsWith(".bpmn")) {
				XMLStreamReader xtr = null;
				try (ByteArrayOutputStream outputStream = null;) {
					XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
					InputStreamReader in = new InputStreamReader(new FileInputStream(newFile), "UTF-8");
					xtr = xif.createXMLStreamReader(in);
				} catch (XMLStreamException e) {
					// Auto-generated catch block
				}
				BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

				if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null
						|| bpmnModel.getLocationMap().isEmpty()) {
					cr = new CommonReturn(RetCode.EXCEPTION, "流程模型文件无法被解析");
				} else {
					String processName = null;
					if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
						processName = bpmnModel.getMainProcess().getName();
					} else {
						processName = bpmnModel.getMainProcess().getId();
					}

					Model modelData = repositoryService.newModel();
					ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
					modelObjectNode.put("name", processName);
					modelObjectNode.put("revision", 1);
					modelData.setMetaInfo(modelObjectNode.toString());
					modelData.setName(processName);

					repositoryService.saveModel(modelData);

					BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
					ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);

					repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
					cr = new CommonReturn(RetCode.SUCCESS, file.getOriginalFilename() + " 文件导入完成");
				}
			} else {
				cr = new CommonReturn(RetCode.EXCEPTION, "只有 .bpmn 文件和 .bpmn20.xml 文件才能被导入");
			}
		} else {
			cr = new CommonReturn(RetCode.ERROR, "检测到上传文件为空");
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/copyProcess")
	public String copyProcess(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id, @RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description) {
		CommonReturn cr = null;
		Model model = repositoryService.getModel(id);
		if (model == null) {
			cr = new CommonReturn(RetCode.EXCEPTION, "找不到指定模型");
		} else {
			Model newModelData = repositoryService.newModel();
			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
			modelObjectNode.put("name", name);
			modelObjectNode.put("description", description);
			newModelData.setMetaInfo(modelObjectNode.toString());
			newModelData.setName(name);
			repositoryService.saveModel(newModelData);

			repositoryService.addModelEditorSource(newModelData.getId(),
					repositoryService.getModelEditorSource(model.getId()));
			repositoryService.addModelEditorSourceExtra(newModelData.getId(),
					repositoryService.getModelEditorSourceExtra(model.getId()));
			cr = new CommonReturn(RetCode.SUCCESS, model.getName() + " 模型复制完成，新名称为 " + name);
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteModel")
	public String deleteModel(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) {
		CommonReturn cr = null;
		Model model = repositoryService.getModel(id);
		if (model == null) {
			cr = new CommonReturn(RetCode.EXCEPTION, "找不到指定模型");
		} else {
			try {
				repositoryService.deleteModel(id);
				cr = new CommonReturn(RetCode.SUCCESS, model.getName() + " 模型已被删除");
			} catch (Exception e) {
				cr = new CommonReturn(RetCode.EXCEPTION, "ID 为 " + id + " 的流程模型部署时发生异常：" + e.getMessage());
			}
		}
		mm.addAttribute("_content", cr);
		return UNIQUE_PATH;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/exportModel")
	public void exportModel(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "id") String id) throws IOException, UnsupportedEncodingException {
		byte[] bpmnBytes = null;
		String filename = null;
		Model modelData = repositoryService.getModel(id);
		if (SimpleTableEditorConstants.TABLE_EDITOR_CATEGORY.equals(modelData.getCategory())) {
			WorkflowDefinition workflowDefinition = ExplorerApp.get().getSimpleWorkflowJsonConverter()
					.readWorkflowDefinition(repositoryService.getModelEditorSource(modelData.getId()));

			filename = workflowDefinition.getName();
			WorkflowDefinitionConversion conversion = ExplorerApp.get().getWorkflowDefinitionConversionFactory()
					.createWorkflowDefinitionConversion(workflowDefinition);
			bpmnBytes = conversion.getBpmn20Xml().getBytes("utf-8");
		} else {
			JsonNode editorNode = new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
			filename = model.getMainProcess().getId() + ".bpmn20.xml";
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
		}
		response.setContentType("application/x-msdownload");
		/* 因为模型名称肯定是英文和数字，不需要进行转码 */
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		try (ByteArrayInputStream is = new ByteArrayInputStream(bpmnBytes);
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
