package ar.com.textillevel.mobile.modules.factura;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.factura.cliente.to.ItemFacturaTO;

public class ItemFacturaAdapter extends Adapter<ItemFacturaViewHolder> {

	private List<ItemFacturaTO> itemFacturaList;
	
	public ItemFacturaAdapter(List<ItemFacturaTO> itemFacturaList) {
		this.itemFacturaList = itemFacturaList;
	}
	
	@Override
	public int getItemCount() {
		return itemFacturaList.size();
	}

	@Override
	public void onBindViewHolder(ItemFacturaViewHolder itemFacturaViewHolder, int i) {
		ItemFacturaTO itemFacturaTO = itemFacturaList.get(i);
		itemFacturaViewHolder.cantidad.setText(itemFacturaTO.getCantidad() + " " + itemFacturaTO.getUnidad());
		itemFacturaViewHolder.descripcion.setText(itemFacturaTO.getDescripcion());
		itemFacturaViewHolder.precioUnitario.setText(itemFacturaTO.getPrecioUnitario());
		itemFacturaViewHolder.importe.setText(itemFacturaTO.getImporte());
	}

	@Override
	public ItemFacturaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_factura, viewGroup, false);
		 return new ItemFacturaViewHolder(itemView);
	}

}