package ar.com.textillevel.gui.modulos.odt.acciones;

import java.io.IOException;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.odt.impresion.ImprimirODTHandler;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionImprimirODT extends Accion<ODTTO> {

	public AccionImprimirODT(){
		setNombre("Imprimir ODT");
		setDescripcion("Imprime la ODT seleccionada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_imprimir_moderno.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		ODTTO odtto = e.getSelectedElements().get(0);
		OrdenDeTrabajo odt = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtto.getId());
		ImprimirODTHandler handler = new ImprimirODTHandler(odt,e.getSource().getFrame(), null, null);
		try {
			handler.imprimir();
		} catch (IOException e1) {
			e1.printStackTrace();
			FWJOptionPane.showErrorMessage(e.getSource().getFrame(), "Ha ocurrido un error al imprimir", "Error");
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		return e.getSelectedElements().size() == 1;
	}

}
