package ar.com.textillevel.gui.modulos.stock.acciones;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class AccionBorrarPiezasEnMemoria extends Accion<ItemMateriaPrimaTO> {

	public AccionBorrarPiezasEnMemoria() {
		setNombre("Borrar piezas en memoria");
		setDescripcion("Permite borrar las piezas seleccionadas en memoria.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_eliminar.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_eliminar_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws CLException {
		if(CLJOptionPane.showQuestionMessage(e.getSource().getFrame(), "¿Está seguro que desea eliminar las piezas en memoria?", "Confirmación") == CLJOptionPane.YES_OPTION) {
			ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
			moduloModel.getPiezasElegidas().clear();
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<ItemMateriaPrimaTO> e) {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		return !moduloModel.getPiezasElegidas().isEmpty();
	}

}