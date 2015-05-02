package ar.com.textillevel.mobile.modules.remito.entrada.proveedor;

import java.util.List;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to.PiezaRemitoProvMobileTO;

public class PiezaRemitoProveedorAdapter extends Adapter<PiezaRemitoProveedorViewHolder> {

	private List<PiezaRemitoProvMobileTO> piezasList;
	
	public PiezaRemitoProveedorAdapter(List<PiezaRemitoProvMobileTO> piezasList) {
		this.piezasList = piezasList;
	}
	
	@Override
	public int getItemCount() {
		return piezasList.size();
	}

	@Override
	public void onBindViewHolder(PiezaRemitoProveedorViewHolder itemFacturaViewHolder, int i) {
		PiezaRemitoProvMobileTO piezaTO = piezasList.get(i);
		itemFacturaViewHolder.descripcion.setText(piezaTO.getDescripcion());
		itemFacturaViewHolder.cantidad.setText(piezaTO.getCantidadMasUnidad());
		itemFacturaViewHolder.contenedor.setText(piezaTO.getContenedor());
		itemFacturaViewHolder.cant_contenedor.setText(piezaTO.getCantContenedor() == null ? "" : piezaTO.getCantContenedor());
	}

	@Override
	public PiezaRemitoProveedorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		 View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_pieza_remito_e_p, viewGroup, false);
		 return new PiezaRemitoProveedorViewHolder(itemView);
	}

}