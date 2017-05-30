package ar.com.textillevel.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.IgnoreDependency;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.FacturaDAOLocal;
import ar.com.textillevel.dao.api.local.FacturaProveedorDAOLocal;
import ar.com.textillevel.dao.api.local.PiezaRemitoDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioMateriaPrimaDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoSalidaDAOLocal;
import ar.com.textillevel.entidades.documentos.factura.Factura;
import ar.com.textillevel.entidades.documentos.factura.proveedor.CorreccionFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.FacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ImpuestoItemProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaMateriaPrima;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;
import ar.com.textillevel.entidades.documentos.factura.proveedor.NotaCreditoProveedor;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.enums.EEstadoControlPiezaRemitoSalida;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemOtro;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemPrecioMateriaPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRelacionContenedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.ItemRemitoSalidaProveedor;
import ar.com.textillevel.entidades.documentos.remito.proveedor.RelacionContenedorPrecioMatPrima;
import ar.com.textillevel.entidades.documentos.remito.proveedor.visitor.IItemRemitoSalidaVisitor;
import ar.com.textillevel.entidades.enums.EPosicionIVA;
import ar.com.textillevel.entidades.enums.ETipoInformeProduccion;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.stock.MovimientoStockResta;
import ar.com.textillevel.entidades.to.remitosalida.PiezaRemitoSalidaTO;
import ar.com.textillevel.entidades.to.remitosalida.PiezaRemitoSalidaTO.EnumTipoPiezaRE;
import ar.com.textillevel.entidades.to.remitosalida.RemitoSalidaConBajaStockTO;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.materiaprima.IBC;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.entidades.ventas.materiaprima.Tela;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.CorreccionFacturaProveedorFacadeLocal;
import ar.com.textillevel.facade.api.local.CuentaArticuloFacadeLocal;
import ar.com.textillevel.facade.api.local.FacturaFacadeLocal;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.ParametrosGeneralesFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.local.RelacionContenedorMatPrimaFacadeLocal;
import ar.com.textillevel.facade.api.local.RemitoSalidaFacadeLocal;
import ar.com.textillevel.facade.api.local.UsuarioSistemaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.modulos.odt.dao.api.local.OrdenDeTrabajoDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;

@Stateless
public class RemitoSalidaFacade implements RemitoSalidaFacadeRemote, RemitoSalidaFacadeLocal {

	@EJB
	private RemitoSalidaDAOLocal remitoSalidaDAOLocal;

	@EJB
	private FacturaDAOLocal facturaDAO;

	@EJB
	private AuditoriaFacadeLocal<RemitoSalida> auditoriaFacade;
	
	@EJB
	private MovimientoStockFacadeLocal movimientoStockFacade;

	@EJB
	private RelacionContenedorMatPrimaFacadeLocal relacionContenedorMatPrimaFacade;

	@EJB
	private PrecioMateriaPrimaFacadeLocal precioMateriaPrimaFacade;

	@EJB
	private OrdenDeTrabajoDAOLocal odtDAO;
	
	@EJB
	private RemitoEntradaDAOLocal remitoEntradaDAO;

	@EJB
	private PrecioMateriaPrimaDAOLocal pmpDAO;

	@EJB
	private PiezaRemitoDAOLocal piezaRemitoDAO;

	@EJB
	private CuentaArticuloFacadeLocal cuentaArticuloFacade;

	@EJB
	private CorreccionFacturaProveedorFacadeLocal correccionFacturaProveedorFacade;

	@EJB
	private ParametrosGeneralesFacadeLocal parametrosGeneralesFacade;

	@EJB
	private FacturaProveedorDAOLocal facturaProveedorDAO;

	@EJB
	@IgnoreDependency
	private FacturaFacadeLocal facturaFacade;
	
	@EJB
	private OrdenDeTrabajoFacadeLocal odtFacade;

	@EJB
	private UsuarioSistemaFacadeLocal usuSistemaFacade;

