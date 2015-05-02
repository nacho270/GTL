package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionPonerChequeSalidaEnCartera extends Accion<Cheque>{
	
	public AccionPonerChequeSalidaEnCartera(){
		setNombre("Pone en cartera un marcado como salida");
		setDescripcion("Pone el cheque seleccionado en cartera luego de que se le haya dado salida"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_cheque_cartera.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_cheque_cartera_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(e.getSource().getFrame(), "Poner cheque en cartera");
		if (jDialogPasswordInput.isAcepto()) {
			String pass = new String(jDialogPasswordInput.getPassword());
			UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
			if (usrAdmin != null) {
				Cheque cheque = e.getSelectedElements().get(0);
				cheque.setEstadoCheque(EEstadoCheque.EN_CARTERA);
				cheque.setFechaSalida(null);
				GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class).grabarCheque(cheque,usrAdmin.getUsrName());
				return true;
			}else{
				CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "La clave ingresada no peternece a un usuario administrador", "Error");
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1 && 
					(e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_BANCO || e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_CLIENTE
					|| e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_PERSONA || e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.SALIDA_PROVEEDOR);
	}
}
