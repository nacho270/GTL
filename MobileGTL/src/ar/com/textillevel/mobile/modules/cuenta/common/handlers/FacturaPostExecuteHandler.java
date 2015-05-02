package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import android.widget.Toast;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.factura.cliente.FacturaClienteFragment;
import ar.com.textillevel.mobile.modules.factura.cliente.to.FacturaMobTO;

public class FacturaPostExecuteHandler extends AbstractPostExecuteHandler<FacturaMobTO> {

	public FacturaPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(FacturaMobTO document) {
		FacturaClienteFragment ccFragment = new FacturaClienteFragment(document);
		showFragment(ccFragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
		Toast.makeText(getActivity(), error.getMensaje(), Toast.LENGTH_LONG).show();
	}
}
