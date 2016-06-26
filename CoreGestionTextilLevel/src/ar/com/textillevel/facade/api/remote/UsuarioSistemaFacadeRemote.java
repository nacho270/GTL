package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.portal.UsuarioSistema;

@Remote
public interface UsuarioSistemaFacadeRemote {
	public List<UsuarioSistema> getAllOrderByName();
	public UsuarioSistema save(UsuarioSistema usuario) throws ValidacionException;
	public void remove (UsuarioSistema usuario);
	public UsuarioSistema login(String usuario, String password);
	public Boolean existeUsuario(String usuario);
	public UsuarioSistema esPasswordDeAdministrador(String pass);
	public UsuarioSistema getById(Integer idUsuarioSistema);
}
