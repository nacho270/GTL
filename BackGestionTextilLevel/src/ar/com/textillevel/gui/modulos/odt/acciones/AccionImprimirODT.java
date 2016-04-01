package ar.com.textillevel.gui.modulos.odt.acciones;

import java.io.IOException;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.odt.impresion.ImprimirODTHandler;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionImprimirODT extends Accion<OrdenDeTrabajo> {

	public AccionImprimirODT(){
		setNombre("Imprimir ODT");
		setDescripcion("Imprime la ODT seleccionada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_imprimir_moderno.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<OrdenDeTrabajo> e) throws FWException {
		OrdenDeTrabajo odt = e.getSelectedElements().get(0);
		odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odt.getId());
		ImprimirODTHandler handler = new ImprimirODTHandler(odt,e.getSource().getFrame(), null);
		try {
			handler.imprimir();
		} catch (IOException e1) {
			e1.printStackTrace();
			FWJOptionPane.showErrorMessage(e.getSource().getFrame(), "Ha ocurrido un error al imprimir", "Error");
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<OrdenDeTrabajo> e) {
		return e.getSelectedElements().size() == 1;
	}
}
