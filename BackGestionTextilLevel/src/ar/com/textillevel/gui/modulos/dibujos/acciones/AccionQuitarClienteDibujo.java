package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

public class AccionQuitarClienteDibujo extends Accion<DibujoEstampado> {

	public AccionQuitarClienteDibujo() {
		setNombre("Quitar cliente asignado");
		setDescripcion("Permite desasignar el dibujo del cliente");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar_recibo_sueldo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_recibo_sueldo_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1 && e.getSelectedElements().get(0).getCliente() != null;
	}

}
