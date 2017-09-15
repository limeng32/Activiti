package org.activiti.myExplorer.service;

import java.util.List;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.myExplorer.persist.ActReProcdef;
import org.activiti.myExplorer.persist.MyBusinessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

	@Autowired
	private ActReProcdefService actReProcdefService;

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
					.deploymentName(myBusinessModel.getActReModel().getName()).orderByDeploymentId().desc()
					.listPage(0, 1);
			if (deploymentC.size() > 0) {
				Deployment deployment = deploymentC.get(0);
				return deployment;
			}
		}
		return null;
	}

	public ActReProcdef getActReProcdef(String businessId) {
		Deployment deployment = getDeployment(businessId);
		if (deployment != null && deployment.getId() != null) {
			ActReProcdef arp_ = new ActReProcdef();
			arp_.setDeploymentId(deployment.getId());
			ActReProcdef actReProcdef = actReProcdefService.selectOne(arp_);
			return actReProcdef;
		}
		return null;
	}
}
