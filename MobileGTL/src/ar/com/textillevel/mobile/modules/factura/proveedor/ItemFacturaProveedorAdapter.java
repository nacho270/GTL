package ar.com.textillevel.mobile.modules.factura.proveedor;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.ItemDocumentoProvMobTO;

public class ItemFacturaProveedorAdapter extends Adapter<ItemFacturaProveedorViewHolder> {

	private List<ItemDocumentoProvMobTO> itemDocumentoProveedorList;
	
	public ItemFacturaProveedorAdapter(List<ItemDocumentoProvMobTO> itemFacturaList) {
		this.itemDocumentoProveedorList = itemFacturaList;
	}
	
	@Override
	public int getItemCount() {
		return itemDocumentoProveedorList.size();
	}

	@Override
	public void onBindViewHolder(ItemFacturaProveedorViewHolder itemFacturaViewHolder, int i) {
		ItemDocumentoProvMobTO itemDocProvTO = itemDocumentoProveedorList.get(i);
		itemFacturaViewHolder.cantidad.setText(itemDocProvTO.getCantidadMasUnidad());
		itemFacturaViewHolder.descripcion.setText(itemDocProvTO.getDescripcion());
		itemFacturaViewHolder.descuento.setText(itemDocProvTO.getDescuento() == null ? "" : itemDocProvTO.getDescuento());
		itemFacturaViewHolder.precioUnitario.setText(itemDocProvTO.getPrecioUnitario());
		itemFacturaViewHolder.factor.setText(itemDocProvTO.getFactor());
		itemFacturaViewHolder.importe.setText(itemDocProvTO.getImporte());
	}

	@Override
	public ItemFacturaProveedorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_factura_prov, viewGroup, false);
		 return new ItemFacturaProveedorViewHolder(itemView);
	}

	
	
}