package ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones;

import main.GTLGlobalCache;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.enums.EEstadoValeAnticipo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ValeAnticipoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class AccionMarcarValeUtilizado extends Accion<ValeAnticipo>{

	public AccionMarcarValeUtilizado(){
		setNombre("Marcar como utilizado");
		setDescripcion("Marca el vale seleccionado como utilizado."); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_verificar_stock.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_verificar_stock_des.png");
	}
	
	@Override
	public boolean ejecutar(AccionEvent<ValeAnticipo> e) throws FWException {
		ValeAnticipo vale = e.getSelectedElements().get(0);
		vale.setEstadoValeAnticipo(EEstadoValeAnticipo.DESCONTADO);
		GTLPersonalBeanFactory.getInstance().getBean2(ValeAnticipoFacadeRemote.class).acutalizarVale(vale);
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<ValeAnticipo> e) {
		return e.getSelectedElements().size()==1 && e.getSelectedElements().get(0).getEstadoVale()==EEstadoValeAnticipo.A_DESCONTAR
				&& GTLGlobalCache.getInstance().getUsuarioSistema().getPerfil().getIsAdmin().booleanValue()==true;
	}
}
