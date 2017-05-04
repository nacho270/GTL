package ar.com.textillevel.gui.modulos.odt.acciones;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.textillevel.entidades.documentos.remito.to.DetallePiezaRemitoEntradaSinSalida;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.facade.api.remote.ClienteFacadeRemote;
import ar.com.textillevel.gui.acciones.RemitoEntradaBusinessDelegate;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.enums.EEstadoODT;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;
import main.acciones.facturacion.IngresoRemitoSalidaNormalHandler;

public class AccionDarSalidaPiezasODT extends Accion<ODTTO> {

	private RemitoEntradaBusinessDelegate delegate;
	
	private ClienteFacadeRemote clienteFacade;
	
	private RemitoEntradaBusinessDelegate getDelegate() {
		if(delegate == null) {
			delegate = new RemitoEntradaBusinessDelegate();
		}
		return delegate;
	}

	public ClienteFacadeRemote getClienteFacade() {
		if(clienteFacade == null) {
			clienteFacade = GTLBeanFactory.getInstance().getBean2(ClienteFacadeRemote.class); 
		}
		return clienteFacade;
	}
	
	public AccionDarSalidaPiezasODT(){
		setNombre("Permite dar salida a las ODTs seleccionadas");
		setDescripcion("Permite dar salida a las ODTs seleccionadas"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_salida.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_salida_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		Set<Integer> extractClientes = extractClientes(e.getSelectedElements()); //tiene que haber uno solo!!
		Cliente cl = getClienteFacade().getClienteByNumero(extractClientes.iterator().next());
		try {
			List<OrdenDeTrabajo> odts = getDelegate().getODTByIdsEager(toDetallePiezaODT(e.getSelectedElements()));
			setIdsEnNULL(odts);
			IngresoRemitoSalidaNormalHandler handler = new IngresoRemitoSalidaNormalHandler(e.getSource().getFrame(), cl, odts);
			handler.gestionarIngresoRemitoSalida();
			return true;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	private void setIdsEnNULL(List<OrdenDeTrabajo> odtByIdsEager) {
		for(OrdenDeTrabajo odt : odtByIdsEager) {
			if(odt.isNoLocal()) {
				odt.setId(null);
			}
		}
	}

	private List<DetallePiezaRemitoEntradaSinSalida> toDetallePiezaODT(List<ODTTO> odts) {
		List<DetallePiezaRemitoEntradaSinSalida> result = new ArrayList<DetallePiezaRemitoEntradaSinSalida>();
		for(ODTTO odt : odts) {
			DetallePiezaRemitoEntradaSinSalida d = new DetallePiezaRemitoEntradaSinSalida();
			d.setIdODT(odt.getId());
			d.setNoLocales(false);
			result.add(d);
		}
		return result;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		List<ODTTO> selectedElements = e.getSelectedElements();
		if(selectedElements.isEmpty()) {
			return false;
		}
		Set<Integer> clientes = extractClientes(selectedElements);
		if(clientes.size() != 1) {
			return false; //las ODTs deben ser todas del mismo cliente
		}
		for(ODTTO odt : selectedElements) {
			if(odt.getEstado() != EEstadoODT.EN_OFICINA) {
				return false;
			}
		}
		return true;
	}

	private Set<Integer> extractClientes(List<ODTTO> selectedElements) {
		Set<Integer> clientes = new HashSet<Integer>();
		for(ODTTO odt : selectedElements) {
			clientes.add(odt.getNroCliente());
		}
		return clientes;
	}

}