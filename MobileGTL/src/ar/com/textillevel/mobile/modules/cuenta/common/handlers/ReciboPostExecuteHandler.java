package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.recibo.ReciboFragment;
import ar.com.textillevel.mobile.modules.recibo.to.ReciboMobileTO;

public class ReciboPostExecuteHandler extends AbstractPostExecuteHandler<ReciboMobileTO> {
	
	public ReciboPostExecuteHandler(Activity ac) {
		super(ac);
	}

	@Override
	public void handleResult(ReciboMobileTO document) {
		ReciboFragment rfragment = new ReciboFragment(document);
		showFragment(rfragment, getActivity());
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}
}
