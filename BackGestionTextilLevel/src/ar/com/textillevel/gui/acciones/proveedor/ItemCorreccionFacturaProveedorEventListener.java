package ar.com.textillevel.gui.acciones.proveedor;

import java.util.EventListener;

public interface ItemCorreccionFacturaProveedorEventListener extends EventListener {

	public abstract void changeItemFactura(ItemCorreccionFacturaProveedorEvent evt);

}
