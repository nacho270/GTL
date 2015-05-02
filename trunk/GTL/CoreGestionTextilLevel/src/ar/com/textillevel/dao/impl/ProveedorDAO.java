package ar.com.textillevel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.ProveedorDAOLocal;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.gente.Rubro;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;

@Stateless
public class ProveedorDAO extends GenericDAO<Proveedor, Integer> implements ProveedorDAOLocal{

	@SuppressWarnings("unchecked")
	public List<Proveedor> getAllOrderByName() {
		Query query = getEntityManager().createQuery("FROM Proveedor AS p " +
				 "LEFT JOIN FETCH p.direccionFiscal dr " +
				 "LEFT JOIN FETCH dr.localidad " +
				 "LEFT JOIN FETCH p.direccionReal df " +
				 "LEFT JOIN FETCH df.localidad " +
				 "ORDER BY p.razonSocial");
		return query.getResultList();
	}

	public boolean existeProveedor(String razonSocial, String cuit) {
		Query query = getEntityManager().createQuery("FROM Proveedor p WHERE p.razonSocial = :razonSocial OR p.cuit = :cuit ");
		query.setParameter("razonSocial", razonSocial);
		query.setParameter("cuit", cuit);
		return !query.getResultList().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> getAllByRazonSocial(String razonSocial) {
		Query query = getEntityManager().createQuery("FROM Proveedor AS p WHERE p.razonSocial LIKE :razon");
		query.setParameter("razon", "%"+razonSocial+"%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> getAllByRazonSocialAndRubro(String razonSocial, Rubro rubro) {
		String q = "FROM Proveedor AS p WHERE p.razonSocial LIKE :razon "+
					(rubro!=null? " AND p.rubro.id = :idTipoRubro ":"");
		Query query = getEntityManager().createQuery(q);
		query.setParameter("razon", "%"+razonSocial+"%");
		if(rubro!=null){
			query.setParameter("idTipoRubro", rubro.getId());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> getAllByRubroOrderByName(Rubro rubro) {
		String q = " FROM Proveedor AS p " +  
		(rubro!=null ?" WHERE p.rubro.id = :idRubro ":"")+ " ORDER BY p.razonSocial";
		Query query = getEntityManager().createQuery(q);
		if(rubro!=null){
			query.setParameter("idRubro", rubro.getId());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> getProveedorByNombreCorto(String nombreCorto) {
		String q = "FROM Proveedor AS p WHERE p.nombreCorto LIKE :nombreCorto OR p.razonSocial LIKE :nombreCorto";
					Query query = getEntityManager().createQuery(q);
		query.setParameter("nombreCorto", "%"+nombreCorto+"%");
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Proveedor> getProveedorByRazonSocial(String razonSocial) {
		String q = "FROM Proveedor AS p WHERE p.razonSocial LIKE :razon ";
		Query query = getEntityManager().createQuery(q);
		query.setParameter("razon", "%"+razonSocial+"%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ClienteDeudaTO> getProveedoresALosQueSeLesDebe() {
		String hql = " SELECT p.razonSocial, cp.saldo *-1 " +
					 " FROM Proveedor p, CuentaProveedor cp " +
					 " WHERE cp.proveedor.id = p.id AND cp.saldo < 0" +
					 " ORDER BY  cp.saldo *-1 DESC ";
					Query q = getEntityManager().createQuery(hql);
		List<Object[]> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			List<ClienteDeudaTO> clientes = new ArrayList<ClienteDeudaTO>();
			ClienteDeudaTO cld = null;
			for(Object[] o : lista){
				cld = new ClienteDeudaTO();
				cld.setRazonSocial((String)o[0]);
				cld.setDeuda(NumUtil.toBigDecimal(o[1]));
				clientes.add(cld);
			}
			return clientes;
		}
		return null;
	}
}
