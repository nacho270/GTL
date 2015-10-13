package ar.com.textillevel.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.TipoArticuloDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;

@Stateless
public class TipoArticuloDAO extends GenericDAO<TipoArticulo, Integer> implements TipoArticuloDAOLocal {

	public TipoArticulo getByIdEager(Integer idTipoArticulo) {
		TipoArticulo tipoArticulo = getById(idTipoArticulo);
		tipoArticulo.getArticulos().size();
		tipoArticulo.getTiposArticuloComponentes().size();
		return tipoArticulo;
	}

	public boolean existeTipoArticuloComoComponente(TipoArticulo tipoArticuloActual) {
		Query q = getEntityManager().createQuery("SELECT ta FROM TipoArticulo ta WHERE :taActual IN ELEMENTS(ta.tiposArticuloComponentes)");
		q.setParameter("taActual", tipoArticuloActual);
		return !q.getResultList().isEmpty();
	}

}
