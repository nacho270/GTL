package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.ImpresionContratoHandler;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;

public class AccionImprimirContrato extends Accion<Empleado>{

	public AccionImprimirContrato(){
		setNombre("Imprimir contrato");
		setDescripcion("Imprime el contrato para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_imprimir_moderno.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws CLException {
		if(CLJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Desea imprimir el contrato?", "Pregunta")==CLJOptionPane.YES_OPTION){
			new ImpresionContratoHandler(e.getSelectedElements().get(0),e.getSource().getFrame()).imprimirContrato();
		}
		return false;
	}
	
	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		return e.getSelectedElements().size()==1 && e.getSelectedElements().get(0).getContratoEmpleado()!=null && e.getSelectedElements().get(0).getContratoEmpleado().getContrato().getTipoContrato()!=ETipoContrato.EFECTIVO;
	}
}
