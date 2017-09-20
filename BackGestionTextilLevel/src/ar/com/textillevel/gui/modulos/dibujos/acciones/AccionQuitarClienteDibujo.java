package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.entidades.ventas.articulos.EEstadoDibujo;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
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
			if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Asignar a 01?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).modificarCliente(e.getSelectedElements().get(0), null);
				return true;
			} else {
				JDialogSeleccionarCliente dialogSeleccionarCliente = new JDialogSeleccionarCliente(null, EModoDialogo.MODO_ID);
				GuiUtil.centrar(dialogSeleccionarCliente);
				dialogSeleccionarCliente.setVisible(true);
				Cliente clienteElegido = dialogSeleccionarCliente.getCliente();
				if (clienteElegido != null) {
					GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).modificarCliente(e.getSelectedElements().get(0), clienteElegido);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1 && e.getSelectedElements().get(0).getCliente() != null && e.getSelectedElements().get(0).getEstado() == EEstadoDibujo.EN_STOCK;
	}

}