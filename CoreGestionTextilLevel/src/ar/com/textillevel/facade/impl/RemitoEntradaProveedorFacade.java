package ar.com.textillevel.facade.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.RelacionContenedorPrecioMatPrimaDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaProveedorDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.PiezaRemitoEntradaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.stock.MovimientoStockSuma;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.RelacionContenedorMatPrimaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoEntradaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaProveedorFacadeRemote;

@Stateless
public class RemitoEntradaProveedorFacade implements RemitoEntradaProveedorFacadeRemote, RemitoEntradaProveedorFacadeLocal {

	@EJB
	private PrecioMateriaPrimaFacadeRemote precioMateriaPrimaFacade;

	@EJB
	private RemitoEntradaProveedorDAOLocal remitoDao;
	
	@EJB
	private FacturaProveedorDAOLocal facturaDAO;

	@EJB
	private MovimientoStockFacadeLocal movimientoStockFacade;

	@EJB
	private RelacionContenedorMatPrimaFacadeLocal relacionContenedorMatPrimaFacade;

	@EJB
	private RelacionContenedorPrecioMatPrimaDAOLocal relacionContenedorPrecioMatPrimaDAO;

	
	public RemitoEntradaProveedor save(RemitoEntradaProveedor remito) throws CLException {
		//Si es modificación hago un undo del movimiento de stock
		if(remito.getId() != null) {
			undoStock(remito);
		}

		for(PiezaRemitoEntradaProveedor prep : remito.getPiezas()) {
			if(prep.getRelContenedorPrecioMatPrima() != null) {
				RelacionContenedorPrecioMatPrima rcpmp = relacionContenedorPrecioMatPrimaDAO.save(prep.getRelContenedorPrecioMatPrima());
				prep.setRelContenedorPrecioMatPrima(rcpmp);
			}
		}

		RemitoEntradaProveedor remitoSaved = remitoDao.save(remito);
		for (PiezaRemitoEntradaProveedor pieza : remitoSaved.getPiezas()) {
			if(pieza.getPrecioMateriaPrima().getMateriaPrima().getTipo() != ETipoMateriaPrima.VARIOS){
				movimientoStockFacade.crearMovimientoSuma(pieza.getPrecioMateriaPrima(), pieza.getCantidad(), remitoSaved,pieza.getPrecioMateriaPrima().getStockActual());
				precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(pieza.getCantidad(), pieza.getPrecioMateriaPrima().getId());
				//gestiono el stock de los contenedores
				if(pieza.getRelContenedorPrecioMatPrima() != null) {
					movimientoStockFacade.crearMovimientoSuma(pieza.getRelContenedorPrecioMatPrima(), pieza.getCantContenedor(), remitoSaved, pieza.getRelContenedorPrecioMatPrima().getStockActual());
					relacionContenedorMatPrimaFacade.actualizarStockRelContPrecioMatPrima(pieza.getCantContenedor(), pieza.getRelContenedorPrecioMatPrima().getId());
				}
			}
		}
		return remitoSaved;
	}

	public List<RemitoEntradaProveedor> getRemitosNoAsocByProveedor(Proveedor proveedor) {
		return remitoDao.getRemitosNoAsocByProveedor(proveedor);
	}

	public List<RemitoEntradaProveedor> getRemitosByProveedor(Proveedor proveedor) {
		return remitoDao.getRemitosByProveedor(proveedor);
	}

	public RemitoEntradaProveedor getByIdEager(Integer idRemito) {
		return remitoDao.getByIdEager(idRemito);
	}

	public boolean existeNroFacturaByProveedor(Integer idRemitoEntrada, String nroRemitoEntrada, Integer idProveedor) {
		return remitoDao.existeNroFacturaByProveedor(idRemitoEntrada, nroRemitoEntrada, idProveedor);
	}

	public void eliminarRemitoEntrada(Integer idRemitoEntrada) throws ValidacionException {
		RemitoEntradaProveedor rep = remitoDao.getById(idRemitoEntrada);
		checkEliminacionEdicionRemitoEntradaProveedor(rep);
		undoStock(rep);
		remitoDao.removeById(rep.getId());
	}

	private void undoStock(RemitoEntradaProveedor rep) {
		RemitoEntradaProveedor repAnt = remitoDao.getById(rep.getId());
		List<MovimientoStockSuma> movList = movimientoStockFacade.getMovimientosSumaByRemito(repAnt.getId());
		for(MovimientoStockSuma mss : movList) {
			if(mss.getRelContPrecioMatPrima() == null) { //Es de precios materias primas 
				PrecioMateriaPrima pmp = precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(mss.getCantidad().negate(), mss.getPrecioMateriaPrima().getId());
				unifyInstance(pmp, rep);
			} else { //Es solo de contenedor
				RelacionContenedorPrecioMatPrima rcmpf = relacionContenedorMatPrimaFacade.actualizarStockRelContPrecioMatPrima(mss.getCantidad().negate(), mss.getRelContPrecioMatPrima().getId());
				unifyInstance(rcmpf, rep);
			}
			movimientoStockFacade.borrarMovimientoById(mss.getId());
		}
	}

	private void unifyInstance(RelacionContenedorPrecioMatPrima rcmpf, RemitoEntradaProveedor rep) {
		for(PiezaRemitoEntradaProveedor prep : rep.getPiezas()) {
			if(prep.getRelContenedorPrecioMatPrima() != null && prep.getRelContenedorPrecioMatPrima().equals(rcmpf)) {
				prep.setRelContenedorPrecioMatPrima(rcmpf);
				return;
			}
		}
	}

	private void unifyInstance(PrecioMateriaPrima pmp, RemitoEntradaProveedor rep) {
		for(PiezaRemitoEntradaProveedor prep : rep.getPiezas()) {
			if(prep.getPrecioMateriaPrima().equals(pmp)) {
				prep.setPrecioMateriaPrima(pmp);
				return;
			}
		}
	}

	public void checkEliminacionEdicionRemitoEntradaProveedor(RemitoEntradaProveedor rep) throws ValidacionException {
		List<FacturaProveedor> facturaList = facturaDAO.getFacturasByRemito(rep);
		if(!facturaList.isEmpty()) {
			throw new ValidacionException(EValidacionException.REMITO_ENTRADA_PROV_IMPOSIBLE_BORRAR.getInfoValidacion(), new String[] { extractInfoFactura(facturaList) });
		}
	}

	private String extractInfoFactura(List<FacturaProveedor> facturaList) {
		List<String> infoList = new ArrayList<String>();
		for(FacturaProveedor fp : facturaList) {
			infoList.add(fp.getNroFactura());
		}
		return StringUtil.getCadena(infoList, ", ");
	}

	public List<RemitoEntradaProveedor> getRemitoEntradaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor) {
		return remitoDao.getRemitoEntradaByFechasAndProveedor(fechaDesde, fechaHasta, idProveedor);
	}

}
