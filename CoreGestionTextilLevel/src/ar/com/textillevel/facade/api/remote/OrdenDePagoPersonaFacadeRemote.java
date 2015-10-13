package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Remote
public interface OrdenDePagoPersonaFacadeRemote {
	public Integer getProximoNumeroOrden();
	public OrdenDePagoAPersona guardarOrden(OrdenDePagoAPersona orden, String usrName) throws FWException;
	public OrdenDePagoAPersona getOrdenByNro(Integer nroOrden);
	public OrdenDePagoAPersona editarOrden(OrdenDePagoAPersona orden, String usuario) throws FWException;
	public void eliminarOrden(OrdenDePagoAPersona orden, String usuario) throws FWException;
	public void confirmarOrden(OrdenDePagoAPersona ordenDePago, String usuario);
}
