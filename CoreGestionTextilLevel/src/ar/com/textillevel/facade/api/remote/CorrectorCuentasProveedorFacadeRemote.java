package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;

@Remote
public interface CorrectorCuentasProveedorFacadeRemote {

	public abstract void corregirCuenta(Integer idProveedor, String usrName) throws ValidacionException;

}
