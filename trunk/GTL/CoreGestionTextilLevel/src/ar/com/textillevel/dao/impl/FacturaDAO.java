package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.clarin.fwjava.dao.impl.GenericDAO;
import ar.clarin.fwjava.util.NumUtil;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.gente.Cliente;

@Stateless
public class FacturaDAO extends GenericDAO<Factura, Integer> implements FacturaDAOLocal {

	public Integer getLastNumeroFactura(ETipoFactura tipoFactura) {
		Query query = getEntityManager().createQuery("SELECT MAX(f.nroFactura) FROM Factura f WHERE f.idTipoFactura = :idTipo ");
		query.setParameter("idTipo", tipoFactura.getId());
		Number lastNumeroFactura = (Number)query.getSingleResult();
		query = getEntityManager().createQuery("SELECT MAX(c.nroFactura) FROM CorreccionFactura c WHERE c.idTipoFactura = :idTipo");
		query.setParameter("idTipo", tipoFactura.getId());
		Number lastNumeroCorreccion = (Number)query.getSingleResult();
		if(lastNumeroFactura==null){
			if(lastNumeroCorreccion==null){
				return null;
			}else{
				return lastNumeroCorreccion.intValue();
			}
		}else{
			if(lastNumeroCorreccion==null){
				return lastNumeroFactura.intValue();
			}else{
				return Math.max(lastNumeroCorreccion.intValue(),lastNumeroFactura.intValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Factura getByNroFacturaConCorrecciones(Integer nroFactura) {
		String hql = " SELECT f FROM Factura f WHERE f.nroFactura = :nroFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroFactura", nroFactura);
		List<Factura> list = q.getResultList();
		if(!list.isEmpty()){
			//list.get(0).getCorreciones().size();
			Factura factura = list.get(0);
			if(factura.getRemitos()!=null){
				factura.getRemitos().size();
			}
			return factura;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Factura getByNroFacturaConItems(Integer nroFactura) {
		String hql = " SELECT f FROM Factura f WHERE f.nroFactura = :nroFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroFactura", nroFactura);
		List<Factura> list = q.getResultList();
		if(!list.isEmpty()){
			list.get(0).getItems().size();
			if(list.get(0).getCliente()!=null){
				list.get(0).getCliente().getCelular();
			}
			if(list.get(0).getRemitos()!=null){
				list.get(0).getRemitos().size();
			}
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Factura> getFacturaImpagaListByClient(Integer idCliente) {
		String hql = "SELECT f " +
					 "FROM Factura f " +
					 "WHERE f.cliente.id = :idCliente AND " +
					 "f.montoFaltantePorPagar > 0 AND f.idEstado!=3 " +
					 "ORDER BY f.fechaEmision, f.id ASC "; 
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		List<Factura> resultList = q.getResultList();
		if(resultList!=null && !resultList.isEmpty()){
			for(Factura f : resultList){
				if(f.getRemitos()!=null){
					f.getRemitos().size();
				}
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public Factura getByIdEager(Integer id) {
		String hql = " SELECT f FROM Factura f WHERE f.id = :idFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idFactura", id);
		List<Factura> list = q.getResultList();
		if(!list.isEmpty()){
			if(list.get(0).getCliente()!=null){
				list.get(0).getCliente().getCelular();
			}
			if(list.get(0).getRemitos()!=null){
				list.get(0).getRemitos().size();
			}
			list.get(0).getItems().size();
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Timestamp> getFacturaAnteriorYPosterior(Integer nroFactura, ETipoFactura tipoFactura) {
		String hql = " SELECT f.fechaEmision FROM Factura f " +
					 " WHERE (f.nroFactura = :nroAnterior OR f.nroFactura = :nroPosterior) " +
					 " AND f.idTipoFactura = :idTipoFactura " +
					 " ORDER BY f.nroFactura ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroAnterior", nroFactura-1);
		q.setParameter("nroPosterior", nroFactura+1);
		q.setParameter("idTipoFactura", tipoFactura.getId());
		List<Timestamp> fechasFacturas= q.getResultList();
		
		hql = " SELECT c.fechaEmision FROM CorreccionFactura c " +
			  " WHERE (nroFactura = :nroAnterior OR c.nroFactura = :nroPosterior) " +
			  " AND c.idTipoFactura = :idTipoFactura " +
			  " ORDER BY nroFactura ASC ";
		q = getEntityManager().createQuery(hql);
		q.setParameter("nroAnterior", nroFactura-1);
		q.setParameter("nroPosterior", nroFactura+1);
		q.setParameter("idTipoFactura", tipoFactura.getId());
		List<Timestamp> fechasCorreccion= q.getResultList();

		List<Timestamp> listaTotal = new ArrayList<Timestamp>();
		listaTotal.addAll(fechasCorreccion);
		listaTotal.addAll(fechasFacturas);
		Collections.sort(listaTotal, new Comparator<Timestamp>() {
			public int compare(Timestamp o1, Timestamp o2) {
				return o1.compareTo(o2);
			}
		});
		
		List<Timestamp> listaRet = new ArrayList<Timestamp>();
		if(listaTotal.size()>0){
			listaRet.add(listaTotal.get(0));
			if(listaTotal.size()>1){
				listaRet.add(listaTotal.get(1));
			}
			return listaRet;
		}
		return listaRet;
	}

	@SuppressWarnings("unchecked")
	public boolean facturaYaTieneRecibo(Factura factura) {
		String hql = " SELECT 1 FROM PagoRecibo pr WHERE pr.factura.id = :idFactura ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idFactura", factura.getId());
		List<Object> list = q.getResultList();
		if(list.isEmpty()){
			return false;
		}else{
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Factura> getFacturasEntreFechas(Date fechaDesde, Date fechaHasta, ETipoFactura tipoFactura, Cliente cliente) {
		String hql = " SELECT f FROM Factura f " +
					 " WHERE f.fechaEmision BETWEEN :fechaDesde AND :fechaHasta "+
					(tipoFactura==null?" ": " AND f.idTipoFactura = :idTipoFactura  ")+
					(cliente !=null?" AND ( f.cliente IS NOT NULL AND f.cliente.id = :idCliente) " : " ")+
					//" ORDER BY f.fechaEmision, f.idTipoFactura, f.id ";
					" ORDER BY f.nroFactura, f.fechaEmision, f.idTipoFactura, f.id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		if(tipoFactura != null){
			q.setParameter("idTipoFactura", tipoFactura.getId());
		}
		if(cliente !=null){
			q.setParameter("idCliente", cliente.getId());
		}
		List<Factura> facturas = q.getResultList();
		if(facturas!=null && facturas.size()>0){
			List<Factura> facturasRet = new ArrayList<Factura>();
			for(Factura f : facturas){
				if(f.getEstadoFactura()==EEstadoFactura.ANULADA){
					facturasRet.add(f);
				}else if(f.getRemitos()!=null){
					if(cliente == null || (cliente!=null && f.getCliente().getId().equals(cliente.getId()))){
						f.getItems().size();
						facturasRet.add(f);
					}
				}else if(f.getCliente()!=null){
					if(cliente== null || (cliente!=null && f.getCliente().getId().equals(cliente.getId()))){
						f.getItems().size();
						facturasRet.add(f);
					}
				}
			}
			return facturasRet;
		}
		return null;
	}

	public boolean esLaUltimaFactura(Factura factura) {
		return  !(factura.getNroFactura().compareTo(getLastNumeroFactura(factura.getTipoFactura()))<0);
	}

	@SuppressWarnings("unchecked")
	public List<Factura> getFacturaByRemitoSalida(Integer idRemitoSalida) {
		String hql =  " SELECT rem.factura " +
					  " FROM RemitoSalida rem " + 
					  " WHERE rem.id = :idRemitoSalida " +
					  " ORDER BY rem.fechaEmision ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idRemitoSalida", idRemitoSalida);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Factura> getAllByIdClienteList(Integer idCliente) {
		String hql = "SELECT f " +
					 "FROM Factura f " +
					 "WHERE f.cliente.id = :idCliente AND f.idEstado!=3 " +
					 "ORDER BY f.fechaEmision, f.nroFactura ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		List<Factura> facturas = q.getResultList();
		if(facturas!=null && !facturas.isEmpty()){
			for(Factura f : facturas){
				if(f.getRemitos()!=null){
					f.getRemitos().size();
				}
			}
		}
		return facturas;
	}

	@SuppressWarnings("unchecked")
	public List<Factura> getAllFacturasByCliente(Integer idCliente) {
		String hql = "SELECT f " +
					 "FROM Factura f " +
					 "WHERE f.cliente.id = :idCliente " +
					 "ORDER BY f.fechaEmision, f.id ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		List<Factura> facturas = q.getResultList();
		if(facturas!=null && !facturas.isEmpty()){
			for(Factura f : facturas){
				if(f.getRemitos()!=null){
					f.getRemitos().size();
				}
			}
		}
		return facturas;
	}

	public Integer getUltimoNumeroFacturaImpreso(ETipoFactura tipoFactura) {
		String hql = " SELECT MAX(f.nroFactura) FROM Factura f WHERE f.idEstadoImpresion = 2 ";
		Query q = getEntityManager().createQuery(hql);
		return NumUtil.toInteger(q.getSingleResult());
	}
}
