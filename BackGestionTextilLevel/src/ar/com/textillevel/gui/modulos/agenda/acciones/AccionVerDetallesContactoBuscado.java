package ar.com.textillevel.gui.modulos.agenda.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.gente.IAgendable;
import ar.com.textillevel.gui.modulos.agenda.gui.JDialogDetalleContacto;

public class AccionVerDetallesContactoBuscado extends Accion<IAgendable> {

	public AccionVerDetallesContactoBuscado(){
		super();
		setNombre("Consultar Contacto.");
		setDescripcion("Permite ver la informaci\u00F3n de un contacto.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_consultar_contacto.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_consultar_contacto_des.png");
	}
	
	public AccionVerDetallesContactoBuscado(Integer idModulo){
		this();
	}
	
	@Override
	public boolean ejecutar(AccionEvent<IAgendable> e) throws FWException {
		new JDialogDetalleContacto(e.getSelectedElements().get(0),e.getSource().getFrame());
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<IAgendable> e) {
		return e.getSelectedElements().size() == 1;
	}
}
