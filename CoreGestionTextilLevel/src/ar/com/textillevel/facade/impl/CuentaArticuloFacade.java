package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.dao.api.local.CuentaArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.MovimientoCuentaArticuloDAOLocal;
import ar.com.textillevel.entidades.cuentaarticulo.CuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoDebeCuentaArticulo;
import ar.com.textillevel.entidades.cuentaarticulo.movimientos.MovimientoHaberCuentaArticulo;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.facade.api.local.CuentaArticuloFacadeLocal;
import ar.com.textillevel.facade.api.remote.CuentaArticuloFacadeRemote;

@Stateless
public class CuentaArticuloFacade implements CuentaArticuloFacadeLocal, CuentaArticuloFacadeRemote {

	@EJB
	private CuentaArticuloDAOLocal cuentaArticuloDAO;

	@EJB
	private MovimientoCuentaArticuloDAOLocal movimientoCuentaArticuloDAO;

	public void crearMovimientosDebe(RemitoSalida remitoSalida, Map<Articulo, BigDecimal> articuloCantMap) {
		Cliente cliente = remitoSalida.getCliente();
		for(Articulo articulo : articuloCantMap.keySet()) {
			BigDecimal cantidad = articuloCantMap.get(articulo);
			CuentaArticulo ctaArticulo = getCuentaArticulo(cliente, articulo, EUnidad.METROS);
			ctaArticulo.setCantidad(ctaArticulo.getCantidad().subtract(cantidad));
			cuentaArticuloDAO.save(ctaArticulo);
			MovimientoDebeCuentaArticulo md = new MovimientoDebeCuentaArticulo();
			md.setCuenta(ctaArticulo);
			md.setCantidad(cantidad.multiply(new BigDecimal(-1)));
			md.setRemitoSalida(remitoSalida);
			md.setDescripcionResumen("Remito Salida: " + remitoSalida.getNroRemito());
			movimientoCuentaArticuloDAO.save(md);
		}
	}

	public void crearMovimientoHaber(RemitoEntrada remitoEntrada) {
		Cliente cliente = remitoEntrada.getCliente();
		BigDecimal cantidad = remitoEntrada.getTotalMetros();
		CuentaArticulo ctaArticulo = getCuentaArticulo(cliente, remitoEntrada.getArticuloStock(), EUnidad.METROS);
		ctaArticulo.setCantidad(ctaArticulo.getCantidad().add(cantidad));
		cuentaArticuloDAO.save(ctaArticulo);

		MovimientoHaberCuentaArticulo mh = new MovimientoHaberCuentaArticulo();
		mh.setCuenta(ctaArticulo);
		mh.setCantidad(cantidad);
		mh.setRemitoEntrada(remitoEntrada);
		mh.setDescripcionResumen("Remito Entrada: " + remitoEntrada.getNroRemito());
		movimientoCuentaArticuloDAO.save(mh);
	}

	
	public CuentaArticulo getCuentaArticulo(Cliente cliente, Articulo articulo, EUnidad tipoUnidad) {
		CuentaArticulo cuentaArticulo = cuentaArticuloDAO.getCuentaArticulo(cliente, articulo, tipoUnidad);
		if(cuentaArticulo == null) {
			cuentaArticulo = new CuentaArticulo();
			cuentaArticulo.setArticulo(articulo);
			cuentaArticulo.setCantidad(new BigDecimal(0));
			cuentaArticulo.setCliente(cliente);
			cuentaArticulo.setTipoUnidad(tipoUnidad);
			cuentaArticulo.setFechaCreacion(DateUtil.getAhora());
			cuentaArticulo = cuentaArticuloDAO.save(cuentaArticulo);
		}
		return cuentaArticulo;
	}

	public List<MovimientoCuentaArticulo> getMovimientosCuentaArticulo(Integer idCtaArticulo, Date fechaDesde, Date fechaHasta) {
		return movimientoCuentaArticuloDAO.getMovimientosCuentaArticulo(idCtaArticulo, fechaDesde, fechaHasta);
	}

	public void borrarMovimientosCuentaArticulo(Integer idRemitoSalida) {
		List<MovimientoCuentaArticulo> movimientosCuentaArticuloByRemitoSalidaList = movimientoCuentaArticuloDAO.getMovimientosCuentaArticuloByRemitoSalida(idRemitoSalida);
		for(MovimientoCuentaArticulo mca : movimientosCuentaArticuloByRemitoSalidaList) {
			CuentaArticulo cuentaArticulo = mca.getCuenta();
			cuentaArticulo.setCantidad(cuentaArticulo.getCantidad().add(mca.getCantidad().multiply(new BigDecimal(-1))));
			cuentaArticuloDAO.save(cuentaArticulo);
			movimientoCuentaArticuloDAO.removeById(mca.getId());
		}
	}

	public void borrarMovimientosCuentaArticulo(RemitoEntrada remitoEntrada) {
		List<MovimientoCuentaArticulo> movimientosCuentaArticuloByRemitoEntradaList = movimientoCuentaArticuloDAO.getMovimientosCuentaArticuloByRemitoEntrada(remitoEntrada.getId());
		for(MovimientoCuentaArticulo mca : movimientosCuentaArticuloByRemitoEntradaList) {
			CuentaArticulo cuentaArticulo = mca.getCuenta();
			cuentaArticulo.setCantidad(cuentaArticulo.getCantidad().subtract(mca.getCantidad()));
			cuentaArticuloDAO.save(cuentaArticulo);
			movimientoCuentaArticuloDAO.removeById(mca.getId());
		}
	}

	public void actualizarMovimiento(MovimientoCuentaArticulo movimiento) {
		movimientoCuentaArticuloDAO.save(movimiento);
	}
}