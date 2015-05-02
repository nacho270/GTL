package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import java.util.List;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogVerInfoFichadas;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class AccionVerFichadas extends Accion<Empleado>{

	public AccionVerFichadas(){
		setNombre("Ver fichadas");
		setDescripcion("Muestra la información de las fichadas del empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_fichadas.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_fichadas_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws CLException {
		new JDialogVerInfoFichadas(e.getSource().getFrame(), e.getSelectedElements().get(0).getLegajo()).setVisible(true);
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		List<Empleado> elementos = e.getSelectedElements();
		if(elementos.size()!=1){
			return false;
		}
		Empleado empleado = elementos.get(0);
		return elementos.size()==1 && empleado.getLegajo()!=null && empleado.getLegajo().getDadoDeBaja()==false;
	}
}
