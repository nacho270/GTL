package ar.com.textillevel.webservices.odt.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ar.com.textillevel.dao.api.local.ArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.ClienteDAOLocal;
import ar.com.textillevel.dao.api.local.CondicionDeVentaDAOLocal;
import ar.com.textillevel.dao.api.local.MateriaPrimaCantidadDAOLocal;
import ar.com.textillevel.dao.api.local.PrecioMateriaPrimaDAOLocal;
import ar.com.textillevel.dao.api.local.ProductoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.TipoArticuloDAOLocal;
import ar.com.textillevel.dao.api.local.UsuarioSistemaDAOLocal;
import ar.com.textillevel.entidades.documentos.remito.PiezaRemito;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.entidades.ventas.materiaprima.Pigmento;
import ar.com.textillevel.entidades.ventas.materiaprima.Quimico;
import ar.com.textillevel.entidades.ventas.materiaprima.anilina.Anilina;
import ar.com.textillevel.facade.api.local.RemitoEntradaFacadeLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.AccionProcedimientoDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.FormulaClienteDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.MaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TipoMaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
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
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;
import ar.com.textillevel.modulos.odt.to.intercambio.CambioAvanceTO;
import ar.com.textillevel.modulos.odt.to.intercambio.FormulaClienteExplotadaTO;
import ar.com.textillevel.modulos.odt.to.intercambio.InstruccionProcedimientoODTTO;
import ar.com.textillevel.modulos.odt.to.intercambio.MateriaPrimaCantidadExplotadaTO;
import ar.com.textillevel.modulos.odt.to.intercambio.ODTEagerTO;
import ar.com.textillevel.modulos.odt.to.intercambio.PasoSecuenciaODTTO;
import ar.com.textillevel.modulos.odt.to.intercambio.PiezaODTTO;
import ar.com.textillevel.modulos.odt.to.intercambio.PiezaRemitoTO;
import ar.com.textillevel.modulos.odt.to.intercambio.ProcedimientoODTTO;
import ar.com.textillevel.modulos.odt.to.intercambio.RemitoEntradaTO;
import ar.com.textillevel.modulos.odt.to.intercambio.SecuenciaODTTO;
import ar.com.textillevel.modulos.odt.to.intercambio.TransicionODTTO;
import ar.com.textillevel.webservices.odt.api.remote.ODTServiceRemote;

@Stateless
@WebService
public class ODTService implements ODTServiceRemote {

	private static final Logger logger = Logger.getLogger(ODTService.class);
	
	@EJB
	private OrdenDeTrabajoFacadeLocal odtFacade;
	
	@EJB
	private RemitoEntradaFacadeLocal remitoEntradaFacade;

	@EJB
	private TransicionODTDAOLocal transicionODTDAO;
	
	@EJB
	private ArticuloDAOLocal artDAO;

	@EJB
	private ClienteDAOLocal clienteDAO;

	@EJB
	private CondicionDeVentaDAOLocal condVentaDAO;

	@EJB
	private PrecioMateriaPrimaDAOLocal pmpDAO;
	
	@EJB
	private MaquinaDAOLocal maqDAO;
	
	@EJB
	private ProductoArticuloDAOLocal paDAO;
	
	@EJB
	private TipoMaquinaDAOLocal tmDAO;

	@EJB
	private TipoArticuloDAOLocal taDAO;

	@EJB
	private AccionProcedimientoDAOLocal apDAO;
	
	@EJB
	private FormulaClienteDAOLocal formulaDAO;
	
	@EJB
	private MateriaPrimaCantidadDAOLocal mpcDAO;
	
	@EJB
	private UsuarioSistemaDAOLocal usDAO;

	//URL: http://localhost:8080/GTL-gtlback-server/ODTService?wsdl
	
	/*
	 Poner estos jars en jboss-salem/lib/endorsed
	 	jaxb-api.jar
		jaxb-impl.jar
		jboss-jaxrpc.jar
		jboss-jaxws.jar
		jboss-saaj.jar
		serializer.jar
		xalan.jar
		xercesImpl.jar
	 */
	
