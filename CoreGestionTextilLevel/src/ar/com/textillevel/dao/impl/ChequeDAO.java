package ar.com.textillevel.dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ar.com.fwcommon.dao.impl.GenericDAO;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.NumUtil;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;

@Stateless
public class ChequeDAO extends GenericDAO<Cheque, Integer> implements ChequeDAOLocal{

	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesPorFechaYPaginado(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		String hql = " SELECT c FROM Cheque c LEFT JOIN FETCH c.proveedorSalida LEFT JOIN FETCH c.clienteSalida LEFT JOIN FETCH c.personaSalida LEFT JOIN FETCH c.bancoSalida " 
				+ " WHERE 1=1 "+
				(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
				+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + "<= :fechaHasta ":"")
				+(nroCliente!=null?" AND c.cliente.nroCliente = :nroCliente ":"")
				+(eEstadoCheque!=null?" AND c.idEstado = :idEstado ":"")
				+ " ORDER BY c.id";
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(eEstadoCheque!=null){
			q.setParameter("idEstado", eEstadoCheque.getId());
		}
		if(nroCliente!=null){
			q.setParameter("nroCliente", nroCliente);
		}
		q.setFirstResult(maxRows * (paginaActual - 1));
		q.setMaxResults(maxRows);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesPorFechaYPaginado(String numeracionCheque, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		String hql = " SELECT c FROM Cheque c  LEFT JOIN FETCH c.proveedorSalida LEFT JOIN FETCH c.clienteSalida LEFT JOIN FETCH c.personaSalida LEFT JOIN FETCH c.bancoSalida  " 
				+ " WHERE 1=1 "+
				(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
				+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
				+(numeracionCheque!=null?" AND c.numeracion.letra = :letra AND c.numeracion.numero = :numeroCheque ":"")
				+(estadoCheque!=null?" AND c.idEstado = :idEstado ":"")
				+ " ORDER BY c.id";
			Query q = getEntityManager().createQuery(hql);
			if(fechaDesde!=null){
				q.setParameter("fechaDesde", fechaDesde);
			}
			if(fechaHasta!=null){
				q.setParameter("fechaHasta", fechaHasta);
			}
			if(estadoCheque!=null){
				q.setParameter("idEstado", estadoCheque.getId());
			}
			if(numeracionCheque!=null){
				q.setParameter("letra", new Character(numeracionCheque.substring(0, 1).toCharArray()[0]));
				q.setParameter("numeroCheque", Integer.valueOf(numeracionCheque.substring(1, numeracionCheque.length())));
			}
			q.setFirstResult(maxRows * (paginaActual - 1));
			q.setMaxResults(maxRows);
			return q.getResultList();
		}

	public Integer getCantidadDeCheques(String numeracionCheque,  EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		String hql = "SELECT COUNT(*) FROM Cheque c "+ " WHERE 1=1 "+
				(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
				+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + "<= :fechaHasta ":"")
				+(numeracionCheque!=null?" AND c.numeracion.letra = :letra AND c.numeracion.numero = :numeroCheque ":"")
				+(eEstadoCheque!=null?" AND c.idEstado = :idEstado ":"");
		
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(eEstadoCheque!=null){
			q.setParameter("idEstado", eEstadoCheque.getId());
		}
		if(numeracionCheque!=null){
			q.setParameter("letra", new Character(numeracionCheque.substring(0, 1).toCharArray()[0]));
			q.setParameter("numeroCheque", Integer.valueOf(numeracionCheque.substring(1, numeracionCheque.length())));
		}
		return NumUtil.toInteger(q.getSingleResult());
	}

	public Integer getCantidadDeCheques(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		String hql = "SELECT COUNT(*) FROM Cheque c "+ " WHERE 1=1 "+
				(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
				+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
				+(nroCliente!=null?" AND c.cliente.nroCliente = :nroCliente ":"")
				+(eEstadoCheque!=null?" AND c.idEstado = :idEstado ":"");
		
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(eEstadoCheque!=null){
			q.setParameter("idEstado", eEstadoCheque.getId());
		}
		if(nroCliente!=null){
			q.setParameter("nroCliente", nroCliente);
		}
		return NumUtil.toInteger(q.getSingleResult());
	}
	
	@SuppressWarnings("unchecked")
	public Cheque getChequeByNumero(String nroCheque) {
		String hql = "SELECT c FROM Cheque c WHERE c.numero = :nroCheque";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroCheque", nroCheque);
		List<Cheque> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Cheque getChequeByNumeroCuitYBanco(String nroCheque, String cuit, Banco banco) {
		String hql = "SELECT c FROM Cheque c WHERE c.numero = :nroCheque"
				+ 	 "	AND c.cuit = :cuit AND c.banco.id = :idBanco";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("nroCheque", nroCheque);
		q.setParameter("cuit", cuit);
		q.setParameter("idBanco", banco.getId());
		List<Cheque> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Integer getUltimoNumeroInternoCheque(Character letra) {
		String hql = "SELECT MAX(c.numeracion.numero) FROM Cheque c WHERE c.numeracion.letra = :letra";
		Query query = getEntityManager().createQuery(hql);
		query.setParameter("letra", letra);
		List<Integer> list = query.getResultList();
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesByCliente(Integer idCliente, EEstadoCheque estadoCheque) {
		String hql = "SELECT c FROM Cheque c " +
					 "WHERE c.cliente.id = :idCliente AND c.idEstado = :idEstado";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("idCliente", idCliente);
		q.setParameter("idEstado", estadoCheque.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Cheque> obtenerChequesVencidos(Date sumarDias) {
		String hql = "SELECT c FROM Cheque c WHERE c.fechaDeposito <= :fecha AND (c.idEstado = 1 OR c.idEstado = 2) ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("fecha", sumarDias);
		List<Cheque> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			return lista;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Cheque> getListaDeChequesProximosAVencer(Date fechaInicio, Date fechaFin) {
		String hql = " SELECT c FROM Cheque c " +
					 " WHERE c.fechaDeposito >= :fechaInicio AND c.fechaDeposito <= :fechaFin AND (c.idEstado = 1 OR c.idEstado = 2)";
		Query q= getEntityManager().createQuery(hql);
		q.setParameter("fechaInicio", fechaInicio);
		q.setParameter("fechaFin", fechaFin);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		String hql = " SELECT c FROM Cheque c LEFT JOIN FETCH c.proveedorSalida LEFT JOIN FETCH c.clienteSalida LEFT JOIN FETCH c.personaSalida LEFT JOIN FETCH c.bancoSalida  " 
			+ " WHERE 1=1 "
			+(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
			+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
			+(nombreProveedor!=null?" AND ( (c.proveedorSalida IS NOT NULL AND c.proveedorSalida.razonSocial LIKE :nombreProveedorSalida) OR " +
									"	    (c.clienteSalida IS NOT NULL AND c.clienteSalida.razonSocial LIKE :nombreProveedorSalida) OR" +
									"		(c.personaSalida IS NOT NULL AND CONCAT(TRIM(c.personaSalida.nombres),' ',TRIM(c.personaSalida.apellido)) LIKE :nombreProveedorSalida)) " +
									"":"")
			+(estadoCheque!=null?" AND c.idEstado = :idEstado ":"")
			+ " ORDER BY c.id";
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(estadoCheque!=null){
			q.setParameter("idEstado", estadoCheque.getId());
		}
		if(nombreProveedor!=null){
			q.setParameter("nombreProveedorSalida","%"+nombreProveedor.trim()+"%");
		}
		q.setFirstResult(maxRows * (paginaActual - 1));
		q.setMaxResults(maxRows);
		return q.getResultList();
	}
	
	public Integer getCantidadDeChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		String hql = " SELECT COUNT(*) FROM Cheque c LEFT JOIN c.proveedorSalida LEFT JOIN c.clienteSalida LEFT JOIN c.personaSalida LEFT JOIN c.bancoSalida  " 
			+ " WHERE 1=1 "
			+(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
			+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
			+(nombreProveedor!=null?" AND ( (c.proveedorSalida IS NOT NULL AND c.proveedorSalida.razonSocial LIKE :nombreProveedorSalida) OR " +
									"	    (c.clienteSalida IS NOT NULL AND c.clienteSalida.razonSocial LIKE :nombreProveedorSalida) OR" +
									"		(c.personaSalida IS NOT NULL AND CONCAT(TRIM(c.personaSalida.nombres),' ',TRIM(c.personaSalida.apellido)) LIKE :nombreProveedorSalida)) " +
									"":"")
			+(estadoCheque!=null?" AND c.idEstado = :idEstado ":"");
		
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(estadoCheque!=null){
			q.setParameter("idEstado", estadoCheque.getId());
		}
		if(nombreProveedor!=null){
			q.setParameter("nombreProveedorSalida","%"+nombreProveedor+"%");
		}
		return NumUtil.toInteger(q.getSingleResult());
	}

	public Integer getCantidadDechequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		String hql = " SELECT COUNT(*) FROM Cheque c  " 
			+ " WHERE 1=1 "+
			(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
			+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
			+(numeroCheque!=null?" AND c.numero = :numeroCheque ":"")
			+(eEstadoCheque!=null?" AND c.idEstado = :idEstado ":"");
		
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(eEstadoCheque!=null){
			q.setParameter("idEstado", eEstadoCheque.getId());
		}
		if(numeroCheque!=null){
			q.setParameter("numeroCheque",numeroCheque);
		}
		return NumUtil.toInteger(q.getSingleResult());
	}

	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer paginaActual, Integer maxRows) {
		String hql = " SELECT c FROM Cheque c  LEFT JOIN FETCH c.proveedorSalida LEFT JOIN FETCH c.clienteSalida LEFT JOIN FETCH c.personaSalida LEFT JOIN FETCH c.bancoSalida " 
			+ " WHERE 1=1 "+
			(fechaDesde!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) + ">= :fechaDesde ":"")
			+(fechaHasta!=null?" AND "+(tipoFecha == EnumTipoFecha.FECHA_ENTRADA?" c.fechaEntrada ": " c.fechaSalida " ) +  "<= :fechaHasta ":"")
			+(numeroCheque!=null?" AND c.numero = :numeroCheque ":"")
			+(eEstadoCheque!=null?" AND c.idEstado = :idEstado ":"");
		
		Query q = getEntityManager().createQuery(hql);
		if(fechaDesde!=null){
			q.setParameter("fechaDesde", fechaDesde);
		}
		if(fechaHasta!=null){
			q.setParameter("fechaHasta", fechaHasta);
		}
		if(eEstadoCheque!=null){
			q.setParameter("idEstado", eEstadoCheque.getId());
		}
		if(numeroCheque!=null){
			q.setParameter("numeroCheque",numeroCheque);
		}
		q.setFirstResult(maxRows * (paginaActual - 1));
		q.setMaxResults(maxRows);
		return q.getResultList();
	}
	
	/** BUSQUEDA DESDE LA CARGA DE ORDEN DE PAGO */
	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesPorNumeracionNumeroFechaEImporte(List<NumeracionCheque> numerosInternos, String numeroCheque, Date fechaDesde, Date fechaHasta, BigDecimal importeDesde, BigDecimal importeHasta, List<Cheque> excluidos) {
		String hql = " SELECT c FROM Cheque c " 
						+ " WHERE 1 = 1 "
						+ (numerosInternos==null || numerosInternos.isEmpty()? " AND c.idEstado = 2":"")
						+ (numeroCheque != null ? " AND c.numero = :numeroCheque " : " ") 
						+ (fechaDesde != null ? " AND c.fechaDeposito >= :fechaDesde " : " ")
						+ (fechaHasta != null ? " AND c.fechaDeposito <= :fechaHasta " : " ") 
						+ (importeDesde != null ? " AND c.importe >= :importeDesde " : " ")
						+ (importeHasta != null ? " AND c.importe <= :importeHasta " : " ")
						+ (excluidos != null && !excluidos.isEmpty() ? " AND c NOT IN (:excluidos)" : " ");

		Query q = getEntityManager().createQuery(hql);
		if (fechaDesde != null) {
			q.setParameter("fechaDesde", fechaDesde);
		}
		if (fechaHasta != null) {
			q.setParameter("fechaHasta", fechaHasta);
		}
		if (numeroCheque != null) {
			q.setParameter("numeroCheque", numeroCheque);
		}
		if (importeDesde != null) {
			q.setParameter("importeDesde", importeDesde);
		}
		if (importeHasta != null) {
			q.setParameter("importeHasta", importeHasta);
		}
		if(excluidos != null && !excluidos.isEmpty()){
			q.setParameter("excluidos", excluidos);
		}
		List<Cheque> lista = q.getResultList();
		if(lista == null || (lista!=null && lista.isEmpty())){
			return null;
		}
		if(numerosInternos!=null){
			List<Cheque> ret = new ArrayList<Cheque>();
			for(Cheque c : lista){
				if(numerosInternos.contains(c.getNumeracion())){
					ret.add(c);
				}
			}
			return ret;
		}
		return lista;
	}

	/** SUGERENCIA DE CHEQUES EN ORDEN DE PAGO */
	@SuppressWarnings("unchecked")
	public List<Cheque> getChequeSugeridos(BigDecimal montoHasta, Integer diasDesde, Integer diasHasta, Date fechaPromedio, List<Cheque> chequesDescartados) {
		String hql = " SELECT c FROM Cheque c " +
					 " WHERE c.fechaDeposito >= :fechaInicio AND c.fechaDeposito <= :fechaFin AND c.idEstado = 2 " +
					 (chequesDescartados!=null && !chequesDescartados.isEmpty()? " AND c NOT IN (:chequesDescartados) ": " ")+
					 " ORDER BY c.capitalOInterior DESC, c.fechaDeposito ASC ";
		Date fechaInicio = (diasDesde == null?DateUtil.getHoy():DateUtil.sumarDias(fechaPromedio, diasDesde));
		Date fechaFin = DateUtil.sumarDias(fechaPromedio, diasHasta);
		Query q= getEntityManager().createQuery(hql);
		q.setParameter("fechaInicio", fechaInicio);
		q.setParameter("fechaFin", fechaFin);
		if(chequesDescartados!=null && !chequesDescartados.isEmpty()){
			q.setParameter("chequesDescartados", chequesDescartados);
		}
		List<Cheque> ret = new ArrayList<Cheque>();
		List<Cheque> lista = q.getResultList();
		BigDecimal suma = new BigDecimal(0d);
		for(Cheque c : lista){
			if(suma.add(c.getImporte()).compareTo(montoHasta)<1){
				ret.add(c);
				suma = suma.add(c.getImporte());
			}
			if(suma.compareTo(montoHasta)==1){
				break;
			}
		}
		return ret;
	}

	/** BUSQUEDA EN LA ORDEN DE DEPOSITO */
	@SuppressWarnings("unchecked")
	public List<Cheque> getChequesByNrosInternos(List<NumeracionCheque> numerosInternos, List<Integer> idsUtilizados) {
		String hql = " SELECT c FROM Cheque c WHERE 1 = 1 " +
					(idsUtilizados!=null && !idsUtilizados.isEmpty()?" AND c.id NOT IN (:idsUtilizados) ":"");
		Query q = getEntityManager().createQuery(hql);
		if(idsUtilizados!=null && !idsUtilizados.isEmpty()){
			q.setParameter("idsUtilizados", idsUtilizados);
		}
		List<Cheque> lista = q.getResultList();
		if(lista!=null && !lista.isEmpty()){
			List<Cheque> ret = new ArrayList<Cheque>();
			for(Cheque c : lista){
				if(numerosInternos.contains(c.getNumeracion())){
					ret.add(c);
				}
			}
			return ret;
		}
		return null;
	}

	public boolean chequeSeUsaEnRecibo(Cheque c) {
		String hql = " SELECT COUNT(*) FROM FormaPagoCheque fpc WHERE  fpc.cheque.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", c.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public boolean chequeSeUsaEnOrdenDePago(Cheque c) {
		String hql = " SELECT COUNT(*) FROM FormaPagoOrdenDePagoCheque fpc WHERE  fpc.cheque.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", c.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	public boolean chequeSeUsaEnOrdenDePagoPersona(Cheque c) {
		String hql = " SELECT COUNT(*) FROM FormaPagoOrdenDePagoPersonaCheque fpc WHERE  fpc.cheque.id = :id ";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("id", c.getId());
		return NumUtil.toInteger(q.getSingleResult())>0;
	}

	@SuppressWarnings("unchecked")
	public Cheque getChequeByNumeracion(String letraCheque, Integer nroCheque) {
		String hql = "SELECT c FROM Cheque c WHERE c.numeracion.letra = :letraCheque AND c.numeracion.numero = :numero";
		Query q = getEntityManager().createQuery(hql);
		q.setParameter("letraCheque", new Character(letraCheque.toCharArray()[0]));
		q.setParameter("numero", nroCheque);
		List<Cheque> list = q.getResultList();
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;

	}
}
