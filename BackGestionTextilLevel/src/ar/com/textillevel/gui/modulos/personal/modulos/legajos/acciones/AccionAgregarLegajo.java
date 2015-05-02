package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogAgregarModificarLegajo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.contratos.ImpresionContratoHandler;
import ar.com.textillevel.modulos.personal.entidades.contratos.ETipoContrato;
import ar.com.textillevel.modulos.personal.entidades.legajos.ContratoEmpleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.legajos.LegajoEmpleado;
import ar.com.textillevel.modulos.personal.facade.api.remote.EmpleadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionAgregarLegajo extends Accion<Empleado>{

	public AccionAgregarLegajo(){
		setNombre("Crear legajo");
		setDescripcion("Crea un legajo para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_agregar_legajo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_agregar_legajo_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws CLException {
		Empleado empleado = e.getSelectedElements().get(0);
		JDialogAgregarModificarLegajo dialog = empleado.getLegajo() ==null?new JDialogAgregarModificarLegajo(e.getSource().getFrame()):new JDialogAgregarModificarLegajo(e.getSource().getFrame(),empleado.getLegajo(),empleado.getContratoEmpleado());
		dialog.setVisible(true);
		if(dialog.isAcepto()){
			LegajoEmpleado legajo = dialog.getLegajo();
			legajo.setEmpleado(empleado);
			empleado.setLegajo(legajo);
			ContratoEmpleado contratoEmpleado = dialog.getContratoEmpleado();
			contratoEmpleado.setEmpleado(empleado);
			empleado.setContratoEmpleado(contratoEmpleado);
			GTLPersonalBeanFactory.getInstance().getBean2(EmpleadoFacadeRemote.class).save(empleado);
			CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "Los datos se han guardado con éxito", "Información");
			
			if(contratoEmpleado.getContrato().getTipoContrato()!=ETipoContrato.EFECTIVO){
				if(CLJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Desea imprimir el contrato?", "Pregunta")==CLJOptionPane.YES_OPTION){
					new ImpresionContratoHandler(e.getSelectedElements().get(0),e.getSource().getFrame()).imprimirContrato();
				}
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		return e.getSelectedElements().size()==1;
	}
}
