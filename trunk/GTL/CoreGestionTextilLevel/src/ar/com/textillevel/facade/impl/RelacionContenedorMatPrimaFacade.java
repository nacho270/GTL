package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.RelacionContenedorPrecioMatPrimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.facade.api.local.RelacionContenedorMatPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.RelacionContenedorMatPrimaFacadeRemote;

@Stateless
public class RelacionContenedorMatPrimaFacade implements RelacionContenedorMatPrimaFacadeRemote, RelacionContenedorMatPrimaFacadeLocal {

	@EJB
	private RelacionContenedorPrecioMatPrimaDAOLocal relacionContenedorPrecioMatPrimaDAO;

	public Map<String, RelacionContenedorPrecioMatPrima> getAllRelacionContenedorByIdProveedor(Integer idProveedor) {
		return relacionContenedorPrecioMatPrimaDAO.getAllRelacionContenedorByIdProveedor(idProveedor);
	}

	public List<RelacionContenedorPrecioMatPrima> getAllRelacionContenedorConStockByIdProveedor(Integer idProveedor, List<ContenedorMateriaPrima> contenedores) {
		return relacionContenedorPrecioMatPrimaDAO.getAllRelacionContenedorConStockByIdProveedor(idProveedor, contenedores);
	}

	public RelacionContenedorPrecioMatPrima actualizarStockRelContPrecioMatPrima(BigDecimal cantidad, Integer idRelacionContenedorPrecioMatPrima) {
		RelacionContenedorPrecioMatPrima rcpmp = relacionContenedorPrecioMatPrimaDAO.getById(idRelacionContenedorPrecioMatPrima);
		if (rcpmp.getStockActual() == null) {
			rcpmp.setStockActual(new BigDecimal(0));
		}
		rcpmp.setStockActual(rcpmp.getStockActual().add(cantidad));
		return relacionContenedorPrecioMatPrimaDAO.save(rcpmp);
	}

}