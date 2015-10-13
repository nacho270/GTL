package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;

@Remote
public interface CorrectorCuentasClientesFacadeRemote {

	public abstract void corregirCuenta(Integer idCliente, String usrName) throws ValidacionException;

}
