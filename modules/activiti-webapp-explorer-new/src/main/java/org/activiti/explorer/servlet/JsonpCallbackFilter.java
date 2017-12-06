package org.activiti.explorer.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
public class JsonpCallbackFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(JsonpCallbackFilter.class);

	public void init(FilterConfig fConfig) throws ServletException {
		// Make sonarqube happy
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Map<String, String[]> parms = httpRequest.getParameterMap();

		if (parms.containsKey("callback")) {
			if (log.isDebugEnabled())
				log.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");

			OutputStream out = httpResponse.getOutputStream();

			GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);

			chain.doFilter(request, wrapper);

			// handles the content-size truncation
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write((parms.get("callback")[0] + "(").getBytes());
			outputStream.write(wrapper.getData());
			outputStream.write((");").getBytes());
			byte[] jsonpResponse = outputStream.toByteArray();

			wrapper.setContentType("text/javascript;charset=UTF-8");
			wrapper.setContentLength(jsonpResponse.length);

			out.write(jsonpResponse);

			out.close();

		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		// Make sonarqube happy
	}
}
