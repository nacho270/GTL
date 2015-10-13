package ar.com.textillevel.gui.modulos.cheques.acciones;

import main.GTLGlobalCache;
import ar.com.fwcommon.boss.BossError;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionAgregarCheque extends Accion<Cheque>{

	public AccionAgregarCheque(){
		setNombre("Agregar Cheque");
		setDescripcion("Permite dar de alta un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/fwcommon/imagenes/b_agregar.png");
		setImagenInactivo("ar/com/fwcommon/imagenes/b_agregar_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws FWException {
		JDialogAgregarCheque dialogAgregarCheque = new JDialogAgregarCheque(e.getSource().getFrame());
		boolean acepto = dialogAgregarCheque.isAcepto();
		if(acepto){
			ChequeFacadeRemote cfr = GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class);
			try{
				Cheque cheque = dialogAgregarCheque.getCheque();
				cfr.grabarCheque(cheque, GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
				boolean agregaOtro = dialogAgregarCheque.isAgregaOtro();
				if(agregaOtro){
					do{
						dialogAgregarCheque = new JDialogAgregarCheque(e.getSource().getFrame(),cheque,false,true);
						cheque = dialogAgregarCheque.getCheque();
						acepto = dialogAgregarCheque.isAcepto();
						agregaOtro = dialogAgregarCheque.isAgregaOtro();
						if(acepto){
							cfr.grabarCheque(cheque,GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
							FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "El cheque se ha guardado con éxito", "Alta de cheque");
						}
					}while(agregaOtro && acepto);
				}else{
					FWJOptionPane.showInformationMessage(e.getSource().getFrame(), "El cheque se ha guardado con éxito", "Alta de cheque");
				}
				return true;
			}catch(FWException cle){
				BossError.gestionarError(cle);
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin();
	}

}
