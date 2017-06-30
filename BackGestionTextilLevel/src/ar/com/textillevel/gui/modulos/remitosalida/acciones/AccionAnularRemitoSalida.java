package ar.com.textillevel.gui.modulos.remitosalida.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionAnularRemitoSalida extends Accion<RemitoSalida> {

	private RemitoSalidaFacadeRemote remitoSalidaFacade;

	public AccionAnularRemitoSalida() {
		setNombre("Anular Remito de Salida");
		setDescripcion("Permite anular un remito de salida"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_anular_recibo.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_anular_recibo_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoSalida> e) throws FWException {
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea anular el Remito de Salida?", "Confirmación") == FWJOptionPane.YES_OPTION) {
			RemitoSalida remitoSalida = e.getSelectedElements().get(0);
			try {
				getRemitoSalidaFacade().checkEliminacionOrAnulacionRemitoSalida(remitoSalida.getId());
				getRemitoSalidaFacade().anularRemitoSalida(remitoSalida);
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "El remito se ha anulado con éxito.", "Información");
				return true;
			} catch (ValidacionException e1) {
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), StringW.wordWrap(e1.getMensajeError()), "Información");
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoSalida> e) {
		return e.getSelectedElements().size() == 1 && (e.getSelectedElements().get(0).getAnulado() == null || !e.getSelectedElements().get(0).getAnulado());
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

}