package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;

@Stateless
public class CorreccionFacturaProveedorDAO extends GenericDAO<CorreccionFacturaProveedor, Integer> implements CorreccionFacturaProveedorDAOLocal {

	public CorreccionFacturaProveedor getByIdEager(Integer id) {
		CorreccionFacturaProveedor cfp = getById(id);
		cfp.getFacturas().size();
		cfp.getProveedor().getNombreCorto();
		List<ItemCorreccionFacturaProveedor> itemsCorreccion = cfp.getItemsCorreccion();
		for(ItemCorreccionFacturaProveedor icfp : itemsCorreccion) {
			icfp.getImpuestos().size();
		}
		return cfp;
	}

	@SuppressWarnings("unchecked")
	public List<NotaDebitoProveedor> getNotasDeDebitoImpagas(Integer idProveedor) {
		String hql = " SELECT nd FROM NotaDebitoProveedor nd WHERE nd.montoFaltantePorPagar > 0 AND nd.proveedor.id = :idProv ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProv", idProveedor);
		List<NotaDebitoProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<NotaCreditoProveedor> getNotasCreditoNoUsadas(Integer idProveedor) {
		String hql = " SELECT nc FROM NotaCreditoProveedor nc WHERE nc.montoSobrantePorUtilizar > 0 AND nc.proveedor.id = :idProv ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProv", idProveedor);
		List<NotaCreditoProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<CorreccionFacturaProveedor> getCorreccionListByFactura( FacturaProveedor factura) {
		String hql = " SELECT c FROM CorreccionFacturaProveedor c WHERE :factura IN ELEMENTS(c.facturas) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("factura", factura);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> calcularInfoFacturasIvaCompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor) {
		String queryStr = "select imp.P_ID, " +
						  "	  itf.A_CANTIDAD * itf.A_PRECIO_UNITARIO * itf.a_factor_conv_moneda * (imp.a_porc_desc/100), " +
						  "	  itf.F_FACTURA_PROV_P_ID, " +
						  "	  f.a_monto_total, " +
						  "	  f.a_fecha_ingreso, " +
						  "	  0, " + //Las correcciones no tienen subtotal
						  "	  f.A_NRO_CORRECCION, " +
						  "	  f.DTYPE, " +
						  "	  f.A_IMP_VARIOS, " + 
						  "	  f.A_PERCEP_IVA, " + 
						  "	  prov.a_razon_social, " +
						  "	  prov.a_cuit, " +
						  "	  prov.A_ID_POSICION_IVA " +
						  " from T_ITEM_CORRECC_FACT_PROV itf " +
						  "		inner join T_CORRECCION_FACTURA_PROV f on f.p_id = itf.F_FACTURA_PROV_P_ID " +
						  "		inner join t_proveedores prov on prov.p_id = f.F_PROV_P_ID " +
						  "		left join T_ITEM_CORR_FACT_PROV_IMPUESTO asoc on itf.p_id = asoc.F_ITEM_FACT_PROV_P_ID " +
						  "	 	left join T_IMPUESTO_ITEM_PROVEEDOR imp on imp.p_id = asoc.F_IMPUESTO_P_ID " +
						  " where 1=1 " +
						  (proveedor == null ? "" : " AND prov.p_id  = :idProveedor") +
						  (fechaDesde == null ? "" : " AND f.a_fecha_ingreso  >= :fechaDesde") +
						  (fechaHasta == null ? "" : " AND f.a_fecha_ingreso  <= :fechaHasta") +
						  "	order by itf.f_factura_prov_p_id ";
		Query nativeQuery = getEntityManager().createNativeQuery(queryStr);
		if(proveedor != null) {
			nativeQuery.setParameter("idProveedor", proveedor.getId());
		}
		if(fechaDesde != null) {
			nativeQuery.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta != null) {
			nativeQuery.setParameter("fechaHasta", DateUtil.getManiana(fechaHasta));
		}
		List<Object[]> resultList = nativeQuery.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public boolean existeNroCorreccionByProveedor(Integer idCorreccion, String nroCorreccion, Integer idProveedor) {
		String hql = " SELECT c " +
		 			 " FROM CorreccionFacturaProveedor c " +
		 			 " WHERE c.proveedor.id = :idProveedor AND c.nroCorreccion = :nroCorreccion AND c.id != :idCorreccion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor",idProveedor);
		q.setParameter("nroCorreccion",nroCorreccion);
		q.setParameter("idCorreccion",idCorreccion == null ? -1 : idCorreccion);
		List<CorreccionFacturaProveedor> lista = q.getResultList();
		return !lista.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public CorreccionFacturaProveedor obtenerNotaDeDebitoByCheque(Cheque c) {
		String sql = " SELECT F_FACTURA_PROV_P_ID FROM T_ITEM_CORRECC_FACT_PROV WHERE F_CHEQUE_P_ID = :idCheque ";
		Query q = getEntityManager().createNativeQuery(sql);
		q.setParameter("idCheque", c.getId());
		List<Object> lista = q.getResultList();
		if(lista.isEmpty()){
			return null;
		}
		Integer idCorreccion = NumUtil.toInteger(lista.get(0));
		return getById(idCorreccion);
	}

	@SuppressWarnings("unchecked")
	public List<NotaCreditoProveedor> getAllNotaCreditoList(Integer idProveedor) {
		String hql = " SELECT nc FROM NotaCreditoProveedor nc WHERE nc.proveedor.id = :idProv ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProv", idProveedor);
		List<NotaCreditoProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return Collections.emptyList();
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<NotaDebitoProveedor> getAllNotaDebitoList(Integer idProveedor) {
		String hql = " SELECT nd FROM NotaDebitoProveedor nd WHERE nd.proveedor.id = :idProv ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProv", idProveedor);
		List<NotaDebitoProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return Collections.emptyList();
		}
		return lista;
	}

}