package ar.com.fwcommon.templates.main.login;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.entidades.Modulo;

public abstract class FWLoginManager implements ILoginManager {

	protected int idAplicacion;
	protected List<Modulo> modulosUsuario;
	protected List<Modulo> modulosAplicacion;

	protected FWLoginManager(int idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public abstract List<Modulo> getModulosUsuario() throws FWException;

	public abstract List<Modulo> getModulosAplicacion() throws FWException;

}