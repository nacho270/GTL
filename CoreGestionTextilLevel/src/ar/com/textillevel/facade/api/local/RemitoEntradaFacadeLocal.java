package ar.com.textillevel.facade.api.local;

import javax.ejb.Local;

import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;

@Local
public interface RemitoEntradaFacadeLocal {
	public RemitoEntrada getByIdEager(Integer idRemito);
	public void eliminarRemitoEntradaForzado(Integer idRE, Boolean borrarRemitos);
}
