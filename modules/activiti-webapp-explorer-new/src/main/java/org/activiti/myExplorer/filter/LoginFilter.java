package org.activiti.myExplorer.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.ApplicationContextProvider;
import org.activiti.account.exception.ActivitiAccountExceptionEnum;
import org.activiti.myExplorer.model.CommonReturn;
import org.activiti.myExplorer.model.RetCode;
import org.activiti.myExplorer.util.RequestUtil;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings("unchecked")
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Just make sonarqube happy
	}

	private static RedisTemplate<String, Object> redisTemplateJson;

	static {
		redisTemplateJson = (RedisTemplate<String, Object>) ApplicationContextProvider.getApplicationContext()
				.getBean("redisTemplateJson", RedisTemplate.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());
		if (path.startsWith("/s/noSession")) {
			chain.doFilter(request, response);
		} else {
			String _uid = RequestUtil.getCookieValue(req, "uid");
			if (_uid == null || redisTemplateJson.opsForValue().get(_uid) == null) {
//				CommonReturn cr = new CommonReturn(RetCode.NOSESSION, ActivitiAccountExceptionEnum.NoSession.name());
//				doWriteRespAndFlush(response, JSON.toJSONString(cr, SerializerFeature.WriteEnumUsingToString));
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
		// Just make sonarqube happy
	}

	private static void doWriteResp(ServletResponse response, String toWriteStr) throws IOException {
		String encoding = "UTF-8";
		String contentType = "text/html;charset=utf-8";
		response.setContentType(contentType);
		response.setCharacterEncoding(encoding);

		response.getOutputStream().write(toWriteStr.getBytes(encoding));
	}

	private static void doWriteRespAndFlush(ServletResponse response, String toWriteStr) throws IOException {
		doWriteResp(response, toWriteStr);
		response.flushBuffer();
	}
}
