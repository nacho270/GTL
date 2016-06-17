package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.AccionProcedimientoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.AccionProcedimiento;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;

@Stateless
public class AccionProcedimientoDAO extends GenericDAO<AccionProcedimiento, Integer> implements AccionProcedimientoDAOLocal {

	public boolean existsAccionProcedimiento(AccionProcedimiento accion) {
		String hql = " SELECT a FROM AccionProcedimiento a where (:id IS NULL OR a.id != :id) AND a.nombre=:nombre AND a.idTipoSector = :idSector";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", accion.getId());
		q.setParameter("nombre", accion.getNombre());
		q.setParameter("idSector", accion.getSectorMaquina().getId());
		return !q.getResultList().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<AccionProcedimiento> getAllSortedBySector(ESectorMaquina sector) {
		String hql = " SELECT a FROM AccionProcedimiento a WHERE a.idTipoSector = :idSector ORDER BY a.nombre";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSector", sector.getId());
		return q.getResultList();
	}

}
