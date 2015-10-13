package ar.com.textillevel.gui.modulos.cheques.acciones;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.cheque.Cheque;
import ar.com.textillevel.entidades.enums.EEstadoCheque;
import ar.com.textillevel.facade.api.remote.ChequeFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionPonerChequeRechazadoEnCartera extends Accion<Cheque> {

	public AccionPonerChequeRechazadoEnCartera(){
		setNombre("Marcar cheque devuelto/en cartera");
		setDescripcion("Marca el cheque seleccionado como rechazado/en cartera"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_cheque_cartera.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_cheque_cartera_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<Cheque> e) throws FWException {
		Cheque cheque = e.getSelectedElements().get(0);
		cheque.setEstadoCheque(EEstadoCheque.RECHAZADO_CARTERA);
		GTLBeanFactory.getInstance().getBean2(ChequeFacadeRemote.class).grabarCheque(cheque,GTLGlobalCache.getInstance().getUsuarioSistema().getUsrName());
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<Cheque> e) {
		return e.getSelectedElements().size()==1 && e.getSelectedElements().get(0).getEstadoCheque()==EEstadoCheque.RECHAZADO;
	}
}
