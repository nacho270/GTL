package ar.com.textillevel.facade.api.remote;

import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Remote
public interface CuentaArticuloFacadeRemote {

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticulo(Integer idCtaArticulo, Date fechaDesde, Date fechaHasta);

	public CuentaArticulo getCuentaArticulo(Cliente cliente, Articulo articulo, EUnidad tipoUnidad);
	
	public void actualizarMovimiento(MovimientoCuentaArticulo movimiento);
	
}
