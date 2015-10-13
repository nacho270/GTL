package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface CorreccionFacturaProveedorDAOLocal extends DAOLocal<CorreccionFacturaProveedor, Integer> {

	public CorreccionFacturaProveedor getByIdEager(Integer id);

	public List<NotaDebitoProveedor> getNotasDeDebitoImpagas(Integer idProveedor);

	public List<NotaCreditoProveedor> getNotasCreditoNoUsadas(Integer idProveedor);

	public List<CorreccionFacturaProveedor> getCorreccionListByFactura(FacturaProveedor factura);

	public List<Object[]> calcularInfoFacturasIvaCompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor);

	public boolean existeNroCorreccionByProveedor(Integer idCorreccion, String nroCorreccion, Integer idProveedor);

	public CorreccionFacturaProveedor obtenerNotaDeDebitoByCheque(Cheque c);

	public List<NotaCreditoProveedor> getAllNotaCreditoList(Integer idProveedor);

	public List<NotaDebitoProveedor> getAllNotaDebitoList(Integer idProveedor);

}
