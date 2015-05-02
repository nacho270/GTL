package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.PrecioMateriaPrimaDAOLocal;
import ar.com.textillevel.entidades.enums.ETipoMateriaPrima;
import ar.com.textillevel.entidades.enums.ETipoVentaStock;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;

@Stateless
@SuppressWarnings("unchecked")
public class PrecioMateriaPrimaDAO extends GenericDAO<PrecioMateriaPrima, Integer> implements PrecioMateriaPrimaDAOLocal{

	public List<PrecioMateriaPrima> getAllWithStockByProveedorOrderByMateriaPrima(Integer idProveedor) {
		String hql = " SELECT pmp FROM PrecioMateriaPrima pmp WHERE pmp.preciosProveedor.proveedor.id = :idProveedor AND pmp.stockActual > 0 " +
					 " ORDER BY pmp.materiaPrima.descripcion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", idProveedor);
		return q.getResultList();
	}

	public List<PrecioMateriaPrima> getAllWithStockInicialDispByArticulo(Integer idArticulo) {
		List<PrecioMateriaPrima> resultList = new ArrayList<PrecioMateriaPrima>();
		String hql = " SELECT pmp FROM PrecioMateriaPrima pmp WHERE pmp.stockInicialDisponible > 0 " +
					 " ORDER BY pmp.materiaPrima.descripcion ";
		Query q = getEntityManager().createQuery(hql);
		List<PrecioMateriaPrima> resultQueryList = q.getResultList();
		for(PrecioMateriaPrima pmp : resultQueryList) {
			if(pmp.getMateriaPrima() instanceof Tela) {
				if(((Tela)pmp.getMateriaPrima()).getArticulo().getId().equals(idArticulo)) {
					resultList.add(pmp);
				}
			}
		}
		return resultList;
	}

	public List<PrecioMateriaPrima> getAllByProveedorOrderByMateriaPrima(Proveedor proveedor) {
		String hql = " SELECT pmp FROM PrecioMateriaPrima pmp WHERE pmp.preciosProveedor.proveedor.id = :idProveedor " +
					 " ORDER BY pmp.materiaPrima.descripcion ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idProveedor", proveedor.getId());
		return q.getResultList();
	}

	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByTipo(ETipoMateriaPrima tipo) {
		String hql = " SELECT m FROM PrecioMateriaPrima m LEFT JOIN FETCH m.materiaPrima LEFT JOIN FETCH m.preciosProveedor LEFT JOIN FETCH m.preciosProveedor.proveedor ORDER BY m.materiaPrima.descripcion ";
		Query q = getEntityManager().createQuery(hql);
		List<PrecioMateriaPrima> lista = q.getResultList();
		List<PrecioMateriaPrima> ret = new ArrayList<PrecioMateriaPrima>();
		for(PrecioMateriaPrima p : lista){
			if(p.getMateriaPrima().getTipo()==tipo ){
				ret.add(p);
			}
		}
		return ret;
	}
	
	public List<PrecioMateriaPrima> getPrecioMateriaConStockPrimaByTipo(ETipoMateriaPrima tipo) {
		String hql = " SELECT m FROM PrecioMateriaPrima m WHERE m.stockActual > 0 ORDER BY m.materiaPrima.descripcion ";
		Query q = getEntityManager().createQuery(hql);
		List<PrecioMateriaPrima> lista = q.getResultList();
		List<PrecioMateriaPrima> ret = new ArrayList<PrecioMateriaPrima>();
		for(PrecioMateriaPrima p : lista){
			if(tipo ==null || (tipo !=null && p.getMateriaPrima().getTipo()==tipo )){
				ret.add(p);
			}
		}
		return ret;
	}

	public List<PrecioMateriaPrima> getPrecioMateriaPrimaByIdsMateriasPrimas(List<Integer> idsMateriasPrimas) {
		String hql = " SELECT pmp FROM PrecioMateriaPrima pmp WHERE pmp.materiaPrima.id IN (:idsMateriasPrimas) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idsMateriasPrimas", idsMateriasPrimas);
		return q.getResultList();
	}

	public List<PrecioMateriaPrima> getPreciosMateriaPrimaByTipoVentaStock(ETipoVentaStock tipoVentaStock) {
		String queryMateriasPrimas = " SELECT id FROM " + tipoVentaStock.getDescripcion();
		Query q = getEntityManager().createQuery(queryMateriasPrimas);
		List<Integer> idsMateriasPrimas =  q.getResultList();
		if(idsMateriasPrimas!=null && !idsMateriasPrimas.isEmpty()){
			String hql = " SELECT pmp FROM PrecioMateriaPrima pmp WHERE pmp.materiaPrima.id IN (:idsMateriasPrimas) ";
			Query q2 = getEntityManager().createQuery(hql);
			q2.setParameter("idsMateriasPrimas", idsMateriasPrimas);
			return q2.getResultList();
		}else{
			return Collections.emptyList();
		}
	}

	public BigDecimal getStockByPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima) {
		PrecioMateriaPrima p = getById(precioMateriaPrima.getId());
		if(p!=null && p.getStockActual()!=null){
			return p.getStockActual();
		}else{
			return new BigDecimal(0d);
		}
	}

	@SuppressWarnings("rawtypes")
	public BigDecimal getPrecioMasRecienteTela(Integer idArticulo) {
		Query q = getEntityManager().createNativeQuery("SELECT PMP.A_PRECIO " +
													   "FROM T_PRECIO_MATERIA_PRIMA PMP " +
													   "INNER JOIN T_MATERIA_PRIMA MP ON MP.P_ID = PMP.F_MATERIA_PRIMA_P_ID " +
													   "WHERE MP.F_ARTICULO_P_ID = :idArticulo " +
													   "ORDER BY PMP.A_FECHA_ULT_MODIF DESC");
		q.setParameter("idArticulo", idArticulo);
		q.setMaxResults(1);
		List resultList = q.getResultList();
		if(resultList.isEmpty()) {
			return null;
		} else {
			Number precio = (Number)resultList.get(0);
			return precio == null ? null : new BigDecimal(precio.doubleValue());
		}
	}

}