package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

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
		if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea quitarle el cliente al dibujo seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
			GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).quitarCliente(e.getSelectedElements().get(0));
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1 && e.getSelectedElements().get(0).getCliente() != null;
	}

}
