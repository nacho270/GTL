package ar.com.textillevel.mobile.modules.recibo;

import android.annotation.SuppressLint;
import android.view.View;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.recibo.to.ReciboMobileTO;

public class DatosCabeceraReciboRenderer extends DatosCabeceraRenderer<ReciboMobileTO> {

	public DatosCabeceraReciboRenderer(ReciboMobileTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	@SuppressLint("DefaultLocale") 
	public void render(){
		setTextOnTextView(R.id.nro_recibo,"RECIBO N° " + getDocument().getNroRecibo(),getRootView());
		setTextOnTextView(R.id.fecha_recibo,"FECHA: " + getDocument().getFecha(),getRootView());
		setTextOnTextView(R.id.owner_recibo, "SEÑOR/ES: " + getDocument().getCliente().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.owner_recibo_dir,"DIRECCIÓN: " + getDocument().getCliente().getDireccion() + " - " + getDocument().getCliente().getLocalidad(),getRootView());
		setTextOnTextView(R.id.owner_recibo_iva,"COND. IVA: " + ( getDocument().getCliente().getPosicionIVA()!=null?getDocument().getCliente().getPosicionIVA().toUpperCase(): getDocument().getCliente().getPosicionIVA()),getRootView());
		setTextOnTextView(R.id.owner_recibo_cuit,"CUIT: " + getDocument().getCliente().getCuit(),getRootView());
		setTextOnTextView(R.id.recibo_cant_pesos,"PESOS: " + getDocument().getTxtCantidadPesos().toUpperCase(),getRootView());
		setTextOnTextView(R.id.recibo_total,"TOTAL: $" + getDocument().getImporteTotal(),getRootView());
	}
}
