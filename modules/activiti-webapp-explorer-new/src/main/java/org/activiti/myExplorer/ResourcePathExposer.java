package org.activiti.myExplorer;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class ResourcePathExposer implements ServletContextAware {
	private ServletContext servletContext;

	private String resourceRoot;

	private String tag = "201612130946";

	public void init() {
		resourceRoot = "/resources-" + tag;
		getServletContext().setAttribute("resourceRoot", getServletContext().getContextPath() + resourceRoot);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getResourceRoot() {
		return resourceRoot;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}