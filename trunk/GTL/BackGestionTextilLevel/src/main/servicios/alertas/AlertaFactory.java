package main.servicios.alertas;

import ar.clarin.fwjava.util.DateUtil;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.modulos.alertas.entidades.AlertaFaltaStock;
import ar.com.textillevel.modulos.alertas.enums.ETipoAlerta;
import ar.com.textillevel.modulos.alertas.facade.api.remote.TipoAlertaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class AlertaFactory {
	public static AlertaFaltaStock crearAlertaStock(PrecioMateriaPrima pmp){
		AlertaFaltaStock afs = new AlertaFaltaStock();
		afs.setFechaMinimaParaMostrar(DateUtil.getAhora());
		afs.setPrecioMateriaPrima(pmp);
		afs.setTipoAlerta(GTLBeanFactory.getInstance().getBean2(TipoAlertaFacadeRemote.class).getReferenceById(ETipoAlerta.STOCK.getId()));
		return afs;
	}
}
