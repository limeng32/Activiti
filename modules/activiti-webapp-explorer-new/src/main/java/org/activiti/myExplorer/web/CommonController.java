package org.activiti.myExplorer.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.ActReProcdefService;
import org.activiti.myExplorer.service.MyBusinessProcdefService;
import org.activiti.myExplorer.service.TestService;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class CommonController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private TestService testServie;

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private ActReProcdefService actReProcdefService;
	
	@Autowired
	private MyBusinessProcdefService myBusinessProcdefService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RepositoryService repositoryService;

	public static final String MODEL_BUSINESS = "business";

	@RequestMapping(method = { RequestMethod.GET }, value = "/hello")
	public String hello(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReModel ac = new ActReModel();
		ac.setName("Mail Task  1");
		Collection<ActReModel> c = actReModelService.selectAll(ac);
		System.out.println("hello!" + c.size());
		for (ActReModel temp : c) {
			System.out.println("!" + temp.getMetaInfo() + "!" + temp.getName() + "!" + temp.getVersion() + "!"
					+ temp.getLastUpdateTime());
		}
		return "index";
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/world")
	public String world(HttpServletRequest request, HttpServletResponse response, ModelMap mm) {
		ActReProcdef arp = new ActReProcdef();
		arp.setKey("Mail_task_1");
		Collection<ActReProcdef> c = actReProcdefService.selectAll(arp);
		System.out.println("world!" + c.size());
		for (ActReProcdef temp : c) {
			System.out.println("!" + temp.getId() + "!" + temp.getName() + "!" + temp.getVersion() + "!"
					+ temp.getHasGraphicalNotation() + "!" + temp.getHasStartFormKey());
		}
		return "index";
	}

	@RequestMapping(value = "/model-new/{modelId}/save", method = { RequestMethod.PUT })
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {
		try {
			System.out.println("::"+modelId);
			Model model = repositoryService.getModel(modelId);
			System.out.println("1::"+model.getCategory());
			System.out.println("2::"+model.getDeploymentId());
			System.out.println("3::"+model.getId());
			System.out.println("4::"+model.getKey());
			System.out.println("5::"+model.getMetaInfo());
			System.out.println("6::"+model.getName());
			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

			modelJson.put(ModelDataJsonConstants.MODEL_NAME, values.getFirst("name"));
			modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, values.getFirst("description"));
			modelJson.put(MODEL_BUSINESS, values.getFirst("business"));
			model.setMetaInfo(modelJson.toString());
			model.setName(values.getFirst("name"));

			repositoryService.saveModel(model);

			repositoryService.addModelEditorSource(model.getId(), values.getFirst("json_xml").getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(values.getFirst("svg_xml").getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			repositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();

		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}
}
