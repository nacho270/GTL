package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.ArrayList;

import main.acciones.facturacion.IngresoRemitoSalidaHandler;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.enums.ETipoRemitoSalida;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class AccionVentaDeTelaPiezasEnMemoria extends Accion<ItemMateriaPrimaTO> {

	public AccionVentaDeTelaPiezasEnMemoria() {
		setNombre("Venta de piezas en memoria");
		setDescripcion("Permite vender las piezas seleccionadas en memoria.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_venta.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_venta_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws FWException {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		IngresoRemitoSalidaHandler rsHandler = new IngresoRemitoSalidaHandler(e.getSource().getFrame(), ETipoRemitoSalida.CLIENTE_VENTA_DE_TELA, false, new ArrayList<DetallePiezaFisicaTO>(moduloModel.getPiezasElegidas()));
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