package ar.com.textillevel.gui.modulos.dibujos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;
import ar.com.textillevel.gui.modulos.dibujos.gui.JDialogAgregarModificarDibujoEstampado;

public class AccionConsultarDibujo extends Accion<DibujoEstampado> {

	public AccionConsultarDibujo(){
		setNombre("Consultar dibujo");
		setDescripcion("Permite consultar un dibujo"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_consultar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_consultar_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<DibujoEstampado> e) throws FWException {
		JDialogAgregarModificarDibujoEstampado dialog = new JDialogAgregarModificarDibujoEstampado(e.getSource().getFrame(), e.getSelectedElements().get(0), true);
		dialog.setVisible(true);
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<DibujoEstampado> e) {
		return e.getSelectedElements().size()==1;
	}

}
