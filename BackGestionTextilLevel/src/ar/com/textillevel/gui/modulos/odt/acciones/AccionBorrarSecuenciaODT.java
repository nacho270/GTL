package ar.com.textillevel.gui.modulos.odt.acciones;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import ar.com.textillevel.util.ODTCodigoHelper;

public class AccionBorrarSecuenciaODT extends Accion<OrdenDeTrabajo> {

	public AccionBorrarSecuenciaODT(){
		setNombre("Borrar Secuencia de Trabajo");
		setDescripcion("Permite borrar la secuencia de trabajo asignada a la ODT seleccionada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_borrar_carpeta.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_borrar_carpeta_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<OrdenDeTrabajo> e) throws FWException {
		OrdenDeTrabajo ordenDeTrabajo = e.getSelectedElements().get(0);
		if (FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "Va a borrar la secuencia de trabajo para la ODT " + ODTCodigoHelper.getInstance().formatCodigo(ordenDeTrabajo.getCodigo()) + ". Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			try {
				GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class).borrarSecuencia(ordenDeTrabajo, GTLGlobalCache.getInstance().getUsuarioSistema());
			} catch (ValidacionException e1) {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), e1.getMensajeError(), "Error");
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<OrdenDeTrabajo> e) {
		if(e.getSelectedElements().size() == 1){
			OrdenDeTrabajo ordenDeTrabajo = e.getSelectedElements().get(0);
			return ordenDeTrabajo.getSecuenciaDeTrabajo()!=null && (ordenDeTrabajo.getEstado() == EEstadoODT.ANTERIOR || ordenDeTrabajo.getEstado() == EEstadoODT.IMPRESA || ordenDeTrabajo.getEstado() == EEstadoODT.PENDIENTE );
		}
		return false;
	}
}
