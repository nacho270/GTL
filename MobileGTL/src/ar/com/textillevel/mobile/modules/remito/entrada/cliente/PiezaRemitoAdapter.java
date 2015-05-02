package ar.com.textillevel.mobile.modules.remito.entrada.cliente;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.PiezaRemitoMobileTO;

public class PiezaRemitoAdapter extends Adapter<PiezaRemitoViewHolder> {

	private List<PiezaRemitoMobileTO> piezasList;
	
	public PiezaRemitoAdapter(List<PiezaRemitoMobileTO> piezasList) {
		this.piezasList = piezasList;
	}
	
	@Override
	public int getItemCount() {
		return piezasList.size();
	}

	@Override
	public void onBindViewHolder(PiezaRemitoViewHolder itemFacturaViewHolder, int i) {
		PiezaRemitoMobileTO piezaTO = piezasList.get(i);
		itemFacturaViewHolder.metros_pieza.setText(piezaTO.getMetros());
		itemFacturaViewHolder.nro_pieza.setText(piezaTO.getNumero());
		itemFacturaViewHolder.observaciones.setText(piezaTO.getObservaciones());
		itemFacturaViewHolder.odt.setText(piezaTO.getOdt());
	}

	@Override
	public PiezaRemitoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_pieza_remito_e, viewGroup, false);
		 return new PiezaRemitoViewHolder(itemView);
	}

}