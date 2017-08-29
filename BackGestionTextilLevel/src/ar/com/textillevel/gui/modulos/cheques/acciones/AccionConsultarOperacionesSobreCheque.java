package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogVerOperacionesConCheque;

public class AccionConsultarOperacionesSobreCheque extends Accion<Cheque> {
	
	public AccionConsultarOperacionesSobreCheque(){
		setNombre("Consultar Operaciones sobre Cheque");
		setDescripcion("Permite consultar las operaciones realizadas sobre un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_consultar_operaciones_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_consultar_operaciones_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws FWException {
		Cheque cheque = e.getSelectedElements().get(0);
		JDialogVerOperacionesConCheque dialog = new JDialogVerOperacionesConCheque(e.getSource().getFrame(), cheque);
		dialog.setVisible(true);
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1;
	}

}