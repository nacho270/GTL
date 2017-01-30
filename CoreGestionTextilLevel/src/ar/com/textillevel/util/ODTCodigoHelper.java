package ar.com.textillevel.util;

import java.sql.Date;

import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.StringUtil;

public class ODTCodigoHelper {

	private static ODTCodigoHelper instance = new ODTCodigoHelper();
	public static final String FORMATO_COD_ODT = "yyyyMMdd";
	private static int OFFSET_ANIO = 4;
	private static int OFFSET_MES = 6;
	private static String SEP_ODT = "*";

	private ODTCodigoHelper() {
	}

	public static ODTCodigoHelper getInstance() {
		return instance;
	}

	/**
	 * Devuelve el próximo codigo si es que <code>codigoODT</code> corresponde al año/mes en curso.
	 * Caso contrario devuelve el primero del siguiente año/mes.
	 * @param codigoODT
	 */
	public String getProximoCodigo(String codigoODT) {
		return getProximoCodigoInternal(codigoODT);
	}

	/**
	 * Devuelve el anio a partir del codigo de la ODT
	 * @param codigoODT
	 */
	public int getAnioFromCodigo(String codigoODT) {
		return Integer.valueOf(codigoODT.substring(0, OFFSET_ANIO));
	}

	/**
	 * Devuelve el mes a partir del codigo de la ODT
	 * @param codigoODT
	 */
	public int getMesFromCodigo(String codigoODT) {
		return Integer.valueOf(codigoODT.substring(OFFSET_ANIO, OFFSET_MES));
	}

	/**
	 * Devuelve el nro de ODT a partir del codigo de la ODT
	 * @param codigoODT
	 */
	public int getNroODTFromCodigo(String codigoODT) {
		return Integer.valueOf(codigoODT.substring(OFFSET_MES));
	}
	
	/**
	 * Devuelve el codigo ODT formateado de acuerdo al separador <code>SEP_ODT</code>
	 * @param codigoODT
	 */
	public String formatCodigo(String codigoODT) {
		return codigoODT.substring(0, OFFSET_ANIO) + SEP_ODT + codigoODT.substring(OFFSET_ANIO, OFFSET_MES) + SEP_ODT + StringUtil.fillLeftWithZeros(codigoODT.substring(OFFSET_MES), 2);
	}

	/**
	 * Devuelve el codigo ODT formateado de acuerdo al separador <code>SEP_ODT</code>
	 * @param codigoODT
	 */
	public String formatCodigo2DigitosAnio(String codigoODT) {
		return codigoODT.substring(2, OFFSET_ANIO) + SEP_ODT + codigoODT.substring(OFFSET_ANIO, OFFSET_MES) + SEP_ODT + StringUtil.fillLeftWithZeros(codigoODT.substring(OFFSET_MES), 2);
	}

	private String getProximoCodigoInternal(String codigoODT) {
		int anio = getAnioFromCodigo(codigoODT);
		int mes = getMesFromCodigo(codigoODT);
		int nroODT = getNroODTFromCodigo(codigoODT);
		Date hoy = DateUtil.getHoy();
		int anioActual = DateUtil.getAnio(hoy);
		int mesActual = DateUtil.getMes(hoy) + 1;
		if(anioActual == anio ) { 
			if(mes == mesActual) { //si es del mismo año/mes
				return  armaCodigo(anio, mes, nroODT+1);  
			} else { //si coincide sólo el año => devuelvo anio*mesActual*1
				return  armaCodigo(anio, mesActual, 1);  
			}
		} else { //si no coincide el año => devuelvo  anioactual*mesActual*1
			return  armaCodigo(anioActual, mesActual, 1);
		}
	}

	private static String armaCodigo(int anio, int mes, int nroODT) {
		return anio + StringUtil.fillLeftWithZeros(String.valueOf(mes), OFFSET_MES - OFFSET_ANIO) + StringUtil.fillLeftWithZeros(String.valueOf(nroODT), 2);
	}

}