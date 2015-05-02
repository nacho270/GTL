package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.enums.EPosicionIVA;

@Local
public interface FacturaFacadeLocal {
	
	public Factura guardarFacturaYGenerarMovimiento(Factura factura, String usuario) throws CLException;
	public Integer getProximoNroFactura(EPosicionIVA posIva);
	public Factura getByNroFacturaConItems(Integer nroFactura);
	public Factura getByIdEager(Integer id);
}
