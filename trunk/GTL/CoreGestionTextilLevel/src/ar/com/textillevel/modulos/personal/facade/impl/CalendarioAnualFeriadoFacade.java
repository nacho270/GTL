package ar.com.textillevel.modulos.personal.facade.impl;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.excepciones.EValidacionException;
import ar.com.textillevel.modulos.personal.dao.api.CalendarioAnualFeriadoDAOLocal;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.ConfigFormaPagoSindicato;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.RangoDiasFeriado;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;

@Stateless
public class CalendarioAnualFeriadoFacade implements CalendarioAnualFeriadoFacadeRemote {

	@EJB
	private CalendarioAnualFeriadoDAOLocal calendarioAnualFeriadoDAO;

	public List<CalendarioAnualFeriado> getAll() {
		return calendarioAnualFeriadoDAO.getAll();
	}

	public CalendarioAnualFeriado getByIdEager(Integer id) {
		CalendarioAnualFeriado calendario = calendarioAnualFeriadoDAO.getById(id);
		if(calendario!=null){
			for(ConfigFormaPagoSindicato config : calendario.getConfigsFormasPagoSindicatos()) {
				config.getTotalHorasPagoPorDias().size();
			}
			for(RangoDiasFeriado f : calendario.getFeriados()) {
				for(ConfigFormaPagoSindicato config2 : f.getConfigsFormasPagoSindicatos()) {
					config2.getTotalHorasPagoPorDias().size();
				}
			}
		}
		return calendario;
	}
	
