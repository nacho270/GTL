package ar.com.textillevel.modulos.odt.facade.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.fwcommon.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.dao.api.local.PiezaRemitoDAOLocal;
import ar.com.textillevel.dao.api.local.RemitoEntradaDAOLocal;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.documentos.remito.enums.ESituacionODTRE;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.modulos.notificaciones.enums.ETipoNotificacion;
import ar.com.textillevel.modulos.notificaciones.facade.api.local.NotificacionUsuarioFacadeLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.CambioAvanceDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.CodigoODTDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.MaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.OrdenDeTrabajoDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.PiezaODTDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.SecuenciaODTDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TipoMaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.PiezaODT;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.EstadoActualMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoActualTipoMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoGeneralODTsTO;
import ar.com.textillevel.modulos.odt.to.InfoAsignacionMaquinaTO;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.modulos.terminal.entidades.Terminal;
import ar.com.textillevel.util.ODTCodigoHelper;

@Stateless
public class OrdenDeTrabajoFacade implements OrdenDeTrabajoFacadeRemote, OrdenDeTrabajoFacadeLocal {

	@EJB
	private OrdenDeTrabajoDAOLocal odtDAO;

	@EJB
	private CodigoODTDAOLocal codigoODTDAO;
	
	@EJB
	private PiezaRemitoDAOLocal piezaRemitoDAO;

	@EJB
	private ParametrosGeneralesDAOLocal paramGeneralesDAO;
	
	@EJB
	private TipoMaquinaDAOLocal tipoMaquinaDAO;
	
	@EJB
	private TransicionODTDAOLocal transicionDao;
	
	@EJB
	private MaquinaDAOLocal maquinaDao;

	@EJB
	private CambioAvanceDAOLocal cambioAvanceDAO;
	
	@EJB
	private AuditoriaFacadeLocal<OrdenDeTrabajo> auditoriaFacade;
	
	@EJB
	private SecuenciaODTDAOLocal secuenciaDao;
	
	@EJB
	private MovimientoStockFacadeLocal movimientoStockFacade;
	
	@EJB
	private PrecioMateriaPrimaFacadeLocal precioMateriaPrimaFacade;
	
	@EJB
	private PiezaODTDAOLocal piezaODTDAO;
	
	@EJB
	private RemitoEntradaDAOLocal remitoDAO;
	
	@EJB
	private NotificacionUsuarioFacadeLocal notificacionesFacadeFacade;

	public List<OrdenDeTrabajo> getOdtNoAsociadasByClient(Integer idCliente) {
		return odtDAO.getOdtNoAsociadasByClient(idCliente);
	}

	public String getUltimoCodigoODT() {
		String ultimoCodigoODT = codigoODTDAO.getUltimoCodigoODT();
		if(ultimoCodigoODT == null) {
			ParametrosGenerales parametrosGenerales = paramGeneralesDAO.getParametrosGenerales();
			if(parametrosGenerales == null || parametrosGenerales.getNroComienzoODT() == null) {
				throw new RuntimeException("Falta configurar el número de comienzo de la ODT en los parámetros generales.");
			} else {
				//Si es el primero calculo uno anterior así el siguiente es el configurado
				String nroComODT = StringUtil.fillLeftWithZeros(parametrosGenerales.getNroComienzoODT().toString(), 2);
				String codTemp = DateUtil.dateToString(DateUtil.getHoy(), ODTCodigoHelper.FORMATO_COD_ODT).substring(0, 6) + nroComODT;
				Date stringToDate = DateUtil.getAyerSinRedondear(DateUtil.stringToDate(codTemp, ODTCodigoHelper.FORMATO_COD_ODT));
				ultimoCodigoODT = DateUtil.dateToString(stringToDate,ODTCodigoHelper.FORMATO_COD_ODT); 
			}
		}
		return ultimoCodigoODT;
	}

	public List<OrdenDeTrabajo> getOdtEagerByRemitoList(Integer idRemito) {
		return odtDAO.getOdtEagerByRemitoList(idRemito);
	}

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaSinSalidaByClient(Integer idCliente) {
		return piezaRemitoDAO.getInfoPiezasEntradaSinSalidaByClient(idCliente);
	}