	public Boolean recibirRemitoEntrada(RemitoEntradaTO remitoEntrada) {
		logger.info("Se recibio remito " + remitoEntrada.getNroRemito() + ".");
		//Construyo entity RemitoEntrada
		RemitoEntrada re = new RemitoEntrada();
		re.setAnchoCrudo(remitoEntrada.getAnchoCrudo());
		re.setAnchoFinal(remitoEntrada.getAnchoCrudo());
		re.setArticuloStock(remitoEntrada.getIdArticuloStock() == null ? null : artDAO.getReferenceById(remitoEntrada.getIdArticuloStock()));
		re.setCliente(clienteDAO.getReferenceById(remitoEntrada.getIdCliente()));
		re.setCondicionDeVenta(condVentaDAO.getReferenceById(remitoEntrada.getIdCondicionDeVenta()));
		re.setEnPalet(remitoEntrada.getEnPalet());
		re.setFechaEmision(new Date(remitoEntrada.getDateFechaEmision()));
		re.setNroRemito(remitoEntrada.getNroRemito());
		re.setPesoTotal(remitoEntrada.getPesoTotal());
		Map<Integer, PiezaRemito> piezasRemitoMap = getPiezasRemitoMap(remitoEntrada.getPiezas());
		re.getPiezas().addAll(piezasRemitoMap.values());
		for(Integer paId : remitoEntrada.getProductoArticuloIdsList()) {
			re.getProductoArticuloList().add(paDAO.getReferenceById(paId));
		}
		//Construyo entitys ODT
		List<TransicionODT> transiciones = new ArrayList<TransicionODT>();
		List<OrdenDeTrabajo> odtList = new ArrayList<OrdenDeTrabajo>();
		for(ODTEagerTO odtTO : remitoEntrada.getOdts()) {
			OrdenDeTrabajo odt = new OrdenDeTrabajo();
			odt.setAvance(EAvanceODT.getById(odtTO.getIdAvance()));
			odt.setCodigo(odtTO.getCodigo());
			odt.setEstadoODT(EEstadoODT.getById(odtTO.getIdEstadoODT()));
			odt.setFechaODT(new Timestamp(odtTO.getTimestampFechaODT()));
			odt.setMaquinaActual(odtTO.getIdMaquinaActual() == null ? null : maqDAO.getReferenceById(odtTO.getIdMaquinaActual()));
			odt.setMaquinaPrincipal(odtTO.getIdMaquinaPrincipal() == null ? null : maqDAO.getReferenceById(odtTO.getIdMaquinaPrincipal()));
			odt.setOrdenEnMaquina(odtTO.getOrdenEnMaquina());
			odt.setProductoArticulo(paDAO.getReferenceById(odtTO.getIdProductoArticulo()));
			odt.setRemito(re);
			odt.getPiezas().addAll(getPiezasODT(odt, odtTO.getPiezas(), piezasRemitoMap));
			odt.setSecuenciaDeTrabajo(secuenciaODTFromTO(odt, odtTO.getSecuenciaDeTrabajo()));
			odtList.add(odt);
			transiciones.addAll(transicionesEntityFromTOWSList(odt, odtTO.getTransiciones()));
		}
		//Construyo las Transiciones
		remitoEntradaFacade.saveWithTransiciones(re, odtList, transiciones, "admin");//TODO: hacer q el usuario venga como parámetro, implica regenerar el WS
		return true;
	}

	private Map<Integer, PiezaRemito> getPiezasRemitoMap(List<PiezaRemitoTO> piezasTO) {
		Map<Integer, PiezaRemito> map = new LinkedHashMap<Integer, PiezaRemito>();
		for(PiezaRemitoTO pTO : piezasTO) {
			PiezaRemito pr = new PiezaRemito();
			pr.setEnSalida(pTO.getEnSalida());
			pr.setMetros(pTO.getMetros());
			pr.setObservaciones(pTO.getObservaciones());
			pr.setOrdenPieza(pTO.getOrdenPieza());
			pr.setOrdenPiezaCalculado(pTO.getOrdenPiezaCalculado());
			pr.setPiezaSinODT(pTO.getPiezaSinODT());
			if(pTO.getIdPmpDescuentoStock() != null) {
				pr.setPmpDescuentoStock(pmpDAO.getReferenceById(pTO.getIdPmpDescuentoStock()));
			}
			map.put(pr.getOrdenPieza(), pr);
		}
		return map;
	}

