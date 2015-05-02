package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.Set;

import ar.clarin.fwjava.componentes.error.CLException;
import ar.clarin.fwjava.templates.modulo.model.acciones.Accion;
import ar.clarin.fwjava.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogDetallePiezasFisicas;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class AccionVerDetallePiezasTerminadas extends Accion<ItemMateriaPrimaTO>{

	public AccionVerDetallePiezasTerminadas() {
		setNombre("Detalle de piezas terminadas");
		setDescripcion("Permite ver las piezas terminadas que conforman el stock actual.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_detalle_piezas_terminadas.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_detalle_piezas_terminadas_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws CLException {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		JDialogDetallePiezasFisicas jDialogDetallePiezasFisicas = new JDialogDetallePiezasFisicas(e.getSource().getFrame(), ((ItemMateriaPrimaTelaTO)e.getSelectedElements().get(0)).getArticulo(),ETipoTela.TERMINADA, moduloModel.getPiezasElegidas());
		jDialogDetallePiezasFisicas.setVisible(true);
		Set<DetallePiezaFisicaTO> piezasSelected = jDialogDetallePiezasFisicas.getPiezasSelected();
		if(!piezasSelected.isEmpty()) {
			moduloModel.getPiezasElegidas().addAll(piezasSelected);
		}
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<ItemMateriaPrimaTO> e) {
		return e.getSelectedElements().size()==1;
	}

}
