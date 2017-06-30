package ar.com.textillevel.gui.modulos.remitosalida.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.facade.api.remote.RemitoSalidaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionEliminarRemitoSalida extends Accion<RemitoSalida> {

	private RemitoSalidaFacadeRemote remitoSalidaFacade;

	public AccionEliminarRemitoSalida() {
		setNombre("Eliminar Remito de Salida");
		setDescripcion("Permite eliminar un remito de salida"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoSalida> e) throws FWException {
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea eliminar el remito?", "Confirmación") == FWJOptionPane.YES_OPTION) {
			RemitoSalida remitoSalida = e.getSelectedElements().get(0);
			try {
				if(remitoSalida.getTipoRemitoSalida() == null || remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.CLIENTE || remitoSalida.getTipoRemitoSalida() == ETipoRemitoSalida.PROVEEDOR) {
					getRemitoSalidaFacade().eliminarRemitoSalida(remitoSalida.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				} else {
					getRemitoSalidaFacade().eliminarRemitoSalida01OrVentaTela(remitoSalida.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				}
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "Remito borrado éxitosamente.", "Información");				
			} catch (ValidacionException ex) {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(ex.getMensajeError()), "Imposible Eliminar");
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean esValida(AccionEvent<RemitoSalida> e) {
		return e.getSelectedElements().size() == 1;
	}

	private RemitoSalidaFacadeRemote getRemitoSalidaFacade() {
		if(remitoSalidaFacade == null) {
			remitoSalidaFacade = GTLBeanFactory.getInstance().getBean2(RemitoSalidaFacadeRemote.class);
		}
		return remitoSalidaFacade;
	}

}