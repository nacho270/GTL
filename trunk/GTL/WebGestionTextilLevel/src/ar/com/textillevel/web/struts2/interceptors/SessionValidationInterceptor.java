package ar.com.textillevel.web.struts2.interceptors;

import java.util.Map;

import ar.com.textillevel.web.util.SessionConstants;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SessionValidationInterceptor implements Interceptor{

	private static final long serialVersionUID = 3815327373026495637L;

	public void destroy() {
		
	}

	public void init() {
		
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		if(session!=null && session.containsKey(SessionConstants.SESSION_USUARIO_SISTEMA)){
			return invocation.invoke();
		}
		return "login"; //Global-Result -> ver struts.xml
	}
}
