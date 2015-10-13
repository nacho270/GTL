package ar.com.textillevel.gui.modulos.personal.modulos.recibossueldo.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.fwcommon.util.DateUtil;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.ReciboSueldo;

public class AntiguedadHelper {

	private static final AntiguedadHelper instance = new AntiguedadHelper();
	
	private AntiguedadHelper() {
	}

	public static AntiguedadHelper getInstance() {
		return instance;
	}

	public Integer calcularAntiguedad(Date fechaLiquidacion, ReciboSueldo reciboSueldo) {
		return calcularAntiguedad(fechaLiquidacion, reciboSueldo.getLegajo());
	}
	
	public Integer calcularAntiguedad(Date fechaLiquidacion, LegajoEmpleado legajo) {
		Date fechaAlta = getFechaAlta(fechaLiquidacion, legajo);
		int diaNac = DateUtil.getDia(fechaAlta);
		int mesNac = DateUtil.getMes(fechaAlta);
		int anioNac = DateUtil.getAnio(fechaAlta);
		int diaHasta = DateUtil.getDia(fechaLiquidacion);
		int mesHasta = DateUtil.getMes(fechaLiquidacion);
		int anioHasta = DateUtil.getAnio(fechaLiquidacion);
		return DateUtil.calcularEdadHasta(diaNac, mesNac, anioNac, diaHasta, mesHasta, anioHasta);
	}

	public Date getFechaAlta(Date fechaLiquidacion, LegajoEmpleado legajo) {
		List<VigenciaEmpleado> vigencias = new ArrayList<VigenciaEmpleado>(legajo.getHistorialVigencias());
		Collections.sort(vigencias, new Comparator<VigenciaEmpleado>() {

			public int compare(VigenciaEmpleado o1, VigenciaEmpleado o2) {
				return o1.getFechaAlta().compareTo(o2.getFechaAlta());
			}

		});
		for(VigenciaEmpleado ve : vigencias) {
			if(!ve.getFechaAlta().after(fechaLiquidacion) && (ve.getFechaBaja() == null || !fechaLiquidacion.after(ve.getFechaBaja()))) {
				return ve.getFechaAlta();
			}
		}
		
		return null;
	}

}