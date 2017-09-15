package org.activiti.myExplorer.service;

import java.util.List;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

	@Autowired
	private MyBusinessModelService myBusinessModelService;

	@Autowired
	private StandaloneProcessEngineConfiguration processEngineConfiguration;

	public Deployment getDeployment(String businessId) {
		MyBusinessModel mbm_ = new MyBusinessModel();
		mbm_.setBusinessId(businessId);
		MyBusinessModel myBusinessModel = myBusinessModelService.selectOne(mbm_);
		if (myBusinessModel != null && myBusinessModel.getActReModel() != null) {
			List<Deployment> deploymentC = processEngineConfiguration.getRepositoryService().createDeploymentQuery()
					.deploymentName(myBusinessModel.getActReModel().getName()).orderByDeploymentId().desc().list();
			if (deploymentC.size() > 0) {
				Deployment deployment = deploymentC.get(0);
				return deployment;
			}
		}
		return null;
	}
}
