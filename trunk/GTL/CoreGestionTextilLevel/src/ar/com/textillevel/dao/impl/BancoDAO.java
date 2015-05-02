package ar.com.textillevel.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.BancoDAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;

@Stateless
public class BancoDAO extends GenericDAO<Banco, Integer> implements BancoDAOLocal{
	
	@SuppressWarnings("unchecked")
	public Banco getBancoByCodigo(Integer codigoBanco) {
		String hql = " SELECT b FROM Banco b WHERE b.codigoBanco = :codigoBanco ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("codigoBanco", codigoBanco);
		List<Banco> list = q.getResultList();
		if(list == null || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
}
