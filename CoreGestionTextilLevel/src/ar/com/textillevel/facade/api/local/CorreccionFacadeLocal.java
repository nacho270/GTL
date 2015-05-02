package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.to.CorreccionFacturaMobTO;

@Local
public interface CorreccionFacadeLocal {

	public CorreccionFactura guardarCorreccionYGenerarMovimiento(CorreccionFactura correccion, String usuario) throws CLException;

	public NotaDebito getNotaDebitoByCheque(Cheque cheque);

	public CorreccionFacturaMobTO getCorreccionMobById(Integer idCorreccion);

}
