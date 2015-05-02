package ar.com.textillevel.mobile.modules.cuenta.proveedor;

import ar.com.textillevel.mobile.modules.cuenta.common.AbstractCuentaFragment;
import ar.com.textillevel.mobile.modules.cuenta.common.ETipoCuenta;
import ar.com.textillevel.mobile.modules.cuenta.common.MovimientoRenderer;

public class CuentaProveedorFragment extends AbstractCuentaFragment {

	@Override
	protected MovimientoRenderer getMovimientoRenderer() {
		return new MovimientoProveedorRenderer();
	}

	@Override
	protected ETipoCuenta getTipoCuenta() {
		return ETipoCuenta.PROVEEDOR;
	}
}
