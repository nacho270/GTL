package ar.com.textillevel.dao.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.PrecioBaseEstampadoDAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioBaseEstampado;

@Stateless
@SuppressWarnings("unchecked")
public class PrecioBaseEstampadoDAO extends GenericDAO<PrecioBaseEstampado, Integer> implements PrecioBaseEstampadoDAOLocal {

	public List<PrecioBaseEstampado> getAllByDibujo(DibujoEstampado dibujo) {
		String hql = "FROM PrecioBaseEstampado pbe " +
				     "WHERE pbe.dibujo = :dibujo ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("dibujo", dibujo);
		return q.getResultList();
	}

}