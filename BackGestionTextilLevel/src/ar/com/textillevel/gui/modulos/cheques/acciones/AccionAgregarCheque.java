package ar.com.textillevel.gui.modulos.cheques.acciones;

import main.GTLGlobalCache;
import ar.clarin.fwjava.boss.BossError;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.gui.modulos.cheques.gui.JDialogAgregarCheque;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionAgregarCheque extends Accion<Cheque>{

	public AccionAgregarCheque(){
		setNombre("Agregar Cheque");
		setDescripcion("Permite dar de alta un cheque"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/clarin/fwjava/imagenes/b_agregar.png");
		setImagenInactivo("ar/clarin/fwjava/imagenes/b_agregar_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws CLException {
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
							CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "El cheque se ha guardado con éxito", "Alta de cheque");
						}
					}while(agregaOtro && acepto);
				}else{
					CLJOptionPane.showInformationMessage(e.getSource().getFrame(), "El cheque se ha guardado con éxito", "Alta de cheque");
				}
				return true;
			}catch(CLException cle){
				BossError.gestionarError(cle);
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return true;
	}
}
