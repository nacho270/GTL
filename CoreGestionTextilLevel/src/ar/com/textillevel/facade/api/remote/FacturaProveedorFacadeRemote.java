package ar.com.textillevel.facade.api.remote;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ivacompras.IVAComprasTO;

@Remote
public interface FacturaProveedorFacadeRemote {

	public FacturaProveedor ingresarFactura(FacturaProveedor factura, String usuario);

	public FacturaProveedor getByIdEager(Integer idFactura);

	public List<FacturaProveedor> getFacturaListByParams(Integer idProveedor,List<Integer> idPrecioMatPrimaList, Date fechaDesde, Date fechaHasta);

	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor);
	
	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor, List<Integer> idsExcluidos, BigDecimal montoHasta);

	public List<FacturaProveedor> getFacturasParaNotasCredito(Integer idProveedor);

	public void confirmarFactura(FacturaProveedor factura, String usrName);

	public void borrarFactura(FacturaProveedor factura, String usuario) throws ValidacionException;

	public IVAComprasTO calcularIVACompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor);

	public boolean existeNroFacturaByProveedor(Integer idFactura, String nroFactura, Integer idProveedor);

	public void checkEliminacionOrEdicionFacturaProveedor(FacturaProveedor factura) throws ValidacionException;

	public List<FacturaProveedor> getFacturasConMateriaPrimaIBC(Integer idProveedor);
	
}
