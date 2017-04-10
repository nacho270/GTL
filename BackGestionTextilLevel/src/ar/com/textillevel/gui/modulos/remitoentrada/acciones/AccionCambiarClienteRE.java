package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.componentes.error.validaciones.ValidacionException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.RemitoEntradaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente;
import ar.com.textillevel.gui.util.dialogs.JDialogSeleccionarCliente.EModoDialogo;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionCambiarClienteRE extends Accion<RemitoEntrada> {

	private RemitoEntradaFacadeRemote reFacade;
	
	public AccionCambiarClienteRE(){
		setNombre("Asignar Remito a otro Cliente");
		setDescripcion("Permite asignar el Remito de entrada a otro Cliente"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_re_cambiar_cliente.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_re_cambiar_cliente_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<RemitoEntrada> e) throws FWException {
		RemitoEntrada remitoEntrada = e.getSelectedElements().get(0);
		if(FWJOptionPane.showQuestionMessage(e.getSource().getFrame(), StringW.wordWrap("¿Está seguro que desea asignar el remito a otro cliente?"), "Confirmación") == FWJOptionPane.YES_OPTION) {
			JDialogSeleccionarCliente dialogoSelCli = new JDialogSeleccionarCliente(e.getSource().getFrame(), EModoDialogo.MODO_ID);
			GuiUtil.centrar(dialogoSelCli);
			dialogoSelCli.setVisible(true);
			GuiUtil.centrarEnFramePadre(dialogoSelCli);
			Cliente cliente = dialogoSelCli.getCliente();
			if(cliente != null) {
				try {
					remitoEntrada.setCliente(cliente);
					getRemitoEntradaFacade().cambiarClienteRE(remitoEntrada, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
					FWJOptionPane.showInformationMessage(e.getSource().getFrame(), StringW.wordWrap("El Remito de Entrada " + remitoEntrada.getNroRemito() + " ahora está asignado al Cliente " + cliente), "Información");				
				} catch (ValidacionException ex) {
					FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap(ex.getMensajeError()), "Imposible Editar");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoEntrada> e) {
		return e.getSelectedElements().size()==1;
	}

	private RemitoEntradaFacadeRemote getRemitoEntradaFacade() {
		if(reFacade == null) {
			reFacade = GTLBeanFactory.getInstance().getBean2(RemitoEntradaFacadeRemote.class);
		}
		return reFacade;
	}

}