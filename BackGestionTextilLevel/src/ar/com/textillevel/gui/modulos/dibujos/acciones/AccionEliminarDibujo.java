package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionEliminarDibujo extends Accion<DibujoEstampado> {

	public AccionEliminarDibujo() {
		setNombre("Eliminar dibujo");
		setDescripcion("Permite eliminar un dibujo");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		try {
			if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea eliminar el dibujo seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).remove(e.getSelectedElements().get(0));
			}
		} catch (Exception e2) {
			FWJOptionPane.showErrorMessage(e.getSource().getFrame(), "No se ha podido eliminar el dibujo debido a que se esta utilizando.", "Error");
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1;
	}
}
