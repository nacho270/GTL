package ar.com.textillevel.mobile.modules.remito.entrada.proveedor;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import ar.com.textillevel.mobile.R;

public class PiezaRemitoProveedorViewHolder extends ViewHolder {

	protected TextView cantidad;
	protected TextView descripcion;
	protected TextView contenedor;
	protected TextView cant_contenedor;

	public PiezaRemitoProveedorViewHolder(View v) {
		super(v);
		this.cantidad = (TextView) v.findViewById(R.id.re_p_item_cantidad);
		this.descripcion = (TextView) v.findViewById(R.id.re_p_item_descripcion);
		this.contenedor = (TextView) v.findViewById(R.id.re_p_item_cont);
		this.cant_contenedor = (TextView) v.findViewById(R.id.re_p_item_cant_cont);
	}

}