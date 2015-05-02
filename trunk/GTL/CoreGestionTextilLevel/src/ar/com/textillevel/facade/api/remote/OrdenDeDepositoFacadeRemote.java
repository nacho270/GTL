package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;

@Remote
public interface OrdenDeDepositoFacadeRemote {
	public OrdenDeDeposito guardarOrden(OrdenDeDeposito orden, String usuario);
	public OrdenDeDeposito getOrdenByNro(Integer nroOrden);
	public Integer getNewNroOrden();
}
