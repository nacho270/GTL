package ar.com.textillevel.mobile.modules.remito.entrada.proveedor;

import android.annotation.SuppressLint;
import android.view.View;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to.RemitoEntradaProvMobileTO;

public class DatosCabeceraRemitoEntradaProveedorRenderer extends DatosCabeceraRenderer<RemitoEntradaProvMobileTO> {

	public DatosCabeceraRemitoEntradaProveedorRenderer(RemitoEntradaProvMobileTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	@SuppressLint("DefaultLocale")
	public void render() {
		setTextOnTextView(R.id.re_nro_p, "REMITO DE ENTRADA Nº " + getDocument().getNroRemito(), getRootView());
		setTextOnTextView(R.id.re_fecha_p,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.re_owner_p,"SEÑOR/ES: " + getDocument().getOwner().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.re_owner_dir_p,"DIRECCIÓN: " + getDocument().getOwner().getDireccion() + " - " + getDocument().getOwner().getLocalidad(),getRootView());
		setTextOnTextView(R.id.re_cond_iva_p,"COND. IVA: " + getDocument().getOwner().getPosicionIVA().toUpperCase(),getRootView());
		setTextOnTextView(R.id.re_ing_brutos_p,"ING. BRUTOS: " + getDocument().getOwner().getNroIngresosBrutos(), getRootView());
	}

}
