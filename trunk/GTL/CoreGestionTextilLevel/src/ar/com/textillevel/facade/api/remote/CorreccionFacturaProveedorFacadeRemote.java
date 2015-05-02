package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;

@Remote
public interface CorreccionFacturaProveedorFacadeRemote {

	public CorreccionFacturaProveedor guardarCorreccionYGenerarMovimiento(CorreccionFacturaProveedor correccion, String usuario, String obsMovimiento);

	public CorreccionFacturaProveedor getCorreccionFacturaByIdEager(Integer id);

	public List<NotaDebitoProveedor> getNotasDeDebitoImpagas(Integer idProveedor);

	public List<NotaCreditoProveedor> getNotasCreditoNoUsadas(Integer idProveedor);

	public void confirmarCorreccion (CorreccionFacturaProveedor correccion, String usrName);

	public CorreccionFacturaProveedor completarDatosCorreccion(CorreccionFacturaProveedor correccion, String usrName);

	public void borrarCorreccionFacturaProveedor(CorreccionFacturaProveedor correFacturaProveedor, String usuario) throws ValidacionException;

	public boolean existeNroCorreccionByProveedor(Integer idCorreccion, String nroCorreccion, Integer idProveedor);

}
