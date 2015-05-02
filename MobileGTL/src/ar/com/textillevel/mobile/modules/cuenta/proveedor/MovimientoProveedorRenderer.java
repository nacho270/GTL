package ar.com.textillevel.mobile.modules.cuenta.proveedor;

import android.graphics.Color;
import ar.com.textillevel.mobile.modules.cuenta.common.MovimientoRenderer;
import ar.com.textillevel.mobile.util.GenericUtils;

public class MovimientoProveedorRenderer implements MovimientoRenderer {
	
	@Override
	public int getTextColorDebe() {
		return Color.RED;
	}

	@Override
	public int getTextColorHaber() {
		return Color.GREEN;
	}

	@Override
	public int getTextColorSaldoParcial(Float saldoParcial) {
		if(saldoParcial == 0f){
			return Color.WHITE;
		}else if(saldoParcial > 0){
			return Color.GREEN;
		}
		return Color.RED;
	}

	@Override
	public String getStrMontoHaber(Float monto) {
		return monto.floatValue() < 0 ? GenericUtils.formatMonto(monto) : " ";
	}

	@Override
	public String getStrMontoDebe(Float monto) {
		return monto.floatValue() > 0 ? GenericUtils.formatMonto(monto) : " ";
	}
}
