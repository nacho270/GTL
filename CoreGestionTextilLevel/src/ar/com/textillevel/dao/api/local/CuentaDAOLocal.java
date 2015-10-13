package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.CuentaBanco;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaPersona;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;

@Local
public interface CuentaDAOLocal extends DAOLocal<Cuenta, Integer>{

	public CuentaCliente getCuentaClienteByIdCliente(Integer idCliente);

	public CuentaProveedor getCuentaProveedorByIdProveedor(Integer idProveedor);

	public CuentaBanco getCuentaBancoByIdBanco(Integer idBanco);

	public CuentaPersona getCuentaPersonaByIdPersona(Integer idPersona);

}
