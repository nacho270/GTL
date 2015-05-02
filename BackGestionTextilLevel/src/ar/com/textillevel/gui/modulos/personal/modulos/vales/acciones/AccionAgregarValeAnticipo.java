package ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.modulos.personal.gui.util.JDialogSeleccionarEmpleado;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogAgregarValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.valeanticipo.ImpresionValeAnticipoHandler;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionAgregarValeAnticipo extends Accion<ValeAnticipo>{

	public AccionAgregarValeAnticipo(){
		setNombre("Agregar vale de anticipo");
		setDescripcion("Crea un un vale de anticipo para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_vale.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_vale_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ValeAnticipo> e) throws CLException {
		JDialogSeleccionarEmpleado dialogoSeleccionarEmpleado = new JDialogSeleccionarEmpleado(e.getSource().getFrame());
		GuiUtil.centrar(dialogoSeleccionarEmpleado);
		dialogoSeleccionarEmpleado.setVisible(true);
		Empleado empleado = dialogoSeleccionarEmpleado.getEmpleadoSeleccionado();
		if(empleado!=null){
			JDialogAgregarValeAnticipo dialog = new JDialogAgregarValeAnticipo(e.getSource().getFrame(), empleado);
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				ValeAnticipo valeAnticipo = dialog.getValeAnticipo();
				GTLPersonalBeanFactory.getInstance().getBean2(ValeAnticipoFacadeRemote.class).save(valeAnticipo,empleado);
				CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "El vale se ha creado satisfactoriamente", "Información");
				if(CLJOptionPane.showQuestionMessage(e.getSource().getFrame(), "Desea imprimir el vale?", "Pregunta")==CLJOptionPane.YES_OPTION){
					ImpresionValeAnticipoHandler handler = new ImpresionValeAnticipoHandler(valeAnticipo,e.getSource().getFrame());
					handler.imprimir();
				}
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean esValida(AccionEvent<ValeAnticipo> e) {
		return true;
	}
}
