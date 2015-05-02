package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.CuentaDAOLocal;
import ar.com.textillevel.entidades.cuenta.Cuenta;
import ar.com.textillevel.entidades.cuenta.CuentaBanco;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.CuentaPersona;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;

@Stateless
@SuppressWarnings("unchecked")
public class CuentaDAO extends GenericDAO<Cuenta, Integer> implements CuentaDAOLocal{

	public CuentaCliente getCuentaClienteByIdCliente(Integer idCliente) {
		String hql = "SELECT c From CuentaCliente c join fetch c.cliente cl WHERE cl.id = :idCliente ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		List<CuentaCliente> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public CuentaProveedor getCuentaProveedorByIdProveedor(Integer idProveedor) {
		String hql = "SELECT c From CuentaProveedor c WHERE c.proveedor.id = :idProveedor ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		List<CuentaProveedor> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public CuentaBanco getCuentaBancoByIdBanco(Integer idBanco) {
		String hql = "SELECT c From CuentaBanco c WHERE c.banco.id = :idBanco";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idBanco", idBanco);
		List<CuentaBanco> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public CuentaPersona getCuentaPersonaByIdPersona(Integer idPersona) {
		String hql = "SELECT c From CuentaPersona c WHERE c.persona.id = :idPersona";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idPersona", idPersona);
		List<CuentaPersona> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}
