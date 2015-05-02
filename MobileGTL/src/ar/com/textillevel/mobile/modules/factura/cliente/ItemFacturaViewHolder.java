package ar.com.textillevel.mobile.modules.factura.cliente;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import ar.com.textillevel.mobile.R;

public class ItemFacturaViewHolder  extends ViewHolder {
	
	protected TextView cantidad;
	protected TextView descripcion;
	protected TextView precioUnitario;
	protected TextView importe;

	public ItemFacturaViewHolder(View v) {
		super(v);
		this.cantidad = (TextView)v.findViewById(R.id.cantidad_item);
		this.descripcion = (TextView)v.findViewById(R.id.descripcion_item);
		this.precioUnitario = (TextView)v.findViewById(R.id.pu_item);
		this.importe = (TextView)v.findViewById(R.id.importe_item);
	}

}