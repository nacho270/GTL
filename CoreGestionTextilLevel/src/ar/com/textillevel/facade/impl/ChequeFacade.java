package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.ChequeDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.ImpuestoItemDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDeDepositoDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDePagoDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDePagoPersonaDAOLocal;
import ar.com.textillevel.dao.api.local.ReciboDAOLocal;
import ar.com.textillevel.entidades.cheque.Banco;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.cheque.NumeracionCheque;
import ar.com.textillevel.entidades.cheque.to.OperacionSobreChequeTO;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaCorreccion;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionCheque;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionResumen;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendedeposito.OrdenDeDeposito;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.pagopersona.OrdenDePagoAPersona;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.enums.ETipoImpuesto;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.enums.EnumTipoFecha;
import ar.com.textillevel.entidades.gente.Proveedor;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.ChequeFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;

@Stateless
public class ChequeFacade implements ChequeFacadeRemote, ChequeFacadeLocal {

	@EJB
	private ChequeDAOLocal chequeDAO;
	
	@EJB
	private ReciboDAOLocal reciboDAO;

	@EJB
	private CorreccionFacadeLocal correccionFacade;
	
	@EJB
	private CorreccionFacturaProveedorFacadeLocal correccionProveedorFacade;

	@EJB
	private FacturaDAOLocal facturaDao;
	
	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private AuditoriaFacadeLocal<Cheque> auditoriaFacade;

	@EJB
	private ImpuestoItemDAOLocal impuestoItemDAO;
	
	@EJB
	private OrdenDeDepositoDAOLocal ordenDao;
	
	@EJB
	private OrdenDePagoDAOLocal ordenDePagoDao;
	
	@EJB
	private OrdenDePagoPersonaDAOLocal ordenDePagoPersonaDao;
	
	/* BUSQUEDAS */
	public List<Cheque> getChequesPorFechaYPaginado(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getChequesPorFechaYPaginado(nroCliente, eEstadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows, tipoFecha, idBanco, montoDesde, montoHasta);
	}
	
	public Integer getCantidadDeCheques(Integer nroCliente, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta){
		return chequeDAO.getCantidadDeCheques(nroCliente, eEstadoCheque,  fechaDesde,  fechaHasta, tipoFecha, idBanco, montoDesde, montoHasta);
	}
	
