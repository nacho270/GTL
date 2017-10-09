package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.to.CorreccionFacturaMobTO;
import ar.com.textillevel.entidades.enums.ETipoCorreccionFactura;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Local
public interface CorreccionFacadeLocal {

	public CorreccionFactura guardarCorreccionYGenerarMovimiento(CorreccionFactura correccion, String usuario) throws ValidacionException, ValidacionExceptionSinRollback;

	public NotaDebito getNotaDebitoByCheque(Cheque cheque);

	public CorreccionFacturaMobTO getCorreccionMobById(Integer idCorreccion);

	public CorreccionFacturaMobTO getCorreccionMobByNumero(Integer idNumero, ETipoCorreccionFactura tipoCorreccion, Integer nroSucursal);

	public NotaCredito generarNCPorBorradoDibujo(DibujoEstampado dibujoEstampado, String user) throws ValidacionException, ValidacionExceptionSinRollback;

}
