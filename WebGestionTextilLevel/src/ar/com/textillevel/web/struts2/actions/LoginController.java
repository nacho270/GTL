package ar.com.textillevel.web.struts2.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.web.util.BeanHelper;
import ar.com.textillevel.web.util.SessionConstants;

import com.opensymphony.xwork2.ActionSupport;

@InterceptorRef(value="stack")
@ParentPackage(value="acciones")
@Namespace(value = "/struts/actions")
@Action(value = "login", results = { 
		@Result(name = ActionSupport.SUCCESS, location = "/jsp/index.jsp"), 
		@Result(name = ActionSupport.ERROR, type="redirect", location = "/html/login.html?error=${error}") 
})
public class LoginController extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = -150350890729615346L;

	private String usuario;
	private String password;
	private int error;
	
	private SessionMap<String, Object> session;

	@Override
	public String execute() throws Exception {
		if(session!=null && session.containsKey(SessionConstants.SESSION_USUARIO_SISTEMA)){
			return ActionSupport.SUCCESS;
		}
		
		if(StringUtil.isNullOrEmpty(getUsuario()) || StringUtil.isNullOrEmpty(getPassword())){
			setError(2);
			return ActionSupport.ERROR;
		}
		
		UsuarioSistema us = BeanHelper.getUsuarioSistemaFacade().login(getUsuario(), getPassword());
		if(us == null){
			setError(1);
			return ActionSupport.ERROR;
		}
		List<Modulo> modulos = us.getPerfil().getModulos();
		Map<String, List<Modulo>> mapModulos = new HashMap<String, List<Modulo>>();
		for(Modulo m : modulos){
			if(m.getTrigger().booleanValue()==true){
				continue;
			}
			String nombre = m.getNombre();
			String grupo = nombre.substring(0,nombre.indexOf("-")-1);
			if(mapModulos.get(grupo)==null){
				mapModulos.put(grupo, new ArrayList<Modulo>());
			}
			m.setNombre(nombre.substring(nombre.indexOf("-")+2));
			mapModulos.get(grupo).add(m);
		}
		us.getPerfil().setModulos(null);
		session.put(SessionConstants.SESSION_USUARIO_SISTEMA, us);
		session.put(SessionConstants.SESSION_MODULOS, mapModulos);
		return ActionSupport.SUCCESS;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSession(Map<String, Object> ses) {
		this.session = (SessionMap<String, Object>) ses;
	}
	
	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

}
