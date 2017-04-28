package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.UsuarioSistemaDAOLocal;
import ar.com.textillevel.entidades.portal.Modulo;
import ar.com.textillevel.entidades.portal.Perfil;
import ar.com.textillevel.entidades.portal.UsuarioSistema;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

@Stateless
public class UsuarioSistemaDAO extends GenericDAO<UsuarioSistema, Integer> implements UsuarioSistemaDAOLocal {

	@SuppressWarnings("unchecked")
	public UsuarioSistema login(String usuario, String password) {
		String hql = " SELECT u FROM UsuarioSistema u WHERE u.usrName = :usuario AND u.password = :password ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("usuario", usuario);
		q.setParameter("password", password);
		List<UsuarioSistema> list = q.getResultList();
		if(list == null || list.isEmpty()){
			return null;
		}
		UsuarioSistema usuarioSistema = list.get(0);
		if(usuarioSistema.getPerfil().getAccionesModulo()!=null){
			usuarioSistema.getPerfil().getAccionesModulo().size();
		}
		if(usuarioSistema.getPerfil().getTiposDeAlertas()!=null){
			usuarioSistema.getPerfil().getTiposDeAlertas().size();
		}
		return usuarioSistema;
	}

	public Boolean existeUsuario(String usuario) {
		String hql = " SELECT COUNT(*) FROM UsuarioSistema u WHERE u.usrName = :usuario ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("usuario", usuario);
		Integer cant = NumUtil.toInteger(q.getSingleResult());
		return cant.intValue()>0;
	}

	@SuppressWarnings("unchecked")
	public UsuarioSistema esPasswordDeAdministrador(String pass) {
		String hql = " SELECT u FROM  UsuarioSistema u WHERE u.password = :password AND u.perfil.isAdmin = 1 ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("password", pass);
		List<UsuarioSistema> lista = q.getResultList();
		if(lista != null && !lista.isEmpty()){
			if(lista.size()>1){
				throw new RuntimeException("Error: hay varios usuarios administradores con la misma clave");
			}
			return lista.get(0);
		}
		return null;
	}

	public boolean yaExisteClave(String pass) {
		String hql = " SELECT COUNT(*) FROM UsuarioSistema u WHERE u.password = :password ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("password", pass);
		Integer cant = NumUtil.toInteger(q.getSingleResult());
		return cant.intValue()>0;
	}

	@SuppressWarnings("unchecked")
	public UsuarioSistema getUsuarioSistemaByUsername(String usrName) {
		String hql = " SELECT u FROM UsuarioSistema u WHERE u.usrName = :usrName";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("usrName", usrName);
		List<UsuarioSistema> lista = q.getResultList();
		if(lista != null && !lista.isEmpty()){
			if(lista.size()>1){
				throw new RuntimeException("Error: hay varios usuarios con el mismo username: " + usrName);
			}
			return lista.get(0);
		}
		return null;
	}

	public UsuarioSistema getByNombre(String nombre) {
		String hql = " SELECT u FROM UsuarioSistema u WHERE u.usrName = :usuario ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("usuario", nombre);
		return (UsuarioSistema) q.getResultList().get(0);
	}

	public Integer getProximoCodigoUsuario() {
		Query query = getEntityManager().createQuery("SELECT MAX(u.codigoUsuario) FROM UsuarioSistema u ");
		Number lastNumero = (Number)query.getSingleResult();
		if(lastNumero==null){
			return 100;
		}else{
			return lastNumero.intValue() + 1;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UsuarioSistema> getByModuloAsociado(Modulo moduloAsociado) {
		final ImmutableList<Integer> idPerfiles = FluentIterable.from(//
				getEntityManager() //
						.createQuery(" SELECT p FROM Perfil p JOIN FETCH p.modulos m WHERE m.id = :idModulo ") //
						.setParameter("idModulo", moduloAsociado.getId()) //
						.getResultList()) //
				.transform(new Function<Perfil, Integer>() {
					@Override
					public Integer apply(Perfil perfil) {
						return perfil.getId();
					}
				}).toList();
		Query q = getEntityManager().createQuery(" SELECT u FROM UsuarioSistema u WHERE u.perfil.id IN(:idPerfiles) ");
		q.setParameter("idPerfiles", idPerfiles);
		return q.getResultList();
	}
}