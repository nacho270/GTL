package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.facade.api.remote.DibujoEstampadoFacadeRemote;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogAgregarModificarDibujoEstampado;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionModificarDibujo extends Accion<DibujoEstampado>{

	public AccionModificarDibujo(){
		setNombre("Modificar dibujo");
		setDescripcion("Permite modificar un dibujo"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_modificar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_modificar_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		JDialogAgregarModificarDibujoEstampado dialog = new JDialogAgregarModificarDibujoEstampado(e.getSource().getFrame(), e.getSelectedElements().get(0), false);
		dialog.setVisible(true);
		if (dialog.isAcepto()) {
			GTLBeanFactory.getInstance().getBean2(DibujoEstampadoFacadeRemote.class).save(dialog.getDibujoActual());
			return true;
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size() == 1;
	}
}
