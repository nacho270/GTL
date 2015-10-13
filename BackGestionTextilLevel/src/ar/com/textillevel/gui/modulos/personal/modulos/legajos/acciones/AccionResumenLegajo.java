package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogResumenLegajo;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class AccionResumenLegajo extends Accion<Empleado> {

	public AccionResumenLegajo(){
		setNombre("Resumen de Legajo");
		setDescripcion("Consulta un resumen y permite cargar sanciones, enfermedades, etc."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_ver_resumen_legajo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_ver_resumen_legajo_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws FWException {
		Empleado empleado = e.getSelectedElements().get(0);
		JDialogResumenLegajo dialog = new JDialogResumenLegajo(e.getSource().getFrame(), empleado);
		dialog.setVisible(true);
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		return e.getSelectedElements().size()==1 && (e.getSelectedElements().get(0).getLegajo()==null ||( e.getSelectedElements().get(0).getLegajo()!=null && e.getSelectedElements().get(0).getLegajo().getDadoDeBaja() == false));
	}

}
