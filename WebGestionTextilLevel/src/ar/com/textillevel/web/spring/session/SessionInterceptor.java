package ar.com.textillevel.web.spring.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object returnedValue, Exception exceptionThrown) throws Exception {
		String token = request.getParameter(SessionMap.SESSION_TOKEN_KEY);
		if (exceptionThrown == null) {
			SessionMap.getInstance().updateSession(token);
		} else {
			SessionMap.getInstance().clearSession(token);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		SessionMap.getInstance().validateSession(request.getParameter(SessionMap.SESSION_TOKEN_KEY));
		return true;
	}
}
