package org.activiti.rest.editor.model;

import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ModelFace {

	public ObjectNode deal(String id);

	public void dealSave(String modelId, MultiValueMap<String, String> values);
}
