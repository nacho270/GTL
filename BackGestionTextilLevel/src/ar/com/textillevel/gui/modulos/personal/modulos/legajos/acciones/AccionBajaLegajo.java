package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import java.util.List;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogIngresarBajaEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.VigenciaEmpleado;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionBajaLegajo extends Accion<Empleado> {

	public AccionBajaLegajo(){
		setNombre("Baja legajo");
		setDescripcion("Da de baja el legajo para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_baja_legajo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_baja_legajo_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws CLException {
		Empleado empleado = e.getSelectedElements().get(0);
		List<VigenciaEmpleado> historialVigencias = empleado.getLegajo().getHistorialVigencias();
		VigenciaEmpleado ultima = historialVigencias.get(historialVigencias.size()-1);
		JDialogIngresarBajaEmpleado dialog = new JDialogIngresarBajaEmpleado(e.getSource().getFrame(),ultima.getFechaAlta());
		dialog.setVisible(true);
		if(dialog.isAcepto()){
			ultima.setFechaBaja(dialog.getFechaElegida());
			ultima.setObservacionesBaja(dialog.getObservacionesIngresadas());
			historialVigencias.set(historialVigencias.indexOf(ultima), ultima);
			empleado.getLegajo().setHistorialVigencias(historialVigencias);
			empleado.getLegajo().setDadoDeBaja(true);
			GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).save(empleado);
			CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "El empleado se ha dado de baja correctamente", "Información");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		if( e.getSelectedElements().size()==1){
			LegajoEmpleado legajo = e.getSelectedElements().get(0).getLegajo();
			return legajo!=null && legajo.getDadoDeBaja()==false && !legajo.getHistorialVigencias().isEmpty();
		}else{ 
			return false;
		}
	}
}
