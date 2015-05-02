package ar.com.textillevel.mobile.modules.remito.entrada.cliente;

import android.annotation.SuppressLint;
import android.view.View;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.RemitoEntradaMobileTO;

public class DatosCabeceraRemitoEntradaRenderer extends DatosCabeceraRenderer<RemitoEntradaMobileTO> {

	public DatosCabeceraRemitoEntradaRenderer(RemitoEntradaMobileTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	@SuppressLint("DefaultLocale")
	public void render() {
		setTextOnTextView(R.id.re_nro, "REMITO DE ENTRADA Nº " + getDocument().getNroRemito(), getRootView());
		setTextOnTextView(R.id.re_fecha,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.re_cant_piezas,"PIEZAS: " + getDocument().getPiezas().size(),getRootView());
		setTextOnTextView(R.id.re_peso_total,"PESO TOTAL (KG): " + getDocument().getPesoTotal(),getRootView());
		setTextOnTextView(R.id.re_metros_total,"MTS. TOTAL: " + getDocument().getTotalMetros(),getRootView());
		setTextOnTextView(R.id.re_owner,"SEÑOR/ES: " + getDocument().getOwner().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.re_owner_dir,"DIRECCIÓN: " + getDocument().getOwner().getDireccion() + " - " + getDocument().getOwner().getLocalidad(),getRootView());
		setTextOnTextView(R.id.re_cond_iva,"COND. IVA: " + getDocument().getOwner().getPosicionIVA().toUpperCase(),getRootView());
		setTextOnTextView(R.id.re_cuit,"CUIT: " + getDocument().getOwner().getCuit(),getRootView());
		setTextOnTextView(R.id.re_ancho_crudo,"ANCHO CRUDO (MTS): " + getDocument().getAnchoCrudo(),getRootView());
		setTextOnTextView(R.id.re_ancho_final,"ANCHO FINAL (MTS): " + getDocument().getAnchoFinal(),getRootView());
		setTextOnTextView(R.id.re_cond_venta,"COND. VENTA: " + getDocument().getOwner().getCondicionVenta(),getRootView());
		setTextOnTextView(R.id.re_tarima,"TARIMA: " + getDocument().getTarima(),getRootView());
		setTextOnTextView(R.id.re_en_palet,"EN PALET: " + (getDocument().getEnPalet().booleanValue()==true?"SI":"NO"),getRootView());
		setTextOnTextView(R.id.re_productos,"PRODUCTO(S): " + getDocument().getProductos(),getRootView());
	}
}
