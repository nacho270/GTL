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

import ar.clarin.fwjava.auditoria.evento.enumeradores.EnumTipoEvento;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.dao.api.local.ParametrosGeneralesDAOLocal;
import ar.com.textillevel.dao.api.local.PiezaRemitoDAOLocal;
import ar.com.textillevel.entidades.config.ParametrosGenerales;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.facade.api.local.MovimientoStockFacadeLocal;
import ar.com.textillevel.facade.api.local.PrecioMateriaPrimaFacadeLocal;
import ar.com.textillevel.facade.api.remote.AuditoriaFacadeLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.CambioAvanceDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.MaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.OrdenDeTrabajoDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.SecuenciaODTDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TipoMaquinaDAOLocal;
import ar.com.textillevel.modulos.odt.dao.api.local.TransicionODTDAOLocal;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.entidades.maquinas.Maquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.workflow.CambioAvance;
import ar.com.textillevel.modulos.odt.entidades.workflow.TransicionODT;
import ar.com.textillevel.modulos.odt.enums.EAvanceODT;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.local.OrdenDeTrabajoFacadeLocal;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.EstadoActualMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoActualTipoMaquinaTO;
import ar.com.textillevel.modulos.odt.to.EstadoGeneralODTsTO;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.modulos.odt.to.stock.InfoBajaStock;
import ar.com.textillevel.util.ODTCodigoHelper;

@Stateless
public class OrdenDeTrabajoFacade implements OrdenDeTrabajoFacadeRemote,OrdenDeTrabajoFacadeLocal {

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

	public OrdenDeTrabajo getByIdEager(Integer idODT) {
		return odtDAO.getByIdEager(idODT);
	}

	public OrdenDeTrabajo getByCodigoEager(String codigo) {
		throw new RuntimeException("IMPLEMENTAR");
	}

	public List<OrdenDeTrabajo> getOrdenesDeTrabajo(EEstadoODT estado, Date fechaDesde, Date fechaHasta) {
		return odtDAO.getOrdenesDeTrabajo(estado,fechaDesde,fechaHasta,null);
	}

	public List<EstadoActualMaquinaTO> getEstadoMaquinas(Integer idTipoMaquina, Date fechaDesde, Date fechaHasta, Cliente cliente) {
		List<Maquina> maquinas = maquinaDao.getAllByIdTipoMaquina(idTipoMaquina);
		Map<Maquina, EstadoActualMaquinaTO> mapa = new LinkedHashMap<Maquina, EstadoActualMaquinaTO>();
		for(Maquina m : maquinas){
			mapa.put(m, new EstadoActualMaquinaTO(m));
		}
		
		List<OrdenDeTrabajo> allODTSEnProceso = odtDAO.getAllEnProcesoByTipoMaquina(fechaDesde,fechaHasta,cliente,idTipoMaquina);
		for(OrdenDeTrabajo odt : allODTSEnProceso){
			Maquina m = odt.getMaquinaActual();
			EstadoActualMaquinaTO estadoActualoMaquinaTO = mapa.get(m);
			estadoActualoMaquinaTO.getOdtsPorEstado().get(odt.getAvance()).add(new ODTTO(odt));
			mapa.put(m,estadoActualoMaquinaTO);
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
		
		List<OrdenDeTrabajo> allODTSEnProceso = odtDAO.getAllEnProceso(fechaDesde,fechaHasta,cliente);
		
		for(OrdenDeTrabajo odt : allODTSEnProceso){
			TipoMaquina tm = odt.getMaquinaActual().getTipoMaquina();
			EstadoActualTipoMaquinaTO estadoActualTipoMaquinaTO = mapa.get(tm);
			estadoActualTipoMaquinaTO.getOdtsPorEstado().get(odt.getAvance()).add(new ODTTO(odt));
			mapa.put(tm,estadoActualTipoMaquinaTO);
		}
		
		return new ArrayList<EstadoActualTipoMaquinaTO>(mapa.values());
	}

	private List<ODTTO> getOdtsPendientes(Date fechaDesde, Date fechaHasta, Cliente cliente) {
		List<OrdenDeTrabajo> odtsPendientes = odtDAO.getOrdenesDeTrabajo(EEstadoODT.IMPRESA,fechaDesde,fechaHasta,cliente);
		odtsPendientes.addAll(odtDAO.getOrdenesDeTrabajo(EEstadoODT.DETENIDA,fechaDesde,fechaHasta,cliente));
		List<ODTTO> odtsPendientesTO = new ArrayList<ODTTO>();
		if(odtsPendientes!=null && !odtsPendientes.isEmpty()){
			for(OrdenDeTrabajo odt : odtsPendientes){
				odtsPendientesTO.add(new ODTTO(odt));
			}
		}
		return odtsPendientesTO;
	}

	public void registrarAvanceODT(Integer idOdt, EAvanceODT avance,boolean oficina, UsuarioSistema usuarioSistema) {
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idOdt);
		odt.setAvance(avance);
		odt.setEstadoODT(oficina?EEstadoODT.EN_OFICINA:EEstadoODT.EN_PROCESO);
		odtDAO.save(odt);
		
		CambioAvance ca = new CambioAvance();
		ca.setFechaHora(DateUtil.getAhora());
		ca.setAvance(avance);
		ca.setUsuario(usuarioSistema);
		
		TransicionODT tr = transicionDao.getByODT(idOdt);
		tr.getCambiosAvance().add(ca);
		transicionDao.save(tr);	
	}

	public void registrarTransicionODT(Integer idOdt, Maquina maquina, UsuarioSistema usuarioSistema) {
		Short ultimoOrdenODT = odtDAO.getUltimoOrdenMaquina(maquina);
		if(ultimoOrdenODT == null){
			ultimoOrdenODT = 0;
		}
		ultimoOrdenODT++;
		OrdenDeTrabajo odt = odtDAO.getReferenceById(idOdt);
		odt.setMaquinaActual(maquina);
		odt.setOrdenEnMaquina(ultimoOrdenODT);
		odt.setAvance(EAvanceODT.POR_COMENZAR);
		odt.setEstadoODT(EEstadoODT.EN_PROCESO);
		odt = odtDAO.save(odt);
		
		CambioAvance ca = new CambioAvance();
		ca.setFechaHora(DateUtil.getAhora());
		ca.setAvance(EAvanceODT.POR_COMENZAR);
		ca.setUsuario(usuarioSistema);
		
		TransicionODT transicion = new TransicionODT();
		transicion.setFechaHoraRegistro(DateUtil.getAhora());
		transicion.setMaquina(maquina);
		transicion.setOdt(odt);
		transicion.setTipoMaquina(maquina.getTipoMaquina());
		transicion.setUsuarioSistema(usuarioSistema);
		transicion.getCambiosAvance().add(ca);
		
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
}