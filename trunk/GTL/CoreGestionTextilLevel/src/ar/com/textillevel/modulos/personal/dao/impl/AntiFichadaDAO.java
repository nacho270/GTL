package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.dao.api.AntiFichadaDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaParcial;
import ar.com.textillevel.modulos.personal.entidades.fichadas.antifichadas.AntiFichadaVigencia;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.sanciones.Sancion;
import ar.com.textillevel.modulos.personal.entidades.legajos.valesdeatencion.ValeAtencion;

@Stateless
public class AntiFichadaDAO extends GenericDAO<AntiFichada, Integer> implements AntiFichadaDAOLocal{

	@SuppressWarnings("unchecked")
	public List<AntiFichadaParcial> getAntiFichadasParcialesPorFechaYEmpleado(Date fecha, LegajoEmpleado legajo) {
		String hql = " SELECT af FROM AntiFichadaParcial af WHERE af.fechaHora BETWEEN :desde AND :hasta AND af.legajo.id = :idLegajo ORDER BY af.fechaHora ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("desde", DateUtil.redondearFecha(fecha));
		q.setParameter("hasta", DateUtil.getManiana(fecha));
		q.setParameter("idLegajo",legajo.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AntiFichadaVigencia> getAntiFichadasDeVigenciaPorFechaYEmpleado(Date fecha, LegajoEmpleado legajo) {
		String hql = " SELECT af FROM AntiFichadaVigencia af WHERE :fecha BETWEEN af.fechaDesde AND af.fechaHasta AND af.legajo.id = :idLegajo ORDER BY af.fechaDesde ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fecha", fecha);
		q.setParameter("idLegajo",legajo.getId());
		return q.getResultList();
	}

	public void borrarAntifichadasValeAtencion(ValeAtencion valeAtencion) {
		String hql = "DELETE FROM AntiFichada af WHERE af.valeAtencion.id = :idValeAtencion";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idValeAtencion", valeAtencion.getId());
		q.executeUpdate();
	}

	public void borrarAntifichadasSancion(Sancion sancion) {
		String hql = "DELETE FROM AntiFichada af WHERE af.sancion.id = :idSancion";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idSancion", sancion.getId());
		q.executeUpdate();
	}

	public void borrarAntiFichadaVacaciones(RegistroVacacionesLegajo registroVacacionesLegajo) {
		String hql = "DELETE FROM AntiFichada af WHERE af.registroVacaciones.id = :idRegistro";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRegistro", registroVacacionesLegajo.getId());
		q.executeUpdate();		
	}
}
