package ar.com.textillevel.modulos.personal.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.RegistroVacacionesLegajo;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;

public class VacacionesHelper {
	
	/*
	 * Lo que se hace, es preguntar por menor. Si es < 5 -> 14. < 10 -> 21...para el ultimo hay que poner un 99 y va a devolver 35
	 */

	public static PeriodoVacaciones getPeriodoCorrespondiente(ConfiguracionVacaciones configuracion, Date fechaIngresoTrabajador){
		int difMeses = Math.abs(DateUtil.getDaysBetween(DateUtil.getHoy(), fechaIngresoTrabajador)/30);
		if(difMeses < configuracion.getMesesMinimosParaEntrar().intValue()){
			return null; //aca se calcula 1 día de descanso por cada 20 días de trabajo efectivo
		}
		int anios = difMeses/12;
		for(PeriodoVacaciones p : configuracion.getPeriodos()){
			if(anios < p.getAntiguedadAniosRequerida()){
				return p;
			}
		}
		return null;
	}
	
	public static PeriodoVacaciones getPeriodoCorrespondiente(ConfiguracionVacaciones configuracion, Date fechaIngresoTrabajador, Date fechaComparacion){
		int difMeses = Math.abs(DateUtil.getDaysBetween(fechaComparacion, fechaIngresoTrabajador)/30);
		if(difMeses < configuracion.getMesesMinimosParaEntrar().intValue()){
			return null; //aca se calcula 1 día de descanso por cada 20 días de trabajo efectivo
		}
		int anios = difMeses/12;
		for(PeriodoVacaciones p : configuracion.getPeriodos()){
			if(anios < p.getAntiguedadAniosRequerida()){
				return p;
			}
		}
		return null;
	}
	
	public static Map<Integer, List<RegistroVacacionesLegajo>> agruparPorAnio(List<RegistroVacacionesLegajo> registros){
		Map<Integer, List<RegistroVacacionesLegajo>> mapa = new HashMap<Integer, List<RegistroVacacionesLegajo>>();
		for(RegistroVacacionesLegajo r : registros){
			if(mapa.get(r.getAnioCorrespondiente())==null){
				mapa.put(r.getAnioCorrespondiente(), new ArrayList<RegistroVacacionesLegajo>());
			}
			mapa.get(r.getAnioCorrespondiente()).add(r);
		}
		return mapa;
	}
	
	public static Integer getDiasDisponiblesParaAnio(List<RegistroVacacionesLegajo> registros,Integer anio, ConfiguracionVacaciones confAnio, Date fechaIngresoTrabajador){
		Integer suma = 0;
		PeriodoVacaciones periodo = getPeriodoCorrespondiente(confAnio, fechaIngresoTrabajador);
		if(periodo == null){
			//TODO: MENOS DE 6 MESES
		}
		Map<Integer, List<RegistroVacacionesLegajo>> mapa = agruparPorAnio(registros);
		List<RegistroVacacionesLegajo> listaPeriodosAnio = mapa.get(anio);
		if(listaPeriodosAnio==null){
			suma += periodo.getCantidadDias();
			for(Integer i : mapa.keySet()){
				List<RegistroVacacionesLegajo> list = mapa.get(i);
				Collections.sort(list, new Comparator<RegistroVacacionesLegajo>() {
					public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
						return o1.getFechaDesde().compareTo(o2.getFechaDesde())*-1;
					}
				});
				suma += list.get(0).getDiasRemanentes();
			}
		}else{
			for(Integer i : mapa.keySet()){
				List<RegistroVacacionesLegajo> list = mapa.get(i);
				Collections.sort(list, new Comparator<RegistroVacacionesLegajo>() {
					public int compare(RegistroVacacionesLegajo o1, RegistroVacacionesLegajo o2) {
						return o1.getFechaDesde().compareTo(o2.getFechaDesde())*-1;
					}
				});
				suma += list.get(0).getDiasRemanentes();
			}
		}
		return suma;
	}
}
