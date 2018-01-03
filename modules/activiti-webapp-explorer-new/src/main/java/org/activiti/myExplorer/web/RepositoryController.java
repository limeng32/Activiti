package org.activiti.myExplorer.web;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.myExplorer.condition.ProcessReturnCondition;
import org.activiti.myExplorer.model.PageInfo;
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

import indi.mybatis.flying.pagination.Page;
import indi.mybatis.flying.pagination.PageParam;

@Controller
public class RepositoryController {

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

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/s/listDesigning")
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

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/s/listDeployed")
	public String listDeployed(HttpServletRequest request, HttpServletResponse response, ModelMap mm,
			@RequestParam(value = "count") int count) {
		count = count > 0 ? count - 1 : count;
		int first = count * 5;
		List<ProcessDefinition> l = repositoryService.createProcessDefinitionQuery().latestVersion().listPage(first, 5);
		int c = (int) (repositoryService.createProcessDefinitionQuery().latestVersion().count());
		int maxPageNum = c / 5;
		int pageNo = count + 1;
		PageInfo<ProcessDefinition> pi = new PageInfo<>(l, pageNo, maxPageNum, c);
		mm.addAttribute("_content", pi);
		return UNIQUE_PATH;
	}
}
