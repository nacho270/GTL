package ar.com.textillevel.dao.api.local;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;

@Local
public interface MovimientoCuentaArticuloDAOLocal extends DAOLocal<MovimientoCuentaArticulo, Integer> {

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticulo(Integer idCtaArticulo, Date fechaDesde, Date fechaHasta);

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticuloByRemitoSalida(Integer idRemitoSalida);

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticuloByRemitoEntrada(Integer idRemitoEntrada);

}