	private List<PiezaODT> getPiezasODT(OrdenDeTrabajo odt, List<PiezaODTTO> piezasTO, Map<Integer, PiezaRemito> piezasRemitoMap) {
		List<PiezaODT> piezasODT = new ArrayList<PiezaODT>();
		for(PiezaODTTO pODTTO : piezasTO) {
			PiezaODT  pODT = new PiezaODT();
			pODT.setMetrosStockInicial(pODTTO.getMetrosStockInicial());
			pODT.setNroPiezaStockInicial(pODTTO.getNroPiezaStockInicial());
			pODT.setOdt(odt);
			pODT.setPiezaRemito(piezasRemitoMap.get(pODTTO.getPiezaRemito().getOrdenPieza()));
			piezasODT.add(pODT);
		}
		return piezasODT;
	}

	private SecuenciaODT secuenciaODTFromTO(OrdenDeTrabajo odt, SecuenciaODTTO secuenciaODTTO) {
		if (secuenciaODTTO == null) {
			return null;
		}
		SecuenciaODT secuenciaODT = new SecuenciaODT();
		secuenciaODT.setCliente(clienteDAO.getReferenceById(secuenciaODTTO.getIdCliente()));
		secuenciaODT.setNombre(secuenciaODTTO.getNombre());
		secuenciaODT.setOdt(odt);
		secuenciaODT.setTipoProducto(ETipoProducto.getById(secuenciaODTTO.getIdTipoProducto()));
		if (secuenciaODTTO.getPasosSecuencia() != null && secuenciaODTTO.getPasosSecuencia().size() > 0) {
			List<PasoSecuenciaODT> pasos = new ArrayList<PasoSecuenciaODT>();
			for (PasoSecuenciaODTTO pasoSecuenciaODTTO : secuenciaODTTO.getPasosSecuencia()) {
				PasoSecuenciaODT pasoSecuenciaODT = new PasoSecuenciaODT();
				pasoSecuenciaODT.setObservaciones(pasoSecuenciaODTTO.getObservaciones());
				TipoMaquina sector = tmDAO.getById(pasoSecuenciaODTTO.getIdSector());
				pasoSecuenciaODT.setSector(sector);
				pasoSecuenciaODT.setProceso(sector.getProcesoById(pasoSecuenciaODTTO.getIdProceso()));
				pasoSecuenciaODT.setSubProceso(subProcesoODTFromTO(pasoSecuenciaODTTO.getSubProceso()));
				pasos.add(pasoSecuenciaODT);
			}
			secuenciaODT.getPasos().addAll(pasos);
		}
		return secuenciaODT;
	}

	private ProcedimientoODT subProcesoODTFromTO(ProcedimientoODTTO subProcesoTO) {
		ProcedimientoODT subProceso = new ProcedimientoODT();
		subProceso.setId(subProceso.getId());
		subProceso.setNombre(subProcesoTO.getNombre());
		if(subProcesoTO.getIdTipoArticulo() != null) {
			subProceso.setTipoArticulo(taDAO.getReferenceById(subProcesoTO.getIdTipoArticulo()));
		}
		for(InstruccionProcedimientoODTTO instruccionTO : subProcesoTO.getPasos()) {
			subProceso.getPasos().add(instruccionProcedimientoODTFromTO(instruccionTO, subProceso));
		}
		return subProceso;
	}

