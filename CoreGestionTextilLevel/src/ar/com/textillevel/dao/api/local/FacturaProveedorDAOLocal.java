package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface FacturaProveedorDAOLocal extends DAOLocal<FacturaProveedor, Integer>{

	public FacturaProveedor getByIdEager(Integer idFactura);

	public List<FacturaProveedor> getFacturaListByParams(Integer idProveedor, List<Integer> idPrecioMatPrimaList, Date fechaDesde, Date fechaHasta);

	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor);

	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor, List<Integer> idsExcluidos);

	public List<FacturaProveedor> getFacturasParaNotasCredito(Integer idProveedor);

	public List<Object[]> calcularInfoFacturasIvaCompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor);

	public boolean existeNroFacturaByProveedor(Integer idFactura, String nroFactura, Integer idProveedor);

	public List<FacturaProveedor> getFacturasByRemito(RemitoEntradaProveedor rep);

	public List<FacturaProveedor> getAllByIdProveedorList(Integer idProveedor);

	public List<FacturaProveedor> getFacturasConMateriaPrimaIBC(Integer idProveedor);

}
