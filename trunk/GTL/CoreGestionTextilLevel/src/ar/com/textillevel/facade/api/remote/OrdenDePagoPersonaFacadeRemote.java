package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;

@Remote
public interface OrdenDePagoPersonaFacadeRemote {
	public Integer getProximoNumeroOrden();
	public OrdenDePagoAPersona guardarOrden(OrdenDePagoAPersona orden, String usrName) throws CLException;
	public OrdenDePagoAPersona getOrdenByNro(Integer nroOrden);
	public OrdenDePagoAPersona editarOrden(OrdenDePagoAPersona orden, String usuario) throws CLException;
	public void eliminarOrden(OrdenDePagoAPersona orden, String usuario) throws CLException;
	public void confirmarOrden(OrdenDePagoAPersona ordenDePago, String usuario);
}
