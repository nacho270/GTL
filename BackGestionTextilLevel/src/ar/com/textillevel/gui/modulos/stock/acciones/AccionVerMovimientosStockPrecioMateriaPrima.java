package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.List;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;
import ar.com.textillevel.facade.api.remote.PrecioMateriaPrimaFacadeRemote;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogDetalleStock;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogVerMovimientosStock;
import ar.com.textillevel.util.GTLBeanFactory;

public class AccionVerMovimientosStockPrecioMateriaPrima extends Accion<ItemMateriaPrimaTO> {

	public AccionVerMovimientosStockPrecioMateriaPrima() {
		setNombre("Ver Movimientos");
		setDescripcion("Permite ver los movimientos que generaron el stock actual");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_verificar_stock.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_verificar_stock_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws FWException {
		ItemMateriaPrimaTO item = e.getSelectedElements().get(0);
		List<PrecioMateriaPrima> lista = GTLBeanFactory.getInstance().getBean2(PrecioMateriaPrimaFacadeRemote.class).getPrecioMateriaPrimaByIdsMateriasPrimas(item.getIdsMateriasPrimas());
		if (lista != null && !lista.isEmpty()) {
			if (lista.size() == 1) { // Si hay un solo PrecioMateriaPrima, me salteo el paso de ver el detalle voy directo a los movimientos
				PrecioMateriaPrima pm = lista.get(0);
				JDialogVerMovimientosStock jdvms = new JDialogVerMovimientosStock(e.getSource().getFrame(), pm);
				jdvms.setVisible(true);
			} else if(lista.size() > 1) { // hay varios PrecioMateriaPrima, voy al detalle y despues voy a los movimientos
				JDialogDetalleStock jdds = new JDialogDetalleStock(e.getSource().getFrame(), lista);
				jdds.setVisible(true);
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<ItemMateriaPrimaTO> e) {
		return e.getSelectedElements().size() == 1;
	}
}
