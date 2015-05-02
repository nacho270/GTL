package ar.com.textillevel.mobile.modules.remito.salida;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import ar.com.textillevel.mobile.R;

public class PiezaRemitoSalidaViewHolder extends ViewHolder {

	protected TextView nro_pieza;
	protected TextView metros_pieza;
	protected TextView observaciones;
	protected TextView metros_entrada;

	public PiezaRemitoSalidaViewHolder(View v) {
		super(v);
		this.metros_entrada = (TextView) v.findViewById(R.id.rs_metros_entrada);
		this.nro_pieza = (TextView) v.findViewById(R.id.rs_nro_pieza);
		this.metros_pieza = (TextView) v.findViewById(R.id.rs_metros_pieza);
		this.observaciones = (TextView) v.findViewById(R.id.rs_observaciones);
	}

}