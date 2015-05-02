package ar.com.textillevel.mobile.modules.remito.salida;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.common.DocumentLinkeableLinearLayout;
import ar.com.textillevel.mobile.modules.remito.salida.to.RemitoSalidaMobileTO;

public class DatosCabeceraRemitoSalidaRenderer extends DatosCabeceraRenderer<RemitoSalidaMobileTO> {

	public DatosCabeceraRemitoSalidaRenderer(RemitoSalidaMobileTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	@SuppressLint("DefaultLocale")
	public void render() {
		setTextOnTextView(R.id.rs_nro, "REMITO Nº " + getDocument().getNroRemito(), getRootView());
		setTextOnTextView(R.id.rs_fecha,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.rs_cant_piezas,"PIEZAS: " + getDocument().getPiezas().size(),getRootView());
		setTextOnTextView(R.id.rs_peso_total,"PESO TOTAL: " + getDocument().getPesoTotal(),getRootView());
		setTextOnTextView(R.id.rs_metros_total,"METROS: " + getDocument().getTotalMetros(),getRootView());
		setTextOnTextView(R.id.rs_owner,"SEÑOR/ES: " + getDocument().getOwner().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.rs_owner_dir,"DIRECCIÓN: " + getDocument().getOwner().getDireccion() + " - " + getDocument().getOwner().getLocalidad(),getRootView());
		setTextOnTextView(R.id.rs_cond_iva,"COND. IVA: " + getDocument().getOwner().getPosicionIVA().toUpperCase(),getRootView());
		setTextOnTextView(R.id.rs_cuit,"CUIT: " + getDocument().getOwner().getCuit(),getRootView());
		setTextOnTextView(R.id.rs_cond_venta,"COND. VENTA: " + getDocument().getOwner().getCondicionVenta(),getRootView());
		setTextOnTextView(R.id.rs_odts,"ODT(S): " + getDocument().getOdts(),getRootView());
		setTextOnTextView(R.id.rs_productos,"PRODUCTO(S): " + getDocument().getProductos(),getRootView());

		DocumentLinkeableLinearLayout ly = new DocumentLinkeableLinearLayout("remito", getDocument().getRemitosEntrada(), getRootView().getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, R.id.rs_cuit);
		((RelativeLayout)getRootView().findViewById(R.id.rs_rl_cabecera)).addView(ly, lp);	

	}

}