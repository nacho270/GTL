package ar.com.textillevel.mobile.modules.factura.cliente;

import android.view.View;
import android.widget.RelativeLayout;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.common.DocumentLinkeableLinearLayout;
import ar.com.textillevel.mobile.modules.common.to.ETipoDocumento;
import ar.com.textillevel.mobile.modules.factura.cliente.to.CorreccionFacturaMobTO;

public class DatosCabeceraCorreccionFacturaClienteRenderer extends DatosCabeceraRenderer<CorreccionFacturaMobTO> {

	public DatosCabeceraCorreccionFacturaClienteRenderer(CorreccionFacturaMobTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	public void render() {
		boolean esNC = getDocument().getTipoDocumento().equals(ETipoDocumento.NOTA_CREDITO.getId() + "");
		setTextOnTextView(R.id.factura_tipo, (esNC ? "NOTA DE CRÉDITO" : "NOTA DE DÉBITO") + " - N° " + getDocument().getNroCorreccion(), getRootView());
		setTextOnTextView(R.id.factura_fecha,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.factura_owner,"SEÑOR/ES: " + getDocument().getCliente().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.factura_owner_dir,"DIRECCIÓN: " + getDocument().getCliente().getDireccion() +  (getDocument().getCliente().getLocalidad() == null ? " " : " - " + getDocument().getCliente().getLocalidad()),getRootView());
		if(esNC) {
			DocumentLinkeableLinearLayout ly = new DocumentLinkeableLinearLayout("factura", getDocument().getFacturas(), getRootView().getContext());
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, R.id.factura_owner_dir);
			((RelativeLayout)getRootView().findViewById(R.id.rl_cab_fact)).addView(ly, lp);	
		}
	}

}
