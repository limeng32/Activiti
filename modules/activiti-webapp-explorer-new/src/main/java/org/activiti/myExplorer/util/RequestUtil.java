package org.activiti.myExplorer.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		String ret = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookieName.equals(cookie.getName())) {
				ret = cookie.getValue();
				break;
			}
		}
		return ret;
	}

}
