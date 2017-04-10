package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionEliminarRemitoEntrada extends Accion<RemitoEntrada> {

	private RemitoEntradaFacadeRemote reFacade;

	public AccionEliminarRemitoEntrada(){
		setNombre("Eliminar Remito de Entrada");
		setDescripcion("Permite eliminar un remito de entrada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoEntrada> e) throws FWException {
		RemitoEntrada remitoEntrada = e.getSelectedElements().get(0);
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), StringW.wordWrap("¿Está seguro que desea eliminar el remito?"), "Confirmación") == FWJOptionPane.YES_OPTION) { 
			try {
				if(remitoEntrada.getArticuloStock() != null || remitoEntrada.getPrecioMatPrima() != null) {
					getRemitoEntradaFacade().eliminarRemitoEntrada01OrCompraDeTela(remitoEntrada.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				} else {
					getRemitoEntradaFacade().eliminarRemitoEntrada(remitoEntrada.getId(), GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				}
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "Remito borrado éxitosamente.", "Información");				
			} catch (ValidacionException ex) {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(ex.getMensajeError()), "Imposible Eliminar");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoEntrada> e) {
		return e.getSelectedElements().size() == 1;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(reFacade == null) {
			reFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return reFacade;
	}

}