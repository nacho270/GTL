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

import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.CorreccionFacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.CuentaDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.MovimientoCuentaDAOLocal;
import ar.com.textillevel.dao.api.local.OrdenDePagoDAOLocal;
import ar.com.textillevel.entidades.cuenta.CuentaProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoCuenta;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoDebeProveedor;
import ar.com.textillevel.entidades.cuenta.movimientos.MovimientoHaberProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaDebitoProveedor;
import ar.com.textillevel.entidades.documentos.ordendepago.OrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePago;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoACuenta;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoFactura;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.PagoOrdenDePagoNotaDebito;
import ar.com.textillevel.entidades.documentos.ordendepago.pagos.visitor.IPagoOrdenPagoVisitor;
import ar.com.textillevel.entidades.enums.EDescripcionPagoFactura;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.remote.CorrectorCuentasProveedorFacadeRemote;

@Stateless
public class CorrectorCuentasProveedorFacade implements CorrectorCuentasProveedorFacadeRemote {

	@EJB
	private CuentaDAOLocal ctaDAO; 

	@EJB
	private CorreccionFacturaProveedorDAOLocal correccionDAO; 

	@EJB
	private OrdenDePagoDAOLocal ordenDePagoDAO; 

	@EJB
	private FacturaProveedorDAOLocal facturaDAO;

	@EJB
	private MovimientoCuentaDAOLocal movDAO; 

	@EJB
	private CuentaFacadeLocal cuentaFacade;

	@EJB
	private MovimientoCuentaDAOLocal movimientoDAO;

	@EJB
	private FacturaProveedorFacadeLocal facturaFacade;
	
	@EJB
	private CorreccionFacturaProveedorFacadeLocal correccionFacade;

