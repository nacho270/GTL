package ar.com.textillevel.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;

@Stateless
@SuppressWarnings("unchecked")
public class RemitoSalidaDAO extends GenericDAO<RemitoSalida, Integer> implements RemitoSalidaDAOLocal {

	public Integer getLastNroRemito() {
		Query query = getEntityManager().createQuery("SELECT MAX(rs.nroRemito) FROM RemitoSalida rs");
		Number lastNumeroRemito = (Number)query.getSingleResult();
		return lastNumeroRemito == null ? null : lastNumeroRemito.intValue();
	}

	public RemitoSalida getByIdConPiezasYProductos(Integer id) {
		String hql = "SELECT rs FROM Remito rs WHERE rs.id = :id";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			RemitoSalida remitoSalida = remitos.get(0);
			doEager(remitoSalida);
			return remitoSalida;
		}
		return null;
	}

	private void doEager(RemitoSalida remitoSalida) {
		for (PiezaRemito pr : remitoSalida.getPiezas()) {
			pr.getPiezasPadreODT().size();
			if (pr.getPiezaEntrada() != null) {
				pr.getPiezaEntrada().getPiezasPadreODT().size();
				for(PiezaODT pODT : pr.getPiezasPadreODT()) {
					pODT.getOdt().getCodigo();
				}
			}
		}
		remitoSalida.getOdts().size();
		for (OrdenDeTrabajo odt : remitoSalida.getOdts()) {
			odt.getPiezas().size();
			for (PiezaODT podt : odt.getPiezas()) {
				podt.getPiezasSalida().size();
			}
		}
		if (remitoSalida.getProveedor() != null) {
			remitoSalida.getProveedor().getNombreCorto();
			remitoSalida.getItems().size();
		}
		if (remitoSalida.getFactura() != null) {
			remitoSalida.getFactura().getFechaEmision();
		}
		for (CorreccionFacturaProveedor cfp : remitoSalida.getCorreccionesProvGeneradas()) {
			cfp.getFacturas().size();
		}
		doEagerDibujo(remitoSalida);
	}

	public RemitoSalida getByNroRemitoConPiezasYProductos(Integer nroRemito) {
		String hql = "SELECT rs FROM RemitoSalida rs WHERE rs.nroRemito = :nroRemito AND rs.nroFactura IS NOT NULL AND (rs.anulado IS NULL OR rs.anulado = 0)";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroRemito", nroRemito);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			RemitoSalida remitoSalida = remitos.get(0);
			doEager(remitoSalida);
			return remitoSalida;
		}
		return null;
	}

	public Integer getUltimoNumeroFactura(EPosicionIVA posIva, Integer nroSucursal) {
		Query query = getEntityManager().createQuery("SELECT MAX(rs.nroFactura) FROM RemitoSalida rs WHERE rs.cliente.idPosicionIva = :idPosIva AND rs.nroSucursal = :nroSucursal ");
		query.setParameter("idPosIva", posIva.getId());
		query.setParameter("nroSucursal", nroSucursal);
		Number lastNumeroRemito = (Number)query.getSingleResult();
		return lastNumeroRemito == null ? 0 : lastNumeroRemito.intValue();
	}

	public List<RemitoSalida> getRemitosByClienteYFecha(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		String hql = " SELECT r FROM RemitoSalida r WHERE  (r.fechaEmision BETWEEN :fechaDesde AND :fechaHasta) AND (r.anulado IS NULL OR r.anulado = 0) "+
					(cliente!=null?" AND r.cliente.id = :idCliente ": " " )+
					" ORDER BY r.fechaEmision ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		if(cliente!=null){
			q.setParameter("idCliente", cliente.getId());
		}
		List<RemitoSalida> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		return lista;
	}

	public List<RemitoSalida> getRemitosByODT(OrdenDeTrabajo odt) {
		String hql = "FROM RemitoSalida rs WHERE :odt IN ELEMENTS(rs.odts) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("odt", odt);
		List<RemitoSalida> remitos = q.getResultList();
		for(RemitoSalida rs : remitos) {
			doEager(rs);
		}
		return remitos;
	}

	public List<RemitoSalida> getRemitoSalidaByParams(Date fechaDesde, Date fechaHasta, Integer idCliente, Integer idProveedor) {
		String hql = "SELECT r " +
					 "FROM RemitoSalida r " +
					 "LEFT JOIN FETCH r.cliente AS cliente " +					 
					 "LEFT JOIN FETCH r.proveedor AS proveedor " +
					 "WHERE  r.fechaEmision BETWEEN :fechaDesde AND :fechaHasta " +
					 (idCliente == null ? "" : " AND cliente.id = :idCliente ") +
					 (idProveedor == null ? "" : " AND proveedor.id = :idProveedor ") + 
					 "ORDER BY r.fechaEmision DESC";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		if(idCliente != null) {
			q.setParameter("idCliente", idCliente);
		}
		if(idProveedor != null) {
			q.setParameter("idProveedor", idProveedor);
		}
		List<RemitoSalida> lista = q.getResultList();
		return lista;
	}

	public List<RemitoSalida> getRemitoSalidaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor) {
		String hql = "SELECT r " +
					 "FROM RemitoSalida r " +
					 "JOIN FETCH r.proveedor AS prov " +
					 "WHERE  r.fechaEmision BETWEEN :fechaDesde AND :fechaHasta " +
					 " AND prov.id = :idProveedor " + 
					 "ORDER BY r.fechaEmision ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);
		q.setParameter("idProveedor", idProveedor);
		List<RemitoSalida> lista = q.getResultList();
		return lista;
	}

	public RemitoSalida getByNroRemitoConPiezasYProductosAnulado(Integer nroRemito) {
		String hql = "SELECT rs FROM RemitoSalida rs WHERE rs.nroRemito = :nroRemito AND rs.nroFactura IS NOT NULL";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroRemito", nroRemito);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			RemitoSalida remitoSalida = remitos.get(0);
			doEager(remitoSalida);
			return remitoSalida;
		}
		return null;
	}

	public List<RemitoSalida> getRemitosConNumerosDeFacturaMenorA(Integer nroDesde, Date fechaDesde) {
		String hql = " SELECT rs FROM RemitoSalida rs " +
					 " WHERE rs.nroFactura < :numero AND rs.nroFactura IS NOT NULL " +
					 "		 AND rs.fechaEmision >= :fecha " +
					 " ORDER BY rs.fechaEmision ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("numero", nroDesde);
		q.setParameter("fecha", fechaDesde);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			return remitos;
		}
		return null;
	}

	public List<RemitoSalida> getRemitosSalidaSinFacturaPorCliente(Cliente cliente) {
		String hql = " SELECT rs FROM RemitoSalida rs " +
					 " WHERE rs.factura IS NULL "+
					 "		 AND rs.cliente.id = :idCliente " +
					 "		 AND rs.nroFactura IS NOT NULL " +
					 " ORDER BY rs.fechaEmision ASC ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", cliente.getId());
		List<RemitoSalida> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			for(RemitoSalida r : lista){
				doEager(r);
			}
			return lista;
		}
		return null;
	}

	public void borrarAsociacionNotaCredito(NotaCreditoProveedor ncp) {
		List<RemitoSalida> remitos = getRemitosSalidaByCorreccion(ncp);
		for(RemitoSalida rs : remitos) {
			rs.getCorreccionesProvGeneradas().remove(ncp);
			save(rs);
		}
	}

	private List<RemitoSalida> getRemitosSalidaByCorreccion(CorreccionFacturaProveedor cfp) {
		String hql = "FROM RemitoSalida rs WHERE :cfp IN ELEMENTS(rs.correccionesProvGeneradas) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("cfp", cfp);
		return q.getResultList();
	}

	public List<RemitoSalida> getRemitosByNroRemitoConPiezasYProductos(Integer nroRemito) {
		String hql = "SELECT rs FROM RemitoSalida rs WHERE rs.nroRemito = :nroRemito ORDER BY rs.fechaEmision DESC";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroRemito", nroRemito);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			for(RemitoSalida remitoSalida : remitos) {
				doEager(remitoSalida);
			}
		}
		return remitos;
	}

	public RemitoSalida getByNumero(String numero) {
		String hql = "SELECT rs FROM RemitoSalida rs WHERE rs.nroRemito = :nroRemito ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroRemito", Integer.valueOf(numero));
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			return remitos.get(0);
		}
		return null;
	}

	@Override
	public RemitoSalida getByIdConDibujo(Integer id) {
		String hql = "SELECT rs FROM Remito rs WHERE rs.id = :id";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", id);
		List<RemitoSalida> remitos = q.getResultList();
		if(remitos != null && remitos.size()>0){
			RemitoSalida remitoSalida = remitos.get(0);
			doEagerDibujo(remitoSalida);
			return remitoSalida;
		}
		return null;
	}
	
	private void doEagerDibujo(RemitoSalida remitoSalida) {
		if (remitoSalida.getProveedor() != null) {
			remitoSalida.getProveedor().getNombreCorto();
			remitoSalida.getItems().size();
		}
		if (remitoSalida.getFactura() != null) {
			remitoSalida.getFactura().getFechaEmision();
		}
		for (CorreccionFacturaProveedor cfp : remitoSalida.getCorreccionesProvGeneradas()) {
			cfp.getFacturas().size();
		}
		if(remitoSalida.getDibujoEstampados() != null) {
			remitoSalida.getDibujoEstampados().size();
		}
	}

	public RemitoSalida getRemitoSalidaByDibujoEstampado(DibujoEstampado dibujo) {
		String hql = "SELECT rs FROM Remito rs WHERE :dibujo IN ELEMENTS(rs.dibujoEstampados)";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("dibujo", dibujo);
		List<RemitoSalida> remitos = q.getResultList();
		if(!remitos.isEmpty()){
			return remitos.get(0);
		}
		return null;
	}

}