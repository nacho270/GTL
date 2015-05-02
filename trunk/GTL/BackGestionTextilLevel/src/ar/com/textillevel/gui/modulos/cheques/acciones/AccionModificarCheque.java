package ar.com.textillevel.gui.modulos.cheques.acciones;

import main.GTLGlobalCache;
import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionModificarCheque extends Accion<Cheque>{

	public AccionModificarCheque(){
		setNombre("Modificar Cheque");
		setDescripcion("Permite modificar un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_modificar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_modificar_cheque_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
		Cheque cheque = e.getSelectedElements().get(0);
		JDialogAgregarCheque dialogAgregarCheque = new JDialogAgregarCheque(e.getSource().getFrame(), cheque,false,false);
		if(dialogAgregarCheque.isAcepto()){
			ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
			try{
				cfr.grabarCheque(dialogAgregarCheque.getCheque(),GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "El cheque se ha guardado con éxito", "Modificar de cheque");
				return true;
			}catch(CLException cle){
				BossError.gestionarError(cle);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1 && (e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.PENDIENTE_COBRAR
				|| e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.EN_CARTERA);
	}
}
