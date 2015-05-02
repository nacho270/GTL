package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.RemitoEntradaClienteFragment;
import ar.com.textillevel.mobile.modules.remito.entrada.cliente.to.RemitoEntradaMobileTO;

public class RemitoEntradaPostExecuteHandler extends AbstractPostExecuteHandler<RemitoEntradaMobileTO>{

	public RemitoEntradaPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(RemitoEntradaMobileTO document) {
		RemitoEntradaClienteFragment rfragment = new RemitoEntradaClienteFragment(document);
		showFragment(rfragment, getActivity());				
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}
}
