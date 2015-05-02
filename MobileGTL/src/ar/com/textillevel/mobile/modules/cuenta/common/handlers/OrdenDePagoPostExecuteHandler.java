package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.ordenpago.OrdenDePagoFragment;
import ar.com.textillevel.mobile.modules.ordenpago.to.OrdenDePagoMobileTO;

public class OrdenDePagoPostExecuteHandler extends AbstractPostExecuteHandler<OrdenDePagoMobileTO> {

	public OrdenDePagoPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(OrdenDePagoMobileTO document) {
		OrdenDePagoFragment rfragment = new OrdenDePagoFragment(document);
		showFragment(rfragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}
}
