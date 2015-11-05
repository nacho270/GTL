package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Stateless
@SuppressWarnings("unchecked")
public class ArtiucloDAO extends GenericDAO<Articulo, Integer> implements ArticuloDAOLocal {

	public List<Articulo> getArticulosConAlgunaPMPConStockInicial() {
		String hql = " SELECT art " +
					 " FROM Articulo art " +
					 " WHERE EXISTS (SELECT 1 FROM PrecioMateriaPrima pmp WHERE pmp.materiaPrima.articulo.id = art.id AND pmp.stockInicialDisponible > 0) " +
					 " ORDER BY art.nombre ";
		Query q = getEntityManager().createQuery(hql);
		List<Articulo> resultQueryList = q.getResultList();
		return resultQueryList;
	}

	public List<Articulo> getArticulosConTipoArticuloFetched() {
		String hql = " SELECT art " +
				 " FROM Articulo art "+
				 " JOIN FETCH art.tipoArticulo as tp " +
				 " ORDER BY art.nombre ";
		Query q = getEntityManager().createQuery(hql);
		List<Articulo> resultQueryList = q.getResultList();
		return resultQueryList;
	}

	public List<Articulo> getAllByTipoArticuloOrderByName(Integer idTipoArticulo) {
		String hql = " SELECT art  FROM Articulo art "+
				 " WHERE art.tipoArticulo.id = :idTipoArticulo " +
				 " ORDER BY art.nombre ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idTipoArticulo", idTipoArticulo);
		return q.getResultList();
	}

}
