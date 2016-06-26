package ar.com.textillevel.gui.acciones;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.to.DetalleRemitoEntradaNoFacturado;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.facade.api.remote.ArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.facade.api.remote.CondicionDeVentaFacadeRemote;
import ar.com.textillevel.facade.api.remote.MateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProductoArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.ProveedorFacadeRemote;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TarimaFacadeRemote;
import ar.com.textillevel.facade.api.remote.TipoArticuloFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.odtwsclient.CambioAvanceTO;
import ar.com.textillevel.gui.acciones.odtwsclient.FormulaClienteExplotadaTO;
import ar.com.textillevel.gui.acciones.odtwsclient.InstruccionProcedimientoODTTO;
import ar.com.textillevel.gui.acciones.odtwsclient.MateriaPrimaCantidadExplotadaTO;
import ar.com.textillevel.gui.acciones.odtwsclient.OdtEagerTO;
import ar.com.textillevel.gui.acciones.odtwsclient.PasoSecuenciaODTTO;
import ar.com.textillevel.gui.acciones.odtwsclient.PiezaODTTO;
import ar.com.textillevel.gui.acciones.odtwsclient.PiezaRemitoTO;
import ar.com.textillevel.gui.acciones.odtwsclient.ProcedimientoODTTO;
import ar.com.textillevel.gui.acciones.odtwsclient.RemitoEntradaTO;
import ar.com.textillevel.gui.acciones.odtwsclient.SecuenciaODTTO;
import ar.com.textillevel.gui.acciones.odtwsclient.TransicionODTTO;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaEstampadoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.FormulaTenidoClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.FormulaClienteExplotada;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoPasadasODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTextoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.InstruccionProcedimientoTipoProductoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.PasoSecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.ProcedimientoODT;
import ar.com.textillevel.modulos.odt.entidades.secuencia.odt.SecuenciaODT;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.AccionProcedimientoFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.FormulaClienteFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.MaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.modulos.odt.facade.api.remote.TransicionODTFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

public final class ODTTOConverter {

