package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionExceptionSinRollback;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.dao.api.local.CorreccionDAOLocal;
import ar.com.textillevel.dao.api.local.CuentaDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.MovimientoCuentaDAOLocal;
import ar.com.textillevel.dao.api.local.NotaDebitoDAOLocal;
import ar.com.textillevel.dao.api.local.ReciboDAOLocal;
import ar.com.textillevel.entidades.cuenta.CuentaCliente;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebe;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaber;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.NotaCredito;
import ar.com.textillevel.entidades.documentos.factura.NotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.Recibo;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPago;
import ar.com.textillevel.entidades.documentos.recibo.formapago.FormaPagoNotaCredito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoRecibo;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboACuenta;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboFactura;
import ar.com.textillevel.entidades.documentos.recibo.pagos.PagoReciboNotaDebito;
import ar.com.textillevel.entidades.documentos.recibo.pagos.visitor.IPagoReciboVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.entidades.enums.EEstadoCorreccion;
import ar.com.textillevel.entidades.enums.EEstadoFactura;
import ar.com.textillevel.facade.api.local.CorreccionFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasClientesFacadeRemote;

@Stateless
public class CorrectorCuentasClientesFacade implements CorrectorCuentasClientesFacadeRemote {

	@EJB
	private CuentaDAOLocal ctaDAO; 

	@EJB
	private NotaDebitoDAOLocal notaDebDAO; 
	
	@EJB
	private CorreccionDAOLocal correccionDAO; 

	@EJB
	private ReciboDAOLocal reciboDAO; 
	
	@EJB
	private FacturaDAOLocal facturaDAO;

	@EJB
	private MovimientoCuentaDAOLocal movDAO; 

	@EJB
	private CuentaFacadeLocal cuentaFacade;

	@EJB
	private MovimientoCuentaDAOLocal movimientoDAO;

	@EJB
	private FacturaFacadeLocal facturaFacade;
	
	@EJB
	private CorreccionFacadeLocal correccionFacade;

	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;
	
	public void corregirCuenta(Integer idCliente, String usrName) throws ValidacionException {
		Map<DocMovimientoHaber, String> observacionesDocMHMap = new HashMap<DocMovimientoHaber, String>();
		Map<PagoReciboWrapper, String> observacionesPagosMDMap = new HashMap<PagoReciboWrapper, String>();

		 //Map para no registrar mas de una vez una ND/Factura
		Set<Integer> movimientosYaRegistrados = new HashSet<Integer>();
		CuentaCliente cuentaClienteByIdCliente = ctaDAO.getCuentaClienteByIdCliente(idCliente);
		if(cuentaClienteByIdCliente==null) {
			return;
		}

		//Borro los movimientos pero antes guardo las observaciones
		List<MovimientoCuenta> movimientosCuentaArticulo = movDAO.getAllMovimientosByIdCuentaCliente(cuentaClienteByIdCliente.getId());
		for(MovimientoCuenta m :  movimientosCuentaArticulo) {
			if(!StringUtil.isNullOrEmpty(m.getObservaciones())) {
				if(m instanceof MovimientoDebe) {
					MovimientoDebe md = (MovimientoDebe)m;
					if(md.getFactura()!=null) {
						observacionesPagosMDMap.put(new PagoReciboWrapper(md.getFactura()), m.getObservaciones());
					} else {
						observacionesPagosMDMap.put(new PagoReciboWrapper(md.getNotaDebito()), m.getObservaciones());
					}
				}
				if(m instanceof MovimientoHaber) {
					MovimientoHaber mh = (MovimientoHaber)m;
					if(mh.getNotaCredito()!=null) {
						observacionesDocMHMap.put(new DocMovimientoHaber(mh.getNotaCredito()), m.getObservaciones());
					} else {
						observacionesDocMHMap.put(new DocMovimientoHaber(mh.getRecibo()), m.getObservaciones());
					}
				}
			}
			movDAO.removeById(m.getId());
		}

		//pongo la cuenta en 0
		cuentaClienteByIdCliente.setSaldo(new BigDecimal(0));
		cuentaClienteByIdCliente = (CuentaCliente)ctaDAO.save(cuentaClienteByIdCliente);

		//Obtengo facturas, nc, nd y recibos
		List<PagoReciboWrapper> prList = obtenerNDsAndFacturas(idCliente);
		List<DocMovimientoHaber> docList = obtenerNCsAndRecibos(idCliente);
		List<MetaDocumentoWrapper> allDocuments = new ArrayList<CorrectorCuentasClientesFacade.MetaDocumentoWrapper>();
		for(PagoReciboWrapper prw : prList) {
			allDocuments.add(new MetaDocumentoWrapper(prw));
		}
		for(DocMovimientoHaber doc : docList) {
			allDocuments.add(new MetaDocumentoWrapper(doc));
		}
		Collections.sort(allDocuments);
		
		//Comienzo a grabar los documentos
		for(MetaDocumentoWrapper metadoc : allDocuments) {
			if(metadoc.isDoc()) {
				simularIngresoUnaNcOrRecibo(metadoc.documento, usrName, prList, movimientosYaRegistrados, observacionesDocMHMap, observacionesPagosMDMap);
			} else {
				persistirNdOrFactura(usrName, movimientosYaRegistrados, metadoc.pagoReciboWrapper, observacionesPagosMDMap);
			}
		}

	}

