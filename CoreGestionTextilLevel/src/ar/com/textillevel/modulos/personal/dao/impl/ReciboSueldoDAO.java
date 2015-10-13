package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.entidades.Mes;
import ar.com.textillevel.modulos.personal.dao.api.ReciboSueldoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ItemReciboSueldoDeduccion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EQuincena;

@Stateless
@SuppressWarnings("unchecked")
public class ReciboSueldoDAO extends GenericDAO<ReciboSueldo, Integer> implements ReciboSueldoDAOLocal {

	public ReciboSueldo getReciboSueldo(LegajoEmpleado legajo, Integer anio, Mes mes, EQuincena quincena) {
		String queryStr = "SELECT rs " +
						  "FROM ReciboSueldo rs " +
						  "WHERE rs.legajo.id = :idLegajo " +
						  "		 AND rs.anio = :anio " +
						  "		 AND rs.mes = :mes " +
						  (quincena == null ? "" : "AND rs.quincena = :quincena");
		Query query = getEntityManager().createQuery(queryStr);
		query.setParameter("idLegajo", legajo.getId());
		query.setParameter("anio", anio);
		query.setParameter("mes", mes);
		if(quincena != null) {
			query.setParameter("quincena", quincena.getId());
		}
		List<ReciboSueldo> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else if(resultList.size() > 1) {
			throw new IllegalStateException("Existe más de un recibo de sueldo para el mismo período, año : " + anio + ", mes : " + mes + " para el legajo " + legajo.getNroLegajo());
		} else {
			return resultList.get(0);
		}
	}

	public ReciboSueldo getByIdEager(Integer idReciboSueldo) {
		ReciboSueldo rs = getById(idReciboSueldo);
		doEager(rs);
		return rs;
	}

	public ReciboSueldo getReciboSueldoByTextoOrden(Integer idLegajo, String textoOrden) {
		String queryStr =  " SELECT rs "
						 + " FROM ReciboSueldo rs "
						 + " WHERE rs.legajo.id = :idLegajo " 
						 + " AND rs.textoOrden = :textoOrden";
		Query query = getEntityManager().createQuery(queryStr);
		query.setParameter("idLegajo", idLegajo);
		query.setParameter("textoOrden", textoOrden);
		List<ReciboSueldo> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else if (resultList.size() > 1) {
			throw new IllegalStateException("Existe más de un recibo de sueldo para el mismo período: " + textoOrden  + " y legajo: " + idLegajo + ".");
		} else {
			ReciboSueldo reciboSueldo = resultList.get(0);
			doEager(reciboSueldo);
			return reciboSueldo;
		}
	}

	private void doEager(ReciboSueldo rs) {
		for(ItemReciboSueldo item : rs.getItems()) {
			item.getDescripcion();
			if(item instanceof ItemReciboSueldoDeduccion) {
				((ItemReciboSueldoDeduccion)item).getVales().size();
			}
		}
		rs.getLegajo().getAfiliadoASindicato();
		rs.getLegajo().getHistorialVigencias().size();
		rs.getAnotaciones().size();
	}

}