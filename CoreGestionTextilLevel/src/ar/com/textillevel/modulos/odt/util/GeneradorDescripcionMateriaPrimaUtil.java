package ar.com.textillevel.modulos.odt.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.explotaciones.fw.MateriaPrimaCantidadExplotada;

public class GeneradorDescripcionMateriaPrimaUtil {

	private static NumberFormat df3;	
	
	static{
		df3 = DecimalFormat.getNumberInstance(new Locale("es_AR"));
		df3.setMaximumFractionDigits(3);
		df3.setMinimumFractionDigits(3);
		df3.setMinimumIntegerDigits(1);
		df3.setGroupingUsed(false);
	}

	public static <T extends Formulable> String generarDescripcion(List<MateriaPrimaCantidadExplotada<T>> materiasPrimasExplotadas, EUnidad unidad) {
		String descripcion = "";
		if(materiasPrimasExplotadas.size() == 1){
			MateriaPrimaCantidadExplotada<T> mpCantidad  = materiasPrimasExplotadas.get(0);
			String descripcionUnidad = (unidad!=null&& unidad == EUnidad.PORCENTAJE?unidad.getDescripcion().replace(" (KG)", ""):mpCantidad.getMateriaPrimaCantidadDesencadenante().getUnidad().getDescripcion());
			descripcion += df3.format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + df3.format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion();
		}else {
			for (int i = 0; i < materiasPrimasExplotadas.size(); i++) {
				MateriaPrimaCantidadExplotada<T> mpCantidad  = materiasPrimasExplotadas.get(i);
				String descripcionUnidad = (unidad!=null&& unidad == EUnidad.PORCENTAJE?unidad.getDescripcion().replace(" (KG)", ""):mpCantidad.getMateriaPrimaCantidadDesencadenante().getUnidad().getDescripcion());
				if (i != 0 && i == materiasPrimasExplotadas.size() - 1) {
					descripcion = descripcion.substring(0, descripcion.length() - 2);
					descripcion += " y ";
					descripcion += df3.format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + df3.format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion();
				} else {
					descripcion += df3.format(mpCantidad.getMateriaPrimaCantidadDesencadenante().getCantidad()) + " " + descripcionUnidad + " - " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getDescripcion() + ": " + df3.format(mpCantidad.getCantidadExplotada()) + " " + mpCantidad.getMateriaPrimaCantidadDesencadenante().getMateriaPrima().getUnidad().getDescripcion() + ", ";
				}
			}
		}
		return descripcion;
	}

}