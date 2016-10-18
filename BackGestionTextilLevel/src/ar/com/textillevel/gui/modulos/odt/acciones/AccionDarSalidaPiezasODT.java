package ar.com.textillevel.gui.modulos.odt.acciones;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.acciones.facturacion.IngresoRemitoSalidaNormalHandler;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class AccionDarSalidaPiezasODT extends Accion<OrdenDeTrabajo> {

	public AccionDarSalidaPiezasODT(){
		setNombre("Permite dar salida a las ODTs seleccionadas");
		setDescripcion("Permite dar salida a las ODTs seleccionadas"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_salida.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_salida_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<OrdenDeTrabajo> e) throws FWException {
		Set<Cliente> extractClientes = extractClientes(e.getSelectedElements()); //tiene que haber uno solo!!
		IngresoRemitoSalidaNormalHandler handler = new IngresoRemitoSalidaNormalHandler(e.getSource().getFrame(), extractClientes.iterator().next(), e.getSelectedElements());
		handler.gestionarIngresoRemitoSalida();
		return true;
	}

	@Override
	public boolean esValida(AccionEvent<OrdenDeTrabajo> e) {
		List<OrdenDeTrabajo> selectedElements = e.getSelectedElements();
		if(selectedElements.isEmpty()) {
			return false;
		}
		Set<Cliente> clientes = extractClientes(selectedElements);
		if(clientes.size() != 1) {
			return false; //las ODTs deben ser todas del mismo cliente
		}
		for(OrdenDeTrabajo odt : selectedElements) {
			if(!odt.puedeDarSalida()) {
				return false;
			}
		}
		return true;
	}

	private Set<Cliente> extractClientes(List<OrdenDeTrabajo> selectedElements) {
		Set<Cliente> clientes = new HashSet<Cliente>();
		for(OrdenDeTrabajo odt : selectedElements) {
			clientes.add(odt.getRemito().getCliente());
		}
		return clientes;
	}

}