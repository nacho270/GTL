package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;

@Stateless
public class TransicionODTDAO extends GenericDAO<TransicionODT, Integer> implements TransicionODTDAOLocal{

	public TransicionODT getByODT(Integer idOdt) {
		return (TransicionODT) getEntityManager().createQuery(" SELECT tr FROM TransicionODT tr WHERE tr.odt.id = :idOdt ORDER BY tr.fechaHoraRegistro DESC ").setParameter("idOdt", idOdt).getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public List<TransicionODT> getAllByODT(Integer idODT) {
		return getEntityManager().createQuery(" SELECT tr FROM TransicionODT tr JOIN FETCH tr.maquina WHERE tr.odt.id = :idOdt ORDER BY tr.fechaHoraRegistro").setParameter("idOdt", idODT).getResultList();
	}

}
