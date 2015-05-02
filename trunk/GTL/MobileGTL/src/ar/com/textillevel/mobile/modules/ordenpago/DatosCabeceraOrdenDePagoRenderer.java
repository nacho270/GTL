package ar.com.textillevel.mobile.modules.ordenpago;

import android.annotation.SuppressLint;
import android.view.View;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;
import ar.com.textillevel.mobile.modules.ordenpago.to.OrdenDePagoMobileTO;

public class DatosCabeceraOrdenDePagoRenderer extends DatosCabeceraRenderer<OrdenDePagoMobileTO> {

	public DatosCabeceraOrdenDePagoRenderer(OrdenDePagoMobileTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	@SuppressLint("DefaultLocale") 
	public void render(){
		setTextOnTextView(R.id.nro_odp,"ORDEN N° " + getDocument().getNroOrden(),getRootView());
		setTextOnTextView(R.id.fecha_odp,"FECHA: " + getDocument().getFechaOrden(),getRootView());
		setTextOnTextView(R.id.owner_odp, "SEÑOR/ES: " + getDocument().getCliente().getRazonSocial(),getRootView());
		setTextOnTextView(R.id.owner_odp_dir,"DIRECCIÓN: " + getDocument().getCliente().getDireccion() + " - " + getDocument().getCliente().getLocalidad(),getRootView());
		setTextOnTextView(R.id.owner_odp_cuit,"CUIT: " + getDocument().getCliente().getCuit(),getRootView());
		setTextOnTextView(R.id.owner_odp_cond_venta,"COND. VTA: " + getDocument().getCliente().getCondicionVenta(),getRootView());
		setTextOnTextView(R.id.odp_cant_pesos,"PESOS: " + getDocument().getTxtCantidadPesos().toUpperCase(),getRootView());
		setTextOnTextView(R.id.odp_total,"TOTAL: $" + getDocument().getTotal(),getRootView());
	}
}
