package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;
import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticulo;

@Local
public interface GrupoArticuloDAOLocal extends DAOLocal<GrupoTipoArticulo, Integer> {

	public void deleteGruposArtEstampadoByDibujo(DibujoEstampado dibujoEstampado);

}
