package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.remito.salida.RemitoSalidaClienteFragment;
import ar.com.textillevel.mobile.modules.remito.salida.to.RemitoSalidaMobileTO;

public class RemitoSalidaPostExecuteHandler extends AbstractPostExecuteHandler<RemitoSalidaMobileTO> {

	public RemitoSalidaPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(RemitoSalidaMobileTO document) {
		RemitoSalidaClienteFragment rfragment = new RemitoSalidaClienteFragment(document);
		showFragment(rfragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
	}

}
