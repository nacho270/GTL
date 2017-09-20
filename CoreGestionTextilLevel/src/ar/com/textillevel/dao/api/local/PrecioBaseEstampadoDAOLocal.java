package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;

@Local
public interface PrecioBaseEstampadoDAOLocal extends DAOLocal<PrecioBaseEstampado, Integer> {

	public List<PrecioBaseEstampado> getAllByDibujo(DibujoEstampado dibujo);

}
