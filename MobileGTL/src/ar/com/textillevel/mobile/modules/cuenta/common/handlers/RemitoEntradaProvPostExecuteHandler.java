package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.RemitoEntradaProveedorFragment;
import ar.com.textillevel.mobile.modules.remito.entrada.proveedor.to.RemitoEntradaProvMobileTO;

public class RemitoEntradaProvPostExecuteHandler extends AbstractPostExecuteHandler<RemitoEntradaProvMobileTO> {

	public RemitoEntradaProvPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(RemitoEntradaProvMobileTO document) {
		RemitoEntradaProveedorFragment rfragment = new RemitoEntradaProveedorFragment(document);
		showFragment(rfragment, getActivity());				
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}

}
