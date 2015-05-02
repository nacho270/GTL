package ar.com.textillevel.mobile.modules.remito.entrada.cliente;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import ar.com.textillevel.mobile.R;

public class PiezaRemitoViewHolder extends ViewHolder {

	protected TextView nro_pieza;
	protected TextView metros_pieza;
	protected TextView observaciones;
	protected TextView odt;

	public PiezaRemitoViewHolder(View v) {
		super(v);
		this.nro_pieza = (TextView) v.findViewById(R.id.nro_pieza);
		this.metros_pieza = (TextView) v.findViewById(R.id.metros_pieza);
		this.observaciones = (TextView) v.findViewById(R.id.observaciones);
		this.odt = (TextView) v.findViewById(R.id.odt);
	}

}