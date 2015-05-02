package ar.com.textillevel.mobile.modules.cuenta.common.handlers;

import android.app.Activity;
import ar.com.textillevel.mobile.modules.common.ErrorTO;
import ar.com.textillevel.mobile.modules.common.handlers.AbstractPostExecuteHandler;
import ar.com.textillevel.mobile.modules.cuenta.common.AbstractCuentaFragment;
import ar.com.textillevel.mobile.modules.cuenta.to.CuentaTO;

public class CuentaPostExecuteHandler extends AbstractPostExecuteHandler<CuentaTO>{

	private AbstractCuentaFragment fragment;
	
	public CuentaPostExecuteHandler(Activity activity, AbstractCuentaFragment fragment) {
		super(activity);
		this.fragment = fragment;
		
	}

	@Override
	public void handleResult(CuentaTO document) {
		getFragment().setCuentaTO(document);
		getFragment().llenarDatos();
	}

	@Override
	public void handleError(ErrorTO error) {
		
	}

	public AbstractCuentaFragment getFragment() {
		return fragment;
	}

	public void setFragment(AbstractCuentaFragment fragment) {
		this.fragment = fragment;
	}
}
