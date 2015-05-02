package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;

@Remote
public interface CorrectorCuentasClientesFacadeRemote {

	public abstract void corregirCuenta(Integer idCliente, String usrName) throws ValidacionException;

}
