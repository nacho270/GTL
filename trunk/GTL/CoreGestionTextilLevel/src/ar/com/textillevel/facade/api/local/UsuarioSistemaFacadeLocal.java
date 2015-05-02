package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.portal.UsuarioSistema;

@Local
public interface UsuarioSistemaFacadeLocal {
	public UsuarioSistema getByNombre(String nombre);
	public UsuarioSistema login(String usuario, String password);
	public UsuarioSistema loginSinHashear(String usuario, String password);
}
