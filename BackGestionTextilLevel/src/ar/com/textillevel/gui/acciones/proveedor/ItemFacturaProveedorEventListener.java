package ar.com.textillevel.gui.acciones.proveedor;

import java.util.EventListener;

public interface ItemFacturaProveedorEventListener extends EventListener {

	public abstract void changeItemFactura(ItemFacturaProveedorEvent evt);

}
