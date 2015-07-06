package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.entidades.cuenta.to.ETipoDocumento;
import ar.com.textillevel.entidades.documentos.factura.CorreccionFactura;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFactura;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPercepcion;
import ar.com.textillevel.entidades.documentos.factura.itemfactura.ItemFacturaPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.entidades.enums.ETipoFactura;
import ar.com.textillevel.entidades.enums.ETipoItemFactura;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.to.ivaventas.DescripcionFacturaIVAVentasTO;
import ar.com.textillevel.entidades.to.ivaventas.IVAVentasTO;
import ar.com.textillevel.entidades.to.ivaventas.TotalesIVAVentasTO;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.DocumentoContableFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaFacadeLocal;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.facade.api.local.UsuarioSistemaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.FacturaFacadeRemote;
import ar.com.textillevel.modulos.fe.connector.AFIPConnector;
import ar.com.textillevel.modulos.fe.connector.DatosRespuestaAFIP;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;

@Stateless
public class FacturaFacade implements FacturaFacadeRemote, FacturaFacadeLocal {

	@EJB
	private FacturaDAOLocal facturaDao;
	
	@EJB
	private CuentaFacadeLocal cuentaFacade;
	
	@EJB
	private RemitoSalidaFacadeLocal remitoSalidaFacade;

	@EJB
	private ParametrosGeneralesDAOLocal paramGeneralesDao;

	@EJB
	private AuditoriaFacadeLocal<Factura> auditoriaFacade;
	
	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	@EJB
	private CorreccionDAOLocal correccionDao;
	
	@EJB
	private PrecioMateriaPrimaFacadeLocal precioMateriaPrimaFacade;
	
	@EJB
	private MovimientoStockFacadeLocal movimientoStockFacade;
	
	@EJB
	private OrdenDeTrabajoFacadeLocal odtFacade;

	@EJB
	private DocumentoContableFacadeLocal docContableFacade; 

	@EJB
	private UsuarioSistemaFacadeLocal usuSistemaFacade;
	
