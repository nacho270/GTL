package ar.com.textillevel.gui.modulos.personal.modulos.vales.acciones;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.gui.modulos.personal.modulos.legajos.impresion.valeanticipo.ImpresionValeAnticipoHandler;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.vale.ValeAnticipo;

public class AccionImprimirValeAnticipo extends Accion<ValeAnticipo> {

	public AccionImprimirValeAnticipo() {
		setNombre("Imprimir vale");
		setDescripcion("Imprime el vale de anticipo seleccionado.");
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_imprimir_moderno.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_imprimir_moderno_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ValeAnticipo> e) throws FWException {
		ImpresionValeAnticipoHandler handler = new ImpresionValeAnticipoHandler(e.getSelectedElements().get(0),e.getSource().getFrame());
		handler.imprimir();
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<ValeAnticipo> e) {
		return e.getSelectedElements().size()==1;
	}
}
