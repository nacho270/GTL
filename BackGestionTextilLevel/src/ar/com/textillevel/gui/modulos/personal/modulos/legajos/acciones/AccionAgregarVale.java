package ar.com.textillevel.gui.modulos.personal.modulos.legajos.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.gui.JDialogAgregarValeAnticipo;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.valeanticipo.ImpresionValeAnticipoHandler;
import ar.com.textillevel.modulos.personal.entidades.legajos.Empleado;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionAgregarVale extends Accion<Empleado> {

	
	public AccionAgregarVale(){
		setNombre("Agregar vale de anticipo");
		setDescripcion("Crea un un vale de anticipo para el empleado seleccionado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_vale.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_vale_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Empleado> e) throws FWException {
		Empleado empleado = e.getSelectedElements().get(0);
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
		}
		return false;
	}


	@Override
	public boolean esValida(AccionEvent<Empleado> e) {
		return e.getSelectedElements().size()==1;
	}
}
