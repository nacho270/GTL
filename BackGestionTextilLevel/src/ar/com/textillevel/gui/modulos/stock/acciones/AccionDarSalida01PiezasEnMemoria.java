package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.ArrayList;
import main.acciones.facturacion.IngresoRemitoSalidaHandler;
import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class AccionDarSalida01PiezasEnMemoria extends Accion<ItemMateriaPrimaTO> {

	public AccionDarSalida01PiezasEnMemoria() {
		setNombre("Salida 01 a piezas en memoria");
		setDescripcion("Permite dar salida 01 a las piezas seleccionadas en memoria.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_salida.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_salida_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws CLException {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(e.getSource().getFrame(), ETipoRemitoSalida.CLIENTE_SALIDA_01, false, new ArrayList<DetallePiezaFisicaTO>(moduloModel.getPiezasElegidas()));
		if(rsHandler.gestionarIngresoRemitoSalida() != null) {
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