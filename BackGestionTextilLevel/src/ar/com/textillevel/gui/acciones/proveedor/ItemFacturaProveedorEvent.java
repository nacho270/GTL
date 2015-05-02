package ar.com.textillevel.gui.acciones.proveedor;

import java.util.EventObject;
import java.util.List;
import ar.com.textillevel.entidades.documentos.factura.proveedor.ItemFacturaProveedor;

public class ItemFacturaProveedorEvent extends EventObject {

	private static final long serialVersionUID = -3696293201604466573L;
	private List<ItemFacturaProveedor> itemFacturaList;

	public ItemFacturaProveedorEvent(Object source, List<ItemFacturaProveedor> itemFacturaList) {
		super(source);
		this.itemFacturaList = itemFacturaList;
	}

	public List<ItemFacturaProveedor> getItemFacturaList() {
		return itemFacturaList;
	}

}