	private static final MaquinaFacadeRemote maquinaFacade = GTLBeanFactory.getInstance().getBean2(MaquinaFacadeRemote.class);
	private static final ProductoArticuloFacadeRemote productoArticuloFacade = GTLBeanFactory.getInstance().getBean2(ProductoArticuloFacadeRemote.class);
	private static final PrecioMateriaPrimaFacadeRemote precioMPFacade = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class);
	private static final ClienteFacadeRemote clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class);
	private static final ProveedorFacadeRemote proveedorFacade = GTLBeanFactory.getInstance().getBean2(ProveedorFacadeRemote.class);
	private static final ArticuloFacadeRemote articuloFacade = GTLBeanFactory.getInstance().getBean2(ArticuloFacadeRemote.class);
	private static final CondicionDeVentaFacadeRemote condicionVentaFacade = GTLBeanFactory.getInstance().getBean2(CondicionDeVentaFacadeRemote.class);
	private static final TarimaFacadeRemote tarimaFacade = GTLBeanFactory.getInstance().getBean2(TarimaFacadeRemote.class);
	private static final TipoMaquinaFacadeRemote tipoMaquinaFacade = GTLBeanFactory.getInstance().getBean2(TipoMaquinaFacadeRemote.class);
	private static final RemitoEntradaFacadeRemote remitoEntradaFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
	private static final TipoArticuloFacadeRemote tipoArticuloFacade = GTLBeanFactory.getInstance().getBean2(TipoArticuloFacadeRemote.class);
	private static final AccionProcedimientoFacadeRemote accionProcedimientoFacade = GTLBeanFactory.getInstance().getBean2(AccionProcedimientoFacadeRemote.class);
	private static final FormulaClienteFacadeRemote formulaFacade = GTLBeanFactory.getInstance().getBean2(FormulaClienteFacadeRemote.class);
	private static final MateriaPrimaFacadeRemote materiaPrimaFacade = GTLBeanFactory.getInstance().getBean2(MateriaPrimaFacadeRemote.class);
	private static final TransicionODTFacadeRemote transicionODTFacade = GTLBeanFactory.getInstance().getBean2(TransicionODTFacadeRemote.class);
	private static final UsuarioSistemaFacadeRemote usuarioSistemaFacade = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class);	

	private ODTTOConverter() {

	}

	public static OrdenDeTrabajo fromTO(OdtEagerTO odtEagerTO) {
		OrdenDeTrabajo odt = new OrdenDeTrabajo();
		odt.setAvance(EAvanceODT.getById(odtEagerTO.getIdAvance()));
		odt.setCodigo(odtEagerTO.getCodigo());
		odt.setEstadoODT(EEstadoODT.getById(odtEagerTO.getIdEstadoODT()));
		odt.setFechaODT(new Timestamp(odtEagerTO.getTimestampFechaODT()));
		odt.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		odt.setOrdenEnMaquina(odtEagerTO.getOrdenEnMaquina());
		if (odtEagerTO.getIdMaquinaActual() != null) {
			odt.setMaquinaActual(maquinaFacade.getByIdEager(odtEagerTO.getIdMaquinaActual()));
		}
		if (odtEagerTO.getIdMaquinaPrincipal() != null) {
			odt.setMaquinaPrincipal(maquinaFacade.getByIdEager(odtEagerTO.getIdMaquinaPrincipal()));
		}
		if (odtEagerTO.getIdProductoArticulo() != null) {
			odt.setProductoArticulo(productoArticuloFacade.getById(odtEagerTO.getIdProductoArticulo()));
		}
		if (odtEagerTO.getPiezas() != null && odtEagerTO.getPiezas().length > 0) {
			List<PiezaODT> piezas = new ArrayList<PiezaODT>();
			for (PiezaODTTO piezaODTTO : odtEagerTO.getPiezas()) {
				piezas.add(piezaODTFromTO(odt, piezaODTTO));
			}
			odt.setPiezas(piezas);
		}

		odt.setSecuenciaDeTrabajo(secuenciaODTFromTO(odt, odtEagerTO.getSecuenciaDeTrabajo()));
		odt.setRemito(remitoEntradaFromTO(odt, odtEagerTO.getRemito()));

		//si hay transiciones las agrego en forma transient a la ODT para luego persistirlas
		odt.setTransiciones(new ArrayList<TransicionODT>());
		for(TransicionODTTO trTO : odtEagerTO.getTransiciones()) {
			odt.getTransiciones().add(transicionEntityFromTOWS(trTO));
		}
		return odt;
	}

	private static RemitoEntrada remitoEntradaFromTO(OrdenDeTrabajo odt, RemitoEntradaTO remitoTO) {
		if (remitoTO == null) {
			return null;
		}
		RemitoEntrada remitoEntrada = new RemitoEntrada();
		remitoEntrada.setId(remitoTO.getId()); // Es el único que interesa que sobreviva para después referenciarlo y borrarlo del otro lado!
		remitoEntrada.setNroRemito(remitoTO.getNroRemito());
		remitoEntrada.setAnchoCrudo(remitoTO.getAnchoCrudo());
		remitoEntrada.setAnchoFinal(remitoTO.getAnchoFinal());
		remitoEntrada.setEnPalet(remitoTO.getEnPalet());
		remitoEntrada.setFechaEmision(new Date(remitoTO.getDateFechaEmision()));
		remitoEntrada.setPesoTotal(remitoTO.getPesoTotal());
		if (remitoTO.getIdCliente() != null) {
			remitoEntrada.setCliente(clienteFacade.getById(remitoTO.getIdCliente()));
		}
		if (remitoTO.getIdArticuloStock() != null) {
			remitoEntrada.setArticuloStock(articuloFacade.getById(remitoTO.getIdArticuloStock()));
		}
		if (remitoTO.getIdCondicionDeVenta() != null) {
			remitoEntrada.setCondicionDeVenta(condicionVentaFacade.getById(remitoTO.getIdCondicionDeVenta()));
		}
		if (remitoTO.getIdProveedor() != null) {
			remitoEntrada.setProveedor(proveedorFacade.getById(remitoTO.getIdProveedor()));
		}
		if (remitoTO.getIdPrecioMatPrima() != null) {
			remitoEntrada.setPrecioMatPrima(precioMPFacade.getById(remitoTO.getIdPrecioMatPrima()));
		}
		if (remitoTO.getProductoArticuloIdsList() != null && remitoTO.getProductoArticuloIdsList().length > 0) {
			List<ProductoArticulo> productoArticuloList = new ArrayList<ProductoArticulo>();
			for (Integer productoArticuloId : remitoTO.getProductoArticuloIdsList()) {
				productoArticuloList.add(productoArticuloFacade.getById(productoArticuloId));
			}
			remitoEntrada.setProductoArticuloList(productoArticuloList);
		}
		if (remitoTO.getIdTarima() != null) {
			remitoEntrada.setTarima(tarimaFacade.getById(remitoTO.getIdTarima()));
		}
		if (remitoTO.getPiezas() != null && remitoTO.getPiezas().length > 0) {
			List<PiezaRemito> piezasRemito = new ArrayList<PiezaRemito>();
			for (PiezaRemitoTO piezaRemitoTO : remitoTO.getPiezas()) {
				piezasRemito.add(piezaRemitoFromTO(odt, piezaRemitoTO));
			}
			remitoEntrada.setPiezas(piezasRemito);
		}
		return remitoEntrada;
	}

	private static SecuenciaODT secuenciaODTFromTO(OrdenDeTrabajo odt, SecuenciaODTTO secuenciaODTTO) {
		if (secuenciaODTTO == null) {
			return null;
		}
		SecuenciaODT secuenciaODT = new SecuenciaODT();
		secuenciaODT.setCliente(clienteFacade.getById(secuenciaODTTO.getIdCliente()));
		secuenciaODT.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		secuenciaODT.setNombre(secuenciaODTTO.getNombre());
		secuenciaODT.setOdt(odt);
		secuenciaODT.setTipoProducto(ETipoProducto.getById(secuenciaODTTO.getIdTipoProducto()));
		if (secuenciaODTTO.getPasosSecuencia() != null && secuenciaODTTO.getPasosSecuencia().length > 0) {
			List<PasoSecuenciaODT> pasos = new ArrayList<PasoSecuenciaODT>();
			for (PasoSecuenciaODTTO pasoSecuenciaODTTO : secuenciaODTTO.getPasosSecuencia()) {
				PasoSecuenciaODT pasoSecuenciaODT = new PasoSecuenciaODT();
				pasoSecuenciaODT.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
				pasoSecuenciaODT.setObservaciones(pasoSecuenciaODTTO.getObservaciones());
				TipoMaquina sector = tipoMaquinaFacade.getByIdEager(pasoSecuenciaODTTO.getIdSector(), TipoMaquinaFacadeRemote.MASK_PROCESOS | TipoMaquinaFacadeRemote.MASK_SUBPROCESOS | TipoMaquinaFacadeRemote.MASK_INSTRUCCIONES);
				pasoSecuenciaODT.setSector(sector);
				pasoSecuenciaODT.setProceso(sector.getProcesoById(pasoSecuenciaODTTO.getIdProceso()));
				pasoSecuenciaODT.setSubProceso(subProcesoODTFromTO(pasoSecuenciaODTTO.getSubProceso()));
				pasos.add(pasoSecuenciaODT);
			}
			secuenciaODT.setPasos(pasos);
		}
		return secuenciaODT;
	}

	private static ProcedimientoODT subProcesoODTFromTO(ProcedimientoODTTO subProcesoTO) {
		ProcedimientoODT subProceso = new ProcedimientoODT();
		subProceso.setId(subProceso.getId());
		subProceso.setNombre(subProcesoTO.getNombre());
		if(subProcesoTO.getIdTipoArticulo() != null) {
			subProceso.setTipoArticulo(tipoArticuloFacade.getByIdEager(subProcesoTO.getIdTipoArticulo()));
		}
		for(InstruccionProcedimientoODTTO instruccionTO : subProcesoTO.getPasos()) {
			subProceso.getPasos().add(instruccionProcedimientoODTFromTO(instruccionTO, subProceso));
		}
		return subProceso;
	}

	private static InstruccionProcedimientoODT instruccionProcedimientoODTFromTO(InstruccionProcedimientoODTTO instruccionTO, ProcedimientoODT subProceso) {
		InstruccionProcedimientoODT instruccion;
		if (instruccionTO.getTipo().equals("IPPODT")) {
			instruccion = new InstruccionProcedimientoPasadasODT();
			((InstruccionProcedimientoPasadasODT) instruccion).setCantidadPasadas(instruccionTO.getCantidadPasadas());
			((InstruccionProcedimientoPasadasODT) instruccion).setTemperatura(instruccionTO.getTemperatura());
			((InstruccionProcedimientoPasadasODT) instruccion).setVelocidad(instruccionTO.getVelocidad());
			((InstruccionProcedimientoPasadasODT) instruccion).setAccion(accionProcedimientoFacade.getById(instruccionTO.getAccionProcedimientoId()));
			List<MateriaPrimaCantidadExplotada<Quimico>> quimicos = new ArrayList<MateriaPrimaCantidadExplotada<Quimico>>();
			for(MateriaPrimaCantidadExplotadaTO mpcto : instruccionTO.getMpCantidadExplotadas()) {
				quimicos.add(materiaPrimaCantidadExplotadaFromTO(mpcto, Quimico.class));
			}
			((InstruccionProcedimientoPasadasODT) instruccion).setQuimicosExplotados(quimicos);
		} else if (instruccionTO.getTipo().equals("IPPTODT")){
			instruccion = new InstruccionProcedimientoTextoODT();
			((InstruccionProcedimientoTextoODT)instruccion).setEspecificacion(instruccionTO.getEspecificacion());
		} else {
			instruccion = new InstruccionProcedimientoTipoProductoODT();
			((InstruccionProcedimientoTipoProductoODT) instruccion).setTipoArticulo(tipoArticuloFacade.getByIdEager(instruccionTO.getIdTipoArticulo()));
			((InstruccionProcedimientoTipoProductoODT) instruccion).setTipoProducto(ETipoProducto.getById(instruccionTO.getIdTipoProducto()));
			FormulaClienteExplotada formula;
			FormulaClienteExplotadaTO formulaTO = instruccionTO.getFormula();
			if (formulaTO.getTipo().equals("TEN")) {
				formula = new FormulaTenidoClienteExplotada();
				for(MateriaPrimaCantidadExplotadaTO mpcto : formulaTO.getAnilinas()) {
					((FormulaTenidoClienteExplotada) formula).getMateriasPrimas().add(materiaPrimaCantidadExplotadaFromTO(mpcto, Anilina.class));
				}
			} else {
				formula = new FormulaEstampadoClienteExplotada();
				for(MateriaPrimaCantidadExplotadaTO mpcto : formulaTO.getPigmentos()) {
					((FormulaEstampadoClienteExplotada) formula).getPigmentos().add(materiaPrimaCantidadExplotadaFromTO(mpcto, Pigmento.class));
				}
				for(MateriaPrimaCantidadExplotadaTO mpcto : formulaTO.getQuimicos()) {
					((FormulaEstampadoClienteExplotada) formula).getQuimicos().add(materiaPrimaCantidadExplotadaFromTO(mpcto, Quimico.class));
				}
			}
			formula.setFormulaDesencadenante(formulaFacade.getById(formulaTO.getIdFormulaDesencadenante()));
			((InstruccionProcedimientoTipoProductoODT) instruccion).setFormula(formula);
		}
		instruccion.setObservaciones(instruccionTO.getObservaciones());
		instruccion.setSectorMaquina(ESectorMaquina.getById(instruccionTO.getIdTipoSector()));
		instruccion.setProcedimiento(subProceso);
		return instruccion;
	}
	
	private static <T extends Formulable> MateriaPrimaCantidadExplotada<T> materiaPrimaCantidadExplotadaFromTO(MateriaPrimaCantidadExplotadaTO mpceTO, Class<T> clazz) {
		MateriaPrimaCantidadExplotada<T> mpce = new MateriaPrimaCantidadExplotada<T>();
		mpce.setCantidadExplotada(mpceTO.getCantidadExplotada());
		if(mpceTO.getIdTipoArticulo() != null) {
			mpce.setTipoArticulo(tipoArticuloFacade.getByIdEager(mpceTO.getIdTipoArticulo()));
		}
		MateriaPrimaCantidad<T> mpCantidad = materiaPrimaFacade.getMateriaPrimaCantidadById(mpceTO.getIdMateriaPrimaCantidad());
		mpce.setMateriaPrimaCantidadDesencadenante(mpCantidad);
		return mpce;	
	}
	
	private static PiezaODT piezaODTFromTO(OrdenDeTrabajo odt, PiezaODTTO piezaODTTO) {
		if (piezaODTTO == null) {
			return null;
		}
		PiezaODT piezaODT = new PiezaODT();
		piezaODT.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		piezaODT.setMetrosStockInicial(piezaODT.getMetrosStockInicial());
		piezaODT.setNroPiezaStockInicial(piezaODT.getNroPiezaStockInicial());
		piezaODT.setOdt(odt);
		if (piezaODTTO.getPiezaRemito() != null) {
			piezaODT.setPiezaRemito(piezaRemitoFromTO(odt, piezaODTTO.getPiezaRemito()));
		}
		List<PiezaRemito> piezasSalida = new ArrayList<PiezaRemito>();
		if (piezaODTTO.getPiezasSalida() != null && piezaODTTO.getPiezasSalida().length > 0) {
			for (PiezaRemitoTO piezaRemitoTO : piezaODTTO.getPiezasSalida()) {
				piezasSalida.add(piezaRemitoFromTO(odt, piezaRemitoTO));
			}
		}
		piezaODT.setPiezasSalida(piezasSalida);
		return piezaODT;
	}

	private static PiezaRemito piezaRemitoFromTO(OrdenDeTrabajo odt, PiezaRemitoTO piezaRemitoTO) {
		if (piezaRemitoTO == null) {
			return null;
		}
		PiezaRemito piezaRemito = new PiezaRemito();
		piezaRemito.setEnSalida(piezaRemitoTO.getEnSalida());
		piezaRemito.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		piezaRemito.setMetros(piezaRemitoTO.getMetros());
		piezaRemito.setObservaciones(piezaRemitoTO.getObservaciones());
		piezaRemito.setOrdenPieza(piezaRemitoTO.getOrdenPieza());
		piezaRemito.setOrdenPiezaCalculado(piezaRemitoTO.getOrdenPiezaCalculado());
		piezaRemito.setPiezaSinODT(piezaRemitoTO.getPiezaSinODT());
		if (piezaRemitoTO.getIdPmpDescuentoStock() != null) {
			piezaRemito.setPmpDescuentoStock(precioMPFacade.getById(piezaRemitoTO.getIdPmpDescuentoStock()));
		}
		if (piezaRemitoTO.getPiezaEntrada() != null) {
			piezaRemito.setPiezaEntrada(piezaRemitoFromTO(odt, piezaRemitoTO.getPiezaEntrada())); // OJO, recursividad
		}
		if (piezaRemitoTO.getPiezasPadreODT() != null && piezaRemitoTO.getPiezasPadreODT().length > 0) {
			PiezaODTTO[] piezasPadreODT = piezaRemitoTO.getPiezasPadreODT();
			List<PiezaODT> piezasPadrePODT = new ArrayList<PiezaODT>();
			for (PiezaODTTO p : piezasPadreODT) {
				piezasPadrePODT.add(piezaODTFromTO(odt, p));
			}
			piezaRemito.setPiezasPadreODT(piezasPadrePODT);
		}

		return piezaRemito;
	}

	private static PiezaRemitoTO piezaRemitoTOFromEntity(PiezaRemito piezaRemito) {
		if (piezaRemito == null) {
			return null;
		}
		PiezaRemitoTO piezaRemitoTO = new PiezaRemitoTO();
		piezaRemitoTO.setEnSalida(piezaRemito.getEnSalida());
		piezaRemitoTO.setId(null); // Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		piezaRemitoTO.setMetros(piezaRemito.getMetros());
		piezaRemitoTO.setObservaciones(piezaRemito.getObservaciones());
		piezaRemitoTO.setOrdenPieza(piezaRemito.getOrdenPieza());
		piezaRemitoTO.setOrdenPiezaCalculado(piezaRemito.getOrdenPiezaCalculado());
		piezaRemitoTO.setPiezaSinODT(piezaRemito.getPiezaSinODT());
		if (piezaRemito.getPmpDescuentoStock() != null) {
			piezaRemitoTO.setIdPmpDescuentoStock(piezaRemito.getPmpDescuentoStock().getId());
		}

		//La pieza de entrada es sólo para el remito de salida, no haría falta setear esa propiedad  
//		if (piezaRemito.getPiezaEntrada() != null) {
//			piezaRemitoTO.setPiezaEntrada(piezaRemitoTOFromEntity(piezaRemito.getPiezaEntrada())); // OJO, recursividad
//		}

		//Las piezas padre ODT son seteadas en las piezas del remito de salida
//		if (piezaRemito.getPiezasPadreODT() != null && piezaRemito.getPiezasPadreODT().size() > 0) {
//			List<PiezaODT> piezasPadreODT = piezaRemito.getPiezasPadreODT();
//			PiezaODTTO[] piezasPadrePODT = new PiezaODTTO[piezasPadreODT.size()];
//			int i = 0;
//			for (PiezaODT p : piezasPadreODT) {
//				piezasPadrePODT[i++] = piezaODTTOFromEntity(p);
//			}
//			piezaRemitoTO.setPiezasPadreODT(piezasPadrePODT);
//		}

		return piezaRemitoTO;
	}

	private static PiezaODTTO piezaODTTOFromEntity(PiezaODT piezaODT) {
		if (piezaODT == null) {
			return null;
		}
		PiezaODTTO piezaODTTO = new PiezaODTTO();
		piezaODTTO.setId(null);	// Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		piezaODTTO.setMetrosStockInicial(piezaODT.getMetrosStockInicial());
		piezaODTTO.setNroPiezaStockInicial(piezaODT.getNroPiezaStockInicial());
		piezaODTTO.setCodigoOdt(piezaODT.getOdt().getCodigo());
		if (piezaODTTO.getPiezaRemito() != null) {
			piezaODTTO.setPiezaRemito(piezaRemitoTOFromEntity(piezaODT.getPiezaRemito()));
		}
		
		//Esto se setea cuando se trata de un R.S. 
		//VER... HACE STACKOVERFLOW
//		List<PiezaRemitoTO> piezasSalida = new ArrayList<PiezaRemitoTO>();
//		if (piezaODT.getPiezasSalida() != null && piezaODT.getPiezasSalida().size() > 0) {
//			for (PiezaRemito piezaRemitoTO : piezaODT.getPiezasSalida()) {
//				piezasSalida.add(piezaRemitoTOFromEntity(piezaRemitoTO));
//			}
//		}

		//Esto se setea cuando se trata de un R.S. 
//		piezaODTTO.setPiezasSalida(piezasSalida.toArray(new PiezaRemitoTO[piezasSalida.size()]));
		return piezaODTTO;
	}

	public static RemitoEntradaTO toRemitoWSTO(DetalleRemitoEntradaNoFacturado elemento) {
		RemitoEntrada re = remitoEntradaFacade.getByIdEagerConPiezasODTYRemito(elemento.getId());
		if (re == null) {
			throw new RuntimeException("Remito no encontrado");
		}
		
		RemitoEntradaTO remitoTO = new RemitoEntradaTO();
		remitoTO.setId(null);	// Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		remitoTO.setAnchoCrudo(re.getAnchoCrudo());
		remitoTO.setAnchoFinal(re.getAnchoFinal());
		remitoTO.setDateFechaEmision(re.getFechaEmision().getTime());
		remitoTO.setEnPalet(re.getEnPalet());
		remitoTO.setIdArticuloStock(re.getArticuloStock() != null ? re.getArticuloStock().getId() : null);
		remitoTO.setIdCliente(re.getCliente().getId());
		remitoTO.setIdCondicionDeVenta(re.getCondicionDeVenta().getId());
		remitoTO.setIdPrecioMatPrima(re.getPrecioMatPrima() != null ? re.getPrecioMatPrima().getId() : null);
		remitoTO.setIdProveedor(re.getProveedor() != null ? re.getProveedor().getId() : null);
		remitoTO.setIdTarima(re.getTarima() != null ? re.getTarima().getId() : null);
		remitoTO.setNroRemito(re.getNroRemito());
		remitoTO.setPesoTotal(re.getPesoTotal());
		if (re.getProductoArticuloList() != null && !re.getProductoArticuloList().isEmpty()) {
			remitoTO.setProductoArticuloIdsList(
					FluentIterable.from(re.getProductoArticuloList()).transform(new Function<ProductoArticulo, Integer>() {
						public Integer apply(ProductoArticulo pa) {
							return pa.getId();
						}
					}).toArray(Integer.class));
		}
		remitoTO.setPiezas(FluentIterable.from(re.getPiezas()).transform(new Function<PiezaRemito, PiezaRemitoTO>() {
			public PiezaRemitoTO apply(PiezaRemito pr) {
				return piezaRemitoTOFromEntity(pr);
			}
		}).toArray(PiezaRemitoTO.class));
		Set<OrdenDeTrabajo> odtsSet = Sets.newHashSet();
		for (PiezaRemito pr : re.getPiezas()) {
			for (PiezaODT podt : pr.getPiezasPadreODT()) {
				odtsSet.add(podt.getOdt());
			}
		}
		int index = 0;
		remitoTO.setOdts(new OdtEagerTO[odtsSet.size()]);
		for (OrdenDeTrabajo odt : odtsSet) {
			remitoTO.getOdts()[index++] = odtEagerTOWSFromEntity(odt);
		}
		return remitoTO;
	}
	
	private static OdtEagerTO odtEagerTOWSFromEntity(OrdenDeTrabajo odt) {
		OdtEagerTO odtto = new OdtEagerTO();
		odtto.setId(null);			// Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		odtto.setRemito(null);		// Ya la estoy agregando al remitoTO de arriba
		odtto.setCodigo(odt.getCodigo());
		odtto.setTimestampFechaODT(odt.getFechaODT().getTime());
		if (odt.getAvance() != null) {
			odtto.setIdAvance(odt.getAvance().getId());
		}
		odtto.setIdEstadoODT(odt.getEstado().getId());
		if (odt.getProductoArticulo() != null) {
			odtto.setIdProductoArticulo(odt.getProductoArticulo().getId());
		}
		if (odt.getMaquinaActual() != null) {
			odtto.setIdMaquinaActual(odt.getMaquinaActual().getId());
		}
		if (odt.getOrdenEnMaquina() != null) {
			odtto.setOrdenEnMaquina(odt.getOrdenEnMaquina());
		}
		if (odt.getMaquinaPrincipal() != null) {
			odtto.setIdMaquinaPrincipal(odt.getMaquinaPrincipal().getId());
		}
		if (odt.getSecuenciaDeTrabajo() != null) {
			odtto.setSecuenciaDeTrabajo(secuenciaTOWsFromEntity(odt.getSecuenciaDeTrabajo()));
		}
		List<TransicionODT> transiciones = transicionODTFacade.getAllEagerByODT(odt.getId());
		if(!transiciones.isEmpty()) {
			odtto.setTransiciones(new TransicionODTTO[transiciones.size()]);
			int index = 0;
			for(TransicionODT tODT : transiciones) {
				odtto.getTransiciones()[index++] = transicionODTTOWSFromEntity(tODT);
			}
		}
		if (odt.getPiezas() != null && !odt.getPiezas().isEmpty()) {
			odtto.setPiezas(new PiezaODTTO[odt.getPiezas().size()]);
			int index = 0;
			for (PiezaODT po : odt.getPiezas()) {
				odtto.getPiezas()[index++] = piezaODTTOFromEntity(po);
			}
		}
		return odtto;
	}

	private static TransicionODT transicionEntityFromTOWS(TransicionODTTO tODT) {
		TransicionODT transicion = new TransicionODT();
		transicion.setMaquina(maquinaFacade.getByIdEager(tODT.getIdMaquina()));
		transicion.setTipoMaquina(tipoMaquinaFacade.getByIdEager(tODT.getIdTipoMaquina(), 0));
		transicion.setFechaHoraRegistro(new Timestamp(tODT.getFechaHoraRegistro()));
		transicion.setUsuarioSistema(usuarioSistemaFacade.getById(tODT.getIdUsuarioSistema()));
		for(CambioAvanceTO ca : tODT.getCambiosAvance()) {
			transicion.getCambiosAvance().add(cambioAvanceEntityFromTOWS(ca));
		}
		return transicion;
	}

	private static CambioAvance cambioAvanceEntityFromTOWS(CambioAvanceTO caTO) {
		CambioAvance cambio = new CambioAvance();
		cambio.setAvance(EAvanceODT.getById(caTO.getIdAvance()));
		cambio.setFechaHora(new Timestamp(caTO.getFechaHora()));
		cambio.setUsuario(usuarioSistemaFacade.getById(caTO.getIdUsuarioSistema()));
		cambio.setObservaciones(caTO.getObservaciones());
		return cambio;
	}

	private static TransicionODTTO transicionODTTOWSFromEntity(TransicionODT tODT) {
		TransicionODTTO transicion = new TransicionODTTO();
		transicion.setIdMaquina(tODT.getMaquina().getId());
		transicion.setIdTipoMaquina(tODT.getTipoMaquina().getId());
		transicion.setFechaHoraRegistro(tODT.getFechaHoraRegistro().getTime());
		transicion.setIdUsuarioSistema(tODT.getUsuarioSistema().getId());
		transicion.setCambiosAvance(new CambioAvanceTO[tODT.getCambiosAvance().size()]);
		int index = 0;
		for(CambioAvance ca : tODT.getCambiosAvance()) {
			transicion.getCambiosAvance()[index++] = cambioAvanceTOWSFromEntity(ca);
		}
		return transicion;
	}

	private static CambioAvanceTO cambioAvanceTOWSFromEntity(CambioAvance ca) {
		CambioAvanceTO cambio = new CambioAvanceTO();
		cambio.setIdAvance(ca.getAvance().getId());
		cambio.setFechaHora(ca.getFechaHora().getTime());
		cambio.setIdUsuarioSistema(ca.getUsuario().getId());
		cambio.setObservaciones(ca.getObservaciones());
		return cambio;
	}

	private static SecuenciaODTTO secuenciaTOWsFromEntity(SecuenciaODT secuencia) {
		SecuenciaODTTO secuenciaTO = new SecuenciaODTTO();
		secuenciaTO.setId(null);	// Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		secuenciaTO.setNombre(secuencia.getNombre());
		secuenciaTO.setIdCliente(secuencia.getCliente().getId());
		secuenciaTO.setIdTipoProducto(secuencia.getTipoProducto().getId());
		secuenciaTO.setPasosSecuencia(new PasoSecuenciaODTTO[secuencia.getPasos().size()]);
		int index = 0;
		for (PasoSecuenciaODT p : secuencia.getPasos()) {
			secuenciaTO.getPasosSecuencia()[index++] = pasoSecuenciaODTTOWsFromEntity(p);
		}
		return secuenciaTO;
	}

	private static PasoSecuenciaODTTO pasoSecuenciaODTTOWsFromEntity(PasoSecuenciaODT p) {
		PasoSecuenciaODTTO paso = new PasoSecuenciaODTTO();
		paso.setId(null);	// Para que quede claro que no quiero ID del otro lado porque se tiene que persistir de cero
		paso.setIdSector(p.getSector().getId());
		paso.setIdProceso(p.getProceso().getId());
		paso.setObservaciones(p.getObservaciones());
		paso.setSubProceso(procedimientoODTTOWsFromEntity(p.getSubProceso()));
		return paso;
	}

	private static ProcedimientoODTTO procedimientoODTTOWsFromEntity(ProcedimientoODT procODT) {
		ProcedimientoODTTO procedimiento = new ProcedimientoODTTO();
		procedimiento.setNombre(procODT.getNombre());
		procedimiento.setIdTipoArticulo(procODT.getTipoArticulo().getId());
		procedimiento.setPasos(new InstruccionProcedimientoODTTO[procODT.getPasos().size()]);
		int index = 0;
		for (InstruccionProcedimientoODT paso : procODT.getPasos()) {
			procedimiento.getPasos()[index++] = instruccionProcedimientoODTTOWSFromEntity(paso);
		}
		return procedimiento;
	}

	private static InstruccionProcedimientoODTTO instruccionProcedimientoODTTOWSFromEntity(InstruccionProcedimientoODT instODT) {
		InstruccionProcedimientoODTTO instruccion = new InstruccionProcedimientoODTTO();
		instruccion.setIdTipoSector(instODT.getSectorMaquina().getId());
		instruccion.setObservaciones(instODT.getObservaciones());
		if (instODT instanceof InstruccionProcedimientoPasadasODT) {
			InstruccionProcedimientoPasadasODT instODTP = (InstruccionProcedimientoPasadasODT) instODT;
			instruccion.setTipo("IPPODT");
			instruccion.setCantidadPasadas(instODTP.getCantidadPasadas());
			instruccion.setTemperatura(instODTP.getTemperatura());
			instruccion.setVelocidad(instODTP.getVelocidad());
			instruccion.setAccionProcedimientoId(instODTP.getAccion().getId());
			instruccion.setMpCantidadExplotadas(new MateriaPrimaCantidadExplotadaTO[instODTP.getQuimicosExplotados().size()]);
			int index = 0;
			for (MateriaPrimaCantidadExplotada<Quimico> mp : instODTP.getQuimicosExplotados()) {
				instruccion.getMpCantidadExplotadas()[index++] = materiaPrimaCantidadExplotadaTOWSFromEntity(mp);
			}
		} else if (instODT instanceof InstruccionProcedimientoTextoODT) {
			InstruccionProcedimientoTextoODT instODTT = (InstruccionProcedimientoTextoODT) instODT;
			instruccion.setTipo("IPPTODT");
			instruccion.setEspecificacion(instODTT.getEspecificacion());
		} else {
			InstruccionProcedimientoTipoProductoODT instODTTP = (InstruccionProcedimientoTipoProductoODT) instODT;
			instruccion.setTipo("IPTPODT");
			instruccion.setIdTipoArticulo(instODTTP.getTipoArticulo().getId());
			instruccion.setIdTipoProducto(instODTTP.getTipoProducto().getId());
			instruccion.setFormula(formulaClienteExplotadaTOWSFromEntity(instODTTP.getFormula()));
		}
		return instruccion;
	}

	private static MateriaPrimaCantidadExplotadaTO materiaPrimaCantidadExplotadaTOWSFromEntity(MateriaPrimaCantidadExplotada<?> mpc) {
		MateriaPrimaCantidadExplotadaTO mpcTO = new MateriaPrimaCantidadExplotadaTO();
		mpcTO.setIdMateriaPrimaCantidad(mpc.getMateriaPrimaCantidadDesencadenante().getId());
		mpcTO.setIdTipoArticulo(mpc.getTipoArticulo() == null ? null : mpc.getTipoArticulo().getId());
		mpcTO.setCantidadExplotada(mpc.getCantidadExplotada());
		return mpcTO;
	}

	private static FormulaClienteExplotadaTO formulaClienteExplotadaTOWSFromEntity(FormulaClienteExplotada formula) {
		FormulaClienteExplotadaTO formulaTO = new FormulaClienteExplotadaTO();
		formulaTO.setIdFormulaDesencadenante(formula.getFormulaDesencadenante().getId());
		if (formula instanceof FormulaTenidoClienteExplotada) {
			formulaTO.setTipo("TEN");
			FormulaTenidoClienteExplotada formulaT = (FormulaTenidoClienteExplotada) formula;
			formulaTO.setAnilinas(new MateriaPrimaCantidadExplotadaTO[formulaT.getMateriasPrimas().size()]);
			int index = 0;
			for (MateriaPrimaCantidadExplotada<?> mpce : formulaT.getMateriasPrimas()) {
				formulaTO.getAnilinas()[index++] = materiaPrimaCantidadExplotadaTOWSFromEntity(mpce);
			}
		} else {
			formulaTO.setTipo("ESTAMP");
			FormulaEstampadoClienteExplotada formulaE = (FormulaEstampadoClienteExplotada) formula;
			formulaTO.setPigmentos(new MateriaPrimaCantidadExplotadaTO[formulaE.getPigmentos().size()]);
			int index = 0;
			for (MateriaPrimaCantidadExplotada<?> mpce : formulaE.getPigmentos()) {
				formulaTO.getPigmentos()[index++] = materiaPrimaCantidadExplotadaTOWSFromEntity(mpce);
			}
			formulaTO.setQuimicos(new MateriaPrimaCantidadExplotadaTO[formulaE.getQuimicos().size()]);
			index = 0;
			for (MateriaPrimaCantidadExplotada<?> mpce : formulaE.getQuimicos()) {
				formulaTO.getPigmentos()[index++] = materiaPrimaCantidadExplotadaTOWSFromEntity(mpce);
			}
		}
		return formulaTO;
	}
}
