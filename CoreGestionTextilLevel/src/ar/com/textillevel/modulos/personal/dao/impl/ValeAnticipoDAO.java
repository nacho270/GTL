package ar.com.textillevel.modulos.personal.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.personal.dao.api.ValeAnticipoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

@Stateless
public class ValeAnticipoDAO extends GenericDAO<ValeAnticipo, Integer> implements ValeAnticipoDAOLocal {

	@SuppressWarnings("unchecked")
	public List<ValeAnticipo> getValesByLegajo(Integer idLegajo) {
		String hql = "SELECT va From ValeAnticipo va WHERE va.legajo.id = :idLegajo ORDER BY va.nroVale ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", idLegajo);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ValeAnticipo> buscarVales(Date fechaDesde, Date fechaHasta, String apellidoEmpleado,  EEstadoValeAnticipo estado) {
		String hql = "SELECT va From ValeAnticipo va WHERE 1=1 "+
					 (fechaDesde!=null?" AND va.fecha >= :fechaDesde  ": " ")+
					 (fechaHasta!=null?" AND va.fecha <= :fechaHasta  ": " ")+
					 (apellidoEmpleado!=null?" AND va.legajo.empleado.apellido LIKE :apellidoEmpleado ": " ")+
					 (estado!=null?" AND va.idEstadoVale = :idEstado ": " ");
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(apellidoEmpleado!=null){
			q.setParameter("apellidoEmpleado","%"+ apellidoEmpleado + "%");
		}
		if(estado!=null){
			q.setParameter("idEstado", estado.getId());
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ValeAnticipo> getValesEnEstado(Integer idLegajo, EEstadoValeAnticipo estadoVale) {
		String hql = "SELECT va From ValeAnticipo va WHERE va.legajo.id = :idLegajo AND va.idEstadoVale = :idEstadoVale ORDER BY va.nroVale ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idLegajo", idLegajo);
		q.setParameter("idEstadoVale", estadoVale.getId());
		return q.getResultList();
	}

}