package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.factura.proveedor.FacturaProveedorFragment;
import ar.com.textillevel.mobile.modules.factura.proveedor.to.FacturaProvMobTO;

public class FacturaProveedorPostExecuteHandler extends AbstractPostExecuteHandler<FacturaProvMobTO> {

	public FacturaProveedorPostExecuteHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void handleResult(FacturaProvMobTO document) {
		FacturaProveedorFragment rfragment = new FacturaProveedorFragment(document);
		showFragment(rfragment, getActivity());		
	}

	@Override
	public void handleError(ErrorTO error) {
	}

}
