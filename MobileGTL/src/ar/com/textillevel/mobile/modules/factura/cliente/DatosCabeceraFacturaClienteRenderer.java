package ar.com.textillevel.mobile.modules.factura.cliente;

import android.view.View;
import android.widget.RelativeLayout;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.common.DocumentLinkeableLinearLayout;
import ar.com.textillevel.mobile.modules.factura.cliente.to.FacturaMobTO;

public class DatosCabeceraFacturaClienteRenderer extends DatosCabeceraRenderer<FacturaMobTO> {

	public DatosCabeceraFacturaClienteRenderer(FacturaMobTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	public void render() {
		setTextOnTextView(R.id.factura_tipo,"FACTURA \"" + getDocument().getTipoFactura() + "\" - N° " + getDocument().getNroFactura(),getRootView());
		setTextOnTextView(R.id.factura_fecha,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.factura_owner,"SEÑOR/ES: " + getDocument().getCliente().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.factura_owner_dir,"DIRECCIÓN: " + getDocument().getCliente().getDireccion() + (getDocument().getCliente().getLocalidad() == null ? "" : " - " + getDocument().getCliente().getLocalidad()), getRootView());
//		setTextOnTextView(R.id.factura_nro_remito,"REMITO: " + getDocument().getNroRemito(),getRootView());

		DocumentLinkeableLinearLayout ly = new DocumentLinkeableLinearLayout("remito", getDocument().getRemitos(), getRootView().getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, R.id.factura_owner_dir);
		((RelativeLayout)getRootView().findViewById(R.id.rl_cab_fact)).addView(ly, lp);
	}

}