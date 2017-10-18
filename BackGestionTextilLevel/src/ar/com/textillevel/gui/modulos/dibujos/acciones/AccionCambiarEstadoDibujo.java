package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogSeleccionarEstadoDibujo;
import ar.com.textillevel.util.CambioEstadoDibujoHelper;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionCambiarEstadoDibujo extends Accion<DibujoEstampado> {

	public AccionCambiarEstadoDibujo() {
		setNombre("Cambiar estado dibujo");
		setDescripcion("Permite cambiar de estado a un dibujo");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_cambiar_estado_dibujo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_cambiar_estado_dibujo_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		String usuario = GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName();
		DibujoEstampadoFacadeRemote dibujoFacade = GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class);
		DibujoEstampado dibujoEstampado = e.getSelectedElements().get(0);
		JDialogSeleccionarEstadoDibujo dialog = new JDialogSeleccionarEstadoDibujo(e.getSource().getFrame(), dibujoEstampado);
		dialog.setVisible(true);
		if(dialog.getEstadoNuevo() != null) {
			dibujoFacade.actualizarEstado(dibujoEstampado, dialog.getEstadoNuevo(), usuario);
			return true;
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1 && CambioEstadoDibujoHelper.getInstance().puedeCambiarEstado(e.getSelectedElements().get(0).getEstado());
	}

}
