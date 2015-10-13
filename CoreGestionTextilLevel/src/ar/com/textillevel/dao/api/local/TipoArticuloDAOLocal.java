package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@Local
public interface TipoArticuloDAOLocal extends DAOLocal<TipoArticulo, Integer>{

	public TipoArticulo getByIdEager(Integer idTipoArticulo);

	public boolean existeTipoArticuloComoComponente(TipoArticulo tipoArticuloActual) ;

}
