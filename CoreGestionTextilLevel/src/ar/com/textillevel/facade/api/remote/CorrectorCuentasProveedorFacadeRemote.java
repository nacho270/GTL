package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;

@Remote
public interface CorrectorCuentasProveedorFacadeRemote {

	public abstract void corregirCuenta(Integer idProveedor, String usrName) throws ValidacionException;

}
