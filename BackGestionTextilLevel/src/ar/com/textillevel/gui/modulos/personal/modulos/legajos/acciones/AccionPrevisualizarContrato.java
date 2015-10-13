package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.ImpresionContratoHandler;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class AccionPrevisualizarContrato extends Accion<Empleado> {

	public AccionPrevisualizarContrato(){
		setNombre("Previsualizar contrato");
		setDescripcion("Permite ver el contrato para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_preview.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_preview_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws FWException {
		new ImpresionContratoHandler(e.getSelectedElements().get(0),e.getSource().getFrame()).previsualizarContrato();
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		return e.getSelectedElements().size()==1 && e.getSelectedElements().get(0).getContratoEmpleado()!=null && e.getSelectedElements().get(0).getContratoEmpleado().getContrato().getTipoContrato()!=ETipoContrato.EFECTIVO;
	}
}
