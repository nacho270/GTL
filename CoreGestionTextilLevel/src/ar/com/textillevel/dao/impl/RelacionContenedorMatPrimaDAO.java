package ar.com.textillevel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.RelacionContenedorPrecioMatPrimaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ContenedorMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;

@Stateless
@SuppressWarnings("unchecked")
public class RelacionContenedorMatPrimaDAO extends GenericDAO<RelacionContenedorPrecioMatPrima, Integer> implements RelacionContenedorPrecioMatPrimaDAOLocal {

	public Map<String, RelacionContenedorPrecioMatPrima> getAllRelacionContenedorByIdProveedor(Integer idProveedor) {
		Map<String, RelacionContenedorPrecioMatPrima> mapResult = new HashMap<String, RelacionContenedorPrecioMatPrima>();
		Query q = getEntityManager().createQuery("SELECT rel " +
									   			 "FROM RelacionContenedorPrecioMatPrima rel " +
									   			 "WHERE rel.precioMateriaPrima.preciosProveedor.proveedor.id = :idProveedor");
		q.setParameter("idProveedor", idProveedor);
		List<RelacionContenedorPrecioMatPrima> resultList = q.getResultList();
		for(RelacionContenedorPrecioMatPrima rel : resultList) {
			mapResult.put(rel.getKey(), rel);
		}
		return mapResult;
	}

	public List<RelacionContenedorPrecioMatPrima> getAllRelacionContenedorConStockByIdProveedor(Integer idProveedor, List<ContenedorMateriaPrima> contenedores) {
		Query q = getEntityManager().createQuery("SELECT rel " +
									   			 "FROM RelacionContenedorPrecioMatPrima rel " +
									   			 "WHERE rel.precioMateriaPrima.preciosProveedor.proveedor.id = :idProveedor " +
									   			 "AND rel.stockActual > 0 " +
									   			 "AND rel.contenedor IN (:contenedores)");
		q.setParameter("idProveedor", idProveedor);
		q.setParameter("contenedores", contenedores);
		List<RelacionContenedorPrecioMatPrima> resultList = q.getResultList();
		return resultList;
	}

}