	public void corregirCuenta(Integer idProveedor, String usrName) throws ValidacionException {
		Map<DocMovimientoHaber, String> observacionesDocMHMap = new HashMap<DocMovimientoHaber, String>();
		Map<PagoReciboWrapper, String> observacionesPagosMDMap = new HashMap<PagoReciboWrapper, String>();

		 //Map para no registrar mas de una vez una ND/Factura
		Set<Integer> movimientosYaRegistrados = new HashSet<Integer>();
		CuentaProveedor cuentaProveedorByIdProveedor = ctaDAO.getCuentaProveedorByIdProveedor(idProveedor);
		if(cuentaProveedorByIdProveedor==null) {
			return;
		}

		//pongo la cuenta en 0
		cuentaProveedorByIdProveedor.setSaldo(new BigDecimal(0));
		cuentaProveedorByIdProveedor = (CuentaProveedor)ctaDAO.save(cuentaProveedorByIdProveedor);

		//Borro los movimientos pero antes guardo las observaciones
		List<MovimientoCuenta> movimientosCuenta = movDAO.getAllMovimientosByIdCuentaCliente(cuentaProveedorByIdProveedor.getId());
		for(MovimientoCuenta m :  movimientosCuenta) {
			if(!StringUtil.isNullOrEmpty(m.getObservaciones())) {
				if(m instanceof MovimientoDebeProveedor) {
					MovimientoDebeProveedor md = (MovimientoDebeProveedor)m;
					if(md.getFacturaProveedor()!=null) {
						observacionesPagosMDMap.put(new PagoReciboWrapper(md.getFacturaProveedor()), m.getObservaciones());
					} else {
						observacionesPagosMDMap.put(new PagoReciboWrapper(md.getNotaDebitoProveedor()), m.getObservaciones());
					}
				}
				if(m instanceof MovimientoHaberProveedor) {
					MovimientoHaberProveedor mh = (MovimientoHaberProveedor)m;
					if(mh.getNotaCredito()!=null) {
						observacionesDocMHMap.put(new DocMovimientoHaber(mh.getNotaCredito()), m.getObservaciones());
					} else {
						observacionesDocMHMap.put(new DocMovimientoHaber(mh.getOrdenDePago()), m.getObservaciones());
					}
				}
			}
			movDAO.removeById(m.getId());
		}

		//Obtengo facturas, nc, nd y recibos
		List<PagoReciboWrapper> prList = obtenerNDsAndFacturas(idProveedor);
		List<DocMovimientoHaber> docList = obtenerNCsAndOrdenesDePago(idProveedor);
		List<MetaDocumentoWrapper> allDocuments = new ArrayList<CorrectorCuentasProveedorFacade.MetaDocumentoWrapper>();
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
				simularIngresoUnaNcOrOp(metadoc.documento, usrName, prList, movimientosYaRegistrados, observacionesDocMHMap, observacionesPagosMDMap);
			} else {
				persistirNdOrFactura(usrName, movimientosYaRegistrados, metadoc.pagoReciboWrapper, observacionesPagosMDMap);
			}
		}

	}

	private List<DocMovimientoHaber> obtenerNCsAndOrdenesDePago(Integer idProveedor) {
		Map<Integer, OrdenDePago> opsRevisadosMap = new HashMap<Integer, OrdenDePago>();

		List<DocMovimientoHaber> resultList = new ArrayList<CorrectorCuentasProveedorFacade.DocMovimientoHaber>();
		List<OrdenDePago> allOrdenesDePago = ordenDePagoDAO.getAllByIdProveedor(idProveedor);

		for(OrdenDePago op : allOrdenesDePago) {
			resultList.add(new DocMovimientoHaber(op));
			//reviso si hay repetidos
			OrdenDePago opRevisado = opsRevisadosMap.get(op.getNroOrden());
			if(opRevisado == null) {
				opsRevisadosMap.put(op.getNroOrden(), op);
			} else {
				tratarDuplicacionOP(op, opRevisado);
			}
		}
		List<NotaCreditoProveedor> allNotaCreditoList = correccionDAO.getAllNotaCreditoList(idProveedor);
		for(NotaCreditoProveedor nc : allNotaCreditoList) {
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

	private void tratarDuplicacionOP(OrdenDePago op, OrdenDePago opRevisado) {
		String mensajeSiSonDistintos = "Existen dos ordenes de pago con el mismo número y no parecen ser iguales en cuanto a sus características. Nro. Orden: " + op.getNroOrden();
		if(op.getMonto().compareTo(opRevisado.getMonto()) != 0) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(op.getFechaEmision().equals(opRevisado.getFechaEmision())) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(mismoDia(op.getFechaEmision(),opRevisado.getFechaEmision())) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(op.getPagos().size() != opRevisado.getPagos().size()) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		if(op.getPagos().size() != opRevisado.getPagos().size()) {
			throw new IllegalStateException(mensajeSiSonDistintos);
		}
		ordenDePagoDAO.removeById(opRevisado.getId());
	}

	private List<PagoReciboWrapper> obtenerNDsAndFacturas(Integer idProveedor) {
		List<NotaDebitoProveedor> allNDByIdProveedor = correccionDAO.getAllNotaDebitoList(idProveedor);
		List<PagoReciboWrapper> prList = new ArrayList<CorrectorCuentasProveedorFacade.PagoReciboWrapper>();
		for(NotaDebitoProveedor nd : allNDByIdProveedor) {
			prList.add(new PagoReciboWrapper(nd));
		}
		List<FacturaProveedor> facturaList = facturaDAO.getAllByIdProveedorList(idProveedor);
		for(FacturaProveedor f : facturaList) {
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

	private void simularIngresoUnaNcOrOp(DocMovimientoHaber ncOrOp, String usrName, List<PagoReciboWrapper> prList, Set<Integer> movimientosYaRegistrados, Map<DocMovimientoHaber, String> observacionesDocMHMap, Map<PagoReciboWrapper, String> observacionesPagosMDMap) throws ValidacionException {
		if(ncOrOp.isOP()) {
			OrdenDePago op = ncOrOp.getOrdenDePago();
			
			if(StringUtil.isNullOrEmpty(op.getUsuarioCreador())) {
				op.setUsuarioCreador(usrName);
			}
			
			op.getPagos().clear();
			getPagosRecibosPosiblesPorFecha(op, prList);
			//Ingreso el recibo
			op.setFechaEmision(op.getFechaEmision());
			op.getPagos().size();

			ActualizarItemPagoOrdenDePagoVisitor visitor = new ActualizarItemPagoOrdenDePagoVisitor();
			for(PagoOrdenDePago pr : op.getPagos()) {
				pr.accept(visitor);
			}

			op = ordenDePagoDAO.save(op);
			cuentaFacade.crearMovimientoHaberProveedor(op);
			//seteo las observaciones si antes tenía
			String obs = observacionesDocMHMap.get(ncOrOp);
			if(!StringUtil.isNullOrEmpty(obs)) {
				MovimientoHaberProveedor movimientoHaberByRecibo = movimientoDAO.getMovimientoHaberProveedorByODP(op.getId());
				movimientoHaberByRecibo.setObservaciones(obs);
				movimientoDAO.save(movimientoHaberByRecibo);
			}
		} else {
			try{
				NotaCreditoProveedor nc = ncOrOp.getNc();
				nc.setMontoTotal(new BigDecimal(Math.abs(nc.getMontoTotal().doubleValue())).negate());//fix porque en algùn momento estabamos grabando mal las NC
				nc = (NotaCreditoProveedor) correccionFacade.guardarCorreccionYGenerarMovimiento(nc, usrName, null);
				ncOrOp.setNc(nc);
				//seteo las observaciones si antes tenía
				String obs = observacionesDocMHMap.get(ncOrOp);
				if(!StringUtil.isNullOrEmpty(obs)) {
					MovimientoHaberProveedor movimientoHaberByNC = movimientoDAO.getMovimientoHaberProveedorByNC(nc.getId());
					movimientoHaberByNC.setObservaciones(obs);
					movimientoDAO.save(movimientoHaberByNC);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void persistirNdOrFactura(String usrName, Set<Integer> movimientosYaRegistrados, PagoReciboWrapper prw, Map<PagoReciboWrapper, String> observacionesPagosMDMap) {
		if(prw.isFactura()) {
//				if(!movimientosYaRegistrados.contains(prw.getFactura().getId())) {
				prw.setFactura(facturaFacade.ingresarFactura(prw.getFactura(), usrName));
				
				prw.getFactura().setMontoFaltantePorPagar(prw.getFactura().getMontoTotal());

				movimientosYaRegistrados.add(prw.getFactura().getId());
				String obs = observacionesPagosMDMap.get(prw);
				if(!StringUtil.isNullOrEmpty(obs)) {
					MovimientoCuenta movimientoDebeByFactura = movimientoDAO.getMovimientoDebeProveedorByFactura(prw.getFactura());
					movimientoDebeByFactura.setObservaciones(obs);
					movimientoDAO.save(movimientoDebeByFactura);
				}
//				}
		} else {
//				if(!movimientosYaRegistrados.contains(prw.getNd().getId())) {
				prw.setNd((NotaDebitoProveedor)correccionFacade.guardarCorreccionYGenerarMovimiento(prw.getNd(), usrName, null));
				
				prw.getNd().setMontoFaltantePorPagar(prw.getNd().getMontoTotal());
				
				movimientosYaRegistrados.add(prw.getNd().getId());
				String obs = observacionesPagosMDMap.get(prw);
				if(!StringUtil.isNullOrEmpty(obs)) {
					MovimientoCuenta movimientoDebeByFactura = movimientoDAO.getMovimientoDPByND(prw.getNd().getId());
					movimientoDebeByFactura.setObservaciones(obs);
					movimientoDAO.save(movimientoDebeByFactura);
				}
//				}
		}
	}

	private void getPagosRecibosPosiblesPorFecha(OrdenDePago op, List<PagoReciboWrapper> prList) throws ValidacionException {
		BigDecimal totalMontoPagado = op.getMonto();
//		List<NotaDebitoProveedor> notaDebitoList = correccionDAO.getNotasDeDebitoImpagas(op.getProveedor().getId());
		List<NotaDebitoProveedor> notaDebitoList = filterNDsFaltantesPorPagar(prList);
		if(notaDebitoList != null) {
			for(NotaDebitoProveedor nd : notaDebitoList) {
				if((mismoDia(nd.getFechaIngreso(), op.getFechaEmision()) || !nd.getFechaIngreso().after(op.getFechaEmision())) && totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
					BigDecimal montoND = nd.getMontoFaltantePorPagar();
					PagoOrdenDePagoNotaDebito prnd = new PagoOrdenDePagoNotaDebito();
					prnd.setNotaDebito(nd);
					if(truncBD(montoND.doubleValue())>= truncBD(totalMontoPagado.doubleValue())) {
						prnd.setMontoPagado(totalMontoPagado);
						totalMontoPagado = new BigDecimal(0);
					} else {
						prnd.setMontoPagado(montoND);
						totalMontoPagado = totalMontoPagado.subtract(montoND);
					}
					op.getPagos().add(prnd);
				}
			}
		}

//		List<FacturaProveedor> facturaList = facturaDAO.getFacturasImpagas(op.getProveedor().getId());
		List<FacturaProveedor> facturaList = filterFacturasFaltantesPorPagar(prList);
		if(facturaList != null) {
			for(FacturaProveedor f : facturaList) {
				if((mismoDia(f.getFechaIngreso(), op.getFechaEmision()) || !f.getFechaIngreso().after(op.getFechaEmision())) && totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
					BigDecimal montoF = f.getMontoFaltantePorPagar();
					PagoOrdenDePagoFactura prf = new PagoOrdenDePagoFactura();
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
					op.getPagos().add(prf);
				}
			}
		}
		//Si sobró algo de monto entonces creo un pago "a cuenta" 
		if(totalMontoPagado.compareTo(new BigDecimal(0)) > 0) {
			PagoOrdenDePagoACuenta prac = new PagoOrdenDePagoACuenta();
			prac.setMontoPagado(totalMontoPagado);
			op.getPagos().add(prac);
		}
	}

	
	private List<FacturaProveedor> filterFacturasFaltantesPorPagar(List<PagoReciboWrapper> prList) {
		List<FacturaProveedor> result = new ArrayList<FacturaProveedor>();
		for(PagoReciboWrapper prw : prList) {
			if(prw.isFactura() && prw.getFactura().getMontoFaltantePorPagar().doubleValue() > 0) {
				result.add(prw.getFactura());
			}
		}
		return result;
	}

	private List<NotaDebitoProveedor> filterNDsFaltantesPorPagar(List<PagoReciboWrapper> prList) {
		List<NotaDebitoProveedor> result = new ArrayList<NotaDebitoProveedor>();
		for(PagoReciboWrapper prw : prList) {
			if(!prw.isFactura() && prw.getNd().getMontoFaltantePorPagar().doubleValue() > 0) {
				result.add(prw.getNd());
			}
		}
		return result;
	}
	
	private double truncBD(double d) {
		d = d*100;
		double tmpD = ((int)d)/100d;
		return tmpD;
	}
	
	private boolean mismoDia(java.util.Date fecha, java.util.Date fecha2) {
		boolean dia = DateUtil.getDia(new java.sql.Date(fecha.getTime())) == DateUtil.getDia(new java.sql.Date(fecha2.getTime()));
		boolean mes = DateUtil.getMes(new java.sql.Date(fecha.getTime())) == DateUtil.getMes(new java.sql.Date(fecha2.getTime()));
		boolean anio = DateUtil.getAnio(new java.sql.Date(fecha.getTime())) == DateUtil.getAnio(new java.sql.Date(fecha2.getTime()));
		return dia && mes && anio;
	}

	private static class PagoReciboWrapper {

		private NotaDebitoProveedor nd;
		public void setNd(NotaDebitoProveedor nd) {
			this.nd = nd;
		}

		public void setFactura(FacturaProveedor factura) {
			this.factura = factura;
		}

		private FacturaProveedor factura;

		public PagoReciboWrapper(FacturaProveedor facturaProveedor) {
			facturaProveedor.setMontoFaltantePorPagar(facturaProveedor.getMontoTotal());
			facturaProveedor.setFechaIngreso(new java.sql.Date(DateUtil.redondearFecha(facturaProveedor.getFechaIngreso()).getTime()));			
			this.factura = facturaProveedor;
		}

		public void sumarSegundos(long segundos) {
			if(isFactura()) {
				long totalSegundos = getFactura().getFechaIngreso().getTime() + segundos;
				getFactura().setFechaIngreso(new java.sql.Date(totalSegundos));
			} else {
				long totalSegundos = getNd().getFechaIngreso().getTime() + segundos;
				getNd().setFechaIngreso(new java.sql.Date(totalSegundos));
			}
		}

		public Date getFecha() {
			if(isFactura()) {
				return getFactura().getFechaIngreso();
			} else {
				return getNd().getFechaIngreso();
			}
		}

		public PagoReciboWrapper(NotaDebitoProveedor nd) {
			nd.setMontoFaltantePorPagar(nd.getMontoTotal());
			nd.setFechaIngreso(new java.sql.Date(DateUtil.redondearFecha(nd.getFechaIngreso()).getTime()));
			this.nd = nd;
		}

		public NotaDebitoProveedor getNd() {
			return nd;
		}

		public FacturaProveedor getFactura() {
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

		public String getNroDocumento() {
			if(isFactura()) {
				return getFactura().getNroFactura();
			} else {
				return getNd().getNroCorreccion();
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

		private NotaCreditoProveedor nc;

		public void setNc(NotaCreditoProveedor nc) {
			this.nc = nc;
		}

		private OrdenDePago ordenDePago;

		public DocMovimientoHaber(NotaCreditoProveedor notaCreditoProveedor) {
			notaCreditoProveedor.setMontoSobrantePorUtilizar(notaCreditoProveedor.getMontoTotal());
			this.nc = notaCreditoProveedor;
			notaCreditoProveedor.setFechaIngreso(new java.sql.Date(DateUtil.redondearFecha(notaCreditoProveedor.getFechaIngreso()).getTime()));
		}

		public void sumarSegundos(long segundos) {
			if(isOP()) {
				long totalSegundos = getOrdenDePago().getFechaEmision().getTime() + segundos;
				getOrdenDePago().setFechaEmision(new Timestamp(totalSegundos));
			} else {
				long totalSegundos = getNc().getFechaIngreso().getTime() + segundos;
				getNc().setFechaIngreso(new java.sql.Date(totalSegundos));
			}
		}

		public DocMovimientoHaber(OrdenDePago ordenDePago) {
			this.ordenDePago = ordenDePago;
		}

		public NotaCreditoProveedor getNc() {
			return nc;
		}

		public OrdenDePago getOrdenDePago() {
			return ordenDePago;
		}
		
		public boolean isOP() {
			return getOrdenDePago() != null;
		}

		@Override
		public boolean equals(Object obj) {
			return toString().compareToIgnoreCase(obj.toString()) == 0;
		}

		@Override
		public int hashCode() {
			if(isOP()) {
				return getOrdenDePago().hashCode();
			} else {
				return getNc().hashCode();
			}
		}

		public Date getFecha() {
			if(isOP()) {
				return getOrdenDePago().getFechaEmision();
			} else {
				return getNc().getFechaIngreso();
			}
		}

		@Override
		public String toString() {
			if(isOP()) {
				return getOrdenDePago().toString();
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

	private class ActualizarItemPagoOrdenDePagoVisitor implements IPagoOrdenPagoVisitor {

		public void visit(PagoOrdenDePagoACuenta prac) {
		}

		public void visit(PagoOrdenDePagoFactura prf) {
			FacturaProveedor f = prf.getFactura();
			if (prf.getMontoPagado().compareTo(f.getMontoFaltantePorPagar()) == 0) {
				f.setMontoFaltantePorPagar(new BigDecimal(0));
			} else {
				f.setMontoFaltantePorPagar(f.getMontoFaltantePorPagar().subtract(prf.getMontoPagado()));
			}
		}

		public void visit(PagoOrdenDePagoNotaDebito prnd) {
			NotaDebitoProveedor nd = prnd.getNotaDebito();
			if (prnd.getMontoPagado().compareTo(nd.getMontoTotal()) == 0) {
				nd.setMontoFaltantePorPagar(new BigDecimal(0));
			} else {
				nd.setMontoFaltantePorPagar(nd.getMontoFaltantePorPagar().subtract(prnd.getMontoPagado()));
			}
		}

	}

}