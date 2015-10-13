package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.modulos.personal.entidades.fichadas.enums.EEstadoDiaFichada;
import ar.com.textillevel.modulos.personal.entidades.fichadas.to.FichadaLegajoTO;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.AnotacionReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.ETipoAnotacionReciboSueldo;

public class AnotacionesHelper {

	private static AnotacionesHelper instance = new AnotacionesHelper();

	private static final String SEPARADOR_TIPO_ANOTACIONES = " | ";
	private static final String SEPARADOR_ANOTACIONES = ", ";

	private AnotacionesHelper() {
	}

	public static AnotacionesHelper getInstance() {
		return instance;
	}

	public List<String> getAnotacionesList(ReciboSueldo reciboSueldo) {
		List<String> anotaciones = new ArrayList<String>();
		Map<String, List<AnotacionReciboSueldo>> anotacionesMap = new HashMap<String, List<AnotacionReciboSueldo>>();
		for(AnotacionReciboSueldo a : reciboSueldo.getAnotaciones()) {
			String key = a.getTipoAnotacion().getAbreviacion();
			List<AnotacionReciboSueldo> valores = anotacionesMap.get(key);
			if(valores == null) {
				valores = new ArrayList<AnotacionReciboSueldo>();
				anotacionesMap.put(key, valores);
			}
			valores.add(a);
		}
		for(String key : anotacionesMap.keySet()) {
			List<AnotacionReciboSueldo> valores = anotacionesMap.get(key);
			Collections.sort(valores);
			anotaciones.add(key + ": " + StringUtil.getCadena(valores, SEPARADOR_ANOTACIONES));
		}
		return anotaciones;
	}

	public String getSingleTextWithAllAnotaciones(ReciboSueldo reciboSueldo) {
		return StringUtil.getCadena(getAnotacionesList(reciboSueldo), SEPARADOR_TIPO_ANOTACIONES);
	}

	public List<AnotacionReciboSueldo> calcularAnotaciones(List<FichadaLegajoTO> fichadas) {
		List<AnotacionReciboSueldo> anotaciones = new ArrayList<AnotacionReciboSueldo>();
		AnotacionReciboSueldo ultAnotacion = null;
		for(FichadaLegajoTO fichada : fichadas) {
			if(fichadaEsAnotacion(fichada)) {
				if(ultAnotacion == null) {
					ultAnotacion = crearAnotacion(fichada);
					anotaciones.add(ultAnotacion);
				} else if(DateUtil.getManiana(ultAnotacion.getFechaHasta()).compareTo(fichada.getDia()) == 0 && mismoTipoFichada(ultAnotacion, fichada)) {
					ultAnotacion.setFechaHasta(fichada.getDia());
				} else {
					ultAnotacion = crearAnotacion(fichada);
					anotaciones.add(ultAnotacion);
				}
			}
		}
		return anotaciones;
	}

	private boolean fichadaEsAnotacion(FichadaLegajoTO fichada) {
		return fichada.getEstadoDiaFichada() == EEstadoDiaFichada.ENFERMEDAD || 
			   fichada.getEstadoDiaFichada() == EEstadoDiaFichada.FALTA || 
			   fichada.getEstadoDiaFichada() == EEstadoDiaFichada.VACACIONES;
	}

	private AnotacionReciboSueldo crearAnotacion(FichadaLegajoTO fichada) {
		ETipoAnotacionReciboSueldo tipoAnotacion = null;
		if(fichada.getEstadoDiaFichada() == EEstadoDiaFichada.ENFERMEDAD) {
			tipoAnotacion = ETipoAnotacionReciboSueldo.LICENCIA_POR_ENFERMEDAD;
		}
		if(fichada.getEstadoDiaFichada() == EEstadoDiaFichada.FALTA) {
			tipoAnotacion = ETipoAnotacionReciboSueldo.AUSENCIA;
		}
		if(fichada.getEstadoDiaFichada() == EEstadoDiaFichada.VACACIONES) {
			tipoAnotacion = ETipoAnotacionReciboSueldo.VACACIONES;
		}
		AnotacionReciboSueldo anotacion = new AnotacionReciboSueldo();
		anotacion.setFecha(fichada.getDia());
		anotacion.setFechaHasta(fichada.getDia());
		anotacion.setTipoAnotacion(tipoAnotacion);
		return anotacion;
	}

	private boolean mismoTipoFichada(AnotacionReciboSueldo a, FichadaLegajoTO f) {
		return 	(f.getEstadoDiaFichada() == EEstadoDiaFichada.ENFERMEDAD &&  a.getTipoAnotacion() == ETipoAnotacionReciboSueldo.LICENCIA_POR_ENFERMEDAD) ||
				(f.getEstadoDiaFichada() == EEstadoDiaFichada.FALTA &&  a.getTipoAnotacion() == ETipoAnotacionReciboSueldo.AUSENCIA) ||
				(f.getEstadoDiaFichada() == EEstadoDiaFichada.VACACIONES &&  a.getTipoAnotacion() == ETipoAnotacionReciboSueldo.VACACIONES);
				
	}

}