package ar.com.textillevel.gui.modulos.odt.acciones;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionBorrarSecuenciaODT extends Accion<ODTTO> {

	public AccionBorrarSecuenciaODT(){
		setNombre("Borrar Secuencia de Trabajo");
		setDescripcion("Permite borrar la secuencia de trabajo asignada a la ODT seleccionada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_borrar_carpeta.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_borrar_carpeta_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		ODTTO odtto = e.getSelectedElements().get(0);
		if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "Va a borrar la secuencia de trabajo para la ODT " + odtto.getCodigo() + ". Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			try {
				OrdenDeTrabajo ordenDeTrabajo = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).getByIdEager(odtto.getId());
				GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).borrarSecuencia(ordenDeTrabajo, GTLGlobalCache.getInstance().getUsuarioSistema());
			} catch (ValidacionException e1) {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), e1.getMensajeError(), "Error");
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		if(e.getSelectedElements().size() == 1){
			ODTTO ordenDeTrabajo = e.getSelectedElements().get(0);
			return ordenDeTrabajo.isTieneSecuencia() && (ordenDeTrabajo.getEstado() == EEstadoODT.ANTERIOR || ordenDeTrabajo.getEstado() == EEstadoODT.IMPRESA || ordenDeTrabajo.getEstado() == EEstadoODT.PENDIENTE );
		}
		return false;
	}

}
