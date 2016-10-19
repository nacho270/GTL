package ar.com.textillevel.gui.modulos.odt.acciones;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.acciones.facturacion.IngresoRemitoSalidaNormalHandler;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.gui.acciones.RemitoEntradaBusinessDelegate;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class AccionDarSalidaPiezasODT extends Accion<OrdenDeTrabajo> {

	private RemitoEntradaBusinessDelegate delegate;
	
	private RemitoEntradaBusinessDelegate getDelegate() {
		if(delegate == null) {
			delegate = new RemitoEntradaBusinessDelegate();
		}
		return delegate;
	}

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
		try {
			List<OrdenDeTrabajo> odts = getDelegate().getODTByIdsEager(toDetallePiezaODT(e.getSelectedElements()));
			IngresoRemitoSalidaNormalHandler handler = new IngresoRemitoSalidaNormalHandler(e.getSource().getFrame(), extractClientes.iterator().next(), odts);
			handler.gestionarIngresoRemitoSalida();
			return true;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	private List<DetallePiezaRemitoEntradaSinSalida> toDetallePiezaODT(List<OrdenDeTrabajo> odts) {
		List<DetallePiezaRemitoEntradaSinSalida> result = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
		for(OrdenDeTrabajo odt : odts) {
			DetallePiezaRemitoEntradaSinSalida d = new DetallePiezaRemitoEntradaSinSalida();
			d.setIdODT(odt.getId());
			d.setNoLocales(odt.isNoLocal());
			result.add(d);
		}
		return result;
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