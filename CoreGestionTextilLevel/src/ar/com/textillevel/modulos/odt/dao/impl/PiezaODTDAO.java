package ar.com.textillevel.modulos.odt.dao.impl;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.odt.dao.api.local.PiezaODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Stateless
public class PiezaODTDAO extends GenericDAO<PiezaODT, Integer> implements PiezaODTDAOLocal {

	@Override
	public PiezaODT getByParams(String codODT, Integer nroPieza, Integer nroSubPieza) {
		Query query = getEntityManager().createQuery("SELECT podt " +
				 "FROM PiezaODT as podt " + 
				 "JOIN FETCH podt.odt as odt " +
				 "WHERE odt.codigo = :codigo AND podt.orden = :nroPieza " +
				 (nroSubPieza == null ? " AND podt.ordenSubpieza IS NULL" : " AND podt.ordenSubpieza = :nroSubPieza"));
		query.setParameter("codigo", codODT);
		query.setParameter("nroPieza", nroPieza);
		if(nroSubPieza != null) {
			query.setParameter("nroSubPieza", nroSubPieza);
		}
		return (PiezaODT)query.getSingleResult();
	}

}