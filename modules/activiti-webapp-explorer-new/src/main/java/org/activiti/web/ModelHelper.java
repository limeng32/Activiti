package org.activiti.web;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.myExplorer.persist.ActReModel;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.activiti.myExplorer.service.ActReModelService;
import org.activiti.myExplorer.service.MyBusinessModelService;
import org.activiti.rest.editor.model.ModelFace;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ModelHelper implements ModelFace {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelHelper.class);

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

}
