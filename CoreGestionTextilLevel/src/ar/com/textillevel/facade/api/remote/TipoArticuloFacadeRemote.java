package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@Remote
public interface TipoArticuloFacadeRemote {

	public List<TipoArticulo> getAllTipoArticulos();
	public TipoArticulo save(TipoArticulo tipoArticulo);
	public TipoArticulo getByIdEager(Integer idTipoArticulo);
	public void remove(TipoArticulo tipoArticuloActual) throws ValidacionException;

}
