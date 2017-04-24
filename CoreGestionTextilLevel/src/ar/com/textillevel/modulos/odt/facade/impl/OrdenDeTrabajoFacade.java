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
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.CambioAvanceDAOLocal;
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

	public List<OrdenDeTrabajo> getOdtNoAsociadasByClient(Integer idCliente) {
		return odtDAO.getOdtNoAsociadasByClient(idCliente);
	}

	public String getUltimoCodigoODT() {
		String ultimoCodigoODT = odtDAO.getUltimoCodigoODT();
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
		throw new RuntimeException("IMPLEMENTAR");
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
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, idTipoMaquina);
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
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, null);
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
		List<ODTTO> allODTSEnProceso = odtDAO.getAllODTTOByParams(fechaDesde, fechaHasta, cliente, null, EEstadoODT.PENDIENTE, EEstadoODT.COMPLETA, EEstadoODT.IMPRESA, EEstadoODT.DETENIDA);
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
	
		TransicionODT transicion = new TransicionODT();
		transicion.setFechaHoraRegistro(DateUtil.getAhora());
		transicion.setMaquina(maquina);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(maquina.getTipoMaquina());
		transicion.setTerminal(terminal);
		transicion.setUsuarioSistema(usuarioSistema);

		CambioAvance ca = new CambioAvance();
		ca.setFechaHora(DateUtil.getAhora());
		ca.setAvance(EAvanceODT.POR_COMENZAR);
		ca.setTerminal(terminal);
		ca.setUsuario(usuarioSistema);
		transicion.getCambiosAvance().add(ca);

		if(sector.isAdmiteInterProcesamiento()) {
			odt.setAvance(EAvanceODT.POR_COMENZAR);
			odt.setOrdenEnMaquina((short)(odtDAO.getUltimoOrdenMaquina(maquina) + 1));
			odt.setEstadoODT(EEstadoODT.EN_PROCESO);
		} else {
			ca = new CambioAvance();
			ca.setFechaHora(DateUtil.getAhora());
			ca.setAvance(EAvanceODT.FINALIZADO);
			ca.setTerminal(terminal);
			ca.setUsuario(usuarioSistema);
			transicion.getCambiosAvance().add(ca);
			odt.setAvance(EAvanceODT.FINALIZADO);
			odt.setOrdenEnMaquina(null);
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
		
		TransicionODT transicion = new TransicionODT();
		transicion.setFechaHoraRegistro(DateUtil.getAhora());
		transicion.setMaquina(null);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(null);
		transicion.setUsuarioSistema(usuarioSistema);
		
		transicionDao.save(transicion);
	}

	public void cambiarODTAOficina(Integer idOdt, UsuarioSistema usuarioSistema) {
		TipoMaquina tp = tipoMaquinaDAO.getTipoMaquinaConOrdenMayor();
		if(tp == null) {//si no hay tipos de máquinas configuradas salgo
			return;
		}
		List<Maquina> allByTipo = maquinaDao.getAllByTipo(tp);
		if(allByTipo.isEmpty()) {//si no hay máquinas configuradas salgo
			return;
		}
		Maquina maquina = allByTipo.get(0); //elijo la primer máquina

		OrdenDeTrabajo odt = odtDAO.getReferenceById(idOdt);
		odt.setMaquinaActual(maquina);
		odt.setEstadoODT(EEstadoODT.EN_OFICINA);
		odt.setAvance(EAvanceODT.FINALIZADO);
		odt.setOrdenEnMaquina((short)(odtDAO.getUltimoOrdenMaquina(maquina)+1));
		odt = odtDAO.save(odt);

		TransicionODT transicion = new TransicionODT();
		Timestamp ahora = DateUtil.getAhora();
		transicion.setFechaHoraRegistro(ahora);
		transicion.setMaquina(maquina);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(tp);
		transicion.setUsuarioSistema(usuarioSistema);

		CambioAvance ca = new CambioAvance();
		ca.setAvance(EAvanceODT.FINALIZADO);
		ca.setFechaHora(ahora);
		ca.setUsuario(usuarioSistema);
		transicion.getCambiosAvance().add(ca);

		transicionDao.save(transicion);
	}

	
	public List<TransicionODT> getHistoricoTransiciones(Integer idODT) {
		return transicionDao.getAllByODT(idODT);
	}

	public CambioAvance actualizarObservacionesCambioAvance(Integer idCambioAvance, String observaciones) {
		CambioAvance ca = cambioAvanceDAO.getById(idCambioAvance);
		ca.setObservaciones(observaciones);
		return cambioAvanceDAO.save(ca);
	}

	public void bajarODT(ODTTO odtto) {
		OrdenDeTrabajo odtABajar = odtDAO.getReferenceById(odtto.getId());
		OrdenDeTrabajo odtASubir = odtDAO.getByMaquinaYOrden(odtto.getMaquinaActual(),(short)(odtto.getOrdenEnMaquina()+1));
		Short ordenActual = odtABajar.getOrdenEnMaquina();
		odtABajar.setOrdenEnMaquina((short)( ordenActual + 1));
		odtASubir.setOrdenEnMaquina(ordenActual);
		odtDAO.updateODT(odtABajar);
		odtDAO.updateODT(odtASubir);
	}

	public void subirODT(ODTTO odtto) {
		OrdenDeTrabajo odtASubir = odtDAO.getReferenceById(odtto.getId());
		OrdenDeTrabajo odtABajar = odtDAO.getByMaquinaYOrden(odtto.getMaquinaActual(),(short)(odtto.getOrdenEnMaquina()-1));
		Short ordenActual = odtASubir.getOrdenEnMaquina();
		odtABajar.setOrdenEnMaquina(ordenActual);
		odtASubir.setOrdenEnMaquina((short)( ordenActual -1));
		odtDAO.updateODT(odtABajar);
		odtDAO.updateODT(odtASubir);
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

	public OrdenDeTrabajo grabarAndRegistrarCambioEstadoAndAvance(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance, UsuarioSistema usuarioSistema) {
		checkConsistenciaCambioEstadoAndAvance(odt, estado, avance);
		checkConsistenciaEstadoFinalizado(odt, estado, avance);

		Maquina maquinaActual = odt.getMaquinaActual();
		if(odt.getEstado() != estado || avance.ordinal() != odt.getAvance().ordinal()) {
			//cambio de avance
			CambioAvance ca = new CambioAvance();
			ca.setFechaHora(DateUtil.getAhora());
			ca.setAvance(avance);
			ca.setUsuario(usuarioSistema);

			TransicionODT transicion = transicionDao.getByODT(odt.getId());
			if(transicion == null || transicion.getTipoMaquina().getSectorMaquina() != maquinaActual.getTipoMaquina().getSectorMaquina()) {
				transicion = new TransicionODT();
				transicion.setFechaHoraRegistro(DateUtil.getAhora());
				transicion.setMaquina(maquinaActual);
				transicion.setOdt(odt);
				transicion.setTipoMaquina(maquinaActual.getTipoMaquina());
				transicion.setUsuarioSistema(usuarioSistema);
			}
			transicion.getCambiosAvance().add(ca);
			transicionDao.save(transicion);
			
			if(avance == EAvanceODT.FINALIZADO) {
				odt.setOrdenEnMaquina(null); //queda en FINALIZADO => no tiene que tener orden en máquina
				if(maquinaActual != null) { //actualizo los ordenes de las ODTs que quedaron en la misma máquina
					actualizarOrdenesMismaMaquina(maquinaActual, odt);
				}
			} else if(odt.getOrdenEnMaquina() == null) { //avance==POR_COMENZAR y sin orden => la pongo al final
				odt.setOrdenEnMaquina((short)(odtDAO.getUltimoOrdenMaquina(maquinaActual)+1));
			}
			
			//estados en la ODT
			odt.setEstadoODT(estado);
			odt.setAvance(avance);
		}

		OrdenDeTrabajo odtSaved = odtDAO.save(odt);
		return odtDAO.getByIdEager(odtSaved.getId());
	}

	private void actualizarOrdenesMismaMaquina(Maquina maquinaActual, OrdenDeTrabajo odtExcluir) {
		List<OrdenDeTrabajo> odts = odtDAO.getODTsEnMaquina(maquinaActual);
		short orden = 1;
		for(OrdenDeTrabajo odt : odts) {
			if(!odt.equals(odtExcluir)) {
				odt.setOrdenEnMaquina(orden);
				orden++;
			}
		}
		odts.remove(odtExcluir);
		odtDAO.save(odts);
	}

	private void checkConsistenciaEstadoFinalizado(OrdenDeTrabajo odt, EEstadoODT estado, EAvanceODT avance) {
		if(avance == EAvanceODT.FINALIZADO) {
			if(estado == EEstadoODT.EN_PROCESO) {//módulo cosido, que todas las piezas tengan orden
				for(PiezaODT p : odt.getPiezas()) {
					if(p.getOrden()==null || p.getOrden() <=0) {
						throw new IllegalArgumentException("La ODT " + odt + " no puede quedar en estado " + EEstadoODT.EN_PROCESO + "- " + EAvanceODT.FINALIZADO + " porque tiene piezas sin orden");
					}
				}
			}
			if(estado == EEstadoODT.EN_OFICINA) {//módulo asignación de metros, que todas las piezas tengan metros
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

}