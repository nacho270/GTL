package ar.com.textillevel.mobile.modules.factura.proveedor;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import ar.com.textillevel.mobile.R;

public class ItemFacturaProveedorViewHolder  extends ViewHolder {
	
	protected TextView cantidad;
	protected TextView descripcion;
	protected TextView descuento;
	protected TextView precioUnitario;
	protected TextView factor;
	protected TextView importe;

	public ItemFacturaProveedorViewHolder(View v) {
		super(v);
		this.cantidad = (TextView)v.findViewById(R.id.cantidad_item_fp);
		this.descripcion = (TextView)v.findViewById(R.id.descripcion_item_fp);
		this.descuento = (TextView)v.findViewById(R.id.desc_item_fp);
		this.precioUnitario = (TextView)v.findViewById(R.id.item_pu_fp);
		this.factor = (TextView)v.findViewById(R.id.item_factor_fp);
		this.importe = (TextView)v.findViewById(R.id.item_importe_fp);
	}

}