	public List<DetallePiezaRemitoEntradaSinSalida> getInfoPiezasEntradaCompletoSinSalidaByClient(Integer idCliente) {
		return piezaRemitoDAO.getInfoPiezasEntradaCompletoSinSalidaByClient(idCliente);
	}
	
	public OrdenDeTrabajo getByIdEager(Integer idODT) {
		return odtDAO.getByIdEager(idODT);
	}

	public List<OrdenDeTrabajo> getByIdsEager(List<Integer> ids) {
		return odtDAO.getByIdsEager(ids);
	}
	
	public OrdenDeTrabajo getByCodigoEager(String codigo) {
		return odtDAO.getODTEagerByCodigo(codigo);
	}

	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta) {
		return odtDAO.getOrdenesDeTrabajo(fechaDesde,fechaHasta,null, estado);
	}

	public List<EstadoActualMaquinaTO> getEstadoMaquinas(Integer idTipoMaquina, Date fechaDesde, Date fechaHasta, Cliente cliente) {
		List<Maquina> maquinas = maquinaDao.getAllByIdTipoMaquina(idTipoMaquina);
		Map<Integer, EstadoActualMaquinaTO> mapa = new LinkedHashMap<Integer, EstadoActualMaquinaTO>();
		for(Maquina m : maquinas){
			mapa.put(m.getId(), new EstadoActualMaquinaTO(m));
		}
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, idTipoMaquina, null, false);
		for(ODTTO odt : allODTSEnProceso){
			Integer m = odt.getMaquinaActual();
			EstadoActualMaquinaTO estadoActualoMaquinaTO = mapa.get(m);
			estadoActualoMaquinaTO.getOdtsPorEstado().get(odt.getAvance()).add(odt);
			mapa.put(m, estadoActualoMaquinaTO);
		}
		return new ArrayList<EstadoActualMaquinaTO>(mapa.values());
	}

	public EstadoGeneralODTsTO getEstadoDeProduccionActual(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		EstadoGeneralODTsTO estadoGeneral = new EstadoGeneralODTsTO();
		List<ODTTO> odtsPendientesTO = getOdtsPendientes(fechaDesde,fechaHasta,cliente);
		List<EstadoActualTipoMaquinaTO> estados = getEstadoMaquinas(fechaDesde,fechaHasta,cliente);
		estadoGeneral.setOdtsDisponibles(odtsPendientesTO);
		estadoGeneral.setEstadoMaquinas(estados);
		return estadoGeneral;
	}

	private List<EstadoActualTipoMaquinaTO> getEstadoMaquinas(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		List<TipoMaquina> maquinas = tipoMaquinaDAO.getAllOrderBy("orden");
		Map<TipoMaquina, EstadoActualTipoMaquinaTO> mapa = new LinkedHashMap<TipoMaquina, EstadoActualTipoMaquinaTO>();
		for(TipoMaquina tm : maquinas){
			mapa.put(tm, new EstadoActualTipoMaquinaTO(tm));
		}
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, null, null, false);
		for(ODTTO odt : allODTSEnProceso){
			if(odt.getMaquinaActual() != null && odt.getTipoProducto() != null && odt.getTipoProducto() != ETipoProducto.DEVOLUCION && odt.getTipoProducto() != ETipoProducto.REPROCESO_SIN_CARGO) {
				TipoMaquina tm = odt.getTipoMaquina();
				EstadoActualTipoMaquinaTO estadoActualTipoMaquinaTO = mapa.get(tm);
				if(estadoActualTipoMaquinaTO.getOdtsPorEstado().get(odt.getAvance()) != null) {
					estadoActualTipoMaquinaTO.getOdtsPorEstado().get(odt.getAvance()).add(odt);
					mapa.put(tm,estadoActualTipoMaquinaTO);
				}
			}
		}
		return new ArrayList<EstadoActualTipoMaquinaTO>(mapa.values());
	}

	private List<ODTTO> getOdtsPendientes(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, null, null, false, EEstadoODT.PENDIENTE, EEstadoODT.COMPLETA, EEstadoODT.IMPRESA, EEstadoODT.DETENIDA);
		List<ODTTO> odtsPendientesTO = new ArrayList<ODTTO>();
		if(allODTSEnProceso!=null && !allODTSEnProceso.isEmpty()){
			for(ODTTO odt : allODTSEnProceso){
				if(odt.getTipoProducto() != null && odt.getTipoProducto() != ETipoProducto.DEVOLUCION && odt.getTipoProducto() != ETipoProducto.REPROCESO_SIN_CARGO) {
					odtsPendientesTO.add(odt);
				}
			}
		}
		return odtsPendientesTO;
	}

	public void grabarAndRegistrarAvanceEnEstadoEnProceso(Integer idODT, Maquina maquina, ESectorMaquina sector, Terminal terminal, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idODT);
	
		Timestamp ahora = DateUtil.getAhora();
		TransicionODT transicion = new TransicionODT();
		transicion.setFechaHoraRegistro(ahora);
		transicion.setMaquina(maquina);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(maquina.getTipoMaquina());
		transicion.setTerminal(terminal);
		transicion.setUsuarioSistema(usuarioSistema);
		odt.resetFechasProcesamiento();

		CambioAvance ca = new CambioAvance();
		ca.setFechaHora(ahora);
		ca.setAvance(EAvanceODT.POR_COMENZAR);
		ca.setTerminal(terminal);
		ca.setUsuario(usuarioSistema);
		transicion.getCambiosAvance().add(ca);

		if(sector.isAdmiteInterProcesamiento()) {
			odt.setAvance(EAvanceODT.POR_COMENZAR);
			odt.setFechaPorComenzarUltSector(ahora);
			odt.setOrdenEnMaquina((short)(odtDAO.getUltimoOrdenMaquina(maquina) + 1));
			odt.setEstadoODT(EEstadoODT.EN_PROCESO);
		} else {
			ca = new CambioAvance();
			ca.setFechaHora(ahora);
			ca.setAvance(EAvanceODT.FINALIZADO);
			ca.setTerminal(terminal);
			ca.setUsuario(usuarioSistema);
			transicion.getCambiosAvance().add(ca);
			odt.setAvance(EAvanceODT.FINALIZADO);
			odt.setOrdenEnMaquina(null);
			odt.setFechaPorComenzarUltSector(ahora);
			odt.setFechaEnProcesoUltSector(ahora);
			odt.setFechaFinalizadoUltSector(ahora);
		}
		odt.setMaquinaActual(maquina);
		if(odt.getEstado() == EEstadoODT.PENDIENTE) {
			odt.setEstadoODT(EEstadoODT.EN_PROCESO);//al tener máquina debe quedar en estado EN_PROCESO
		}
		
		//persist
		odtDAO.save(odt);
		transicionDao.save(transicion);
	}

	public void detenerODT(Integer idOdt, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idOdt);
		odt.setMaquinaActual(null);
		odt.setOrdenEnMaquina(null);
		odt.setEstadoODT(EEstadoODT.DETENIDA);
		odt = odtDAO.save(odt);
		
		TransicionODT transicion = new TransicionODT();
		transicion.setFechaHoraRegistro(DateUtil.getAhora());
		transicion.setMaquina(null);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(null);
		transicion.setUsuarioSistema(usuarioSistema);
		
		transicionDao.save(transicion);
	}

	public void cambiarODTAFacturada(Integer idOdt, UsuarioSistema usuarioSistema){
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idOdt);
		//chequeo que todas las piezas de la ODT tengan salida, si existe una sin salida => salgo
		for(PiezaODT pODT : odt.getPiezas()) {
			if(pODT.getPiezasSalida().isEmpty()) {
				return;
			}
		}
		
		odt.setMaquinaActual(null);
		odt.setEstadoODT(EEstadoODT.FACTURADA);
		odt.setAvance(null);
		odt.setOrdenEnMaquina(null);
		odt = odtDAO.save(odt);
	}

	public List<TransicionODT> getHistoricoTransiciones(Integer idODT) {
		return transicionDao.getAllByODT(idODT);
	}

	public CambioAvance actualizarObservacionesCambioAvance(Integer idCambioAvance, String observaciones) {
		CambioAvance ca = cambioAvanceDAO.getById(idCambioAvance);
		ca.setObservaciones(observaciones);
		return cambioAvanceDAO.save(ca);
	}

	public void bajarODT(ODTTO odtto, ESectorMaquina sectorFrom) throws ValidacionException {
		OrdenDeTrabajo odtABajar = odtDAO.getReferenceById(odtto.getId());
		checkDirtyODT(odtABajar, sectorFrom);
		OrdenDeTrabajo odtASubir = odtDAO.getByMaquinaYOrden(odtto.getMaquinaActual(),(short)(odtto.getOrdenEnMaquina()+1));
		checkDirtyODT(odtASubir, sectorFrom);
		Short ordenActual = odtABajar.getOrdenEnMaquina();
		odtABajar.setOrdenEnMaquina((short)(ordenActual + 1));
		odtASubir.setOrdenEnMaquina(ordenActual);
		odtDAO.updateODT(odtABajar);
		odtDAO.updateODT(odtASubir);
	}

	public void subirODT(ODTTO odtto, ESectorMaquina sectorFrom) throws ValidacionException {
		OrdenDeTrabajo odtASubir = odtDAO.getReferenceById(odtto.getId());
		checkDirtyODT(odtASubir, sectorFrom);
		OrdenDeTrabajo odtABajar = odtDAO.getByMaquinaYOrden(odtto.getMaquinaActual(),(short)(odtto.getOrdenEnMaquina()-1));
		checkDirtyODT(odtABajar, sectorFrom);
		Short ordenActual = odtASubir.getOrdenEnMaquina();
		odtABajar.setOrdenEnMaquina(ordenActual);
		odtASubir.setOrdenEnMaquina((short)(ordenActual -1));
		odtDAO.updateODT(odtABajar);
		odtDAO.updateODT(odtASubir);
	}

	private void checkDirtyODT(OrdenDeTrabajo odt, ESectorMaquina sectorFrom) throws ValidacionException {
		if(odt==null || odt.getAvance() != EAvanceODT.POR_COMENZAR || odt.getMaquinaActual() == null || odt.getMaquinaActual().getSector() != sectorFrom) {
			throw new ValidacionException(EValidacionException.ODT_DIRTY_DATA.getInfoValidacion());
		}
	}

	public OrdenDeTrabajo grabarODT(OrdenDeTrabajo odt, UsuarioSistema usuario) {
		OrdenDeTrabajo odtRet = odtDAO.save(odt);
		auditoriaFacade.auditar(usuario.getUsrName(), "Creacion/Edición secuencia " + odt.getCodigo(), EnumTipoEvento.ALTA, odt);
		return odtRet;
	}

	public void borrarSecuencia(OrdenDeTrabajo ordenDeTrabajo, UsuarioSistema usuarioSistema) throws ValidacionException {
		//odtDAO.borrarSecuencia(ordenDeTrabajo);
		if(ordenDeTrabajo.getEstado() == EEstadoODT.ANTERIOR || ordenDeTrabajo.getEstado() == EEstadoODT.IMPRESA || ordenDeTrabajo.getEstado() == EEstadoODT.PENDIENTE ){
			movimientoStockFacade.borrarMovientosStockODT(ordenDeTrabajo.getId());
			Integer idSecuencia = ordenDeTrabajo.getSecuenciaDeTrabajo().getId();
			ordenDeTrabajo.setSecuenciaDeTrabajo(null);
			ordenDeTrabajo.setEstadoODT(EEstadoODT.PENDIENTE);
			odtDAO.save(ordenDeTrabajo);
			odtDAO.flush();
			secuenciaDao.removeById(idSecuencia);
			auditoriaFacade.auditar(usuarioSistema.getUsrName(), "Borrado secuencia " + ordenDeTrabajo.getCodigo(), EnumTipoEvento.BAJA, ordenDeTrabajo);
		}else{
			throw new ValidacionException(EValidacionException.ODT_NO_SE_PUEDE_BORRAR_SECUENCIA.getInfoValidacion(),Arrays.asList(ordenDeTrabajo.getEstado().getDescripcion().toUpperCase()));
		}
	}

	public OrdenDeTrabajo grabarODTYDescontarStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odtRet = odtDAO.save(odt);
		auditoriaFacade.auditar(usuarioSistema.getUsrName(), "Creacion secuencia y descuento de stock " + odt.getCodigo(), EnumTipoEvento.ALTA, odt);
		for(InfoBajaStock info : infoStock){
			movimientoStockFacade.crearMovimientoResta(odt, info.getPrecioMateriaPrima(), info.getCantidadADescontar());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(new BigDecimal(info.getCantidadADescontar().doubleValue()).multiply(new BigDecimal(-1d)), info.getPrecioMateriaPrima().getId());
		}
		return odtRet;
	}
	
	public OrdenDeTrabajo actualizarODTYStock(OrdenDeTrabajo odt, Set<InfoBajaStock> infoStock, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odtRet = odtDAO.save(odt);
		auditoriaFacade.auditar(usuarioSistema.getUsrName(), "Edicion secuencia y de stock " + odt.getCodigo(), EnumTipoEvento.ALTA, odt);
		movimientoStockFacade.borrarMovientosStockODT(odtRet.getId());
		for(InfoBajaStock info : infoStock){
			movimientoStockFacade.crearMovimientoResta(odt, info.getPrecioMateriaPrima(), info.getCantidadADescontar());
			precioMateriaPrimaFacade.actualizarStockPrecioMateriaPrima(new BigDecimal(info.getCantidadADescontar().doubleValue()).multiply(new BigDecimal(-1d)), info.getPrecioMateriaPrima().getId());
		}
		return odtRet;
	}

	public OrdenDeTrabajo getODTEagerByCodigo(String codigo) {
		return odtDAO.getODTEagerByCodigo(codigo);
	}

	public OrdenDeTrabajo grabarAndRegistrarCambioEstadoAndAvance(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance, Terminal terminal, UsuarioSistema usuarioSistema) {
		Maquina maquinaActual = maquinaDao.getById(odt.getMaquinaActual().getId());
		checkConsistenciaCambioEstadoAndAvance(odt, estado, avance);
		checkConsistenciaEstadoFinalizado(odt, maquinaActual, estado, avance);
		Timestamp ahora = DateUtil.getAhora();

		if(odt.getEstado() != estado || avance.ordinal() != odt.getAvance().ordinal()) {
			//cambio de avance
			CambioAvance ca = new CambioAvance();
			ca.setFechaHora(ahora);
			ca.setAvance(avance);
			ca.setUsuario(usuarioSistema);
			ca.setTerminal(terminal);

			TransicionODT transicion = transicionDao.getByODT(odt.getId());
			if(transicion == null || transicion.getTipoMaquina().getSectorMaquina() != maquinaActual.getTipoMaquina().getSectorMaquina()) {
				transicion = new TransicionODT();
				transicion.setFechaHoraRegistro(ahora);
				transicion.setMaquina(maquinaActual);
				transicion.setOdt(odt);
				transicion.setTipoMaquina(maquinaActual.getTipoMaquina());
				transicion.setUsuarioSistema(usuarioSistema);
				transicion.setTerminal(terminal);
				odt.resetFechasProcesamiento();
				odt.setFechaPorComenzarUltSector(ahora);
				
			}
			transicion.getCambiosAvance().add(ca);
			transicionDao.save(transicion);
			
			if(avance == EAvanceODT.FINALIZADO) {
				odt.setOrdenEnMaquina(null); //queda en FINALIZADO => no tiene que tener orden en máquina
				if(maquinaActual != null) { //actualizo los ordenes de las ODTs que quedaron en la misma máquina
					actualizarOrdenesMismaMaquina(maquinaActual, odt, (short)1);
				}
				odt.setFechaFinalizadoUltSector(ahora);
			} else if(avance == EAvanceODT.POR_COMENZAR && odt.getOrdenEnMaquina() == null) { //avance==POR_COMENZAR y sin orden seteado => la pongo al final
				odt.setOrdenEnMaquina((short)(odtDAO.getUltimoOrdenMaquina(maquinaActual)+1));
			} else if(avance == EAvanceODT.EN_PROCESO) { //avance==EN_PROCESO => la pongo al principio
				odt.setOrdenEnMaquina((short)1);
				actualizarOrdenesMismaMaquina(maquinaActual, odt, (short)2);
				odt.setFechaEnProcesoUltSector(ahora);
			}
			
			//estados en la ODT
			odt.setEstadoODT(estado);
			odt.setAvance(avance);
			
			if(estado == EEstadoODT.EN_OFICINA && avance == EAvanceODT.FINALIZADO) {
				notificacionesFacadeFacade.generarNotificaciones(ETipoNotificacion.ODT_EN_OFICINA, odt.getId(), odt.getCodigo());
			}
		}

		OrdenDeTrabajo odtSaved = odtDAO.save(odt);
		return odtDAO.getByIdEager(odtSaved.getId());
	}

	public List<OrdenDeTrabajo> getAllNoFinalizadasBySector(ESectorMaquina sector) {
		return odtDAO.getAllNoFinalizadasBySector(sector);
	}
	
	private void actualizarOrdenesMismaMaquina(Maquina maquinaActual, OrdenDeTrabajo odtExcluir, Short from) {
		List<OrdenDeTrabajo> odts = odtDAO.getODTsEnMaquina(maquinaActual);
		short orden = from;
		for(OrdenDeTrabajo odt : odts) {
			if(!odt.equals(odtExcluir)) {
				odt.setOrdenEnMaquina(orden);
				orden++;
			}
		}
		odts.remove(odtExcluir);
		odtDAO.save(odts);
	}

	private void checkConsistenciaEstadoFinalizado(OrdenDeTrabajo odt, Maquina maquinaActual, EEstadoODT estado, EAvanceODT avance) {
		if(avance == EAvanceODT.FINALIZADO) {
			if(estado == EEstadoODT.EN_PROCESO && maquinaActual.getSector() == ESectorMaquina.SECTOR_COSIDO) {//sector cosido, que todas las piezas tengan orden
				for(PiezaODT p : odt.getPiezas()) {
					if(p.getOrden()==null || p.getOrden() <=0) {
						throw new IllegalArgumentException("La ODT " + odt + " no puede quedar en estado " + EEstadoODT.EN_PROCESO + "- " + EAvanceODT.FINALIZADO + " porque tiene piezas sin orden");
					}
				}
			}
			if(estado == EEstadoODT.EN_OFICINA && maquinaActual.getSector() == ESectorMaquina.SECTOR_TERMINADO) {//sector terminado, que todas las piezas tengan metros
				for(PiezaODT p : odt.getPiezas()) {
					if(p.getMetros()==null || p.getMetros().floatValue() <=0f) {
						throw new IllegalArgumentException("La ODT " + odt + " no puede quedar en estado " + EEstadoODT.EN_OFICINA + "- " + EAvanceODT.FINALIZADO + " porque tiene piezas sin metros");
					}
				}
			}
		}
	}

	private void checkConsistenciaCambioEstadoAndAvance(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance) {
		if(odt.getEstado() != null && odt.getEstado().ordinal() > estado.ordinal()) {
			throw new IllegalArgumentException("La ODT tiene estado " + odt.getEstado()  + " y se está intentando cambiarla a estado (menor) " + estado + ".");
		}
//		if(odt.getEstado() != null && odt.getEstado() == estado && odt.getAvance() != null && odt.getAvance().ordinal() > avance.ordinal()) {
//			throw new IllegalArgumentException("La ODT con estado " + odt.getEstado()  + " se está intentando avanzarla de " + odt.getAvance() + " a estado (menor) " + avance + ".");
//		}
	}

	public List<OrdenDeTrabajo> getOrdenesDeTrabajoSinSalida(Date fechaDesde, Date fechaHasta) {
		return odtDAO.getOrdenesDeTrabajoSinSalida(fechaDesde, fechaHasta);
	}

	@Override
	public PiezaODT getPiezaODTByCodigo(String codPiezaODT) {
		String codODT = codPiezaODT.substring(0, 8);//cod odt
		Integer nroPieza = Integer.valueOf(codPiezaODT.substring(8, 10));
		Integer nroSubPieza = Integer.valueOf(codPiezaODT.substring(10, codPiezaODT.length()));
		if(nroSubPieza == 0) {//no hay subpieza => todo el string es un número de pieza
			nroSubPieza = null;
		}
		return piezaODTDAO.getByParams(codODT, nroPieza, nroSubPieza);
	}

	public InfoAsignacionMaquinaTO getMaquinaAndProximoOrdenBySector(ESectorMaquina sector) {
		List<Maquina> maquinas = maquinaDao.getAllBySector(sector);
		if(maquinas.isEmpty()) {
			throw new IllegalArgumentException("No existen máquinas cargadas para el sector " + sector + ". Por favor, cargue una y reintente la operación.");
		}
		Maquina maquina = maquinas.get(0); // tomo la primera 
		int proximoOrden = odtDAO.getUltimoOrdenMaquina(maquina)+1;
		return new InfoAsignacionMaquinaTO(maquina, (short)proximoOrden);
	}

	@Override
	public List<ODTTO> getAllODTTOByParams(Date fechaDesde, Date fechaHasta, Cliente cliente, Integer idTipoMaquina,Integer idProducto, boolean conProductoParcial, EEstadoODT... estado) {
		return odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, idTipoMaquina, idProducto, conProductoParcial, estado);
	}

	@Override
	public OrdenDeTrabajo borrarPiezasSinSalida(OrdenDeTrabajo odt, UsuarioSistema usuarioSistema) {
		//chequeos
		checkBorradoPiezasSinSalida(odt);
		//borro las piezas que no tuvieron salida
		List<PiezaODT> borrarPiezasList = new ArrayList<PiezaODT>();
		for(PiezaODT p : odt.getPiezas()) {
			if(!p.tieneSalida()) {
				borrarPiezasList.add(p);
			}
		}
		odt.getPiezas().removeAll(borrarPiezasList);
		//cambio a estado facturada
		odt.setEstadoODT(EEstadoODT.FACTURADA);
		auditoriaFacade.auditar(usuarioSistema.getUsrName(), "ELIM PIEZAS SIN SALIDA ODT " + odt.getCodigo() + " #PIEZAS: " + borrarPiezasList.size(), EnumTipoEvento.MODIFICACION, odt);
		return odtDAO.save(odt);
	}

	private void checkBorradoPiezasSinSalida(OrdenDeTrabajo odt) {
		EEstadoODT eODT = odt.getEstado();
		if(eODT != EEstadoODT.EN_OFICINA && eODT != EEstadoODT.ANTERIOR && eODT != EEstadoODT.EN_PROCESO &&  eODT != EEstadoODT.FACTURADA) {
			throw new IllegalArgumentException("La ODT debe estar en estado " + EEstadoODT.EN_OFICINA + ", " + EEstadoODT.ANTERIOR + ", " + EEstadoODT.EN_PROCESO + " o bien " + EEstadoODT.FACTURADA);
		}
		int cantPiezasConSalida = odt.contarDeAcuerdoASalida(true);
		if(cantPiezasConSalida == 0) {
			throw new IllegalArgumentException("Para ejecutar esta operación alguna pieza de la ODT tuvo que haber tenido salida.");
		}
		int cantPiezasSinSalida = odt.contarDeAcuerdoASalida(false);
		if(cantPiezasSinSalida == 0) {
			throw new IllegalArgumentException("Para ejecutar esta operación la ODT tiene que tener al menos una pieza SIN salida.");
		}
	}

	public OrdenDeTrabajo asignarProductoArticuloODT(OrdenDeTrabajo odt, ProductoArticulo productoArticulo, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odtDB = odtDAO.getById(odt.getId());
		checkAsignarProductoArticuloODT(odtDB);
		//limpio el PA parcial
		if(odtDB.getProductoParcial() != null) {
			odtDB.getProductoParcial().setArticulo(null);
			odtDB.getProductoParcial().setProducto(null);
		}
		//actualizo el PA y sit ODT en el RE
		RemitoEntrada re = remitoDAO.getById(odtDB.getRemito().getId());
		if(!re.getProductoArticuloList().contains(productoArticulo)) {
			re.getProductoArticuloList().add(productoArticulo);
			remitoDAO.save(re);
		}
		re.setSituacion(ESituacionODTRE.CON_ODT);
		//seteo el PA
		odtDB.setProductoArticulo(productoArticulo);
		auditoriaFacade.auditar(usuarioSistema.getUsrName(), "ASIG. PROD. ART. ODT " + odt.getCodigo() + " PA: " + productoArticulo.getId(), EnumTipoEvento.MODIFICACION, odt);
		return odtDAO.save(odtDB);
	}

	private void checkAsignarProductoArticuloODT(OrdenDeTrabajo odt) {
		if(odt.getProductoArticulo() != null) {
			throw new IllegalArgumentException("La ODT ya tenía un producto articulo seteado: " + odt.getProductoArticulo());
		}
	}

}