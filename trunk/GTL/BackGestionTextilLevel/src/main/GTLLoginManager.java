package main;

import java.util.ArrayList;
import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.entidades.Modulo;
import ar.clarin.fwjava.templates.main.login.CLLoginManager;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GTLLoginManager extends CLLoginManager {

	protected GTLLoginManager(int idAplicacion) {
		super(idAplicacion);
	}

	@Override
	public List<Modulo> getModulosAplicacion() throws CLException {
		return null;
	}

	@Override
	public List<Modulo> getModulosUsuario() throws CLException {
		List<Modulo> modulos = new ArrayList<Modulo>();
		List<ar.com.textillevel.entidades.portal.Modulo> modulosPerfil = GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getModulos();
		for(ar.com.textillevel.entidades.portal.Modulo m : modulosPerfil){
			if(!m.getTrigger() && !m.getServicio()){
				modulos.add(new Modulo(m.getId(), m.getNombre(), m.getClassName()));
			}
		}
		return modulos;
	}

	public boolean login(String usuario, String password) throws CLException {
		UsuarioSistema usuarioSistema = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).login(usuario, password);
		if(usuarioSistema == null){
			return false;
		}
		GTLGlobalCache.getInstance().setUsuarioSistema(usuarioSistema);
		GTLGlobalCache.getInstance().setArticuloDefault(GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class).getById(1));//AP-240
		return true;
	}
}
