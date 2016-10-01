package ar.com.fwcommon.templates.main.login;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;

public abstract class EmptyLoginManager extends FWLoginManager {

	public EmptyLoginManager(int idAplicacion) {
		super(idAplicacion);
	}

	public boolean login(String usuario, String password) throws FWException {
		return true;
	}

	@Override
	public abstract List<Modulo> getModulosUsuario() throws FWException;

	@Override
	public List<Modulo> getModulosAplicacion() throws FWException {
		return null;
	}

}