package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.PersonaDAOLocal;
import ar.com.textillevel.entidades.gente.Persona;
import ar.com.textillevel.entidades.gente.Rubro;

@Stateless
public class PersonaDAO extends GenericDAO<Persona, Integer> implements PersonaDAOLocal {
	@SuppressWarnings("unchecked")
	public List<Persona> getAllOrderByName() {
		Query query = getEntityManager().createQuery("FROM Persona AS p ORDER BY p.apellido, p.nombres");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Persona> getByNombreOApellido(String criterio) {
		Query query = getEntityManager().createQuery("FROM Persona AS p WHERE p.apellido LIKE :ap OR p.nombres LIKE :nom ORDER BY p.apellido, p.nombres");
		query.setParameter("ap", "%"+criterio+"%");
		query.setParameter("nom", "%"+criterio+"%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Persona> getByNombreOApellidoYRubro(String criterio, Rubro rubro) {
		String hql = "FROM Persona AS p WHERE 1=1 " + 
					 (rubro != null?" AND p.rubroPersona.id = :idRubro ":" ")+
					 (!criterio.trim().equalsIgnoreCase(" ")?" AND (p.apellido = :ap OR p.nombres = :nom ) ":" ")+
					 " ORDER BY p.apellido, p.nombres ";
		Query query = getEntityManager().createQuery(hql);
		if(!criterio.trim().equalsIgnoreCase("")){
			query.setParameter("ap", "%"+criterio+"%");
			query.setParameter("nom", "%"+criterio+"%");
		}
		if(rubro != null){
			query.setParameter("idRubro", rubro.getId());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Persona> getAllByRubroOrderByName(Rubro rubro) {
		Query query = getEntityManager().createQuery("FROM Persona AS p " +
						(rubro != null? " WHERE p.rubroPersona.id = :idRubro " : "") + " ORDER BY p.apellido, p.nombres");
		if(rubro != null){
			query.setParameter("idRubro", rubro.getId());
		}
		return query.getResultList();
	}
}