	private InstruccionProcedimientoODT instruccionProcedimientoODTFromTO(InstruccionProcedimientoODTTO instruccionTO, ProcedimientoODT subProceso) {
		InstruccionProcedimientoODT instruccion;
		if (instruccionTO.getTipo().equals("IPPODT")) {
			instruccion = new InstruccionProcedimientoPasadasODT();
			((InstruccionProcedimientoPasadasODT) instruccion).setCantidadPasadas(instruccionTO.getCantidadPasadas());
			((InstruccionProcedimientoPasadasODT) instruccion).setTemperatura(instruccionTO.getTemperatura());
			((InstruccionProcedimientoPasadasODT) instruccion).setVelocidad(instruccionTO.getVelocidad());
			((InstruccionProcedimientoPasadasODT) instruccion).setAccion(apDAO.getReferenceById(instruccionTO.getAccionProcedimientoId()));
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
			((InstruccionProcedimientoTipoProductoODT) instruccion).setTipoArticulo(taDAO.getReferenceById(instruccionTO.getIdTipoArticulo()));
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
			formula.setFormulaDesencadenante(formulaDAO.getReferenceById(formulaTO.getIdFormulaDesencadenante()));
			((InstruccionProcedimientoTipoProductoODT) instruccion).setFormula(formula);
		}
		instruccion.setObservaciones(instruccionTO.getObservaciones());
		instruccion.setSectorMaquina(ESectorMaquina.getById(instruccionTO.getIdTipoSector()));
		instruccion.setProcedimiento(subProceso);
		return instruccion;
	}

	private List<TransicionODT> transicionesEntityFromTOWSList(OrdenDeTrabajo odt, List<TransicionODTTO> tODTList) {
		List<TransicionODT> transiciones = new ArrayList<TransicionODT>(); 
		for(TransicionODTTO tODT : tODTList) {
			TransicionODT transicion = new TransicionODT();
			transicion.setOdt(odt);
			transicion.setMaquina(tODT.getIdMaquina() == null ? null : maqDAO.getReferenceById(tODT.getIdMaquina()));
			transicion.setTipoMaquina(tODT.getIdTipoMaquina() == null ? null : tmDAO.getReferenceById(tODT.getIdTipoMaquina()));
			transicion.setFechaHoraRegistro(new Timestamp(tODT.getFechaHoraRegistro()));
			transicion.setUsuarioSistema(usDAO.getReferenceById(tODT.getIdUsuarioSistema()));
			if(tODT.getCambiosAvance() != null) {
				for(CambioAvanceTO ca : tODT.getCambiosAvance()) {
					transicion.getCambiosAvance().add(cambioAvanceEntityFromTOWS(ca));
				}
			}
			transiciones.add(transicion);
		}
		return transiciones;
	}

	private CambioAvance cambioAvanceEntityFromTOWS(CambioAvanceTO caTO) {
		CambioAvance cambio = new CambioAvance();
		cambio.setAvance(EAvanceODT.getById(caTO.getIdAvance()));
		cambio.setFechaHora(new Timestamp(caTO.getFechaHora()));
		cambio.setUsuario(usDAO.getReferenceById(caTO.getIdUsuarioSistema()));
		cambio.setObservaciones(caTO.getObservaciones());
		return cambio;
	}

	@SuppressWarnings("unchecked")
	private <T extends Formulable> MateriaPrimaCantidadExplotada<T> materiaPrimaCantidadExplotadaFromTO(MateriaPrimaCantidadExplotadaTO mpceTO, Class<T> clazz) {
		MateriaPrimaCantidadExplotada<T> mpce = new MateriaPrimaCantidadExplotada<T>();
		mpce.setCantidadExplotada(mpceTO.getCantidadExplotada());
		if(mpceTO.getIdTipoArticulo() != null) {
			mpce.setTipoArticulo(taDAO.getReferenceById(mpceTO.getIdTipoArticulo()));
		}
		mpce.setMateriaPrimaCantidadDesencadenante(mpcDAO.getReferenceById(mpceTO.getIdMateriaPrimaCantidad()));
		return mpce;	
	}
	
	public Boolean borrarRemitoDeEntrada(Integer id) {
		try {
			remitoEntradaFacade.eliminarRemitoEntradaForzado(id, false);
			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		return odtFacade.getInfoPiezasEntradaCompletoSinSalidaByClient(idCliente);
	}

	public List<ODTEagerTO> getByIdsEager(List<Integer> ids) {
		List<ODTEagerTO> lista = new ArrayList<ODTEagerTO>();
		for(OrdenDeTrabajo odt : odtFacade.getByIdsEager(ids)) {
			lista.add(new ODTEagerTO(odt, transicionODTDAO.getAllEagerByODT(odt.getId())));
		}
		return lista;
	}

}