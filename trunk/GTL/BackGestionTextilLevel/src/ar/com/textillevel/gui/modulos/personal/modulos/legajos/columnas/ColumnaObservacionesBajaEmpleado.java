package ar.com.textillevel.gui.modulos.personal.modulos.legajos.columnas;

import java.util.List;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;

public class ColumnaObservacionesBajaEmpleado extends ColumnaString<Empleado>{

	public ColumnaObservacionesBajaEmpleado() {
		super("Observaciones baja");
		setAncho(200);
	}

	@Override
	public String getValor(Empleado item) {
		if(item.getLegajo()!=null){
			List<VigenciaEmpleado> historialVigencias = item.getLegajo().getHistorialVigencias();
			if(historialVigencias.isEmpty()){
				return null;
			}
			VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
			return ultima.getObservacionesBaja();
		}
		return null;
	}
}
