package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.MateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.MateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.TipoAnilina;

@Stateless
@SuppressWarnings("unchecked")
public class MateriaPrimaDAO extends GenericDAO<MateriaPrima, Integer> implements MateriaPrimaDAOLocal {

	public List<MateriaPrima> getAllOrderByName() {
		Query query = getEntityManager().createQuery("FROM MateriaPrima AS mp ORDER BY mp.descripcion");
		return query.getResultList();
	}

	public MateriaPrima getByIdEager(Integer idMateriaPrima) {
		Query query = getEntityManager().createQuery("SELECT mp FROM MateriaPrima AS mp " +
													 "LEFT JOIN FETCH mp.tipoMateriaPrima " +
													 "LEFT JOIN FETCH mp.proveedores " +
													 "WHERE mp.id = :idMateriaPrima");
		query.setParameter("idMateriaPrima", idMateriaPrima);
		return (MateriaPrima)query.getResultList().get(0);
	}

	public Anilina getAnilinaByColorIndex(Integer colorIndex) {
		String hql = " SELECT a FROM Anilina a WHERE a.colorIndex = :colorIndex ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("colorIndex", colorIndex);
		List<Anilina> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			return lista.get(0);
		}
		return null;
	}

	public boolean existeAnilina(TipoAnilina tipoAnilina, Integer colorIndex, BigDecimal concentracion) {
		String hql = " SELECT COUNT(*) FROM Anilina a WHERE a.colorIndex = :colorIndex " + (concentracion != null? " AND a.concentracion = :concentracion " : " " ) + "AND a.tipoAnilina.id = :idTipoAnilina";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("colorIndex", colorIndex);
		q.setParameter("idTipoAnilina", tipoAnilina.getId());
		if(concentracion != null){
			q.setParameter("concentracion", concentracion);
		}
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public boolean existeMateriaPrima(String nombre, Integer idAExcluir) {
		String hql = " SELECT COUNT(*) FROM MateriaPrima mp WHERE LOWER(mp.descripcion) = LOWER(:descripcion) "+
					 (idAExcluir != null?" AND mp.id <> :idAExcluir ": " ");
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("descripcion", nombre);
		if(idAExcluir!=null){
			q.setParameter("idAExcluir", idAExcluir);
		}
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public List<Anilina> getAllAnilinasByTipoArticulo(TipoArticulo tipoArticulo) {
		Query query = getEntityManager().createQuery("SELECT a FROM Anilina AS a " +
													 "WHERE :ta IN ELEMENTS(a.tipoAnilina.tiposArticulosSoportados) " +
													 "ORDER BY a.descripcion");
		query.setParameter("ta", tipoArticulo);
		return query.getResultList();
	}

	public <T extends MateriaPrima> List<T> getAllByClase(Class<T> clazz) {
		Query query = getEntityManager().createQuery("FROM " + clazz.getName() + " AS mp ORDER BY mp.descripcion");
		return query.getResultList();
	}

}