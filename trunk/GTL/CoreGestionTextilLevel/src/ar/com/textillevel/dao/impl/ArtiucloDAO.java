package ar.com.textillevel.dao.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.ArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Stateless
public class ArtiucloDAO extends GenericDAO<Articulo, Integer> implements ArticuloDAOLocal {

	@SuppressWarnings("unchecked")
	public List<Articulo> getArticulosConAlgunaPMPConStockInicial() {
		String hql = " SELECT art " +
					 " FROM Articulo art " +
					 " WHERE EXISTS (SELECT 1 FROM PrecioMateriaPrima pmp WHERE pmp.materiaPrima.articulo.id = art.id AND pmp.stockInicialDisponible > 0) " +
					 " ORDER BY art.nombre ";
		Query q = getEntityManager().createQuery(hql);
		List<Articulo> resultQueryList = q.getResultList();
		return resultQueryList;
	}

}
