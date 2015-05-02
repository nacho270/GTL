package ar.com.textillevel.gui.modulos.stock.status;

import java.util.Set;

import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.clarin.fwjava.templates.modulo.model.status.Status;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class StatusPiezasMemoria extends Status<ItemMateriaPrimaTO> {

	@Override
	protected void update(AccionEvent<ItemMateriaPrimaTO> e) {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		double metrosCrudo = getMetros(moduloModel.getPiezasElegidas(), ETipoTela.CRUDA);
		double metrosTerm = getMetros(moduloModel.getPiezasElegidas(), ETipoTela.TERMINADA);
		setValue("Piezas en memoria: " + (metrosCrudo + metrosTerm) + " [CRUDAS : " + metrosCrudo + ", TERMINADAS: " + metrosTerm + "]");
	}

	private double getMetros(Set<DetallePiezaFisicaTO> piezasElegidasList, ETipoTela tipoTela) {
		double metros = 0;
		for(DetallePiezaFisicaTO d : piezasElegidasList) {
			if(tipoTela == ETipoTela.TERMINADA) {
				if(d.getOdt() != null) {
					metros += d.getMetros().doubleValue(); 
				}
			} else {
				if(d.getOdt() == null) {
					metros += d.getMetros().doubleValue(); 
				}
			}
		}
		return metros;
	}

}