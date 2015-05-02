package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;

@Local
public interface RemitoEntradaProveedorFacadeLocal {

	public RemitoEntradaProveedor save(RemitoEntradaProveedor remito) throws CLException;

	public void eliminarRemitoEntrada(Integer idRemitoEntrada) throws ValidacionException;
	
	public RemitoEntradaProveedor getByIdEager(Integer idRemito);

}