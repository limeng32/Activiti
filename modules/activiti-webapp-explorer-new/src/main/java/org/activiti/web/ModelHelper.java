package org.activiti.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.MyBusinessModelService;
import org.activiti.rest.editor.model.ModelFace;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ModelHelper implements ModelFace {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelHelper.class);

	public static final String MODEL_BUSINESS = "business";

	@Autowired
	private ActReModelService actReModelService;

	@Autowired
	private MyBusinessModelService myBusinessModelService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RepositoryService repositoryService;

	@Override
	public ObjectNode deal(String modelId) {
		ObjectNode modelNode = null;

		Model model = repositoryService.getModel(modelId);

		if (model != null) {
			try {
				if (StringUtils.isNotEmpty(model.getMetaInfo())) {
					ActReModel actReModel_ = new ActReModel();
					actReModel_.setId(modelId);
					actReModel_.setTenantId(null);
					MyBusinessModel myBusinessModel_ = new MyBusinessModel();
					myBusinessModel_.setActReModel(actReModel_);
					MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(myBusinessModel_);

					modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
					if (myBusinessModel != null) {
						modelNode.put("business", myBusinessModel.getBusinessId());
					}
				} else {
					modelNode = objectMapper.createObjectNode();
					modelNode.put(ModelDataJsonConstants.MODEL_NAME, model.getName());
				}
				modelNode.put(ModelDataJsonConstants.MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) objectMapper
						.readTree(new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
				modelNode.put("model", editorJsonNode);

			} catch (Exception e) {
				LOGGER.error("Error creating model JSON", e);
				throw new ActivitiException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}

	@Override
	public void dealSave(String modelId, MultiValueMap<String, String> values) {
		try {
			System.out.println("1::" + modelId);
			String business = values.getFirst("business");
			Model model = repositoryService.getModel(modelId);

			System.out.println("1::" + model.getCategory());
			System.out.println("2::" + model.getDeploymentId());
			System.out.println("3::" + model.getId());
			System.out.println("4::" + model.getKey());
			System.out.println("5::" + model.getMetaInfo());
			System.out.println("6::" + model.getName());
			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

			modelJson.put(ModelDataJsonConstants.MODEL_NAME, values.getFirst("name"));
			modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, values.getFirst("description"));
			modelJson.put(MODEL_BUSINESS, business);
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
			MyBusinessModel mbm = new MyBusinessModel();
			mbm.setBusinessId(business);
			ActReModel actReModel = new ActReModel(model);
			System.out.println("::" + actReModel.getId());
			mbm.setActReModel(actReModel);
			myBusinessModelService.insert(mbm);
		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}

	}

}
