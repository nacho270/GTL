package ar.com.textillevel.mobile.modules.cuenta.cliente;

import ar.com.textillevel.mobile.modules.cuenta.common.AbstractCuentaFragment;
import ar.com.textillevel.mobile.modules.cuenta.common.ETipoCuenta;
import ar.com.textillevel.mobile.modules.cuenta.common.MovimientoRenderer;

public class CuentaClienteFragment extends AbstractCuentaFragment {

	@Override
	protected MovimientoRenderer getMovimientoRenderer() {
		return new MovimientoClienteRenderer();
	}

	@Override
	protected ETipoCuenta getTipoCuenta() {
		return ETipoCuenta.CLIENTE;
	}
}
