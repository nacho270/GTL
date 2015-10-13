package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ClienteDeudaTO;

@Stateless
@SuppressWarnings("unchecked")
public class ClienteDAO extends GenericDAO<Cliente, Integer> implements ClienteDAOLocal {

	public List<Cliente> getAllOrderByName() {
		Query query = getEntityManager().createQuery("FROM Cliente AS cl " +
													 "LEFT JOIN FETCH cl.direccionFiscal dr " +
													 "LEFT JOIN FETCH dr.localidad " +
													 "LEFT JOIN FETCH cl.direccionReal df " +
													 "LEFT JOIN FETCH df.localidad " +
													 "ORDER BY cl.razonSocial");
		return query.getResultList();
	}

	public List<Cliente> getAllByRazonSocial(String razonSocial) {
		Query query = getEntityManager().createQuery("FROM Cliente AS cl WHERE cl.razonSocial LIKE :razon");
		query.setParameter("razon", "%"+razonSocial+"%");
		return query.getResultList();
	}

	public boolean existeNroCliente(Integer idCliente, Integer nroCliente) {
		Query query = getEntityManager().createQuery("SELECT 1 FROM Cliente AS cl WHERE cl.nroCliente = :nroCliente AND cl.id != :idCliente");
		query.setParameter("idCliente", idCliente == null ? -1 : idCliente);
		query.setParameter("nroCliente", nroCliente);
		return !query.getResultList().isEmpty();
	}

	public Integer getMaxNroCliente() {
		Query query = getEntityManager().createQuery("SELECT MAX(cl.nroCliente) FROM Cliente AS cl");
		Number number = ((Number)query.getSingleResult());
		if(number == null) {
			return null;
		} else {
			return number.intValue();
		}
	}

	public Cliente getClienteByNumero(Integer nroCliente) {
		Query query = getEntityManager().createQuery("FROM Cliente AS cl WHERE cl.nroCliente = :nroCliente ");
		query.setParameter("nroCliente", nroCliente);
		List<Cliente> clienteList = query.getResultList();
		if(clienteList.isEmpty()) {
			return null;
		} else if(clienteList.size() == 1) {
			return clienteList.get(0);
		} else {
			throw new RuntimeException("Inconsistencia en la DB. Existen 2 clientes con el mismo n�mero: " + nroCliente);
		}
	}

	public List<ClienteDeudaTO> getClientesConDeudaMayorA(BigDecimal monto) {
		monto = monto.negate();
		String hql = " SELECT cl.razonSocial, cu.saldo * -1 " +
					 " FROM Cliente cl, CuentaCliente cu " +
					 " WHERE cu.cliente.id = cl.id AND cu.saldo < :monto " +
					 " ORDER BY cu.saldo*-1 DESC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("monto", monto);
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

	public Set<String> getCuits() {
		Query query = getEntityManager().createQuery("SELECT cl.cuit FROM Cliente AS cl");
		Set<String> lista = new LinkedHashSet<String>(query.getResultList());
		query = getEntityManager().createQuery("SELECT c.cuit FROM Cheque AS c");
		lista.addAll(query.getResultList());
		return lista;
	}

	public List<Cliente> getClienteByCUIT(String cuit, Integer idCliente) {
		Query query = getEntityManager().createQuery("FROM Cliente AS cl WHERE cl.cuit = :cuit AND cl.id != :idCliente");
		query.setParameter("cuit", cuit);
		query.setParameter("idCliente", idCliente);
		return query.getResultList();
	}

}