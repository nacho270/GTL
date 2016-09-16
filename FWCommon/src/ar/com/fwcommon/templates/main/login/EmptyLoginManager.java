package ar.com.fwcommon.templates.main.login;

import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;

public class EmptyLoginManager extends FWLoginManager {

	public EmptyLoginManager(int idAplicacion) {
		super(idAplicacion);
	}

	@Override
	public boolean login(String usuario, String password) throws FWException {
		return true;
	}

	@Override
	public List<Modulo> getModulosUsuario() throws FWException {
		return new ArrayList<Modulo>();
	}

	@Override
	public List<Modulo> getModulosAplicacion() throws FWException {
		return null;
	}

}