	public RemitoSalida save(RemitoSalida remitoSalida, String usuario) {
		boolean isAlta = remitoSalida.getId() == null;
		remitoSalida = remitoSalidaDAOLocal.save(remitoSalida);
		if(isAlta) {
			auditoriaFacade.auditar(usuario, "Creacion del remito de salida N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.ALTA, remitoSalida);
		} else {
			auditoriaFacade.auditar(usuario, "Modificación del remito de salida N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.MODIFICACION, remitoSalida);
		}
		return remitoSalida;
	}

	public Integer getLastNroRemito() {
		return remitoSalidaDAOLocal.getLastNroRemito();
	}

	public RemitoSalida getById(Integer id) {
		return remitoSalidaDAOLocal.getById(id);
	}

	public RemitoSalida getByIdConPiezasYProductos(Integer id){
		return remitoSalidaDAOLocal.getByIdConPiezasYProductos(id);
	}

	public List<RemitoSalida> getByIdsConPiezasYProductos(List<Integer> ids) {
		List<RemitoSalida> result = new ArrayList<RemitoSalida>();
		for(Integer id : ids) {
			result.add(getByIdConPiezasYProductos(id));
		}
		return result;
	}

	public Integer getUltimoNumeroFactura(EPosicionIVA posIva) {
		return remitoSalidaDAOLocal.getUltimoNumeroFactura(posIva, parametrosGeneralesFacade.getParametrosGenerales().getNroSucursal());
	}

	public void eliminarRemitoSalida(Integer idRemitoSalida, String usrName) throws ValidacionException {
		RemitoSalida remitoSalida = remitoSalidaDAOLocal.getById(idRemitoSalida);
		if(remitoSalida.getProveedor() == null) { //es un remito de salida de cliente
			checkEliminacionOrAnulacionRemitoSalida(remitoSalida.getId());
			desvincularODTs(remitoSalida);
			remitoSalidaDAOLocal.removeById(remitoSalida.getId());
			auditoriaFacade.auditar(usrName, "Eliminación del remito de salida N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.BAJA, remitoSalida);
		} else {//es un remito de salida de proveedor
			undoRemitoSalidaProveedor(idRemitoSalida, usrName);
			remitoSalidaDAOLocal.removeById(remitoSalida.getId());
			auditoriaFacade.auditar(usrName, "Eliminación del remito de salida N°: " + remitoSalida.getNroRemito() + " del proveedor " + remitoSalida.getProveedor().getNombreCorto() , EnumTipoEvento.BAJA, remitoSalida);
		}
	}

	private void undoRemitoSalidaProveedor(Integer idRemitoSalida, String usrName) throws ValidacionException {
		List<MovimientoStockResta> movRestaList =  movimientoStockFacade.getMovimientosRestaByRemitoSalida(idRemitoSalida);
		for(MovimientoStockResta msr : movRestaList) {
			if(msr.getRelContPrecioMatPrima() == null && msr.getPrecioMateriaPrima() != null) { //Es sólo de precio materia prima
				precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(msr.getCantidad(), msr.getPrecioMateriaPrima().getId());
			} else if(msr.getRelContPrecioMatPrima() != null) { //Es solo de contenedor
				relacionContenedorMatPrimaFacade.actualizarStockRelContPrecioMatPrima(msr.getCantidad(), msr.getRelContPrecioMatPrima().getId());				
			}
			movimientoStockFacade.borrarMovimientoById(msr.getId());
		}

		//Si el remito de salida generó NCs entonces las borro
		RemitoSalida rs = remitoSalidaDAOLocal.getById(idRemitoSalida);
		List<CorreccionFacturaProveedor> correccionesProvGeneradas = new ArrayList<CorreccionFacturaProveedor>(rs.getCorreccionesProvGeneradas());
		rs.getCorreccionesProvGeneradas().clear();
		for(CorreccionFacturaProveedor cfp : correccionesProvGeneradas) {
			correccionFacturaProveedorFacade.borrarCorreccionFacturaProveedor(cfp, usrName);
		}
	}

	public void checkEliminacionOrAnulacionRemitoSalida(Integer idRemitoSalida) throws ValidacionException {
		List<Factura> facturaList = facturaDAO.getFacturaByRemitoSalida(idRemitoSalida);
		if(!facturaList.isEmpty()) {
			throw new ValidacionException(EValidacionException.REMITO_SALIDA_IMPOSIBLE_BORRAR_O_EDITAR_O_ANULAR.getInfoValidacion(), new String[] { extractInfoFacturas(facturaList) });
		}
	}

	public void eliminarRemitoSalida01OrVentaTela(Integer idRemitoSalida, String usrName) throws ValidacionException {
		RemitoSalida remitoSalida = remitoSalidaDAOLocal.getById(idRemitoSalida);
		//Chequeo si se puede realizar la eliminación
		if(remitoSalida.getTipoRemitoSalida() == null || remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.CLIENTE) {
			throw new ValidacionException(EValidacionException.REMITO_SALIDA_VTA_OR_SAL_01_IMP_ELIMINAR.getInfoValidacion());			
		}
		checkEliminacionOrAnulacionRemitoSalida(remitoSalida.getId());
		//hago undo de las cosas del remito de salida
		undoRemitoSalida01OrVentaTelaPersistedStuff(remitoSalida.getId());

		//procedo a eliminar
		try {
			remitoSalidaDAOLocal.remove(remitoSalida);
		} catch (FWException e) {
			e.printStackTrace();
		}
		auditoriaFacade.auditar(usrName, "Eliminación del remito de salida N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.BAJA, remitoSalida);
	}

	private void undoRemitoSalida01OrVentaTelaPersistedStuff(Integer idRemitoSalida) {
		RemitoSalida remitoSalida = remitoSalidaDAOLocal.getById(idRemitoSalida);
		movimientoStockFacade.borrarMovientosStock(idRemitoSalida);
		cuentaArticuloFacade.borrarMovimientosCuentaArticulo(idRemitoSalida);
		for(PiezaRemito ps : remitoSalida.getPiezas()) {
			PiezaRemito piezaRE = ps.getPiezaEntrada();
			if(piezaRE == null) {
				PrecioMateriaPrima pmp = pmpDAO.getById(ps.getPmpDescuentoStock().getId());
				pmp.setStockInicialDisponible(pmp.getStockInicialDisponible().add(ps.getMetros()));
				pmpDAO.save(pmp);
			} else {
				piezaRE = piezaRemitoDAO.getById(ps.getPiezaEntrada().getId());
				piezaRE.setEnSalida(false);
				piezaRemitoDAO.save(piezaRE);
			}
		}
		//TODO: Falta eliminar las ODTs que existian antes para el remito y ahora ya no están
	}
	
	private String extractInfoFacturas(List<Factura> facturaList) {
		List<String> infoFacturaList = new ArrayList<String>();
		for(Factura f : facturaList) {
			infoFacturaList.add(String.valueOf(f.getNroFactura()));
		}
		return StringUtil.getCadena(infoFacturaList, ", ");
	}

	public RemitoSalida ingresarRemitoSalidaProveedor(RemitoSalida remitoSalida, List<FacturaProveedor> facturasParaGenerarNC, String user) throws ValidacionException {
		if(remitoSalida.getProveedor() == null) {
			throw new IllegalArgumentException("El Remito a persistir tiene que tener el proveedor seteado");
		}
		if(remitoSalida.getId() != null) {
			undoRemitoSalidaProveedor(remitoSalida.getId(), user);
		}
		//Genero una nota de credito por el valor de las facturas
		remitoSalida.getCorreccionesProvGeneradas().clear();
		if(!facturasParaGenerarNC.isEmpty()) {
			BigDecimal montoNC = BigDecimal.ZERO;
			List<ItemCorreccionMateriaPrima> itemsMatPrimaCorreccion = new ArrayList<ItemCorreccionMateriaPrima>();
			for(FacturaProveedor f : facturasParaGenerarNC) {
				f = facturaProveedorDAO.getById(f.getId());
				for(ItemFacturaProveedor ifp : f.getItems()) {
					//tiene que existir al menos un item IBC! xq las facturas se traen con esa precondicion en el dialogo del remito de salida
					if(ifp instanceof ItemFacturaMateriaPrima) {
						ItemFacturaMateriaPrima ifmp = (ItemFacturaMateriaPrima)ifp;
						//Creo un item de NC por cada item IBC de las facturas
						if(ifmp.getPrecioMateriaPrima().getMateriaPrima() instanceof IBC) {
							ItemCorreccionMateriaPrima icmt = new ItemCorreccionMateriaPrima();
							icmt.setCantidad(ifmp.getCantidad());
							icmt.setDescripcion(ifmp.getDescripcion());
							icmt.setFactorConversionMoneda(ifmp.getFactorConversionMoneda());
							icmt.setImporte(ifmp.getImporte());
							icmt.setPorcDescuento(ifmp.getPorcDescuento());
							icmt.setPrecioMateriaPrima(ifmp.getPrecioMateriaPrima());
							icmt.setPrecioUnitario(ifmp.getPrecioUnitario());
							icmt.getImpuestos().addAll(ifmp.getImpuestos());
							itemsMatPrimaCorreccion.add(icmt);
							double importeSinImpuestos = icmt.recalcularImporteTotal().doubleValue();
							double importeTotalPorItem = importeSinImpuestos; 
							for(ImpuestoItemProveedor iip : icmt.getImpuestos()) {
								importeTotalPorItem += importeSinImpuestos * (iip.getPorcDescuento()/100);
							}
							montoNC = montoNC.add(new BigDecimal(importeTotalPorItem));
						}
					}
				}
			}
			NotaCreditoProveedor nc = new NotaCreditoProveedor();
			nc.setNroCorreccion(""); //Sin nro. Se completa cuando luego con la info del proveedor
			nc.setMontoTotal(montoNC);
			nc.setProveedor(remitoSalida.getProveedor());
			nc.setFechaIngreso(DateUtil.getHoy());
			nc.getFacturas().addAll(facturasParaGenerarNC);
			nc.getItemsCorreccion().addAll(itemsMatPrimaCorreccion);
			nc.setUsuarioConfirmacion(user);
			
			nc = (NotaCreditoProveedor)correccionFacturaProveedorFacade.guardarCorreccionYGenerarMovimiento(nc, user, "Generada automáticamente por " + remitoSalida.toString());
			remitoSalida.getCorreccionesProvGeneradas().add(nc);
		}

		remitoSalida = remitoSalidaDAOLocal.save(remitoSalida);

		MovimientoPersisterItemRemitoSalidaVisitor movPersisterVisitor = new MovimientoPersisterItemRemitoSalidaVisitor(remitoSalida); 
		for(ItemRemitoSalidaProveedor irsp : remitoSalida.getItems()) {
			irsp.accept(movPersisterVisitor);
		}

		return remitoSalida;
	}

	private class MovimientoPersisterItemRemitoSalidaVisitor implements IItemRemitoSalidaVisitor {

		private final RemitoSalida remitoSalidaProveedor;

		public MovimientoPersisterItemRemitoSalidaVisitor(RemitoSalida remitoSalidaProveedor) {
			this.remitoSalidaProveedor = remitoSalidaProveedor;
		}

		public void visit(ItemPrecioMateriaPrima ipmp) {
			PrecioMateriaPrima precioMateriaPrima = ipmp.getPrecioMateriaPrima();
			movimientoStockFacade.crearMovimientoResta(precioMateriaPrima, ipmp.getCantSalida(), remitoSalidaProveedor, ipmp.getStockActual());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(ipmp.getCantSalida().multiply(new BigDecimal(-1)), ipmp.getPrecioMateriaPrima().getId());
		}

		public void visit(ItemRelacionContenedor irc) {
			RelacionContenedorPrecioMatPrima relacionContPrecioMP = irc.getRelacionContPrecioMP();
			movimientoStockFacade.crearMovimientoResta(relacionContPrecioMP, irc.getCantSalida(), remitoSalidaProveedor, relacionContPrecioMP.getStockActual());
			relacionContenedorMatPrimaFacade.actualizarStockRelContPrecioMatPrima(irc.getCantSalida().multiply(new BigDecimal(-1)), relacionContPrecioMP.getId());
		}

		public void visit(ItemOtro io) {
		}

	}

	public Map<Date, List<Map<String, BigDecimal>>> getInformeProduccion(Date fechaDesde, Date fechaHasta, Cliente cliente, ETipoInformeProduccion tipoInforme) {
		List<RemitoSalida> listaRemitos = remitoSalidaDAOLocal.getRemitosByClienteYFecha(fechaDesde, fechaHasta, cliente);
		if(listaRemitos == null){
			return null;
		}
		Map<Date, List<Map<String, BigDecimal>>> ret = new LinkedHashMap<Date, List<Map<String,BigDecimal>>>();
		for(RemitoSalida rem : listaRemitos){
			BigDecimal sumaTotal = new BigDecimal(0d);
			Date fechaRemito = DateUtil.redondearFecha(rem.getFechaEmision());
			if(ret.get(fechaRemito) == null){
				ret.put(fechaRemito, new ArrayList<Map<String, BigDecimal>>());
			}
			String nombreCliente = rem.getCliente()!=null?rem.getCliente().getRazonSocial():rem.getProveedor().getRazonSocial();
			if(tipoInforme == ETipoInformeProduccion.KILOS){
				sumaTotal = sumaTotal.add(rem.getPesoTotal());
			}else{
				BigDecimal sumaPiezas = new BigDecimal(0d);
				for(PiezaRemito pieza : rem.getPiezas()){
					sumaPiezas = sumaPiezas.add(pieza.getMetros());
				}
				sumaTotal = sumaTotal.add(sumaPiezas);
			}
			if(ret.get(fechaRemito)!=null && ret.get(fechaRemito).isEmpty()){
				Map<String, BigDecimal> mapaClienteCantidad = new LinkedHashMap<String, BigDecimal>();
				mapaClienteCantidad.put(nombreCliente, sumaTotal);
				ret.get(fechaRemito).add(mapaClienteCantidad);
			}else if(ret.get(fechaRemito)!=null){
				boolean esta = false;
				for(Map<String, BigDecimal> m : ret.get(fechaRemito)){
					if(m.get(nombreCliente)!=null){
						BigDecimal cantMapa = m.get(nombreCliente);
						cantMapa = cantMapa.add(sumaTotal);
						m.put(nombreCliente, cantMapa);
						esta = true;
					}
				}
				if(!esta){
					Map<String, BigDecimal> mapaClienteCantidad = new LinkedHashMap<String, BigDecimal>();
					mapaClienteCantidad.put(nombreCliente, sumaTotal);
					ret.get(fechaRemito).add(mapaClienteCantidad);
				}
			}
		}
		
		return ret;
	}

	public List<RemitoSalida> getRemitoSalidaByFechasAndCliente(Date fechaDesde, Date fechaHasta, Integer idCliente) {
		return remitoSalidaDAOLocal.getRemitoSalidaByFechasAndCliente(fechaDesde, fechaHasta, idCliente);
	}

	public List<RemitoSalida> getRemitoSalidaByFechasAndProveedor(Date fechaDesde, Date fechaHasta, Integer idProveedor) {
		return remitoSalidaDAOLocal.getRemitoSalidaByFechasAndProveedor(fechaDesde, fechaHasta, idProveedor);
	}

	public RemitoSalida ingresarRemitoSalidaPorVentaDeTela(RemitoSalidaConBajaStockTO remitoSalidaTO) throws ValidacionException {
		if(remitoSalidaTO.getRemitoSalida().getId() != null) {
			undoRemitoSalida01OrVenta(remitoSalidaTO);
		}
		Set<OrdenDeTrabajo> odtSet = new HashSet<OrdenDeTrabajo>();
		Map<PrecioMateriaPrima, BigDecimal> pmpModifStockMap = new HashMap<PrecioMateriaPrima, BigDecimal>();
		Map<PrecioMateriaPrima, BigDecimal> pmpModifStockInicialMap = new HashMap<PrecioMateriaPrima, BigDecimal>();
		Map<Articulo, BigDecimal> articuloCantMap = new HashMap<Articulo, BigDecimal>();
		RemitoSalida remitoSalida = prepararRSAndMaps(remitoSalidaTO, pmpModifStockMap, pmpModifStockInicialMap, articuloCantMap, odtSet);
		remitoSalida = persistRemitoSalida(remitoSalida, remitoSalidaTO, odtSet);
		//Modifico el stock de los PMP
		for(PrecioMateriaPrima pmp : pmpModifStockMap.keySet()) {
			movimientoStockFacade.crearMovimientoResta(pmp, pmpModifStockMap.get(pmp), remitoSalida, pmp.getStockActual());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(pmpModifStockMap.get(pmp).multiply(new BigDecimal(-1)), pmp.getId());
		}
		//Modifico el stock inicial de los PMP
		for(PrecioMateriaPrima pmp : pmpModifStockInicialMap.keySet()) {
			pmp = pmpDAO.getById(pmp.getId());
			BigDecimal descStockInicial = pmpModifStockInicialMap.get(pmp);
			movimientoStockFacade.crearMovimientoResta(pmp, descStockInicial, remitoSalida, pmp.getStockActual());
			pmp = precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(descStockInicial.multiply(new BigDecimal(-1)), pmp.getId());
			pmp.setStockInicialDisponible(pmp.getStockInicialDisponible().subtract(descStockInicial));
			precioMateriaPrimaFacade.save(pmp);
		}
		auditoriaFacade.auditar(remitoSalidaTO.getUserName(), "Creación del remito de salida (venta de tela) N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.ALTA, remitoSalida);
		return remitoSalida;
	}

	public RemitoSalida ingresarRemitoSalidaPorSalida01(RemitoSalidaConBajaStockTO remitoSalidaTO) throws ValidacionException {
		if(remitoSalidaTO.getRemitoSalida().getId() != null) {
			undoRemitoSalida01OrVenta(remitoSalidaTO);
		}
		Set<OrdenDeTrabajo> odtSet = new HashSet<OrdenDeTrabajo>();
		Map<PrecioMateriaPrima, BigDecimal> pmpModifStockMap = new HashMap<PrecioMateriaPrima, BigDecimal>();
		Map<PrecioMateriaPrima, BigDecimal> pmpModifStockInicialMap = new HashMap<PrecioMateriaPrima, BigDecimal>();
		Map<Articulo, BigDecimal> articuloCantMap = new HashMap<Articulo, BigDecimal>();
		RemitoSalida remitoSalida = prepararRSAndMaps(remitoSalidaTO, pmpModifStockMap, pmpModifStockInicialMap, articuloCantMap, odtSet);
		remitoSalida = persistRemitoSalida(remitoSalida, remitoSalidaTO, odtSet);
		//Modifico la cuenta de tela del cliente
		cuentaArticuloFacade.crearMovimientosDebe(remitoSalida, articuloCantMap);
		//Modifico el stock de los PMP
		for(PrecioMateriaPrima pmp : pmpModifStockMap.keySet()) {
			movimientoStockFacade.crearMovimientoResta(pmp, pmpModifStockMap.get(pmp), remitoSalida, pmp.getStockActual());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(pmpModifStockMap.get(pmp).multiply(new BigDecimal(-1)), pmp.getId());
		}
		//Modifico el stock inicial de los PMP
		for(PrecioMateriaPrima pmp : pmpModifStockInicialMap.keySet()) {
			pmp = pmpDAO.getById(pmp.getId());
			BigDecimal descStockInicial = pmpModifStockInicialMap.get(pmp);
			movimientoStockFacade.crearMovimientoResta(pmp, descStockInicial, remitoSalida, pmp.getStockActual());
			pmp = precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(descStockInicial.multiply(new BigDecimal(-1)), pmp.getId());
			pmp.setStockInicialDisponible(pmp.getStockInicialDisponible().subtract(descStockInicial));
			precioMateriaPrimaFacade.save(pmp);
		}
		auditoriaFacade.auditar(remitoSalidaTO.getUserName(), "Creación del remito de salida N°: " + remitoSalida.getNroRemito(), EnumTipoEvento.ALTA, remitoSalida);
		return remitoSalida;
	}

	private void undoRemitoSalida01OrVenta(RemitoSalidaConBajaStockTO remitoSalidaTO) {
		undoRemitoSalida01OrVentaTelaPersistedStuff(remitoSalidaTO.getRemitoSalida().getId());
		remitoSalidaTO.getRemitoSalida().getPiezas().clear();
		sincronizeODTsYaGrabadas(remitoSalidaTO);
	}

	private void sincronizeODTsYaGrabadas(RemitoSalidaConBajaStockTO remitoSalidaTO) {
		Map<Integer, OrdenDeTrabajo> mapODT = new HashMap<Integer, OrdenDeTrabajo>();
		for(PiezaRemitoSalidaTO pr : remitoSalidaTO.getPiezasRemitoSalidaTO()) {
			if(pr.getOdt() != null) {
				OrdenDeTrabajo odtInMap = mapODT.get(pr.getOdt().getId());
				if(odtInMap == null) {
					OrdenDeTrabajo odt = odtDAO.getById(pr.getOdt().getId());
					odt.getPiezas().clear();
					mapODT.put(odt.getId(), odt);
					pr.setOdt(odt);
				} else {
					pr.setOdt(odtInMap);
				}
			}
		}
	}

	private RemitoSalida persistRemitoSalida(RemitoSalida remitoSalida, RemitoSalidaConBajaStockTO remitoSalidaTO, Set<OrdenDeTrabajo> odtSet) {
		Map<String, OrdenDeTrabajo> odtMap = new HashMap<String, OrdenDeTrabajo>();
		for(OrdenDeTrabajo odt : odtSet) {
			odt = odtDAO.save(odt);
			odtMap.put(odt.getCodigo(), odt);
		}
		remitoSalida.getOdts().addAll(odtMap.values());
		for(PiezaRemito pr : remitoSalida.getPiezas()) {
			if(!pr.getPiezasPadreODT().isEmpty()) {
				PiezaODT piezaODT = pr.getPiezasPadreODT().get(0);
				OrdenDeTrabajo odt = piezaODT.getOdt();
				odt = odtMap.get(odt.getCodigo());
				piezaODT = findPiezaODT(odt, piezaODT);
				pr.getPiezasPadreODT().clear();
				pr.getPiezasPadreODT().add(piezaODT);
			}
		}

		for(PiezaRemitoSalidaTO prto : remitoSalidaTO.getPiezasRemitoSalidaTO()) {
			if(prto.getPiezaRemitoEntrada().getId() != null) {
				modifRemitoEntrada(remitoSalidaTO.getRemitoEntradaModificadosList(), prto.getPiezaRemitoEntrada());
			}
		}

		remitoSalida = remitoSalidaDAOLocal.save(remitoSalida);
		return remitoSalida;
	}
	
	private RemitoSalida prepararRSAndMaps(RemitoSalidaConBajaStockTO remitoSalidaTO, Map<PrecioMateriaPrima, BigDecimal> pmpModifStockMap, Map<PrecioMateriaPrima, BigDecimal> pmpModifStockInicialMap, Map<Articulo, BigDecimal> articuloCantMap, Set<OrdenDeTrabajo> odtSet) throws ValidacionException {
		RemitoSalida remitoSalida = remitoSalidaTO.getRemitoSalida();
		for(PiezaRemitoSalidaTO prto : remitoSalidaTO.getPiezasRemitoSalidaTO()) {
			OrdenDeTrabajo odt = prto.getOdt();
			//Creo la pieza de salida y pieza odt (si existe la misma)
			PiezaRemito piezaSalida = new PiezaRemito();
			if(odt != null) {
				PiezaODT piezaODT = new PiezaODT();
				piezaODT.setOdt(odt);
				odt.getPiezas().add(piezaODT);
				if(prto.isPiezaStockInicial()) {
					piezaODT.setNroPiezaStockInicial(prto.getNroPieza());
					piezaODT.setMetrosStockInicial(prto.getTotalMetrosStockConsumido());
				} else {
					piezaODT.setPiezaRemito(prto.getPiezaRemitoEntrada());
				}
				piezaSalida.setPiezaSinODT(false);
				piezaSalida.getPiezasPadreODT().add(piezaODT);
				odtSet.add(odt);
			}
			if(prto.isPiezaStockInicial()) {
				piezaSalida.setMetros(prto.getTotalMetrosStockConsumido());
			} else {
				piezaSalida.setMetros(prto.getPiezaRemitoEntrada().getMetros());
				piezaSalida.setPiezaEntrada(prto.getPiezaRemitoEntrada());
			}
			piezaSalida.setObservaciones(prto.getObservaciones());
			piezaSalida.setOrdenPieza(prto.getNroPieza());
			remitoSalida.getPiezas().add(piezaSalida);

			//Agrupo los precios materias primas para descontar stock
			if(prto.getTipoPiezaRE() == EnumTipoPiezaRE.PIEZA_STOCK_INICIAL) {//fue una pieza de stock inicial => tengo que modificar stock de PMP iniciales
				for(PrecioMateriaPrima pmp : prto.getPmpStockConsumido().keySet()) {
					BigDecimal cant = pmpModifStockInicialMap.get(pmp);
					if(cant == null) {
						cant = new BigDecimal(0);
					}
					cant = cant.add(prto.getPmpStockConsumido().get(pmp));
					pmpModifStockInicialMap.put(pmp, cant);
					piezaSalida.setPmpDescuentoStock(pmp);
					articuloCantMap.put(((Tela)pmp.getMateriaPrima()).getArticulo(), cant);
				}
			} else if(prto.getTipoPiezaRE() == EnumTipoPiezaRE.COMPRA_DE_TELA) {//fue una pieza de compra de tela => tengo que modificar stock de PMP
				PrecioMateriaPrima pmp = prto.getPrecioMateriaPrimaRE();
				BigDecimal cant = pmpModifStockMap.get(pmp);
				if(cant == null) {
					cant = new BigDecimal(0);
				}
				cant = cant.add(prto.getPiezaRemitoEntrada().getMetros());
				pmpModifStockMap.put(pmp, cant);
				articuloCantMap.put(((Tela)pmp.getMateriaPrima()).getArticulo(), cant);
			} else {//fue una pieza de entrada 01 => NO tengo que modificar stock de PMP, sólo se alteraran las cuentas
				Articulo articulo = null;
				if(odt == null) {
					articulo = prto.getArticulo();
				} else {
					articulo = odt.getIProductoParaODT().getArticulo();
				}
				BigDecimal cant = articuloCantMap.get(articulo);
				if(cant == null) {
					cant = new BigDecimal(0);
				}
				cant = cant.add(prto.getPiezaRemitoEntrada().getMetros());
				articuloCantMap.put(articulo, cant);
			}
		}
		
		checkConsistenciaRemitoSalida(remitoSalida);

		return remitoSalida;
	}

	private void checkConsistenciaRemitoSalida(RemitoSalida remitoSalida) throws ValidacionException {
		int piezasConPiezaRE = 0;
		Set<PiezaRemito> prSet = new HashSet<PiezaRemito>();
		for(PiezaRemito pr : remitoSalida.getPiezas()) {
			if(pr.getPiezaEntrada() != null) {
				piezasConPiezaRE++;
				prSet.add(pr.getPiezaEntrada());
			}
		}
		if(prSet.size() != piezasConPiezaRE) {
			throw new ValidacionException(EValidacionException.REMITO_SALIDA_01_OR_COMP_TELA_IMPOSIBLE_GRABAR.getInfoValidacion(), new String[] { "Existe más de una pieza de salida asociada a la misma pieza de remito de entrada. " });
		}
	}

	private PiezaODT findPiezaODT(OrdenDeTrabajo odt, PiezaODT piezaODT) {
		for(PiezaODT podt : odt.getPiezas()) {
			if(piezaODT.getPiezaRemito() == null) {
				//FIXME: Analizar que pasa cuando descuento desde stock y tengo el mismo número de pieza (alcanza con comparar por metros?)
				if(podt.getNroPiezaStockInicial()!=null && podt.getNroPiezaStockInicial().equals(piezaODT.getNroPiezaStockInicial())) {
					return podt; 
				}
			} else {
				if(podt.getPiezaRemito()!= null && podt.getPiezaRemito().equals(piezaODT.getPiezaRemito())) {
					return podt; 
				}
			}
		}
		throw new RuntimeException("Pieza ODT inexistente: " + piezaODT.getPiezaRemito().getMetros().toString()); 
	}

	private void modifRemitoEntrada(List<RemitoEntrada> remitoEntradaModificadosList, PiezaRemito piezaRemitoEntrada) {
		for(RemitoEntrada re : remitoEntradaModificadosList) {
			RemitoEntrada reDB = remitoEntradaDAO.getById(re.getId());
			boolean esDeEseRemito = false;
			for(PiezaRemito pr : reDB.getPiezas()) {
				if(pr.getId().equals(piezaRemitoEntrada.getId())) {
					pr.setPiezaSinODT(false);
					pr.setEnSalida(true);
					esDeEseRemito = true;
					break;
				}
			}
			if(esDeEseRemito) {
				remitoEntradaDAO.save(reDB);
			}
		}
	}

	public void anularRemitoSalida(RemitoSalida remito) {
		remito = remitoSalidaDAOLocal.getById(remito.getId());
		remito.setAnulado(true);
		desvincularODTs(remito);
		remitoSalidaDAOLocal.save(remito);
	}

	private void desvincularODTs(RemitoSalida remito) {
		//desvinculo las ODTs del remito de salida
		for(OrdenDeTrabajo odt :  remito.getOdts()) {
			List<PiezaODT> piezas = odt.getPiezas();
			for(PiezaODT p : piezas) {
				List<PiezaRemito> piezasCopy = new ArrayList<PiezaRemito>(p.getPiezasSalida());
				piezasCopy.retainAll(remito.getPiezas());
				p.getPiezasSalida().removeAll(piezasCopy);
				for(PiezaRemito pr : piezasCopy) {
					pr.getPiezasPadreODT().clear();
				}
			}
			//cambio la ODT a estado IMPRESA ya que la creación del RS la vuelve a EN_OFICINA
			odt.setEstadoODT(EEstadoODT.IMPRESA);
			odtDAO.save(odt);
		}
		remito.getOdts().clear();
	}

	public RemitoSalida guardarRemito(RemitoSalida remito) {
		return remitoSalidaDAOLocal.save(remito);
	}

	public RemitoSalida getByNroRemitoConPiezasYProductosAnulado(Integer nroRemito) {
		return remitoSalidaDAOLocal.getByNroRemitoConPiezasYProductosAnulado(nroRemito);
	}

	public List<RemitoSalida> save(List<RemitoSalida> remitosSalida, String usrName) {
		List<RemitoSalida> result = new ArrayList<RemitoSalida>();
		for(RemitoSalida rs : remitosSalida) {
			result.add(save(rs, usrName));
		}
		return result;
	}

	public List<RemitoSalida> getRemitosSalidaSinFacturaPorCliente(Cliente cliente) {
		return remitoSalidaDAOLocal.getRemitosSalidaSinFacturaPorCliente(cliente);
	}

	public List<RemitoSalida> getRemitosByNroRemitoConPiezasYProductos(Integer nroRemito) {
		return remitoSalidaDAOLocal.getRemitosByNroRemitoConPiezasYProductos(nroRemito);
	}

	public void marcarEntregado(String numero, String nombreTerminal) {
		RemitoSalida rs = remitoSalidaDAOLocal.getByNumero(numero);
		if (rs == null) {
			throw new RuntimeException("Orden de pago no encontrada");
		}
		if (rs.getEntregado() == null || rs.getEntregado().equals(Boolean.FALSE)) {
			rs.setEntregado(true);
			rs.setFechaHoraEntregado(DateUtil.getAhora());
			rs.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Marcar remito salida numero: " + numero + " como entregado", EnumTipoEvento.MODIFICACION, rs);
		}
	}

	public void reingresar(String numero, String nombreTerminal) {
		RemitoSalida rs = remitoSalidaDAOLocal.getByNumero(numero);
		if (rs == null) {
			throw new RuntimeException("Remito de salida no encontrada");
		}
		if (rs.getEntregado() != null && rs.getEntregado().equals(Boolean.TRUE)) {
			rs.setEntregado(false);
			rs.setFechaHoraEntregado(DateUtil.getAhora());
			rs.setTerminalEntrega(nombreTerminal);
			auditoriaFacade.auditar(nombreTerminal, "Reingreso remito salida numero: " + numero, EnumTipoEvento.MODIFICACION, rs);
		}
	}

	public void marcarRemitoSalidaComoControlado(RemitoSalida rs, String nombreTerminal) {
		RemitoSalida rsDB = getById(rs.getId());
		checkMarcarComoControlado(rsDB);
		rsDB.setControlado(true);
		rsDB.setTerminalControl(nombreTerminal);
		remitoSalidaDAOLocal.save(rsDB);
		auditoriaFacade.auditar(nombreTerminal, "Control de remito de salida: " + rsDB.getNroRemito(), EnumTipoEvento.MODIFICACION, rs);		
	}

	private void checkMarcarComoControlado(RemitoSalida rs) {
		for(PiezaRemito pr : rs.getPiezas()) {
			if(pr.getEstadoControl() == null || pr.getEstadoControl() == EEstadoControlPiezaRemitoSalida.PENDIENTE) {
				throw new IllegalArgumentException("No se puede marcar como controlado el remito ya que aún faltan controlar piezas.");
			}
		}
	}

	public List<RemitoSalida> getRemitosSalidaByODT(Integer idODT) {
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idODT);
		return remitoSalidaDAOLocal.getRemitosByODT(odt);
	}

}