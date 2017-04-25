package ar.com.textillevel.modulos.notificaciones.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.modulos.notificaciones.dao.api.local.ConfiguracionNotificacionDAOLocal;
import ar.com.textillevel.modulos.notificaciones.entidades.ConfiguracionNotificacion;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

@Stateless
public class ConfiguracionNotificacionDAO extends GenericDAO<ConfiguracionNotificacion, Integer> implements ConfiguracionNotificacionDAOLocal {

	@Override
	public ConfiguracionNotificacion getByTipo(ETipoNotificacion tipo) {
		return (ConfiguracionNotificacion) getEntityManager() //
				.createQuery(" SELECT conf FROM ConfiguracionNotificacion conf WHERE conf.idTipo = :idTipo ") //
				.setParameter("idTipo", tipo.getId()) //
				.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConfiguracionNotificacion> getConfiguracionesHabilitadasParaUsuario(UsuarioSistema usuario) {
		Perfil perfil = (Perfil) getEntityManager().createQuery(" SELECT u.perfil FROM UsuarioSistema u WHERE u.id = :idUsuario").setParameter("idUsuario", usuario.getId()).getSingleResult();
		final ImmutableList<Integer> idModulos = FluentIterable.from(perfil.getModulos()).transform(new Function<Modulo, Integer>() {
			@Override
			public Integer apply(Modulo modulo) {
				return modulo.getId();
			}
		}).toList();
		Query q = getEntityManager().createQuery(" SELECT conf FROM ConfiguracionNotificacion conf JOIN FETCH conf.moduloAsociado m " + " WHERE m.id IN(:idModulos) ");
		q.setParameter("idModulos", idModulos);
		return q.getResultList();
	}
}
