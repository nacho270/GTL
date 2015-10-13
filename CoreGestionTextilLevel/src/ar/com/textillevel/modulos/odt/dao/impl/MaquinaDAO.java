package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.MaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Stateless
@SuppressWarnings("unchecked")
public class MaquinaDAO extends GenericDAO<Maquina, Integer> implements MaquinaDAOLocal{

	public List<Maquina> getAllByTipo(TipoMaquina tipoMaquina) {
		return getAllByIdTipoMaquina(tipoMaquina.getId());
	}

	public List<Maquina> getAllByIdTipoMaquina(Integer idTipoMaquina) {
		return getEntityManager().createQuery(" SELECT m FROM Maquina m WHERE m.tipoMaquina.id = :idTipoMaquina ORDER BY m.nombre ASC").setParameter("idTipoMaquina", idTipoMaquina).getResultList();
	}

	public List<Maquina> getAllSorted() {
		return getEntityManager().createQuery("FROM Maquina m ORDER BY m.tipoMaquina.orden, m.nombre").getResultList();
	}

	public boolean existsNombreByTipoMaquina(Maquina maquina) {
		String hql = "FROM Maquina m where (:id IS NULL OR m.id != :id) AND m.nombre=:nombre AND m.tipoMaquina = :tipoMaquina";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", maquina.getId());
		q.setParameter("nombre", maquina.getNombre());
		q.setParameter("tipoMaquina", maquina.getTipoMaquina());
		return !q.getResultList().isEmpty();
	}

	public Maquina getByIdEager(Integer id) {
		Maquina m = getById(id);
		m.getTipoArticulos().size();
		return m;
	}

	public List<Maquina> getAllBySector(ESectorMaquina sector) {
		return getEntityManager().createQuery(" SELECT m FROM Maquina m WHERE m.tipoMaquina.idTipoSector = :idTipoSector").setParameter("idTipoSector", sector.getId()).getResultList();
	}

}
