package ar.com.textillevel.mobile.modules.remito.salida;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.PiezaRemitoMobileTO;

public class PiezaRemitoSalidaAdapter extends Adapter<PiezaRemitoSalidaViewHolder> {

	private List<PiezaRemitoMobileTO> piezasList;
	
	public PiezaRemitoSalidaAdapter(List<PiezaRemitoMobileTO> piezasList) {
		this.piezasList = piezasList;
	}
	
	@Override
	public int getItemCount() {
		return piezasList.size();
	}

	@Override
	public void onBindViewHolder(PiezaRemitoSalidaViewHolder itemFacturaViewHolder, int i) {
		PiezaRemitoMobileTO piezaTO = piezasList.get(i);
		itemFacturaViewHolder.metros_entrada.setText(piezaTO.getMetrosEntrada());
		itemFacturaViewHolder.nro_pieza.setText(piezaTO.getNumero());
		itemFacturaViewHolder.metros_pieza.setText(piezaTO.getMetros());
		itemFacturaViewHolder.observaciones.setText(piezaTO.getObservaciones());
	}

	@Override
	public PiezaRemitoSalidaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_pieza_remito_s, viewGroup, false);
		 return new PiezaRemitoSalidaViewHolder(itemView);
	}

}