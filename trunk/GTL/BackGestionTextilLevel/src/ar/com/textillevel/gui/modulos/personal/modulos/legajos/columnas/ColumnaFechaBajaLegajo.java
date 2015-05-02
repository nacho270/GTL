package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import java.util.Date;
import java.util.List;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaDate;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;

public class ColumnaFechaBajaLegajo extends ColumnaDate<Empleado>{

	public ColumnaFechaBajaLegajo() {
		super("Fecha de baja AFIP");
		setAncho(150);
	}

	@Override
	public Date getValor(Empleado item) {
		if(item.getLegajo()!=null){
			List<VigenciaEmpleado> historialVigencias = item.getLegajo().getHistorialVigencias();
			if(historialVigencias.isEmpty()){
				return null;
			}
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
			return ultima.getFechaBaja();
		}
		return null;
	}
}
