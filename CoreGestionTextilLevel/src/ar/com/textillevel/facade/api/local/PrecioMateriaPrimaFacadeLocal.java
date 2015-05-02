package ar.com.textillevel.facade.api.local;

import java.math.BigDecimal;

import javax.ejb.Local;

import ar.com.textillevel.entidades.ventas.materiaprima.PrecioMateriaPrima;

@Local
public interface PrecioMateriaPrimaFacadeLocal {
	public PrecioMateriaPrima actualizarStockPrecioMateriaPrima(BigDecimal cantidad, Integer idPrecioMateriaPrima);
	public BigDecimal getStockByPrecioMateriaPrima(PrecioMateriaPrima precioMateriaPrima);
	public PrecioMateriaPrima save(PrecioMateriaPrima pmpActualizada);
}
