package ar.com.textillevel.modulos.notificaciones.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.modulos.notificaciones.dao.api.local.NotificacionUsuarioDAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.NotificacionUsuario;

@Stateless
public class NotificacionUsuarioDAO extends GenericDAO<NotificacionUsuario, Integer> implements NotificacionUsuarioDAOLocal {

	@Override
	@SuppressWarnings("unchecked")
	public List<NotificacionUsuario> getNotificacionesByUsuario(Integer idUsuarioSistema, Integer max) {
		Query q = getEntityManager().createQuery(" SELECT n FROM NotificacionUsuario n " //
				+ "WHERE n.usuarioSistema.id = :idUsuarioSistema ORDER BY n.fecha DESC ");
		q.setMaxResults(max);
		q.setParameter("idUsuarioSistema", idUsuarioSistema);
		return q.getResultList();
	}

	@Override
	public Integer getCountNotificacionesNoLeidasByUsuario(Integer idUsuarioSistema) {
		Query q = getEntityManager().createQuery(" SELECT COUNT(n) FROM NotificacionUsuario n " //
				+ " WHERE n.usuarioSistema.id = :idUsuarioSistema AND (n.leida IS NULL OR n.leida = :leida)");
		q.setParameter("idUsuarioSistema", idUsuarioSistema);
		q.setParameter("leida", Boolean.FALSE);
		Number count = (Number) q.getSingleResult();
		if (count == null) {
			return 0;
		}
		return count.intValue();
	}
}
