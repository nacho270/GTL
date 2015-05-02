package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionCheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionResumen;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.CorreccionFacadeRemote;

@Stateless
public class ChequeFacade implements ChequeFacadeRemote, ChequeFacadeLocal{

	@EJB
	private ChequeDAOLocal chequeDAO;
	
	@EJB
	private CorreccionFacadeRemote correccionFacade;
	
	@EJB
	private CorreccionFacturaProveedorFacadeLocal correccionProveedorFacade;

	@EJB
	private FacturaDAOLocal facturaDao;
	
	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private AuditoriaFacadeLocal<Cheque> auditoriaFacade;

	
	public List<Cheque> getChequesPorFechaYPaginado(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		return chequeDAO.getChequesPorFechaYPaginado(nroCliente, eEstadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows, tipoFecha);
	}
	
	public List<Cheque> getChequesPorFechaYPaginado(String numeracionCheque, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		return chequeDAO.getChequesPorFechaYPaginado(numeracionCheque, estadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows,tipoFecha);
	}
	
	public List<Cheque> getChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows , EnumTipoFecha tipoFecha) {
		return chequeDAO.getChequesPorFechaYPaginadoPorProveedor(nombreProveedor, estadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows, tipoFecha);
	}

	public Integer getCantidadDeCheques(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha){
		return chequeDAO.getCantidadDeCheques(nroCliente, eEstadoCheque,  fechaDesde,  fechaHasta, tipoFecha);
	}

	public Cheque grabarCheque(Cheque cheque, String usuario) {
		boolean mod = cheque.getId()!=null;
		Cheque chequeAnt = null;
		EEstadoCheque estadoAnt = null;
		if(mod){
			chequeAnt = chequeDAO.getById(cheque.getId());
			estadoAnt = chequeAnt.getEstadoCheque();
		}else{
			if(cheque.getCliente().getNroCliente().equals(1)){
				cheque.setEstadoCheque(EEstadoCheque.EN_CARTERA);
			}
		}
		cheque = chequeDAO.save(cheque);
		if(mod){
			String descripcion = "Actualizacion de datos del cheque: " + cheque.getNumeracion()+".";
			if(estadoAnt != cheque.getEstadoCheque()){
				descripcion += " Estado nuevo: " + cheque.getEstadoCheque().getDescripcion()+".";
			}
			auditoriaFacade.auditar(usuario, descripcion, EnumTipoEvento.MODIFICACION, cheque);
		}else{
			auditoriaFacade.auditar(usuario, "Creci�n del cheque: " + cheque.getNumeracion(), EnumTipoEvento.ALTA, cheque);
		}
		return cheque;
	}

	public Cheque getChequeByNumero(String nroCheque) {
		return chequeDAO.getChequeByNumero(nroCheque);
	}
	
	public Cheque getChequebyId(Integer idCheque) {
		Cheque cheque = chequeDAO.getById(idCheque);
		if(cheque.getBancoSalida()!=null){
			cheque.getBancoSalida().getDireccion();
		}
		if(cheque.getClienteSalida()!=null){
			cheque.getClienteSalida().getCelular();
		}
		if(cheque.getProveedorSalida()!=null){
			cheque.getProveedorSalida().getCelular();
		}
		if(cheque.getPersonaSalida()!=null){
			cheque.getPersonaSalida().getCelular();
		}
		return cheque;
	}

	public Integer getUltimoNumeroInternoCheque(Character letra) {
		return chequeDAO.getUltimoNumeroInternoCheque(letra);
	}

	public List<Cheque> getChequesByCliente(Integer idCliente, EEstadoCheque estadoCheque) {
		return chequeDAO.getChequesByCliente(idCliente, estadoCheque);
	}

	public CorreccionFactura rechazarCheque (Cheque cheque, Date fecha, String motivoRechazo, BigDecimal gastos, String usuario, boolean debeDiscriminarIVA) throws CLException{
		//Genero una nota de d�bito al proveedor que tiene asignado el cheque
		if(cheque.getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR) {
			generarNotaDebitoProveedor(cheque, usuario, gastos);
		}
		EEstadoCheque estadoAnterior = cheque.getEstadoCheque();
		cheque.setEstadoCheque(EEstadoCheque.RECHAZADO);
		//cheque.setFechaSalida(DateUtil.getHoy());
		cheque = chequeDAO.save(cheque);
		Integer lastNumeroFactura = facturaDao.getLastNumeroFactura(cheque.getCliente().getPosicionIva().getTipoFactura());
		if(lastNumeroFactura == null){
			lastNumeroFactura = parametrosGeneralesFacade.getParametrosGenerales().getNroComienzoFactura();
		}else{
			lastNumeroFactura+=1;
		}
		NotaDebito correccion = new NotaDebito();
		correccion.setChequeRechazado(cheque);
		correccion.setEstadoAnteriorCheque(estadoAnterior);
		correccion.setIsParaRechazarCheque(true);
		correccion.setCliente(cheque.getCliente());
		double importe = cheque.getImporte().doubleValue();
		String descripcion = "Cheque rechazado - N�: " + cheque.getNumero() + " - Banco: " + cheque.getBanco().getNombre() + " - Importe: $" + importe + " - Motivo: " + motivoRechazo;
		Double subTotal = importe;
		if(gastos!=null){ //Si tiene gastos, calcular IVA.
			descripcion +=" - Gastos: $" + gastos;
			subTotal += gastos.doubleValue();
			correccion.setMontoSubtotal(new BigDecimal(subTotal));
			correccion.setGastos(gastos);
			if(debeDiscriminarIVA){
				BigDecimal porcentajeIVAInscr = parametrosGeneralesFacade.getParametrosGenerales().getPorcentajeIVAInscripto();
				Double montoIva = porcentajeIVAInscr.doubleValue()/100 * gastos.doubleValue();
				correccion.setPorcentajeIVAInscripto(porcentajeIVAInscr);
				subTotal += montoIva;
			}
		}else{
			correccion.setMontoSubtotal(new BigDecimal(subTotal));
		}
		Double total = subTotal;
		correccion.setDescripcion(descripcion);
		
		long longFecha = 0;
		if(esHoy(new java.sql.Date(fecha.getTime()))){//hoy
			longFecha = DateUtil.getAhora().getTime();
		}else{
			longFecha=fecha.getTime();
		}
		
		correccion.setFechaEmision(new Timestamp(longFecha));
		correccion.setMontoTotal(new BigDecimal(total));
		correccion.setMontoFaltantePorPagar(new BigDecimal(total));
		correccion.setNroFactura(lastNumeroFactura);
		correccion.setTipoFactura(cheque.getCliente().getPosicionIva().getTipoFactura());
		CorreccionFactura correccionNueva = correccionFacade.guardarCorreccionYGenerarMovimiento(correccion,usuario);
		auditoriaFacade.auditar(usuario, "Rechazo del cheque N�: " + cheque.getNumeracion() + ". Motivo: " + motivoRechazo, EnumTipoEvento.ANULACION, cheque);
		return correccionNueva;
	}

	private boolean esHoy(java.sql.Date fecha){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(DateUtil.getAhora());
		return cal.get(GregorianCalendar.DAY_OF_MONTH)==DateUtil.getDia(fecha) && 
			   cal.get(GregorianCalendar.MONTH)==DateUtil.getMes(fecha) && 
			   cal.get(GregorianCalendar.YEAR)== DateUtil.getAnio(fecha);
	}

	
	private void generarNotaDebitoProveedor(Cheque cheque, String usuario, BigDecimal gastos) {
		Proveedor proveedorSalida = cheque.getProveedorSalida();
		
		if(gastos==null){
			gastos = BigDecimal.ZERO;
		}
		
		NotaDebitoProveedor ndp = new NotaDebitoProveedor();
		ndp.setVerificada(false);
		ndp.setNroCorreccion("");
		ndp.setFechaIngreso(DateUtil.getHoy());
		ndp.setMontoTotal(cheque.getImporte());
		ndp.setMontoTotal(ndp.getMontoTotal().add(gastos));
		ndp.setMontoFaltantePorPagar(cheque.getImporte());
		ndp.setProveedor(proveedorSalida);
		
		ItemCorreccionCheque icch = new ItemCorreccionCheque();
		icch.setCantidad(new BigDecimal(1));
		icch.setDescripcion("Rechazo de Cheque: " + cheque.toString());
		icch.setFactorConversionMoneda(new BigDecimal(1));
		icch.setPorcDescuento(new BigDecimal(0));
		icch.setImporte(cheque.getImporte());
		icch.setPrecioUnitario(cheque.getImporte());
		icch.setCheque(cheque);
		
		ItemCorreccionFacturaProveedor itr = new ItemCorreccionResumen();
		itr.setImporte(gastos);
		itr.setPrecioUnitario(gastos);
		
		NumberFormat df = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(true);
		
		itr.setDescripcion("Gastos: " + df.format(gastos.doubleValue()));

		ndp.getItemsCorreccion().add(icch);
		ndp.getItemsCorreccion().add(itr);
		
		correccionProveedorFacade.guardarCorreccionYGenerarMovimiento(ndp, usuario, null);
	}

	public void eliminarCheque(Integer id, String usuario) throws ValidacionException {
		Cheque cheque = chequeDAO.getById(id);
		if(chequeDAO.chequeSeUsaEnRecibo(cheque)){
			throw new ValidacionException(EValidacionException.CHEQUE_SE_USA_EN_RECIBO.getInfoValidacion());
		}
		if(chequeDAO.chequeSeUsaEnOrdenDePago(cheque)){
			throw new ValidacionException(EValidacionException.CHEQUE_SE_USA_EN_ODP.getInfoValidacion());
		}
		if(chequeDAO.chequeSeUsaEnOrdenDePagoPersona(cheque)){
			throw new ValidacionException(EValidacionException.CHEQUE_SE_USA_EN_ODP_PERSONA.getInfoValidacion());
		}
		chequeDAO.removeById(id);
		auditoriaFacade.auditar(usuario, "Eliminaci�n del cheque N�: " + cheque.getNumeracion() + ".", EnumTipoEvento.BAJA, cheque);
	}

	public List<Cheque> obtenerChequesVencidos(Integer toleraciaDias) {
		return chequeDAO.obtenerChequesVencidos(DateUtil.sumarDias(DateUtil.getHoy(),toleraciaDias*-1));
	}

	public List<Cheque> getListaDeChequesProximosAVencer(Integer diasAntes, Integer diasEnQueVencen) {
		//TODO: NO FUNCA, PENSAR BIEN
		//fechaDeposito + diasEnQueVencen - diasAntes <= fecha de hoy <= fechaDeposito + diasEnQueVencen
		// <=>
		// fechaDeposito <= fecha de hoy - diasEnQueVencen + diasAntes
//							8/8 - 30 = 8/7 + 10 = 18/7
		// fechaDeposito  >= fecha de hoy - diasEnQueVencen
//							8/8 - 30 = 8/7
		
		//VIEJO
		//Date fechaInicio = DateUtil.sumarDias(DateUtil.getHoy(),diasAntes*-1);
		//Date fechaFin = DateUtil.sumarDias(DateUtil.getHoy(),(diasEnQueVencen*-1)-diasAntes);
		
		
		Date fechaInicio = DateUtil.sumarDias(DateUtil.getHoy(), -1*diasEnQueVencen);
		Date fechaFin = DateUtil.sumarDias(DateUtil.getHoy(), -1*diasEnQueVencen + diasAntes);
		return chequeDAO.getListaDeChequesProximosAVencer( fechaInicio,  fechaFin);
	}

	public Integer getCantidadDeCheques(String numeracionCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		return chequeDAO.getCantidadDeCheques(numeracionCheque, eEstadoCheque, fechaDesde, fechaHasta, tipoFecha);
	}

	public Integer getCantidadDeChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		return chequeDAO.getCantidadDeChequesPorFechaYPaginadoPorProveedor(nombreProveedor, estadoCheque, fechaDesde, fechaHasta,tipoFecha);
	}

	public Integer getCantidadDechequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha) {
		return chequeDAO.getCantidadDechequesPorNumeroDeCheque(numeroCheque, eEstadoCheque,fechaDesde,fechaHasta,tipoFecha);
	}

	public List<Cheque> getChequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha) {
		return chequeDAO.getChequesPorNumeroDeCheque(numeroCheque, eEstadoCheque,fechaDesde,fechaHasta,tipoFecha,paginaActual,maxRows);
	}

	/** BUSQUEDA DESDE LA CARGA DE ORDEN DE PAGO */
	
	public List<Cheque> getChequesPorNumeracionNumeroFechaEImporte(List<NumeracionCheque> numerosInternos,String nroCheque, Date fechaDesde, Date fechaHasta, BigDecimal importeDesde, BigDecimal importeHasta, List<Cheque> excluidos) {
		return chequeDAO.getChequesPorNumeracionNumeroFechaEImporte(numerosInternos, nroCheque, fechaDesde, fechaHasta, importeDesde, importeHasta,excluidos);
	}

	/** SUGERENCIA DE CHEQUES EN ORDEN DE PAGO */
	public List<Cheque> getChequeSugeridos(BigDecimal montoHasta, Integer diasDesde, Integer diasHasta, Date fechaPromedio, List<Cheque> chequesDescartados) {
		return chequeDAO.getChequeSugeridos(montoHasta, diasDesde,diasHasta,fechaPromedio,chequesDescartados);
	}

	/** BUSQUEDA EN LA ORDEN DE DEPOSITO */
	public List<Cheque> getChequesByNrosInternos(List<NumeracionCheque> numerosInternos, List<Integer> idsUtilizados) {
		return chequeDAO.getChequesByNrosInternos(numerosInternos,idsUtilizados);
	}

	public void eliminarCheques(List<Cheque> cheques, String usuario) throws ValidacionException {
		for(Cheque ch : cheques) {
			eliminarCheque(ch.getId(), usuario);
		}
	}

	public Cheque getChequeByNumeracion(String letraCheque, Integer nroCheque) {
		Cheque cheque = chequeDAO.getChequeByNumeracion(letraCheque,nroCheque);
		if(cheque != null){
			if(cheque.getBancoSalida()!=null){
				cheque.getBancoSalida().getDireccion();
			}
			if(cheque.getClienteSalida()!=null){
				cheque.getClienteSalida().getCelular();
			}
			if(cheque.getProveedorSalida()!=null){
				cheque.getProveedorSalida().getCelular();
			}
			if(cheque.getPersonaSalida()!=null){
				cheque.getPersonaSalida().getCelular();
			}
		}
		return cheque;
	}
}
