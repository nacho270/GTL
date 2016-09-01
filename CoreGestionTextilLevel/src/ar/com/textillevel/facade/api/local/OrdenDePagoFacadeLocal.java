package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;

@Local
public interface OrdenDePagoFacadeLocal {
	public OrdenDePago getByIdEager(Integer idODP);
	public OrdenDePago getOrdenDePagoByNroOrdenEager(Integer nroOrden);
	public void marcarEntregada(String numero);
	public void reingresar(String numero);
}
