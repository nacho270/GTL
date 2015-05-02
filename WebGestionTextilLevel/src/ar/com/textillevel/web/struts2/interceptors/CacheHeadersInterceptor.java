package ar.com.textillevel.web.struts2.interceptors;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class CacheHeadersInterceptor implements Interceptor{

	private static final long serialVersionUID = -5686977106393826500L;

	private static final Logger logger = Logger.getLogger(CacheHeadersInterceptor.class);
	
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.info("Intercepting!!");
		ActionContext ac = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) ac.get(StrutsStatics.HTTP_RESPONSE);
		if(response!=null){
			response.setHeader("Cache-control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");
		}
		return invocation.invoke();
	}

	public void destroy() {
		logger.info("Interceptor destroy");
	}

	public void init() {
		logger.info("Interceptor init");
	}
}
