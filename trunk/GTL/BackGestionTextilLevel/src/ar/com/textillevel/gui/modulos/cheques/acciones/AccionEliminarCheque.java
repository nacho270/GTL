package ar.com.textillevel.gui.modulos.cheques.acciones;

import java.util.List;

import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionEliminarCheque extends Accion<Cheque>{

	public AccionEliminarCheque(){
		setNombre("Eliminar Cheque");
		setDescripcion("Permite eliminar un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(e.getSource().getFrame(), "Eliminar cheque");
		if (jDialogPasswordInput.isAcepto()) {
			String pass = new String(jDialogPasswordInput.getPassword());
			UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
			if (usrAdmin != null) {
				if(CLJOptionPane.showQuestionMessage(e.getSource().getFrame(), "Va a eliminar definitivamente los cheques seleccionados. Está seguro?", "Pregunta")==CLJOptionPane.YES_OPTION){
					try{
						boolean avisar = false;
						List<Cheque> chequesSeleccionados = e.getSelectedElements();
						for(Cheque c : chequesSeleccionados){
							if(c.getEstadoCheque()== EEstadoCheque.PENDIENTE_COBRAR){
								try {
									GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class).eliminarCheque(c.getId(), usrAdmin.getUsrName());
								} catch (ValidacionException e1) {
									CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "Error al eliminar el cheque Nº: " + c.getNumeracion()+". " +e1.getMensajeError(), "Error");
								}
							}else{
								avisar=true;
							}
						}
						if(avisar){
							CLJOptionPane.showInformationMessage(e.getSource(), "Algunos cheques no han podido ser eliminados por no estar en estado PENDIENTE", "Advertencia");
						}
					}catch(RuntimeException cle){
						BossError.gestionarError(cle);
					}
				}
			} else {
				CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "La clave ingresada no peternece a un usuario administrador", "Error");
			}
		}
			
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		if(e.getSelectedElements().size()==1 && e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.PENDIENTE_COBRAR){
			return true;
		}
		return e.getSelectedElements().size()>0;
	}
}
