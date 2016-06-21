package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

@Stateless
@SuppressWarnings("unchecked")
public class TransicionODTDAO extends GenericDAO<TransicionODT, Integer> implements TransicionODTDAOLocal {

	public TransicionODT getByODT(Integer idOdt) {
		return (TransicionODT) getEntityManager().createQuery(" SELECT tr FROM TransicionODT tr WHERE tr.odt.id = :idOdt ORDER BY tr.fechaHoraRegistro DESC ").setParameter("idOdt", idOdt).getResultList().get(0);
	}

	public List<TransicionODT> getAllByODT(Integer idODT) {
		return getEntityManager().createQuery(" SELECT tr FROM TransicionODT tr LEFT JOIN FETCH tr.maquina WHERE tr.odt.id = :idOdt ORDER BY tr.fechaHoraRegistro").setParameter("idOdt", idODT).getResultList();
	}

	public void deleteTransicionesFromODT(Integer idODT) {
		List<TransicionODT> allByODT = getAllByODT(idODT);
		for(TransicionODT t : allByODT) {
			this.removeById(t.getId());
		}
	}

}