package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Local
public interface RemitoEntradaProveedorDAOLocal extends DAOLocal<RemitoEntradaProveedor, Integer> {

	public List<RemitoEntradaProveedor> getRemitosNoAsocByProveedor(Proveedor proveedor);

	public List<RemitoEntradaProveedor> getRemitosByProveedor(Proveedor proveedor);

	public RemitoEntradaProveedor getByIdEager(Integer idRemito);

	public boolean existeNroFacturaByProveedor(Integer idRemitoEntrada, String nroRemitoEntrada, Integer idProveedor);

	public List<RemitoEntradaProveedor> getRemitoEntradaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor);

	public RemitoEntradaProveedor getREProveedorByIdRECliente(Integer idRECliente);

}
