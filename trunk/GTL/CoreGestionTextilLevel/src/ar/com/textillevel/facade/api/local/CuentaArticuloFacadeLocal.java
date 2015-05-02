package ar.com.textillevel.facade.api.local;

import java.math.BigDecimal;
import java.util.Map;

import javax.ejb.Local;

import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Local
public interface CuentaArticuloFacadeLocal {

	public void crearMovimientosDebe(RemitoSalida remitoSalida, Map<Articulo, BigDecimal> articuloCantMap);

	public void crearMovimientoHaber(RemitoEntrada remitoEntrada);

	public CuentaArticulo getCuentaArticulo(Cliente cliente, Articulo articulo, EUnidad tipoUnidad);

	public void borrarMovimientosCuentaArticulo(Integer idRemitoSalida);

	public void borrarMovimientosCuentaArticulo(RemitoEntrada remitoEntrada);

}
