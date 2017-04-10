package ar.com.textillevel.gui.modulos.remitoentrada.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoEntrada;
import main.acciones.facturacion.OperacionSobreRemitoEntradaHandler;

public class AccionModificarRemitoEntrada extends Accion<RemitoEntrada> {

	public AccionModificarRemitoEntrada(){
		setNombre("Modificar Remito de Entrada");
		setDescripcion("Permite modificart un remito de entrada"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_modificar_fila.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_modificar_fila_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoEntrada> e) throws FWException {
		RemitoEntrada remitoEntrada = e.getSelectedElements().get(0);
		OperacionSobreRemitoEntradaHandler consultaREHandler = new OperacionSobreRemitoEntradaHandler(e.getSource().getFrame(), remitoEntrada, false);
		consultaREHandler.showRemitoEntradaDialog();
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoEntrada> e) {
		return e.getSelectedElements().size() == 1;
	}

}