	public List<Cheque> getChequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getChequesPorNumeroDeCheque(numeroCheque, eEstadoCheque,fechaDesde,fechaHasta,tipoFecha,paginaActual,maxRows, idBanco, montoDesde, montoHasta);
	}
	
	public Integer getCantidadDechequesPorNumeroDeCheque(String numeroCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getCantidadDechequesPorNumeroDeCheque(numeroCheque, eEstadoCheque,fechaDesde,fechaHasta,tipoFecha, idBanco, montoDesde, montoHasta);
	}
	
	public List<Cheque> getChequesPorFechaYPaginado(String numeracionCheque, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getChequesPorFechaYPaginado(numeracionCheque, estadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows,tipoFecha, idBanco, montoDesde, montoHasta);
	}
	
	public Integer getCantidadDeCheques(String numeracionCheque, EEstadoCheque eEstadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getCantidadDeCheques(numeracionCheque, eEstadoCheque, fechaDesde, fechaHasta, tipoFecha, idBanco, montoDesde, montoHasta);
	}
	
	public List<Cheque> getChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, Integer paginaActual, Integer maxRows , EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getChequesPorFechaYPaginadoPorProveedor(nombreProveedor, estadoCheque, fechaDesde,  fechaHasta,  paginaActual,  maxRows, tipoFecha, idBanco, montoDesde, montoHasta);
	}

	public Integer getCantidadDeChequesPorFechaYPaginadoPorProveedor(String nombreProveedor, EEstadoCheque estadoCheque, Date fechaDesde, Date fechaHasta, EnumTipoFecha tipoFecha, Integer idBanco, Double montoDesde, Double montoHasta) {
		return chequeDAO.getCantidadDeChequesPorFechaYPaginadoPorProveedor(nombreProveedor, estadoCheque, fechaDesde, fechaHasta,tipoFecha, idBanco, montoDesde, montoHasta);
	}

	/* FIN BUSQUEDAS */
	
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
			auditoriaFacade.auditar(usuario, "Creación del cheque: " + cheque.getNumeracion(), EnumTipoEvento.ALTA, cheque);
		}
		return cheque;
	}

	public Cheque getChequeByNumeroCuitYBanco(String nroCheque, String cuit, Banco banco) {
		return chequeDAO.getChequeByNumeroCuitYBanco(nroCheque, cuit, banco);
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

	public CorreccionFactura rechazarCheque (Cheque chequeElegido, Date fecha, String motivoRechazo, BigDecimal gastos, String usuario, boolean debeDiscriminarIVA) throws ValidacionException, ValidacionExceptionSinRollback {
		Cheque cheque = chequeDAO.getById(chequeElegido.getId());
		checkPrecondicionesRechazar(cheque);
		
		//Genero una nota de débito al proveedor que tiene asignado el cheque
		if(gastos==null){
			gastos = BigDecimal.ZERO;
		}
		
		if(cheque.getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR) {
			generarNotaDebitoProveedor(cheque, usuario, gastos, debeDiscriminarIVA);
		}
		EEstadoCheque estadoAnterior = cheque.getEstadoCheque();
		cheque.setEstadoCheque(EEstadoCheque.RECHAZADO);
		//cheque.setFechaSalida(DateUtil.getHoy());
		cheque = chequeDAO.save(cheque);
		Integer lastNumeroFactura = facturaDao.getLastNumeroFactura(cheque.getCliente().getPosicionIva().getTipoFactura(), ETipoDocumento.NOTA_DEBITO, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
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
		String descripcion = "Cheque rechazado - N°: " + cheque.getNumero() + " - Banco: " + cheque.getBanco().getNombre() + " - Importe: $" + importe + " - Motivo: " + motivoRechazo;
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

		ItemFacturaCorreccion ifCorrecc = new ItemFacturaCorreccion();
		ifCorrecc.setCantidad(new BigDecimal(1));
		ifCorrecc.setDescripcion(correccion.getDescripcion());
		ifCorrecc.setImporte(correccion.getMontoSubtotal());
		ifCorrecc.setUnidad(EUnidad.UNIDAD);
		ifCorrecc.setPrecioUnitario(correccion.getMontoSubtotal());
		correccion.getItems().add(ifCorrecc);

		CorreccionFactura correccionNueva = correccionFacade.guardarCorreccionYGenerarMovimiento(correccion,usuario);
		auditoriaFacade.auditar(usuario, "Rechazo del cheque N°: " + cheque.getNumeracion() + ". Motivo: " + motivoRechazo, EnumTipoEvento.ANULACION, cheque);
		return correccionNueva;
	}

	private void checkPrecondicionesRechazar(Cheque cheque) throws ValidacionException {
		EEstadoCheque estadoActual = cheque.getEstadoCheque();
		boolean estadoPosible = estadoActual == EEstadoCheque.PENDIENTE_COBRAR || 
								estadoActual == EEstadoCheque.EN_CARTERA ||
								estadoActual == EEstadoCheque.SALIDA_BANCO ||
								estadoActual == EEstadoCheque.SALIDA_CLIENTE ||
								estadoActual == EEstadoCheque.SALIDA_PERSONA ||
								estadoActual == EEstadoCheque.SALIDA_PROVEEDOR;
		
		if(!estadoPosible) {
			throw new ValidacionException(EValidacionException.CHEQUE_ESTADO_INVALIDO_PARA_RECHAZAR.getInfoValidacion(), Collections.singletonList(estadoActual.toString()));
		}
	}

	private boolean esHoy(java.sql.Date fecha){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(DateUtil.getAhora());
		return cal.get(GregorianCalendar.DAY_OF_MONTH)==DateUtil.getDia(fecha) && 
			   cal.get(GregorianCalendar.MONTH)==DateUtil.getMes(fecha) && 
			   cal.get(GregorianCalendar.YEAR)== DateUtil.getAnio(fecha);
	}

	
	private void generarNotaDebitoProveedor(Cheque cheque, String usuario, BigDecimal gastos, boolean debeDiscriminarIVA) {
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
		
		ndp.setMontoFaltantePorPagar(ndp.getMontoTotal());
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

		if (debeDiscriminarIVA) {
			ImpuestoItemProveedor impuestoIVA21 = impuestoItemDAO.getByTipoYPorcentaje(ETipoImpuesto.IVA, 21d);
			if (impuestoIVA21 != null) {
				itr.setImpuestos(Collections.singletonList(impuestoIVA21));
				ndp.setMontoTotal(ndp.getMontoTotal().add(itr.getImporte().multiply(new BigDecimal(0.21d)))); //monto total + gastos * IVA
				ndp.setMontoFaltantePorPagar(ndp.getMontoTotal());
			}
		}
		
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
		auditoriaFacade.auditar(usuario, "Eliminación del cheque N°: " + cheque.getNumeracion() + ".", EnumTipoEvento.BAJA, cheque);
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

	public List<OperacionSobreChequeTO> getOperacionSobreChequeTOList(Cheque ch) {
		List<OperacionSobreChequeTO> operaciones = new ArrayList<OperacionSobreChequeTO>();
		NotaDebito nd = correccionFacade.getNotaDebitoByCheque(ch);//ND
		if(nd != null) {
			String suc = nd.getNroSucursal() == null ? null : StringUtil.fillLeftWithZeros(String.valueOf(nd.getNroSucursal()), 4);
			String nro = StringUtil.fillLeftWithZeros(String.valueOf(nd.getNroFactura()), 8);
			operaciones.add(new OperacionSobreChequeTO(nd.getFechaEmision(), EEstadoCheque.RECHAZADO, ETipoDocumento.NOTA_DEBITO, nd.getId(), suc == null ? nro : (suc + "- " + nro), nd.getUsuarioVerificador()));
		}
		OrdenDeDeposito odd = ordenDao.getOrdenDepositoByCheque(ch); //ORDEN DE DEPÓSITO
		if(odd != null) {
			operaciones.add(new OperacionSobreChequeTO(new Timestamp(odd.getFecha().getTime()), EEstadoCheque.SALIDA_BANCO, ETipoDocumento.ORDEN_DE_DEPOSITO, odd.getNroOrden(), odd.getNroOrden()+"", odd.getPersonaResponsable()));
		}
		Recibo r = reciboDAO.getReciboByCheque(ch);
		if(r != null) {//RECIBO
			operaciones.add(new OperacionSobreChequeTO(r.getFechaEmision(), EEstadoCheque.EN_CARTERA, ETipoDocumento.RECIBO, r.getNroRecibo(), r.getNroRecibo() + "", r.getUsuarioConfirmacion()));
		}
		OrdenDePago odp = ordenDePagoDao.getByCheque(ch);
		if(odp != null) {//ORDEN DE PAGO
			operaciones.add(new OperacionSobreChequeTO(odp.getFechaEmision(), EEstadoCheque.SALIDA_PROVEEDOR, ETipoDocumento.ORDEN_PAGO, odp.getNroOrden(), odp.getNroOrden()+"", odp.getUsuarioCreador()));
		}
		OrdenDePagoAPersona odpp = ordenDePagoPersonaDao.getByCheque(ch);
		if(odpp != null) {//ORDEN DE PAGO A PERSONA
			operaciones.add(new OperacionSobreChequeTO(odpp.getFechaHoraEntregada(), EEstadoCheque.SALIDA_PERSONA, ETipoDocumento.ORDEN_PAGO_PERSONA, odpp.getNroOrden(), odpp.getNroOrden()+"", odpp.getUsuarioCreador()));
		}
		Collections.sort(operaciones);
		return operaciones;
	}

}