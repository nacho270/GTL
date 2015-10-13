package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;

public class AccionConsultarCheque extends Accion<Cheque> {

	public AccionConsultarCheque(){
		setNombre("Consultar Cheque");
		setDescripcion("Permite consultar un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_consultar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_consultar_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws FWException {
		Cheque cheque = e.getSelectedElements().get(0);
		new JDialogAgregarCheque(e.getSource().getFrame(), cheque,true,false);
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1;
	}
}
