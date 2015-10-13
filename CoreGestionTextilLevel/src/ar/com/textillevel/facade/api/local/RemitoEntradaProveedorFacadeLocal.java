package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;

@Local
public interface RemitoEntradaProveedorFacadeLocal {

	public RemitoEntradaProveedor save(RemitoEntradaProveedor remito) throws FWException;

	public void eliminarRemitoEntrada(Integer idRemitoEntrada) throws ValidacionException;
	
	public RemitoEntradaProveedor getByIdEager(Integer idRemito);

}