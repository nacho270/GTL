package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.portal.UsuarioSistema;

@Local
public interface UsuarioSistemaDAOLocal extends DAOLocal<UsuarioSistema, Integer> {

	public UsuarioSistema login(String usuario, String password);
	public Boolean existeUsuario(String usuario);
	public UsuarioSistema esPasswordDeAdministrador(String pass);
	public boolean yaExisteClave(String pass);
	public UsuarioSistema getUsuarioSistemaByUsername(String usrName);
	public UsuarioSistema getByNombre(String nombre);

}