	private List<DocMovimientoHaber> obtenerNCsAndRecibos(Integer idCliente) {
		Map<Integer, Recibo> recibosRevisadosMap = new HashMap<Integer, Recibo>();
		
		List<DocMovimientoHaber> resultList = new ArrayList<CorrectorCuentasClientesFacade.DocMovimientoHaber>();
		List<Recibo> allRecibos = reciboDAO.getAllNoAnuladosByIdCliente(idCliente);
		for(Recibo r : allRecibos) {
			resultList.add(new DocMovimientoHaber(r));
			//reviso si hay repetidos
			Recibo reciboRevisado = recibosRevisadosMap.get(r.getNroRecibo());
			if(reciboRevisado == null) {
				recibosRevisadosMap.put(r.getNroRecibo(), r);
			} else {
				tratarDuplicacionRecibo(r, reciboRevisado);
			}
		}
		List<NotaCredito> allNotaCreditoList = correccionDAO.getAllNotaCreditoList(idCliente, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
		for(NotaCredito nc : allNotaCreditoList) {
			resultList.add(new DocMovimientoHaber(nc));
		}
		Collections.sort(resultList);
		//Corrige las fechas para que sean consistentes con los números de recibo
		int i = 1;
		for(DocMovimientoHaber prw : resultList) {
			prw.sumarSegundos(DateUtil.ONE_SECOND*i);
			i++;
		}
		return resultList;
	}

	private void tratarDuplicacionRecibo(Recibo r, Recibo reciboRevisado) {
		String mensajeSiSonDistintos = "Existen dos recibos con el mismo número y noparecen ser iguales en cuanto a sus características. Nro. Recibo: " + r.getNroRecibo();
		if(r.getMonto().compareTo(reciboRevisado.getMonto()) != 0) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(r.getFechaEmision().equals(reciboRevisado.getFechaEmision())) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(mismoDia(r.getFecha(),reciboRevisado.getFecha())) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(r.getPagos().size() != reciboRevisado.getPagos().size()) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(r.getPagoReciboList().size() != reciboRevisado.getPagoReciboList().size()) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(r.getEstadoRecibo() != reciboRevisado.getEstadoRecibo()) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		reciboDAO.removeById(reciboRevisado.getId());
	}

	private List<PagoReciboWrapper> obtenerNDsAndFacturas(Integer idCliente) {
		List<NotaDebito> allNDByIdCliente = notaDebDAO.getAllByIdCliente(idCliente);
		List<PagoReciboWrapper> prList = new ArrayList<CorrectorCuentasClientesFacade.PagoReciboWrapper>();
		for(NotaDebito nd : allNDByIdCliente) {
			prList.add(new PagoReciboWrapper(nd));
		}
		List<Factura> facturaList = facturaDAO.getAllByIdClienteList(idCliente, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
		for(Factura f : facturaList) {
			prList.add(new PagoReciboWrapper(f));
		}
		Collections.sort(prList, new Comparator<PagoReciboWrapper>() {

			public int compare(PagoReciboWrapper o1, PagoReciboWrapper o2) {
				if(!o1.isFactura() && !o2.isFactura()) {
					return o1.getNroDocumento().compareTo(o2.getNroDocumento());
				}
				if(o1.isFactura() && o2.isFactura()) {
					return o1.getNroDocumento().compareTo(o2.getNroDocumento());
				}
				if(!o1.isFactura() && o2.isFactura()) {
					return -1;
				}
				if(o1.isFactura() && !o2.isFactura()) {
					return 1;
				}
				return 0;
			}
		});
		
		//Corrige las fechas para que sean consistentes con los números de factura
		int i = 1;
		for(PagoReciboWrapper prw : prList) {
			prw.sumarSegundos(DateUtil.ONE_SECOND*i);
			i++;
		}
		return prList;
	}

	private void simularIngresoUnaNcOrRecibo(DocMovimientoHaber ncOrRecibo, String usrName, List<PagoReciboWrapper> prList, Set<Integer> movimientosYaRegistrados, Map<DocMovimientoHaber, String> observacionesDocMHMap, Map<PagoReciboWrapper, String> observacionesPagosMDMap) throws ValidacionException {
		if(ncOrRecibo.isRecibo()) {
			Recibo recibo = ncOrRecibo.getRecibo();
			recibo.getPagoReciboList().clear();
			getPagosRecibosPosiblesPorFecha(recibo);
			//Ingreso el recibo
			recibo.setFechaEmision(new Timestamp(recibo.getFecha().getTime()));
			recibo.getPagoReciboList().size();
			
			ActualizarItemPagoReciboVisitor visitor = new ActualizarItemPagoReciboVisitor();
			for(PagoRecibo pr : recibo.getPagoReciboList()) {
				pr.accept(visitor);
			}
			
			//Si el recibo posee notas de crédito las pongo en cero
			for(FormaPago fp : recibo.getPagos()) {
				if(fp instanceof FormaPagoNotaCredito) {
					((FormaPagoNotaCredito)fp).getNotaCredito().setMontoSobrante(BigDecimal.ZERO);
				}
			}

			recibo = reciboDAO.save(recibo);
			cuentaFacade.crearMovimientoHaber(recibo);
			//seteo las observaciones si antes tenía
			String obs = observacionesDocMHMap.get(ncOrRecibo);
			if(!StringUtil.isNullOrEmpty(obs)) {
				MovimientoHaber movimientoHaberByRecibo = movimientoDAO.getMovimientoHaberByRecibo(recibo.getId());
				movimientoHaberByRecibo.setObservaciones(obs);
				movimientoDAO.save(movimientoHaberByRecibo);
			}
		} else {
			try{
				NotaCredito nc = ncOrRecibo.getNc();
				nc = (NotaCredito) correccionFacade.guardarCorreccionYGenerarMovimiento(nc, usrName);
				ncOrRecibo.setNc(nc);
				//seteo las observaciones si antes tenía
				String obs = observacionesDocMHMap.get(ncOrRecibo);
				if(!StringUtil.isNullOrEmpty(obs)) {
					MovimientoHaber movimientoHaberByNC = movimientoDAO.getMovimientoHaberByNC(nc.getId());
					movimientoHaberByNC.setObservaciones(obs);
					movimientoDAO.save(movimientoHaberByNC);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void persistirNdOrFactura(String usrName, Set<Integer> movimientosYaRegistrados, PagoReciboWrapper prw, Map<PagoReciboWrapper, String> observacionesPagosMDMap) {
		try {
			if(prw.isFactura()) {
//				if(!movimientosYaRegistrados.contains(prw.getFactura().getId())) {
					prw.setFactura(facturaFacade.guardarFacturaYGenerarMovimiento(prw.getFactura(), usrName));
					movimientosYaRegistrados.add(prw.getFactura().getId());
					String obs = observacionesPagosMDMap.get(prw);
					if(!StringUtil.isNullOrEmpty(obs)) {
						MovimientoCuenta movimientoDebeByFactura = movimientoDAO.getMovimientoDebeByFactura(prw.getFactura());
						movimientoDebeByFactura.setObservaciones(obs);
						movimientoDAO.save(movimientoDebeByFactura);
					}
//				}
			} else {
//				if(!movimientosYaRegistrados.contains(prw.getNd().getId())) {
					prw.setNd((NotaDebito)correccionFacade.guardarCorreccionYGenerarMovimiento(prw.getNd(), usrName));
					movimientosYaRegistrados.add(prw.getNd().getId());
					String obs = observacionesPagosMDMap.get(prw);
					if(!StringUtil.isNullOrEmpty(obs)) {
						MovimientoCuenta movimientoDebeByFactura = movimientoDAO.getMovimientoDebeByND(prw.getNd().getId());
						movimientoDebeByFactura.setObservaciones(obs);
						movimientoDAO.save(movimientoDebeByFactura);
					}
//				}
			}
		} catch (ValidacionException e) {
			e.printStackTrace();
		} catch (ValidacionExceptionSinRollback e) {
			e.printStackTrace();
		}
	}

	private BigDecimal getMontoRealRecibo(Recibo r) {
		BigDecimal mr = r.getMonto();
		for(FormaPago fp : r.getPagos()) {
			if(fp instanceof FormaPagoNotaCredito) {
				mr = mr.add(((FormaPagoNotaCredito) fp).getImporteNC());
			}
		}
		return mr;
	}

	private void getPagosRecibosPosiblesPorFecha(Recibo r) throws ValidacionException {
		BigDecimal totalMontoPagado = getMontoRealRecibo(r);
		List<NotaDebito> notaDebitoList = notaDebDAO.getNotaDebitoPendientePagarList(r.getCliente().getId());
		for(NotaDebito nd : notaDebitoList) {
			if((mismoDia(nd.getFechaEmision(), r.getFecha()) || !nd.getFechaEmision().after(r.getFecha())) && totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
				BigDecimal montoND = nd.getMontoFaltantePorPagar();
				PagoReciboNotaDebito prnd = new PagoReciboNotaDebito();
				prnd.setNotaDebito(nd);
				if(truncBD(montoND.doubleValue())>= truncBD(totalMontoPagado.doubleValue())) {
					prnd.setMontoPagado(totalMontoPagado);
					totalMontoPagado = new BigDecimal(0);
				} else {
					prnd.setMontoPagado(montoND);
					totalMontoPagado = totalMontoPagado.subtract(montoND);
				}
				r.getPagoReciboList().add(prnd);
			}
		}
		List<Factura> facturaList = facturaDAO.getFacturaImpagaListByClient(r.getCliente().getId(), parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
		for(Factura f : facturaList) {
			if((mismoDia(f.getFechaEmision(), r.getFecha()) || !f.getFechaEmision().after(r.getFecha())) && totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
				BigDecimal montoF = f.getMontoFaltantePorPagar();
				PagoReciboFactura prf = new PagoReciboFactura();
				prf.setFactura(f);
				if(truncBD(montoF.doubleValue()) >= truncBD(totalMontoPagado.doubleValue())) {
					prf.setMontoPagado(totalMontoPagado);
					totalMontoPagado = new BigDecimal(0);
				} else {
					prf.setMontoPagado(montoF);
					totalMontoPagado = totalMontoPagado.subtract(montoF);
				}
				//Seteo la descripción del pago de factura
				if(prf.getMontoPagado().compareTo(f.getMontoTotal()) == 0) {
					prf.setDescrPagoFactura(EDescripcionPagoFactura.FACTURA);
				} else {
					if(prf.getMontoPagado().compareTo(montoF) == 0) {
						prf.setDescrPagoFactura(EDescripcionPagoFactura.SALDO);
					} else {
						prf.setDescrPagoFactura(EDescripcionPagoFactura.A_CUENTA);
					}
				}
				r.getPagoReciboList().add(prf);
			}
		}
		//Si sobró algo de monto entonces creo un pago "a cuenta" 
		if(totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
			PagoReciboACuenta prac = new PagoReciboACuenta();
			prac.setMontoPagado(totalMontoPagado);
			r.getPagoReciboList().add(prac);
		}
	}

	private double truncBD(double d) {
		d = d*100;
		double tmpD = ((int)d)/100d;
		return tmpD;
	}
	
	private boolean mismoDia(Date fecha, java.sql.Date fecha2) {
		boolean dia = DateUtil.getDia(new java.sql.Date(fecha.getTime())) == DateUtil.getDia(fecha2);
		boolean mes = DateUtil.getMes(new java.sql.Date(fecha.getTime())) == DateUtil.getMes(fecha2);
		boolean anio = DateUtil.getAnio(new java.sql.Date(fecha.getTime())) == DateUtil.getAnio(fecha2);
		return dia && mes && anio;
	}

	private static class PagoReciboWrapper {

		private NotaDebito nd;
		public void setNd(NotaDebito nd) {
			this.nd = nd;
		}

		public void setFactura(Factura factura) {
			this.factura = factura;
		}

		private Factura factura;

		public PagoReciboWrapper(Factura factura) {
			factura.setMontoFaltantePorPagar(factura.getMontoTotal());
			factura.setEstadoFactura(EEstadoFactura.IMPAGA);
			factura.setFechaEmision(new Timestamp(DateUtil.redondearFecha(factura.getFechaEmision()).getTime()));			
			this.factura = factura;
		}

		public void sumarSegundos(long segundos) {
			if(isFactura()) {
				long totalSegundos = getFactura().getFechaEmision().getTime() + segundos;
				getFactura().setFechaEmision(new Timestamp(totalSegundos));
			} else {
				long totalSegundos = getNd().getFechaEmision().getTime() + segundos;
				getNd().setFechaEmision(new Timestamp(totalSegundos));
			}
		}

		public Date getFecha() {
			if(isFactura()) {
				return getFactura().getFechaEmision();
			} else {
				return getNd().getFechaEmision();
			}
		}

		public PagoReciboWrapper(NotaDebito nd) {
			nd.setMontoFaltantePorPagar(nd.getMontoTotal());
			nd.setEstadoCorreccion(EEstadoCorreccion.IMPAGA);
			nd.setFechaEmision(new Timestamp(DateUtil.redondearFecha(nd.getFechaEmision()).getTime()));
			this.nd = nd;
		}

		public NotaDebito getNd() {
			return nd;
		}

		public Factura getFactura() {
			return factura;
		}

		public boolean isFactura() {
			return getFactura() != null;
		}

		@Override
		public boolean equals(Object obj) {
			return toString().compareToIgnoreCase(obj.toString()) == 0;
		}

		@Override
		public int hashCode() {
			if(isFactura()) {
				return getFactura().hashCode();
			} else {
				return getNd().hashCode();
			}
		}

		public Integer getNroDocumento() {
			if(isFactura()) {
				return getFactura().getNroFactura();
			} else {
				return getNd().getNroFactura();
			}
		}

		@Override
		public String toString() {
			if(isFactura()) {
				return getFactura().toString();
			} else {
				return getNd().toString();
			}
		}

	}

	private static class DocMovimientoHaber implements Comparable<DocMovimientoHaber> {

		private NotaCredito nc;
		public void setNc(NotaCredito nc) {
			this.nc = nc;
		}

		private Recibo recibo;

		public DocMovimientoHaber(NotaCredito nc) {
			nc.setMontoSobrante(nc.getMontoTotal());
			this.nc = nc;
			nc.setFechaEmision(new Timestamp(DateUtil.redondearFecha(nc.getFechaEmision()).getTime()));
		}

		public void sumarSegundos(long segundos) {
			if(isRecibo()) {
				long totalSegundos = getRecibo().getFecha().getTime() + segundos;
				getRecibo().setFecha(new java.sql.Date(totalSegundos));
			} else {
				long totalSegundos = getNc().getFechaEmision().getTime() + segundos;
				getNc().setFechaEmision(new Timestamp(totalSegundos));
			}
		}

		public DocMovimientoHaber(Recibo recibo) {
			this.recibo = recibo;
		}

		public NotaCredito getNc() {
			return nc;
		}

		public Recibo getRecibo() {
			return recibo;
		}
		
		public boolean isRecibo() {
			return getRecibo() != null;
		}

		@Override
		public boolean equals(Object obj) {
			return toString().compareToIgnoreCase(obj.toString()) == 0;
		}

		@Override
		public int hashCode() {
			if(isRecibo()) {
				return getRecibo().hashCode();
			} else {
				return getNc().hashCode();
			}
		}

		public Date getFecha() {
			if(isRecibo()) {
				return getRecibo().getFecha();
			} else {
				return getNc().getFechaEmision();
			}
		}
		
		@Override
		public String toString() {
			if(isRecibo()) {
				return getRecibo().toString();
			} else {
				return getNc().toString();
			}
		}

		public int compareTo(DocMovimientoHaber o) {
			return getFecha().compareTo(o.getFecha());
		}

	}

	private static class MetaDocumentoWrapper implements Comparable<MetaDocumentoWrapper> {

		private PagoReciboWrapper pagoReciboWrapper;
		private DocMovimientoHaber documento;
		
		public MetaDocumentoWrapper(PagoReciboWrapper pagoReciboWrapper) {
			this.pagoReciboWrapper = pagoReciboWrapper;
		}

		public MetaDocumentoWrapper(DocMovimientoHaber documento) {
			this.documento = documento;
		}
		
		public boolean isDoc() {
			return documento != null;
		}
		
		public Date getFecha() {
			if(isDoc()) {
				return documento.getFecha();
			} else {
				return pagoReciboWrapper.getFecha();
			}
		}

		public int compareTo(MetaDocumentoWrapper o) {
			return getFecha().compareTo(o.getFecha());
		}
	
	}

	private class ActualizarItemPagoReciboVisitor implements IPagoReciboVisitor {

		public void visit(PagoReciboACuenta prac) {
		}

		public void visit(PagoReciboFactura prf) {
			Factura f = prf.getFactura();
			if (igualANDecimales(prf.getMontoPagado().doubleValue(), f.getMontoFaltantePorPagar().doubleValue(), 4)) {
				f.setEstadoFactura(EEstadoFactura.PAGADA);
				f.setMontoFaltantePorPagar(new BigDecimal(0));
			} else {
				f.setMontoFaltantePorPagar(f.getMontoFaltantePorPagar().subtract(prf.getMontoPagado()));
				f.setEstadoFactura(EEstadoFactura.IMPAGA);
			}
		}

		private boolean igualANDecimales(double d1, double d2, int n) {
			double base = Math.pow(10, n);
			d1 = ((int)(d1*base))/base;
			d2 = ((int)(d2*base))/base;
			return d1 == d2;
		}

		public void visit(PagoReciboNotaDebito prnd) {
			NotaDebito nd = prnd.getNotaDebito();
			if (igualANDecimales(prnd.getMontoPagado().doubleValue(), nd.getMontoFaltantePorPagar().doubleValue(), 4)) {
				nd.setMontoFaltantePorPagar(new BigDecimal(0));
				nd.setEstadoCorreccion(EEstadoCorreccion.PAGADA);
			} else {
				nd.setMontoFaltantePorPagar(nd.getMontoFaltantePorPagar().subtract(prnd.getMontoPagado()));
				nd.setEstadoCorreccion(EEstadoCorreccion.IMPAGA);
			}
		}

	}

}