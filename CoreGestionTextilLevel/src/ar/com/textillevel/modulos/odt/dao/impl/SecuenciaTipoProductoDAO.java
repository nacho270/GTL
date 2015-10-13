package ar.com.textillevel.modulos.odt.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.dao.api.local.SecuenciaTipoProductoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.secuencia.fw.SecuenciaAbstract;
import ar.com.textillevel.modulos.odt.entidades.secuencia.generica.SecuenciaTipoProducto;

@Stateless
public class SecuenciaTipoProductoDAO extends GenericDAO<SecuenciaTipoProducto, Integer> implements SecuenciaTipoProductoDAOLocal{

	@SuppressWarnings("unchecked")
	public List<SecuenciaTipoProducto> getByTipoProducto(ETipoProducto tipoProducto) {
		String hql = " SELECT stp FROM SecuenciaTipoProducto stp WHERE stp.idTipoProducto = :idTipoProducto ";
		return getEntityManager().createQuery(hql).setParameter("idTipoProducto", tipoProducto.getId()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T extends SecuenciaAbstract<?, ?>> List<T> getAllByTipoProductoYCliente(ETipoProducto tipoProducto, Cliente cliente, Boolean incluirDefault, Class<T> clase) {
		String hql = " SELECT stp FROM " + clase.getCanonicalName() + " stp " +
					 " WHERE stp.idTipoProducto = :idTipoProducto " +
					 (cliente != null ?" AND ( stp.cliente.id = :idCliente " : " AND stp.cliente is null ")+ 
					 (incluirDefault?" OR stp.cliente IS NULL ": " ")+" ) ";
		Query query = getEntityManager().createQuery(hql);
		query.setParameter("idTipoProducto", tipoProducto.getId());
		if(cliente != null){
			query.setParameter("idCliente", cliente.getId());
		}
		return query.getResultList();
	}
}
