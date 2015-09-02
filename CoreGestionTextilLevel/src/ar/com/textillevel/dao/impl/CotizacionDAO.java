package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.CotizacionDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.Cotizacion;
import ar.com.textillevel.entidades.ventas.cotizacion.VersionListaDePrecios;

@Stateless
@SuppressWarnings("unchecked")
public class CotizacionDAO extends GenericDAO<Cotizacion, Integer> implements CotizacionDAOLocal {

	public Cotizacion getUltimaCotizacion(Cliente cliente) {
		Query query = getEntityManager().createQuery("SELECT cot " +
				 									"FROM Cotizacion AS cot " +
													"WHERE cot.cliente.id = :idCliente " + 
													"ORDER BY cot.numero DESC");
		query.setParameter("idCliente", cliente.getId());
		List<Cotizacion> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public Integer getUltimoNumeroCotizacion() {
		Query query = getEntityManager().createQuery("SELECT MAX(cot.numero) " +
													 "FROM Cotizacion AS cot ");
		Number singleResult = (Number)query.getSingleResult();
		if(singleResult == null) {
			return 0;
		} else {
			return singleResult.intValue();
		}
	}

	public Cotizacion getUltimaCotizacionParaVersion(VersionListaDePrecios version) {
		Query query = getEntityManager().createQuery("SELECT cot " +
													"FROM Cotizacion AS cot " +
													"WHERE cot.versionListaPrecio.id = :idVersion " + 
													"ORDER BY cot.numero DESC");
				query.setParameter("idVersion", version.getId());
				List<Cotizacion> resultList = query.getResultList();
				if(resultList.isEmpty()) {
		return null;
		} else {
			return resultList.get(0);
		}
	}

}