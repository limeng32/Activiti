package org.activiti.myExplorer.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.MyBusinessModel;
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
			String business = values.getFirst("business");
			Model model = repositoryService.getModel(modelId);

			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

			modelJson.put(ModelDataJsonConstants.MODEL_NAME, values.getFirst("name"));
			modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, values.getFirst("description"));
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

			// Deal MyBusinessModel
			saveMyBusinessModel(model, business);
		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}

	}

	public void saveMyBusinessModel(Model model, String business) {
		MyBusinessModel mbm_ = new MyBusinessModel(), mbm2_ = new MyBusinessModel();
		mbm_.setBusinessId(business);
		Collection<MyBusinessModel> myBusinessModelC = myBusinessModelService.selectAll(mbm_);
		for (MyBusinessModel t : myBusinessModelC) {
			myBusinessModelService.delete(t);
		}
		ActReModel arm_ = new ActReModel();
		arm_.setId(model.getId());
		mbm2_.setActReModel(arm_);
		Collection<MyBusinessModel> myBusinessModelC2 = myBusinessModelService.selectAll(mbm2_);
		for (MyBusinessModel t : myBusinessModelC2) {
			myBusinessModelService.delete(t);
		}

		ActReModel actReModel = new ActReModel(model);
		mbm_.setActReModel(actReModel);
		myBusinessModelService.insert(mbm_);
	}
}