	public Integer getLastNumeroFactura(ETipoFactura tipoFactura, ETipoDocumento tipoDoc){
		return facturaDao.getLastNumeroFactura(tipoFactura, tipoDoc, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}
	
	public void eliminarFactura(Factura factura, String usrName) throws ValidacionException, CLException {
		if(!facturaDao.esLaUltimaFactura(factura, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal())){
			throw new ValidacionException(EValidacionException.FACTURA_NO_ES_LA_ULTIMA.getInfoValidacion());
		}
		docContableFacade.checkAutorizacionAFIP(factura);
		factura = eliminarInterno(factura);
		auditoriaFacade.auditar(usrName, "Eliminación de factura  Nº: " + factura.getNroFactura(),EnumTipoEvento.BAJA,factura);
	}

	private Factura eliminarInterno(Factura factura) throws ValidacionException {
		if(facturaDao.facturaYaTieneRecibo(factura)){
			throw new ValidacionException(EValidacionException.FACTURA_YA_TIENE_RECIBO.getInfoValidacion());
		}
		cuentaFacade.borrarMovimientoFactura(factura);
		NotaCredito nc = correccionDao.getNotaDeCreditoByFacturaRelacionada(factura);
		if(nc!=null){
//			nc.getFacturasRelacionadas().remove(factura);
//			//TODO: Si la nota de credito, se queda sin facturas relacionadas, se borra?
//			correccionDao.save(nc);
			throw new ValidacionException(EValidacionException.FACTURA_TIENE_NOTA_CREDITO.getInfoValidacion());
		}
		if(factura.getRemitos()==null){ //Si es factura sin remito, tengo que borrar los movimientos de stock y aumentar el stock
			factura = getByNroFacturaConItems(factura.getNroFactura());
			movimientoStockFacade.borrarMovientoStock(factura);
		}
		facturaDao.removeById(factura.getId());
		return factura;
	}

	public Factura guardarFacturaYGenerarMovimiento(Factura factura, String usuario) throws ValidacionException, ValidacionExceptionSinRollback  {
		Factura f = guardarInterno(factura,usuario);
		auditoriaFacade.auditar(usuario, "Creación de factura Nº: " + factura.getNroFactura(), EnumTipoEvento.ALTA, f);
		return docContableFacade.autorizarDocumentoContableAFIP(f); 
	}

	public Factura editarFactura(Factura factura, String usuario) throws ValidacionException {
		docContableFacade.checkAutorizacionAFIP(factura);
		Factura facturaAnterior = getByIdEager(factura.getId());
//		factura = guardarInterno(factura);
		cuentaFacade.actualizarMovimientoFacturaCliente(factura,facturaAnterior.getMontoTotal());
		factura = facturaDao.save(factura);
		auditoriaFacade.auditar(usuario, "Edición de factura Nº: " + factura.getNroFactura(), EnumTipoEvento.MODIFICACION,factura);
		return factura;
	}

	private Factura guardarInterno(Factura factura,String usuario) {
		factura.setNroSucursal(parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
		Factura f = facturaDao.save(factura);
		cuentaFacade.crearMovimientoDebe(f);
		if (f.getRemitos() == null) { // es factura sin remito
			for (ItemFactura it : f.getItems()) {
				if (it.getTipo() == ETipoItemFactura.STOCK) {
					ItemFacturaPrecioMateriaPrima itm = (ItemFacturaPrecioMateriaPrima) it;
					PrecioMateriaPrima precioMateriaPrima = itm.getPrecioMateriaPrima();
					movimientoStockFacade.crearMovimientoResta(precioMateriaPrima, it.getCantidad(), f, precioMateriaPrima.getStockActual());
					precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(it.getCantidad().multiply(new BigDecimal(-1d)), precioMateriaPrima.getId());
				}
			}
		}else{ //actualizo las ODTs
			for(RemitoSalida rs : f.getRemitos()){
				for(OrdenDeTrabajo odt : rs.getOdts()){
					odtFacade.cambiarODTAFacturada(odt.getId(), usuSistemaFacade.getByNombre(usuario));
				}
			}
		}
		return f;
	}
	
	public Factura actualizarFactura(Factura factura) throws CLException{
		 return facturaDao.save(factura);
	}

	public Factura getByNroFactura(Integer nroFactura) {
		return facturaDao.getByNroFacturaConCorrecciones(nroFactura, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}
	
	public Factura getByNroFacturaConItems(Integer nroFactura){
		return facturaDao.getByNroFacturaConItems(nroFactura, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public List<Factura> getFacturaImpagaListByClient(Integer idCliente) {
		return facturaDao.getFacturaImpagaListByClient(idCliente, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public Factura getByIdEager(Integer id) {
		return facturaDao.getByIdEager(id);
	}

	public List<Timestamp> getFechasFacturasAnteriorYPosterior(Integer nroFactura, ETipoFactura tipoFactura, ETipoDocumento tipoDoc) {
		return facturaDao.getFacturaAnteriorYPosterior(nroFactura,tipoFactura, tipoDoc, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public void anularFactura(Factura factura, boolean anularRemitoSalida, String usuario) throws ValidacionException, CLException {
		docContableFacade.checkAutorizacionAFIP(factura);		
		if(facturaDao.facturaYaTieneRecibo(factura)){
			throw new ValidacionException(EValidacionException.FACTURA_YA_TIENE_RECIBO.getInfoValidacion());
		}
		factura.setEstadoFactura(EEstadoFactura.ANULADA);
		if(factura.getRemitos()!=null) {
			for(RemitoSalida remito : factura.getRemitos()){
				if(anularRemitoSalida) {
					remitoSalidaFacade.anularRemitoSalida(remito);
				} else {
					remito.setNroFactura(docContableFacade.getProximoNroDocumentoContable(factura.getCliente().getPosicionIva(), ETipoDocumento.FACTURA));
					remitoSalidaFacade.guardarRemito(remito);
				}
			}
		}
		cuentaFacade.borrarMovimientoFactura(factura);
		NotaCredito nc = correccionDao.getNotaDeCreditoByFacturaRelacionada(factura);
		if(nc!=null){
			nc.getFacturasRelacionadas().remove(factura);
			correccionDao.save(nc);
		}
		factura.setRemitos(null);
		factura = facturaDao.save(factura);
		auditoriaFacade.auditar(usuario, "Anulación de factura  Nº: " + factura.getNroFactura(),EnumTipoEvento.ANULACION,factura);
	}

	public void cambiarEstadoFactura(Factura factura, EEstadoFactura estadoNuevo, String usuario) {
		factura.setEstadoFactura(estadoNuevo);
		if(estadoNuevo == EEstadoFactura.VERIFICADA){
			factura.setUsuarioConfirmacion(usuario);
		}
		facturaDao.save(factura);
		auditoriaFacade.auditar(usuario, "Cambio de estado de factura Nº: " + factura.getNroFactura() + ". Estado nuevo: " + estadoNuevo.getDescripcion(),EnumTipoEvento.MODIFICACION,factura);
	}
	
	public IVAVentasTO calcularIVAVentas(Date fechaDesde, Date fechaHasta, ETipoFactura tipoFactura, Cliente cliente){
		IVAVentasTO ivaVentas = new IVAVentasTO();
		TotalesIVAVentasTO totalRI = new TotalesIVAVentasTO();
		TotalesIVAVentasTO totalGral = new TotalesIVAVentasTO();
		calcularFacturas(fechaDesde, fechaHasta, ivaVentas, totalRI, totalGral,tipoFactura,cliente);
		calcularCorrecciones(fechaDesde, fechaHasta, ivaVentas, totalRI, totalGral,cliente);
		ivaVentas.setTotalGeneral(totalGral);
		ivaVentas.setTotalResponsableInscripto(totalRI);
		Collections.sort(ivaVentas.getFacturas(), new DescripcionFacturaIVAVentasNroComparator());
		return ivaVentas;
	}
	
	private class DescripcionFacturaIVAVentasNroComparator implements Comparator<DescripcionFacturaIVAVentasTO>{
		public int compare(DescripcionFacturaIVAVentasTO o1, DescripcionFacturaIVAVentasTO o2) {
			return o1.getNroComprobanteSort().compareTo(o2.getNroComprobanteSort());
		}
	}

	private void calcularCorrecciones(Date fechaDesde, Date fechaHasta, IVAVentasTO ivaVentas, TotalesIVAVentasTO totalRI, TotalesIVAVentasTO totalGral, Cliente cl) {
		Integer nroSucursal = parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal();
		List<CorreccionFactura> correcciones = correccionDao.getCorreccionesByFecha(fechaDesde, fechaHasta, cl, nroSucursal);
		if(correcciones!=null && !correcciones.isEmpty()){
			for(CorreccionFactura c : correcciones){
				DescripcionFacturaIVAVentasTO dfiv = new DescripcionFacturaIVAVentasTO();
				String nro = c.getTipoFactura().getDescripcion() +  
							StringUtil.fillLeftWithZeros(String.valueOf(c.getNroSucursal()), 4) + 
							 "-" + StringUtil.fillLeftWithZeros(String.valueOf(c.getNroFactura()), 8);
				dfiv.setNroCte(nro);
				dfiv.setNroComprobanteSort(c.getNroFactura());
				dfiv.setTipoCte(c.getTipo().getDescripcionResumida());
				dfiv.setFecha(DateUtil.dateToString(c.getFechaEmision(), DateUtil.SHORT_DATE));
				if(c.getAnulada().booleanValue() == false){
					dfiv.setExento("0.00");
					dfiv.setNoGravado("0.00");//no se que es
					Cliente cliente = c.getCliente();
					dfiv.setPercepcion("0.00");
					dfiv.setPosIVA(cliente.getPosicionIva().getDescripcionResumida());
					if(c.getMontoSubtotal()!=null){ //FIXME: ESTO ES UN WORKAROUND PORQUE ANTES NO SE GUARDABA EL SUBTOTAL, ENTONCES PUEDE HABER NULLS
						dfiv.setNetoGravado(getDecimalFormat().format(c.getMontoSubtotal().doubleValue()*(c instanceof NotaCredito?-1:1)));
					}else{
						dfiv.setNetoGravado(getDecimalFormat().format(c.getMontoTotal().doubleValue()*(c instanceof NotaCredito?-1:1)));
					}
					dfiv.setRazonSocial(cliente.getRazonSocial());
					dfiv.setTotalComp(getDecimalFormat().format(c.getMontoTotal().doubleValue()*(c instanceof NotaCredito?-1:1)));
					dfiv.setCuit(cliente.getCuit());// *(c instanceof NotaCredito?-1:1)
					if(c.getPorcentajeIVAInscripto()!= null && c.getPorcentajeIVAInscripto().doubleValue()>0){
						double iva = 0;
						if(c instanceof NotaCredito){
							iva = c.getMontoSubtotal().doubleValue() * c.getPorcentajeIVAInscripto().doubleValue()/100*-1;
						}else{
							NotaDebito nd = (NotaDebito)c;
							if( (nd.getChequeRechazado()!=null || ( (nd.getIsParaRechazarCheque()!=null) && (nd.getIsParaRechazarCheque()==true) )) && nd.getGastos()!=null){
								iva = nd.getGastos().doubleValue() * c.getPorcentajeIVAInscripto().doubleValue()/100;
							}else{
								iva = c.getMontoSubtotal().doubleValue() * c.getPorcentajeIVAInscripto().doubleValue()/100;
							}
						}
						dfiv.setMontoIVA21(getDecimalFormat().format(iva));
						totalRI.setSumaTotalComp(totalRI.getSumaTotalComp() + c.getMontoTotal().doubleValue()*(c instanceof NotaCredito?-1:1));
						totalRI.setTotalIVA21(totalRI.getTotalIVA21() + iva);
						totalRI.setTotalNetoGravado(totalRI.getTotalNetoGravado() + c.getMontoSubtotal().doubleValue()*(c instanceof NotaCredito?-1:1));
						totalGral.setTotalIVA21(totalGral.getTotalIVA21() + iva);
					}else{
						dfiv.setMontoIVA21("0.00");
					}
					totalGral.setSumaTotalComp(totalGral.getSumaTotalComp() + c.getMontoTotal().doubleValue()*(c instanceof NotaCredito?-1:1));
					totalGral.setTotalNetoGravado(totalGral.getTotalNetoGravado() + (c.getMontoSubtotal()!=null?c.getMontoSubtotal().doubleValue():c.getMontoTotal().doubleValue())*(c instanceof NotaCredito?-1:1));
				}else{
					dfiv.setExento("0.00");
					dfiv.setPercepcion("0.00");
					dfiv.setMontoIVA21("0.00");
					dfiv.setNetoGravado("0.00");
					dfiv.setNoGravado("0.00");
					dfiv.setRazonSocial(EEstadoFactura.ANULADA.getDescripcion());
					dfiv.setTotalComp("0.00");
					dfiv.setPosIVA("");
				}
				ivaVentas.getFacturas().add(dfiv);
			}
		}
	}

	private void calcularFacturas(Date fechaDesde, Date fechaHasta, IVAVentasTO ivaVentas, TotalesIVAVentasTO totalRI, TotalesIVAVentasTO totalGral, ETipoFactura tipoFactura, Cliente cl) {
		Integer nroSucursal = parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal();
		List<Factura> facturas = facturaDao.getFacturasEntreFechas(fechaDesde, fechaHasta, tipoFactura, cl, nroSucursal);
		if(facturas!=null && !facturas.isEmpty()){
			for(Factura f : facturas){
				DescripcionFacturaIVAVentasTO dfiv = new DescripcionFacturaIVAVentasTO();
				String nro =  f.getTipoFactura().getDescripcion()+ 
							 StringUtil.fillLeftWithZeros(String.valueOf(f.getNroSucursal()), 4) + 
							 "-" + StringUtil.fillLeftWithZeros(String.valueOf(f.getNroFactura()), 8);
				dfiv.setNroCte(nro);
				dfiv.setNroComprobanteSort(f.getNroFactura());
				dfiv.setTipoCte("FCV");
				dfiv.setFecha(DateUtil.dateToString(f.getFechaEmision(), DateUtil.SHORT_DATE));
				if(f.getEstadoFactura()!=EEstadoFactura.ANULADA){
					dfiv.setExento("0.00"); //por ahora, despues hay que agregarlo en algun lado
					Cliente cliente = f.getCliente();
					dfiv.setPosIVA(cliente.getPosicionIva().getDescripcionResumida());
					Double percepcion = getPercepcion(f);
					dfiv.setPercepcion(percepcion>0?getDecimalFormat().format(percepcion):"0.00");
					dfiv.setNoGravado("0.00");//no se que es
					dfiv.setRazonSocial(cliente.getRazonSocial());
					dfiv.setTotalComp(getDecimalFormat().format(f.getMontoTotal().doubleValue()));
					dfiv.setCuit(cliente.getCuit());
					
					if(f.getTipoFactura() == ETipoFactura.A){ //RESPONSABLE INSCRIPTO O RESPONSABLE INSCRIPTO + AGENTE DE RETENCION
						dfiv.setMontoIVA21(getDecimalFormat().format(f.getMontoSubtotal().doubleValue() * f.getPorcentajeIVAInscripto().doubleValue()/100));
						totalRI.setSumaTotalComp(totalRI.getSumaTotalComp() + f.getMontoTotal().doubleValue());
						totalRI.setTotalIVA21(totalRI.getTotalIVA21() + f.getMontoSubtotal().doubleValue() * f.getPorcentajeIVAInscripto().doubleValue()/100);
						totalRI.setTotalNetoGravado(totalRI.getTotalNetoGravado() + f.getMontoSubtotal().doubleValue());
						totalRI.setTotalPercepcion(totalRI.getTotalPercepcion()+ percepcion);
						totalGral.setTotalIVA21(totalGral.getTotalIVA21() + f.getMontoSubtotal().doubleValue() * f.getPorcentajeIVAInscripto().doubleValue()/100);
						totalGral.setTotalPercepcion(totalGral.getTotalPercepcion()+ percepcion);
						dfiv.setNetoGravado(getDecimalFormat().format(f.getMontoSubtotal().doubleValue()));
						totalGral.setTotalNetoGravado(totalGral.getTotalNetoGravado() + f.getMontoSubtotal().doubleValue());
					}else{
						//hardcodetti
						BigDecimal iva21 = f.getMontoTotal().multiply(new BigDecimal(0.21));
						dfiv.setMontoIVA21(getDecimalFormat().format(iva21));
						totalGral.setTotalIVA21(totalGral.getTotalIVA21() + iva21.doubleValue());
						BigDecimal subTotalB = f.getMontoTotal().subtract(iva21);
						dfiv.setNetoGravado(getDecimalFormat().format(subTotalB));
						totalGral.setTotalNetoGravado(totalGral.getTotalNetoGravado() + subTotalB.doubleValue());
					}
					totalGral.setSumaTotalComp(totalGral.getSumaTotalComp() + f.getMontoTotal().doubleValue());
				}else{
					dfiv.setExento("0.00");
					dfiv.setPercepcion("0.00");
					dfiv.setMontoIVA21("0.00");
					dfiv.setNetoGravado("0.00");
					dfiv.setNoGravado("0.00");
					dfiv.setRazonSocial(EEstadoFactura.ANULADA.getDescripcion());
					dfiv.setTotalComp("0.00");
					dfiv.setPosIVA("");
				}
				ivaVentas.getFacturas().add(dfiv);
			}
		}
	}
	
	private Double getPercepcion(Factura f) {
		for (ItemFactura it : f.getItems()) {
			if (it instanceof ItemFacturaPercepcion) {
				return it.getImporte().doubleValue();
			}
		}
		return 0d;
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		df.setMaximumFractionDigits(2);
		df.setGroupingUsed(true);
		return df;
	}

	public List<Factura> getAllFacturasByCliente(Integer idCliente) {
		return facturaDao.getAllFacturasByCliente(idCliente, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public Integer getUltimoNumeroFacturaImpreso(ETipoFactura tipoFactura) {
		return facturaDao.getUltimoNumeroFacturaImpreso(tipoFactura, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public void pruebaAutorizar() {
		try {
			Factura byId = facturaDao.getById(3872);
			byId.setNroFactura(2);
			DatosRespuestaAFIP autorizarDocumento = AFIPConnector.getInstance().autorizarDocumento(byId,parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal(),1);
			System.out.println(autorizarDocumento);
			
//			FERecuperaLastCbteResponse ultimoNroComprobanteAutorizado = AFIPConnector.getInstance().getUltimoNroComprobanteAutorizado(1, 1);
//			System.out.println(ultimoNroComprobanteAutorizado);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}