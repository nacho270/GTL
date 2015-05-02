package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;

@Local
public interface CorreccionFacturaProveedorFacadeLocal {
	public CorreccionFacturaProveedor actualizarCorreccion(CorreccionFacturaProveedor correccion);
	public CorreccionFacturaProveedor guardarCorreccionYGenerarMovimiento(CorreccionFacturaProveedor correccion, String usuario, String obsMovimiento);
	public void borrarCorreccionFacturaProveedor(CorreccionFacturaProveedor correFacturaProveedor, String usuario) throws ValidacionException;
	public CorreccionFacturaProveedor obtenerNotaDeDebitoByCheque(Cheque c);
	public CorreccionFacturaProveedor getCorreccionFacturaByIdEager(Integer id);
}
