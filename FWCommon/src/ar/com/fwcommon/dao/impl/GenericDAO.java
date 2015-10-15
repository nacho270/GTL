package ar.com.fwcommon.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.FWRuntimeException;
import ar.com.fwcommon.dao.api.local.DAOLocal;

/**
 * Dao Generico para la persistencia de las entidades del negocio.
 * 
 * 
 * 
 * @param <E>
 *            Entidad de negocio
 * @param <PK>
 *            Tipo de Primary key de la Entidad de negocio. Actualmente
 *            trabajamos con Integer y String
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class GenericDAO<E, PK> implements DAOLocal<E, PK> {

	private static Logger log = Logger.getLogger(GenericDAO.class);

	@PersistenceContext(name="GTLDS", unitName="GTLDS")
	protected EntityManager entityManager;

	protected Class<E> getType() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		Type type = actualTypeArguments[0];
		return (Class<E>) type;
	}

	public void mergeCollection(Collection entidades) {
		for (Object entidad : entidades) {
			getEntityManager().merge(entidad);
		}
	}

	public List<E> getByIds(List<PK> ids){
		if (ids.isEmpty())
			return new ArrayList<E>();
		Class<E> type = this.getType();
		Query query = getEntityManager().createQuery("from " + type.getName() + " where id in (:ids)" );
		query.setParameter("ids", ids);
		return query.getResultList();
	}
	
	public E getById(PK id) {
		
		return getEntityManager().find(this.getType(), id);
		/*
		// FIXME: return getEntityManager().find(type, id);//no funciona. debe
		// se un bug de hibernate en esta version (jboss 4.0.5GA).
		Class<E> type = this.getType();
		List list = getEntityManager().createQuery(
				"from " + type.getName() + " where id = " + id).getResultList();
		if (list.size() == 0)
			return null;
		return (E) list.get(0);
		*/
	}

	public List<E> getAll() {
		Class<E> type = this.getType();
		return getEntityManager()
				.createQuery("from " + type.getCanonicalName()).getResultList();
	}
	
	public Query getQueryAllRO() {
		Class<E> type = this.getType();
		Query query = getEntityManager()
				.createQuery("from " + type.getCanonicalName());
		query.setHint("org.hibernate.cacheable", Boolean.TRUE);
		query.setHint("org.hibernate.readOnly", Boolean.TRUE);
		return query;
	}
	
	
	

	public List<E> getAllOrderBy(String nameAttribute) {
		Class<E> type = this.getType();
		return getEntityManager().createQuery(
				"from " + type.getCanonicalName() + " ORDER BY "
						+ nameAttribute).getResultList();
	}

	/**
	 * Ejecuta el save y/o update
	 * 
	 * @param object
	 * @return
	 */
	public E save(E object) {
		E newEntity = getEntityManager().merge(object);
		//getEntityManager().flush();
		return newEntity;
	}

	/**
	 * Ejecuta el save y/o update para la colección de objetos,
	 * 
	 * @param objectList
	 */
	public void save(Collection<E> objectList) {
		// FIXME: Implementar
		// OARIAS - probando obtener la sesion  Hibernate porque con EJB no anda
		Session sesion = getHibernateSession(); 
		for(E objeto : objectList) {
			sesion.saveOrUpdate(objeto);
		}
	}

	public void persist(E object) {
		this.getEntityManager().persist(object);
	}

	/**
	 * Elimina fisicamente a la entidad.
	 * 
	 * @param object
	 * @throws FWException
	 */
	public void remove(E object) throws FWException {
		getEntityManager().remove(object);
	}

	/**
	 * Elimina fisicamente a la entidad por medio se su id.
	 * 
	 * @param id
	 */
	public void removeById(PK id) {
		//E object = getEntityManager().find(this.getType(), id);
		E object = getEntityManager().getReference(this.getType(), id);
		getEntityManager().remove(object);
		getEntityManager().flush();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<E> getBy(Map<String, Object> parametros) {

		Class<E> type = this.getType();
		StringBuffer hql = new StringBuffer();
		hql.append("from  " + type.getCanonicalName());

		Set<String> keys = parametros.keySet();
		boolean esPrimero = true;
		for (String key : keys) {
			if (esPrimero) {
				hql.append("    where " + key + " = :" + key);
				esPrimero = false;
			} else
				hql.append("   and  " + key + " = :" + key);
		}

		log.debug(hql);

		Query query = entityManager.createQuery(hql.toString());
		for (String key : keys) {
			query.setParameter(key, parametros.get(key));
		}
		return query.getResultList();
	}

	public List createQuery(String consulta, String[] parameterNames,
			Object[] parameterValues) {
		Query query = getEntityManager().createQuery(consulta);
		for (int i = 0; i < parameterNames.length; i++) {
			query.setParameter(parameterNames[i], parameterValues[i]);
		}
		return query.getResultList();
	}

	public List createQuery(String consulta, String[] parameterNames,
			Object[] parameterValues, int firstResult, int maxResults) {
		Query query = getEntityManager().createQuery(consulta);
		for (int i = 0; i < parameterNames.length; i++) {
			query.setParameter(parameterNames[i], parameterValues[i]);
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	public List<E> executeListResultQuery(Query query) throws FWException {
		List<E> lista = null;
		try {
			lista = query.getResultList();
		} catch (PersistenceException ex) {
			this.convertirPersistenceException(ex);
		}
		return lista;
	}

	public E executeSingleResultQuery(Query query) throws FWException {
		E e = null;
		try {
			e = (E) query.getSingleResult();
		} catch (PersistenceException ex) {
			this.convertirPersistenceException(ex);
		}
		return e;
	}

	protected void convertirPersistenceException(PersistenceException ex)
			throws FWException {
		// TODO: Convertir
		throw new FWException("Se ha producido un error de infraestructura", ex);
	}
	
	/**
	 * Obtiene la session de hibernate.
	 * @return
	 */
	protected Session getHibernateSession() {
		Session session=null;
		try{
			session = (Session)getEntityManager().getDelegate();//jboss 4.2
		}catch (ClassCastException cast){
			session = ((HibernateEntityManager)getEntityManager().getDelegate()).getSession();//jboss 4.0.5
		}
		return session;
	}

	public E getReferenceById(PK id){
		return getEntityManager().getReference(this.getType(), id);
	}
	
	public void clearSession() {
		getHibernateSession().flush();
		getHibernateSession().clear();
	}
	
	private static Map<String,DataSource> dsMap = new HashMap<String, DataSource>();
	
	public static Connection getConnection(String jndiDataSource) {
		Connection cnn = null;
		try {
			
			DataSource ds = dsMap.get(jndiDataSource);
			if (ds == null){
				InitialContext initialContext = new InitialContext();
				ds = (DataSource)initialContext.lookup(jndiDataSource);
				dsMap.put(jndiDataSource, ds);
			}

			try {
				cnn = ds.getConnection();
				//cnn.setAutoCommit(false);
			} catch(SQLException sqle) {
				log.error("Error obteniendo la conexión en GenericDAO: " + sqle.getMessage(),sqle);
				throw new FWRuntimeException("Error obteniendo la conexión en GenericDAO: " + sqle.getMessage(),sqle);
			}
		} catch(NamingException ne) {
			log.error("Error en el método getConnection() de la clase GenericDAO: " + ne.getMessage());
			throw new FWRuntimeException("Error en el método getConnection() de la clase GenericDAO: " + ne.getMessage(),ne);
		}
		return cnn;
	}
	
	
	
}