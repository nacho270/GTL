package ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
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
	public boolean ejecutar(AccionEvent<ValeAnticipo> e) throws FWException {
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
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "El vale se ha creado satisfactoriamente", "Información");
				if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "Desea imprimir el vale?", "Pregunta")==FWJOptionPane.YES_OPTION){
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
