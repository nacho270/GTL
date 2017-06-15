package ar.com.textillevel.gui.modulos.odt.acciones;

import java.util.List;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionBorrarPiezasSinSalidaODT extends Accion<ODTTO> {
	
	public AccionBorrarPiezasSinSalidaODT(){
		setNombre("Borrar piezas sin salida");
		setDescripcion("Permite borrar piezas que no tuvieron salida"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_borrar_piezas_sin_salida.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_borrar_piezas_sin_salida_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		ODTTO odtto = e.getSelectedElements().get(0);
		OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		OrdenDeTrabajo odt = odtFacade.getByIdEager(odtto.getId());
		int cantPiezasConSalida = odt.contarDeAcuerdoASalida(true);
		if(cantPiezasConSalida == 0) {
			FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap("Para ejecutar esta operación alguna pieza de la ODT tuvo que haber tenido salida."), "Error");
			return false;
		}
		int cantPiezasSinSalida = odt.contarDeAcuerdoASalida(false);
		if(cantPiezasSinSalida == 0) {
			FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap("Para ejecutar esta operación la ODT tiene que tener al menos una pieza SIN salida."), "Error");
			return false;
		}
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), StringW.wordWrap("¿Está seguro que desea eliminar " + cantPiezasSinSalida + " piezas de la ODT que no tuvieron salida? \nDetalle:\n  Piezas con salida: " + cantPiezasConSalida + " \n  Piezas sin salida: " + cantPiezasSinSalida), "Confirmación") == FWJOptionPane.YES_OPTION) {
			odtFacade.borrarPiezasSinSalida(odt, GTLGlobalCache.getInstance().getUsuarioSistema());
			FWJOptionPane.showInformationMessage(e.getSource().getFrame(), StringW.wordWrap("Se borraron " + cantPiezasSinSalida + " piezas de la ODT y la misma quedó en estado " + EEstadoODT.FACTURADA), "Información");
			return true;
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		List<ODTTO> selectedElements = e.getSelectedElements();
		if(selectedElements.isEmpty() || selectedElements.size() != 1) {
			return false;
		}
		ODTTO odtto = selectedElements.get(0);
		return odtto.getEstado() == EEstadoODT.EN_OFICINA || odtto.getEstado() == EEstadoODT.ANTERIOR;
	}

}