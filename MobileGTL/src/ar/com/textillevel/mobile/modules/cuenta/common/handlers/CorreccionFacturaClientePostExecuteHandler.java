package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.factura.cliente.CorreccionFacturaClienteFragment;
import ar.com.textillevel.mobile.modules.factura.cliente.to.CorreccionFacturaMobTO;

public class CorreccionFacturaClientePostExecuteHandler extends AbstractPostExecuteHandler<CorreccionFacturaMobTO> {

	public CorreccionFacturaClientePostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(CorreccionFacturaMobTO document) {
		CorreccionFacturaClienteFragment rfragment = new CorreccionFacturaClienteFragment(document);
		showFragment(rfragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}

}
