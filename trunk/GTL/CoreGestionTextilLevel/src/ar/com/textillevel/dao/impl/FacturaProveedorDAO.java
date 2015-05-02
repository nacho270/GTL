package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RemitoEntradaProveedor;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.to.ivacompras.ETipoDocumentoProveedor;

@Stateless
public class FacturaProveedorDAO extends GenericDAO<FacturaProveedor, Integer> implements FacturaProveedorDAOLocal {

	public FacturaProveedor getByIdEager(Integer idFactura) {
		FacturaProveedor fp = getById(idFactura);
		for(ItemFacturaProveedor ifp : fp.getItems()) {
			ifp.getImpuestos().size();
		}
		fp.getRemitoList().size();
		return fp;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturaListByParams(Integer idProveedor, List<Integer> idPrecioMatPrimaList, Date fechaDesde, Date fechaHasta) {
		Query query = getEntityManager().createQuery(
				"SELECT f " +
				"FROM FacturaProveedor f " +
				"WHERE f.proveedor.id = :idProveedor " +
				"AND f.fechaIngreso BETWEEN :fechaDesde AND :fechaHasta " +
				(idPrecioMatPrimaList.isEmpty() ? "" : 
				(" AND EXISTS (SELECT 1 FROM ItemFacturaMateriaPrima ifmpp " +
				"			   WHERE ifmpp IN ELEMENTS(f.items) AND ifmpp.precioMateriaPrima.id IN (:idPrecioMatPrimaList)" +
				"			) "))
				);
		if(!idPrecioMatPrimaList.isEmpty()) {
			query.setParameter("idPrecioMatPrimaList", idPrecioMatPrimaList);
		}
		query.setParameter("idProveedor", idProveedor);
		query.setParameter("fechaDesde", fechaDesde);
		query.setParameter("fechaHasta", fechaHasta);
		List<FacturaProveedor> resultList = query.getResultList();
		for(FacturaProveedor f : resultList) {
			f.getItems().size();
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor) {
		String hql = " SELECT f FROM FacturaProveedor f " +
					 " WHERE f.montoFaltantePorPagar > 0 AND f.proveedor.id = :idProveedor " +
					 " ORDER BY f.fechaIngreso ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor",idProveedor);
		List<FacturaProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturasImpagas(Integer idProveedor, List<Integer> idsExcluidos) {
		String hql = " SELECT f FROM FacturaProveedor f  " +
					 " WHERE f.montoFaltantePorPagar > 0 AND f.proveedor.id = :idProveedor "+
					 (idsExcluidos !=null && !idsExcluidos.isEmpty()?" AND f.id NOT IN (:idsExcluidos) " : " ") +
					 " ORDER BY f.fechaIngreso ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor",idProveedor);
		if (idsExcluidos !=null && !idsExcluidos.isEmpty()){
			q.setParameter("idsExcluidos", idsExcluidos);
		}
		List<FacturaProveedor> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturasParaNotasCredito(Integer idProveedor) {
		/*
		 * ANTES TRAIA SOLO LAS QUE TENIAN UN REMITO.
		String hql = " SELECT f FROM FacturaProveedor f JOIN f.remitoList " +
		 " WHERE EXISTS (SELECT 1 " +
		 "			   FROM RemitoEntradaProveedor rep " +
		 "			   WHERE rep IN ELEMENTS(f.remitoList) AND rep.proveedor.id = :idProveedor" +
		 "		  	   )  " +
		 " ORDER BY f.fechaIngreso ASC ";
		 */
		String hql = " SELECT f FROM FacturaProveedor f " +
		 " WHERE f.proveedor.id = :idProveedor " +
		 " ORDER BY f.fechaIngreso ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor",idProveedor);
		List<FacturaProveedor> lista = q.getResultList();
		for(FacturaProveedor fp : lista) {
			for(ItemFacturaProveedor item : fp.getItems()) {
				item.getImpuestos().size();
			}
		}
		if(lista == null || (lista!=null && lista.isEmpty())){
			return Collections.EMPTY_LIST;
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> calcularInfoFacturasIvaCompras(Date fechaDesde, Date fechaHasta, Proveedor proveedor) {
		String queryStr = "select imp.P_ID, " +
						  "	  itf.A_CANTIDAD * itf.A_PRECIO_UNITARIO * itf.a_factor_conv_moneda * (imp.a_porc_desc/100), " +
						  "	  itf.f_factura_prov_p_id, " +
						  "	  f.a_monto_total, " +
						  "	  f.a_fecha_ingreso, " +
						  "	  f.A_MONTO_SUBTOTAL, " +
						  "	  f.A_NRO_FACTURA, " +
						  "	  '" + ETipoDocumentoProveedor.FACTURA.getId() + "', " +
						  "	  f.A_IMP_VARIOS, " +
						  "	  f.A_PERCEP_IVA, " +
						  "	  prov.a_razon_social, " +
						  "	  prov.a_cuit, " +
						  "	  prov.A_ID_POSICION_IVA " +
						  " from T_ITEM_FACTURA_IMPUESTO asoc " +
						  "		inner join T_ITEM_FACTURA_PROV itf on itf.p_id = asoc.F_ITEM_FACTURA_P_ID " +
						  "	 	inner join T_IMPUESTO_ITEM_PROVEEDOR imp on imp.p_id = asoc.F_IMPUESTO_P_ID " +
						  "		inner join t_factura_proveedor f on f.p_id = itf.f_factura_prov_p_id " +
						  "		inner join t_proveedores prov on prov.p_id = f.F_PROV_P_ID " +
						  " where 1=1 " +
						  (proveedor == null ? "" : " AND prov.p_id  = :idProveedor") +
						  (fechaDesde == null ? "" : " AND f.a_fecha_ingreso  >= :fechaDesde") +
						  (fechaHasta == null ? "" : " AND f.a_fecha_ingreso  <= :fechaHasta") +
						  "	order by imp.P_ID";
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
		List<Object[]> resultList = (List<Object[]>)nativeQuery.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public boolean existeNroFacturaByProveedor(Integer idFactura, String nroFactura, Integer idProveedor) {
		String hql = " SELECT f " +
					 " FROM FacturaProveedor f " +
		 			 " WHERE f.proveedor.id = :idProveedor AND f.nroFactura = :nroFactura AND f.id != :idFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor",idProveedor);
		q.setParameter("nroFactura",nroFactura);
		q.setParameter("idFactura",idFactura == null ? -1 : idFactura);
		List<FacturaProveedor> lista = q.getResultList();
		return !lista.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturasByRemito(RemitoEntradaProveedor rep) {
		String hql = "  SELECT f "
					+ " FROM FacturaProveedor f "
					+ " WHERE :rep IN ELEMENTS(f.remitoList) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("rep", rep);
		List<FacturaProveedor> lista = q.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getAllByIdProveedorList(Integer idProveedor) {
		String hql = "  SELECT f " + 
					 " FROM FacturaProveedor f " + 
					" WHERE f.proveedor.id = :idProv ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProv", idProveedor);
		List<FacturaProveedor> lista = q.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<FacturaProveedor> getFacturasConMateriaPrimaIBC(Integer idProveedor) {
		List<FacturaProveedor> facturas = new ArrayList<FacturaProveedor>();
		String hql =  "SELECT ifp.F_FACTURA_PROV_P_ID "
					+ "FROM T_ITEM_FACTURA_PROV ifp "
					+ "INNER JOIN T_FACTURA_PROVEEDOR f ON f.P_ID = ifp.F_FACTURA_PROV_P_ID "
					+ "INNER JOIN T_PRECIO_MATERIA_PRIMA ipmp ON ipmp.P_ID = ifp.F_PRECIO_MAT_PRIMA_P_ID "
					+ "INNER JOIN T_MATERIA_PRIMA mp ON mp.P_ID = ipmp.F_MATERIA_PRIMA_P_ID "
					+ "WHERE f.F_PROV_P_ID = :idProveedor AND  mp.TIPO = 'IBC' AND "
					+ "NOT EXISTS (SELECT 1 "
							+ "FROM T_CORR_FACT_PROV_ASOC ASOC "
							+ "INNER JOIN T_CORRECCION_FACTURA_PROV CF ON CF.P_ID = ASOC.F_CORR_FACT_PROV_P_ID "
							+ "WHERE  ASOC.F_FACT_PROV_P_ID = ifp.F_FACTURA_PROV_P_ID AND CF.DTYPE='NC') " 
					+ "GROUP BY ifp.F_FACTURA_PROV_P_ID";
		Query q = getEntityManager().createNativeQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		List<Integer> idsFactra = q.getResultList();
		for(Integer id : idsFactra) {
			facturas.add(getById(id));
		}
		return facturas;
	}

}