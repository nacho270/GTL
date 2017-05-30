package ar.com.textillevel.gui.modulos.odt.acciones;

import java.util.ArrayList;
import java.util.List;
import org.apache.taglibs.string.util.StringW;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.error.FWException;
import ar.com.fwcommon.templates.modulo.model.acciones.Accion;
import ar.com.fwcommon.templates.modulo.model.listeners.AccionEvent;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.gui.acciones.JDialogSeleccionarProducto;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;
import ar.com.textillevel.modulos.odt.facade.api.remote.OrdenDeTrabajoFacadeRemote;
import ar.com.textillevel.modulos.odt.to.ODTTO;
import ar.com.textillevel.util.GTLBeanFactory;
import main.GTLGlobalCache;

public class AccionAsignarProductoArticuloODT extends Accion<ODTTO> {
	
	public AccionAsignarProductoArticuloODT(){
		setNombre("Permite asignar un producto");
		setDescripcion("Permite asignar un producto"); 
		setTooltip(crearTooltip(getNombre(), getDescripcion(), null));
		setImagenActivo("ar/com/textillevel/imagenes/b_asignar_producto_articulo_odt.png");
		setImagenInactivo("ar/com/textillevel/imagenes/b_asignar_producto_articulo_odt_des.png");
	}

	@Override
	public boolean ejecutar(AccionEvent<ODTTO> e) throws FWException {
		ODTTO odtto = e.getSelectedElements().get(0);
		OrdenDeTrabajoFacadeRemote odtFacade = GTLBeanFactory.getInstance().getBean2(OrdenDeTrabajoFacadeRemote.class);
		OrdenDeTrabajo odt = odtFacade.getByIdEager(odtto.getId());
		
		JDialogSeleccionarProducto dialogSeleccionarProducto = new JDialogSeleccionarProducto(e.getSource().getFrame(), odt.getRemito().getCliente(), new ArrayList<ProductoArticulo>());
		GuiUtil.centrar(dialogSeleccionarProducto);
		dialogSeleccionarProducto.setVisible(true);
		if(dialogSeleccionarProducto.isAcepto()) {
			List<ProductoArticulo> productoSelectedList = dialogSeleccionarProducto.getProductoSelectedList();
			if(productoSelectedList.size()>1) {
				FWJOptionPane.showErrorMessage(e.getSource().getFrame(), StringW.wordWrap("Debe seleccionar SÓLO un producto"), "Error");
				return false;
			} else if(productoSelectedList.size() == 1) {
				ProductoArticulo productoArticulo = productoSelectedList.get(0);
				odtFacade.asignarProductoArticuloODT(odt, productoArticulo, GTLGlobalCache.getInstance().getUsuarioSistema());
				FWJOptionPane.showInformationMessage(e.getSource().getFrame(), StringW.wordWrap("Se asignó con éxito el producto '" + productoArticulo + "'."), "Información");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean esValida(AccionEvent<ODTTO> e) {
		List<ODTTO> selectedElements = e.getSelectedElements();
		if(selectedElements.isEmpty() || selectedElements.size() != 1) {
			return false;
		}
		ODTTO odtto = selectedElements.get(0);
		return odtto.isParcial();
	}

}