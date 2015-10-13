package ar.com.textillevel.modulos.personal.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.SancionDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Apercibimiento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.CartaDocumento;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.visitor.ISancionVisitor;

@Stateless
@SuppressWarnings("unchecked")
public class SancionDAO extends GenericDAO<Sancion, Integer> implements SancionDAOLocal {

	public List<Sancion> getSanciones(LegajoEmpleado legajo) {
		String hql = "SELECT s From Sancion s WHERE s.legajo.id = :idLegajo ORDER BY s.fechaSancion DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", legajo.getId());
		List<Sancion> resultList = q.getResultList();
		SancionLazyInitializationVisitor visitor = new SancionLazyInitializationVisitor();
		for(Sancion s : resultList) {
			s.accept(visitor);
		}
		return resultList;
	}

	public List<Sancion> getSancionesNoAsociadas(LegajoEmpleado legajo) {
		String hql = "SELECT s From Sancion s WHERE s.legajo.id = :idLegajo AND s.cartaDocumento IS NULL ORDER BY s.fechaSancion DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", legajo.getId());
		List<Sancion> resultList = q.getResultList();
		SancionLazyInitializationVisitor visitor = new SancionLazyInitializationVisitor();
		for(Sancion s : resultList) {
			s.accept(visitor);
		}
		return resultList;
	}

	private static class SancionLazyInitializationVisitor implements ISancionVisitor {

		public void visit(Apercibimiento apercibimiento) {
		}

		public void visit(CartaDocumento cd) {
			cd.getSancionesAsociadas().size();
		}
		
	}

}
