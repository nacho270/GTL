package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.acciones.JDialogInputDatosRechazaCheque;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionRechazarCheque extends Accion<Cheque>{

	public AccionRechazarCheque(){
		setNombre("Rechazar Cheque");
		setDescripcion("Permite rechazar un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_rechazar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_rechazar_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(e.getSource().getFrame(),"Rechazar cheque");
		if (jDialogPasswordInput.isAcepto()) {
			String pass = new String(jDialogPasswordInput.getPassword());
			UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
			if (usrAdmin != null) {
				Cheque cheque = e.getSelectedElements().get(0);
				JDialogInputDatosRechazaCheque jdinput = new JDialogInputDatosRechazaCheque(e.getSource().getFrame(), cheque, usrAdmin.getUsrName());
				jdinput.setVisible(true);
				if(jdinput.isAcepto()){
					return true;
				}
				return false;
			} else {
				CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "La clave ingresada no peternece a un usuario administrador", "Error");
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1 && 
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.PENDIENTE_COBRAR || 
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.EN_CARTERA) || 
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_BANCO) ||
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_CLIENTE) ||
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_PERSONA)||
		(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_PROVEEDOR));
	}
}
