package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.List;

import ar.clarin.fwjava.templates.modulo.model.accionesmouse.AccionAdicional;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogDetalleStock;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogVerMovimientosStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionDobleClickVerMovimientosStock extends AccionAdicional<ItemMateriaPrimaTO>{

	@Override
	protected void update(AccionEvent<ItemMateriaPrimaTO> e) {
		ItemMateriaPrimaTO item = e.getSelectedElements().get(0);
		List<PrecioMateriaPrima> lista = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getPrecioMateriaPrimaByIdsMateriasPrimas(item.getIdsMateriasPrimas());
		if (lista != null && !lista.isEmpty()) {
			if (lista.size() == 1) { // Si hay un solo PrecioMateriaPrima, me salteo el paso de ver el detalle voy directo a los movimientos
				PrecioMateriaPrima pm = lista.get(0);
				JDialogVerMovimientosStock jdvms = new JDialogVerMovimientosStock(e.getSource().getFrame(), pm);
				jdvms.setVisible(true);
			} else if(lista.size() > 1)  { // hay varios PrecioMateriaPrima, voy al detalle y despues voy a los movimientos
				JDialogDetalleStock jdds = new JDialogDetalleStock(e.getSource().getFrame(), lista);
				jdds.setVisible(true);
			}
		}
	}
}
