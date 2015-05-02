package ar.com.textillevel.web.struts2.actions;

import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

@InterceptorRef(value="stack")
@ParentPackage(value="acciones")
@Namespace(value = "/struts/actions")
@Action(value = "logout", results = { 
		@Result(name = ActionSupport.SUCCESS, location = "/html/login.html", type="redirect") 
})
public class LogoutController extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = -150350890729615346L;

	private SessionMap<String, Object> session;

	@Override
	public String execute() throws Exception {
		if(session!=null){
			session.invalidate();
		}
		return ActionSupport.SUCCESS;
	}

	public void setSession(Map<String, Object> ses) {
		this.session = (SessionMap<String, Object>) ses;
	}
}
