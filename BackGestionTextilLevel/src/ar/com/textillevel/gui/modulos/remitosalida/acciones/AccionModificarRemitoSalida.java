package ar.com.textillevel.gui.modulos.remitosalida.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.RemitoSalida;
import main.acciones.facturacion.OperacionSobreRemitoSalidaHandler;

public class AccionModificarRemitoSalida extends Accion<RemitoSalida> {

	public AccionModificarRemitoSalida(){
		setNombre("Modificar Remito de Salida");
		setDescripcion("Permite modificart un remito de salida"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_modificar_fila.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_modificar_fila_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<RemitoSalida> e) throws FWException {
		RemitoSalida remitoEntrada = e.getSelectedElements().get(0);
		OperacionSobreRemitoSalidaHandler consultaREHandler = new OperacionSobreRemitoSalidaHandler(e.getSource().getFrame(), remitoEntrada, false);
		consultaREHandler.showRemitoEntradaDialog();
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<RemitoSalida> e) {
		return e.getSelectedElements().size() == 1 && (e.getSelectedElements().get(0).getAnulado() == null || !e.getSelectedElements().get(0).getAnulado());
	}

}