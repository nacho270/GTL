package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.dao.api.local.UsuarioSistemaDAOLocal;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.UsuarioSistemaFacadeLocal;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.util.PortalUtils;

@Stateless
public class UsuarioSistemaFacade implements UsuarioSistemaFacadeRemote,UsuarioSistemaFacadeLocal{

	@EJB
	private UsuarioSistemaDAOLocal usuarioDao;
	
	public List<UsuarioSistema> getAllOrderByName() {
		return usuarioDao.getAllOrderBy("usrName");
	}

	public void remove(UsuarioSistema usuario) {
		usuarioDao.removeById(usuario.getId());
	}

	public UsuarioSistema save(UsuarioSistema usuario) throws ValidacionException {
		usuario.setPassword(PortalUtils.getHash(usuario.getPassword(),"MD5"));
		if(usuarioDao.yaExisteClave(usuario.getPassword())){
			throw new ValidacionException(EValidacionException.CLAVE_YA_EN_USO.getInfoValidacion());
		}
		return usuarioDao.save(usuario);
	}

	public UsuarioSistema login(String usuario, String password) {
		password = PortalUtils.getHash(password,"MD5");
		return usuarioDao.login(usuario,password);
	}
	
	public UsuarioSistema loginSinHashear(String usuario, String password) {
		return usuarioDao.login(usuario,password);
	}

	public Boolean existeUsuario(String usuario) {
		return usuarioDao.existeUsuario(usuario);
	}
	
	public UsuarioSistema esPasswordDeAdministrador(String pass){
		return usuarioDao.esPasswordDeAdministrador(PortalUtils.getHash(pass,"MD5"));
	}

	public UsuarioSistema getByNombre(String nombre) {
		return usuarioDao.getByNombre(nombre);
	}

	public UsuarioSistema getById(Integer idUsuarioSistema) {
		return usuarioDao.getById(idUsuarioSistema);
	}

	public Integer getProximoCodigoUsuario() {
		return usuarioDao.getProximoCodigoUsuario();
	}
}
