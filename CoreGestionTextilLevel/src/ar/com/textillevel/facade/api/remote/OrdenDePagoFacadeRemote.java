package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;

@Remote
public interface OrdenDePagoFacadeRemote {

	public Integer getNewNroOrdenDePago();
	public OrdenDePago guardarOrdenDePago(OrdenDePago orden, String usuario) throws CLException;
	public OrdenDePago getOrdenDePagoByNroOrdenEager(Integer nroOrden);
	public void confirmarOrden(OrdenDePago op, String usrName);
	public OrdenDePago actualizarOrden(OrdenDePago orden, String usrName);
	public void borrarOrdenDePago(OrdenDePago orden, String usuario) throws CLException;
	public OrdenDePago editarOrdenDePago(OrdenDePago orden, String usrName) throws CLException;
}
