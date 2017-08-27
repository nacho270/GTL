package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class AccionAgregarDibujo extends Accion<DibujoEstampado>{

	public AccionAgregarDibujo(){
		setNombre("Agregar Cheque");
		setDescripcion("Permite dar de alta un dibujo"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/fwcommon/imagenes/b_agregar.png");
		setImagenInactivo("ar/com/fwcommon/imagenes/b_agregar_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return true;
	}
}