	public CalendarioAnualFeriado getByAnioEager(Integer anio) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("anio", anio);
		List<CalendarioAnualFeriado> calendariosAnio = calendarioAnualFeriadoDAO.getBy(params);
		if(calendariosAnio!=null && !calendariosAnio.isEmpty()){
			CalendarioAnualFeriado calendario = calendariosAnio.get(0);
			if(calendario!=null){
				for(ConfigFormaPagoSindicato config : calendario.getConfigsFormasPagoSindicatos()) {
					config.getTotalHorasPagoPorDias().size();
				}
				for(RangoDiasFeriado f : calendario.getFeriados()) {
					for(ConfigFormaPagoSindicato config2 : f.getConfigsFormasPagoSindicatos()) {
						config2.getTotalHorasPagoPorDias().size();
					}
				}
			}
			return calendario;
		}
		return null;
	}

	public CalendarioAnualFeriado save(CalendarioAnualFeriado calendario) throws ValidacionException {
		checkConsistenciaCalendario(calendario);
		return calendarioAnualFeriadoDAO.save(calendario);
	}

	private void checkConsistenciaCalendario(CalendarioAnualFeriado calendario) throws ValidacionException {
		
		for(RangoDiasFeriado f : calendario.getFeriados()) {
			int anioD = DateUtil.getAnio(f.getDesde());
			int anioH = DateUtil.getAnio(f.getHasta());
			if(anioD != anioH || anioD != calendario.getAnio()) {
				throw new ValidacionException(EValidacionException.CALENDARIO_LABORAL_INVALIDO_MAL_LOS_ANIOS.getInfoValidacion());
			}
		}

		Collections.sort(calendario.getFeriados(), new Comparator<RangoDiasFeriado>() {

			public int compare(RangoDiasFeriado o1, RangoDiasFeriado o2) {
				return o1.getDesde().compareTo(o2.getDesde());
			}

		});
		
		for(int i = 0; i < calendario.getFeriados().size(); i++) {
			RangoDiasFeriado rangoI = calendario.getFeriados().get(i);
			for(int j = i + 1 ; j < calendario.getFeriados().size(); j ++) {
				RangoDiasFeriado rangoJ = calendario.getFeriados().get(j);
				if(rangoI.seSolapaCon(rangoJ)) {
					throw new ValidacionException(EValidacionException.CALENDARIO_LABORAL_INVALIDO_FERIADOS_SOLAPADOS.getInfoValidacion(), new String[] { rangoI.toString()  + " / "  + rangoJ.toString()});
				}
			}
		}
		
	}

	public void eliminarCalendario(CalendarioAnualFeriado calendario) {
		calendarioAnualFeriadoDAO.removeById(calendario.getAnio());
	}

	public CalendarioAnualFeriado createCalendario(CalendarioAnualFeriado calendario) throws ValidacionException {
		checkExistenciaCalendario(calendario);
		calendarioAnualFeriadoDAO.persist(calendario);
		return calendario;
	}

	private void checkExistenciaCalendario(CalendarioAnualFeriado calendario) throws ValidacionException {
		boolean existe = calendarioAnualFeriadoDAO.existeCalendario(calendario.getAnio());
		if(existe) {
			throw new ValidacionException(EValidacionException.CALENDARIO_LABORAL_YA_EXISTE_ANIO.getInfoValidacion());
		}
	}

	public CalendarioAnualFeriado copiarCalendario(CalendarioAnualFeriado calendario, CalendarioAnualFeriado calenEjemplo) throws ValidacionException {
		calenEjemplo = calendarioAnualFeriadoDAO.getById(calenEjemplo.getAnio());
		calendario.getFeriados().clear();
		for(RangoDiasFeriado f : calenEjemplo.getFeriados()) {
			calendario.getFeriados().add(clonarFeriado(calendario.getAnio(), f));
		}
		return createCalendario(calendario);
	}

	private RangoDiasFeriado clonarFeriado(Integer anio, RangoDiasFeriado f) {
		RangoDiasFeriado feriadoNuevo = new RangoDiasFeriado();
		feriadoNuevo.setDescripcion(f.getDescripcion());
		feriadoNuevo.setDesde(DateUtil.setAnio(f.getDesde(), anio));
		feriadoNuevo.setHasta(DateUtil.setAnio(f.getHasta(), anio));
		return feriadoNuevo;
	}

	public CalendarioAnualFeriado getCalendarioActual() {
		int anioActual = DateUtil.getAnio(DateUtil.getHoy());
		CalendarioAnualFeriado calendarioActual = getByIdEager(anioActual);
		return calendarioActual;
	}

	public Integer getCantidadDiasHabilesBetweenFechas(Date fechaDesde, Date fechaHasta) {
		Integer anio1 = DateUtil.getAnio(fechaDesde);
		Integer anio2 = DateUtil.getAnio(fechaHasta);
		Integer cantidadDiasHabiles = 0;
		if(anio1.equals(anio2)){
			CalendarioAnualFeriado calendario = getByAnioEager(anio1);
			Date indiceFechaActual = fechaDesde;
			Date hoy = DateUtil.getHoy();
			while (indiceFechaActual.compareTo(fechaHasta) <= 0 && indiceFechaActual.compareTo(hoy) <= 0) {
				int diaSemana = DateUtil.getDiaSemana(indiceFechaActual);
				if(diaSemana != 1){
					if(calendario.getRangoEnFecha(indiceFechaActual)==null){
						cantidadDiasHabiles++;
					}
				}
				indiceFechaActual = DateUtil.sumarDias(indiceFechaActual, 1);
			}
		}else{
			CalendarioAnualFeriado calendario = getByAnioEager(anio1);
			CalendarioAnualFeriado calendario2 = getByAnioEager(anio2);
			Date indiceFechaActual = fechaDesde;
			Date hoy = DateUtil.getHoy();
			while (indiceFechaActual.compareTo(fechaHasta) <= 0 && indiceFechaActual.compareTo(hoy) <= 0) {
				int diaSemana = DateUtil.getDiaSemana(indiceFechaActual);
				if(diaSemana != 1){
					if(calendario.getRangoEnFecha(indiceFechaActual)==null && calendario2.getRangoEnFecha(indiceFechaActual) == null){
						cantidadDiasHabiles++;
					}
				}
				indiceFechaActual = DateUtil.sumarDias(indiceFechaActual, 1);
			}
		}
		return cantidadDiasHabiles;
	}

}