package ar.com.textillevel.gui.acciones.proveedor;

import java.util.EventObject;
import java.util.List;

import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemCorreccionFacturaProveedor;

public class ItemCorreccionFacturaProveedorEvent extends EventObject {

	private static final long serialVersionUID = 6036530764683148523L;

	private List<ItemCorreccionFacturaProveedor> itemList;

	public ItemCorreccionFacturaProveedorEvent(Object source, List<ItemCorreccionFacturaProveedor> itemList) {
		super(source);
		this.itemList = itemList;
	}

	public List<ItemCorreccionFacturaProveedor> getItemList() {
		return itemList;
	}

}
