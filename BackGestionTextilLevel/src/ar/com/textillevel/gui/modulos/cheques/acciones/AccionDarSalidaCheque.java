package ar.com.textillevel.gui.modulos.cheques.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.entidades.portal.UsuarioSistema;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.facade.api.remote.UsuarioSistemaFacadeRemote;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogDarSalidaCheque;
import ar.com.textillevel.gui.util.dialogs.JDialogPasswordInput;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionDarSalidaCheque extends Accion<Cheque>{

	public AccionDarSalidaCheque(){
		setNombre("Dar salida Cheque");
		setDescripcion("Permite dar salida a un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_salida.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_salida_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
		JDialogPasswordInput jDialogPasswordInput = new JDialogPasswordInput(e.getSource().getFrame(), "Dar salida cheque");
		if (jDialogPasswordInput.isAcepto()) {
			String pass = new String(jDialogPasswordInput.getPassword());
			UsuarioSistema usrAdmin = GTLBeanFactory.getInstance().getBean2(UsuarioSistemaFacadeRemote.class).esPasswordDeAdministrador(pass);
			if (usrAdmin!=null) {
				JDialogDarSalidaCheque jddsc = new JDialogDarSalidaCheque(e.getSource().getFrame(), e.getSelectedElements().get(0));
				jddsc.setVisible(true);
				if(jddsc.isAcepto()){
					Cheque chequeNuevo = jddsc.getCheque();
					for(Cheque c : e.getSelectedElements()){
						c.setEstadoCheque(chequeNuevo.getEstadoCheque());
						if(chequeNuevo.getProveedorSalida()!=null){
							c.setProveedorSalida(chequeNuevo.getProveedorSalida());
						}else if(chequeNuevo.getClienteSalida()!=null){
							c.setClienteSalida(chequeNuevo.getCliente());
						}else if(chequeNuevo.getPersonaSalida()!=null) {
							c.setPersonaSalida(chequeNuevo.getPersonaSalida());
						}else{
							c.setBancoSalida(chequeNuevo.getBancoSalida());
						}
						c.setFechaSalida(DateUtil.getHoy());
						GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class).grabarCheque(c,usrAdmin.getUsrName());
					}
					return true;
				}else{
					return false;
				}
			}else {
				CLJOptionPane.showErrorMessage(e.getSource().getFrame(), "La clave ingresada no peternece a un usuario adminsitrador", "Error");
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return 	e.getSelectedElements().size()>0 && 
			   ( e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.EN_CARTERA ||
				 e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.SALIDA_BANCO || 
				 e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.SALIDA_PROVEEDOR || 
				 e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.SALIDA_CLIENTE || 
				 e.getSelectedElements().get(0).getEstadoCheque() == EEstadoCheque.SALIDA_PERSONA);
	}
}
