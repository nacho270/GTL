package ar.com.textillevel.modulos.alertas.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.modulos.alertas.dao.api.local.AlertaDAOLocal;
import ar.com.textillevel.modulos.alertas.entidades.Alerta;
import ar.com.textillevel.modulos.alertas.entidades.AlertaFaltaStock;
import ar.com.textillevel.modulos.alertas.entidades.IVisitorAlerta;
import ar.com.textillevel.modulos.alertas.entidades.TipoAlerta;

@Stateless
public class AlertaDAO extends GenericDAO<Alerta, Integer> implements AlertaDAOLocal{

	@SuppressWarnings("unchecked")
	public boolean existeAlerta(Alerta alerta) {
		String hql = " SELECT a FROM Alerta a WHERE a.tipoAlerta.id = :idTipo ";
		Query q = getEntityManager().createQuery(hql).setParameter("idTipo", alerta.getTipoAlerta().getId());
		List<Alerta> alertas = q.getResultList();
		if(alertas != null && !alertas.isEmpty() ){
			for(Alerta a : alertas){
				CoincidenciaAlertaVisitor cav = new CoincidenciaAlertaVisitor(a);
				a.accept(cav);
				if(cav.isCoincide()){
					return true;
				}
			}
		}
		return false;
	}

	private class CoincidenciaAlertaVisitor implements IVisitorAlerta{
		
		private final Alerta comparable;
		private boolean coincide;
		
		
		public CoincidenciaAlertaVisitor(Alerta comparable) {
			this.comparable = comparable;
		}

		public void visit(AlertaFaltaStock alerta) {
			setCoincide(comparable instanceof AlertaFaltaStock && ((AlertaFaltaStock)comparable).getPrecioMateriaPrima().getId().equals(alerta.getPrecioMateriaPrima().getId()) );
		}

		public boolean isCoincide() {
			return coincide;
		}

		
		public void setCoincide(boolean coincide) {
			this.coincide = coincide;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Alerta> getAlertasVigentesByPerfil(Perfil perfil) {
		if(perfil.getTiposDeAlertas() == null || perfil.getTiposDeAlertas().isEmpty()){
			return null;
		}
		String hql = " SELECT a FROM Alerta a WHERE a.fechaMinimaParaMostrar <= :fechaHoy AND a.tipoAlerta.id in (:idsTipos) ";

		List<Integer> ids = new ArrayList<Integer>();
		for(TipoAlerta ta : perfil.getTiposDeAlertas()){
			ids.add(ta.getId());
		}
		Query q = getEntityManager().createQuery(hql).setParameter("fechaHoy", DateUtil.getAhora()).setParameter("idsTipos", ids);
		List<Alerta> alertas = q.getResultList();
		if(alertas!=null && !alertas.isEmpty()){
			AlertaLazyInitializatorVisitor aliv = new AlertaLazyInitializatorVisitor();
			for(Alerta a : alertas){
				a.accept(aliv);
			}
		}
		return alertas;
	}
	
	private class AlertaLazyInitializatorVisitor implements IVisitorAlerta{

		public void visit(AlertaFaltaStock alerta) {
			alerta.getPrecioMateriaPrima().getAlias();
			alerta.getPrecioMateriaPrima().getMateriaPrima().getDescripcion();
		}
		
	}
}
