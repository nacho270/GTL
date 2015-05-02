package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Remote
public interface RemitoEntradaProveedorFacadeRemote {

	public RemitoEntradaProveedor save(RemitoEntradaProveedor remito) throws CLException;

	public List<RemitoEntradaProveedor> getRemitosNoAsocByProveedor(Proveedor provedor);

	public List<RemitoEntradaProveedor> getRemitosByProveedor(Proveedor proveedor);
	
	public RemitoEntradaProveedor getByIdEager(Integer idRemito);

	public boolean existeNroFacturaByProveedor(Integer idRemitoEntrada, String nroRemitoEntrada, Integer idProveedor);

	public void eliminarRemitoEntrada(Integer idRemitoEntrada) throws ValidacionException;

	public List<RemitoEntradaProveedor> getRemitoEntradaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor);

	public void checkEliminacionEdicionRemitoEntradaProveedor(RemitoEntradaProveedor rep) throws ValidacionException;

}
