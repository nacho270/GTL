package ar.com.textillevel.mobile.modules.cheque;

import android.view.View;
import ar.com.textillevel.mobile.R;
import ar.com.textillevel.mobile.modules.cheque.to.ChequeFullTO;
import ar.com.textillevel.mobile.modules.common.DatosCabeceraRenderer;

public class DatosCabeceraChequeRenderer extends DatosCabeceraRenderer<ChequeFullTO> {

	public DatosCabeceraChequeRenderer(ChequeFullTO document, View rootView) {
		super(document, rootView);
	}

	@Override
	public void render() {
		setTextOnTextView(R.id.cheque_nro_int,"CHEQUE - N° " + getDocument().getNumeroInterno(),getRootView());
		setTextOnTextView(R.id.cheque_monto,"Monto: $" + getDocument().getImporte(),getRootView());
		setTextOnTextView(R.id.cheque_fecha_entrada,"ENTRADA: " + getDocument().getFechaEntrada(),getRootView());
		setTextOnTextView(R.id.cheque_fecha_deposito,"DEPOSITO: " + getDocument().getFechaDeposito(),getRootView());
		setTextOnTextView(R.id.cheque_fecha_salida,"SALIDA: " + getDocument().getFechaSalida(),getRootView());
		setTextOnTextView(R.id.cheque_banco,"BANCO: " + getDocument().getBanco(),getRootView());
		setTextOnTextView(R.id.cheque_numero,"NUMERO: " + getDocument().getNumero(),getRootView());
		setTextOnTextView(R.id.cheque_cuit,"CUIT: " + getDocument().getCuit(),getRootView());
		setTextOnTextView(R.id.cheque_cap_o_int,"CAP. O INT.: " + getDocument().getCapOInt(),getRootView());
		setTextOnTextView(R.id.cheque_owner,"CLIENTE: " + getDocument().getOwner(),getRootView());
		setTextOnTextView(R.id.cheque_estado,"ESTADO: " + getDocument().getEstado(),getRootView());
	}
}
