package ar.com.textillevel.gui.modulos.stock.acciones;

import java.util.Set;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.enums.ETipoTela;
import ar.com.textillevel.entidades.ventas.DetallePiezaFisicaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTO;
import ar.com.textillevel.entidades.ventas.materiaprima.ItemMateriaPrimaTelaTO;
import ar.com.textillevel.gui.modulos.stock.gui.JDialogDetallePiezasFisicas;
import ar.com.textillevel.gui.modulos.stock.model.ModuloStockTelasFisicasModel;

public class AccionVerDetallePiezasCrudas extends Accion<ItemMateriaPrimaTO>{

	public AccionVerDetallePiezasCrudas() {
		setNombre("Detalle de piezas crudas");
		setDescripcion("Permite ver las piezas crudas que conforman el stock actual.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_cobrar_cheque.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_cobrar_cheque_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ItemMateriaPrimaTO> e) throws FWException {
		ModuloStockTelasFisicasModel moduloModel = (ModuloStockTelasFisicasModel)e.getSource().getModulosModel().get(1);
		JDialogDetallePiezasFisicas jDialogDetallePiezasFisicas = new JDialogDetallePiezasFisicas(e.getSource().getFrame(), ((ItemMateriaPrimaTelaTO)e.getSelectedElements().get(0)).getArticulo(),ETipoTela.CRUDA, moduloModel.getPiezasElegidas());
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
